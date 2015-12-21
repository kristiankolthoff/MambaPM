package de.unima.ki.mamba.pm.activitymatcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.index.CorruptIndexException;
import org.xml.sax.SAXException;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.DISCO;
import de.linguatools.disco.ReturnDataBN;
import de.linguatools.disco.WrongWordspaceTypeException;
import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.pm.nlp.NLPHelper;
import de.unima.ki.mamba.pm.utils.Settings;
import de.unima.ki.mamba.semafor.FrameNetAnnotator;
import de.unima.ki.mamba.semafor.model.Frame;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class FrameNetActivityMatcher implements ActivityMatcher{

	private FrameNetAnnotator fnAnno;
	private DISCO disco;
	private IDictionary dict;
	private MaxentTagger tagger;
	private NLPHelper nlpHelper;
	private Map<String, List<Frame>> frameMap;
	
	public static final int MAX_K = 20;
	public static final boolean LOAD_IN_MEMORY = false;
	
	public FrameNetActivityMatcher() throws FileNotFoundException, CorruptIndexException, 
					IOException, CorruptConfigFileException {
		this.disco = new DISCO(Settings.WORDSPACE_WORD2VEC_DIRECTORY, LOAD_IN_MEMORY);
		this.dict = new Dictionary(new URL("file:" + Settings.WORDNET_DIRECTORY));
		this.tagger = new MaxentTagger(Settings.POS_TAGGER_DIRECTORY);
		this.nlpHelper = new NLPHelper();
		this.dict.open();
	}
	
	public void annotateFNActivities(List<Model> models) {
		List<String> labelsToAnnotate = new ArrayList<String>();
		try {
			for(Model model : models) {
				for(Activity a : model.getActivities()) {
					labelsToAnnotate.addAll(getSimilarLabels(a.getLabel()));
				}
			}
			this.fnAnno.annotateSentences(labelsToAnnotate);
			this.frameMap = this.fnAnno.fetchFNResults();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongWordspaceTypeException e) {
			e.printStackTrace();
		}
	}
	
	public boolean match(Activity a1, Activity a2) {
		try {
			/**Get all frames for the k similar sentences for the label of activity a1**/
			List<String> similarLabels1 = getSimilarLabels(a1.getLabel());
			List<List<Frame>> framesAct1 = new ArrayList<List<Frame>>();
			for(String simLabel1 : similarLabels1) {
				framesAct1.add(frameMap.get(simLabel1));				
			}
			/**Get all frames for the k similar sentences for the label of activity a2**/
			List<String> similarLabels2 = getSimilarLabels(a2.getLabel());
			List<List<Frame>> framesAct2 = new ArrayList<List<Frame>>();
			for(String simLabel2 : similarLabels2) {
				framesAct2.add(frameMap.get(simLabel2));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongWordspaceTypeException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<String> getSimilarVerbs(String verb, int k) throws IOException, WrongWordspaceTypeException {
		List<String> similarVerbs = new ArrayList<String>();
		ReturnDataBN dbn = disco.similarWords(verb);
		if(dbn == null) {
			return similarVerbs;
		}
		for (int i = 0; i < dbn.words.length; i++) {
			if(i > k) {
				return similarVerbs;
			}
			String word = dbn.words[i].replace("_", " ");
			if(isVerb(word)) {
				similarVerbs.add(word);
			}
		}
		return similarVerbs;
	}
	
	public List<String> getSimilarLabels(String label) throws IOException, WrongWordspaceTypeException {
		List<String> similarSentences = new ArrayList<String>();
		label = label.toLowerCase();
		similarSentences.add(label);
		Optional<String> verb = extractVerb(label);
		if(verb.isPresent()) {
			List<String> similarVerbs = getSimilarVerbs(verb.get(), MAX_K);
			for(String simV : similarVerbs) {
				similarSentences.add(label.replace(verb.get(), simV));
				System.out.println(simV);
			}
		}
		System.out.println();
		return similarSentences;
	}
	
	/**
	 * TODO Consider extracting the stem of the verb instead of the inflated form
	 * TODO Consider the case with multiple verbs in one sentence
	 * @param label
	 * @return
	 */
	public Optional<String> extractVerb(String label) {
		Reader reader = new StringReader(label);
		DocumentPreprocessor docPre = new DocumentPreprocessor(reader);
		for(List<HasWord> list : docPre) {
			List<TaggedWord> taggedWords = tagger.apply(list);
			for(TaggedWord tw : taggedWords) {
				String t = tw.tag();
				System.out.println(tw.tag() + " " + tw.word());
				if(t.equals("VB") || t.equals("VBZ") || t.equals("VBD") || t.equals("VBG")
						|| t.equals("VBN") || t.equals("VBP")) {
					return Optional.of(nlpHelper.getNormalized(tw.word(), POS.VERB).toLowerCase());
				}
				
			}
		}
		return Optional.empty();
	}
	
	
	public boolean isVerb(String word) {
		word = nlpHelper.getNormalized(word, POS.VERB);
		IIndexWord idxWord = dict.getIndexWord(word, POS.VERB);
		return !(idxWord == null);
	}

	public static void main(String[] args) {
		try {
			FrameNetActivityMatcher fnam = new FrameNetActivityMatcher();
			List<String> verbs = fnam.getSimilarVerbs("send", FrameNetActivityMatcher.MAX_K);
			for(String s : verbs) {
				System.out.println(s);
			}
			fnam.extractVerb("The student sends the letter to the university.");
			NLPHelper nlp = new NLPHelper();
			Set<POS> set = nlp.getPOS("sending");
			for(POS pos : set) {
				System.out.println(pos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CorruptConfigFileException e) {
			e.printStackTrace();
		} catch (WrongWordspaceTypeException e) {
			e.printStackTrace();
		}
		
	}
}
