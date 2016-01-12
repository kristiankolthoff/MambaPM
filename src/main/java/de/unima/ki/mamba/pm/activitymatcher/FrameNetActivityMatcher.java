package de.unima.ki.mamba.pm.activitymatcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

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

public class FrameNetActivityMatcher implements BiPredicate<Activity, Activity>{

	private FrameNetAnnotator fnAnno;
	private DISCO disco;
	private IDictionary dict;
	private MaxentTagger tagger;
	private NLPHelper nlpHelper;
	private Map<String, List<Frame>> frameMap;
	
	public static final int MAX_K = 20;
	public static final boolean LOAD_IN_MEMORY = false;
	
	public FrameNetActivityMatcher() throws FileNotFoundException, CorruptIndexException, 
					IOException, CorruptConfigFileException, ParserConfigurationException {
		this.fnAnno = new FrameNetAnnotator(Settings.getJavaHomeDirectory());
		this.disco = new DISCO(Settings.getWordspaceWord2VecDirectory(), LOAD_IN_MEMORY);
		this.dict = new Dictionary(new URL("file:" + Settings.getWordnetDirectory()));
		this.tagger = new MaxentTagger(Settings.getPosTaggerDirectory());
		this.nlpHelper = new NLPHelper();
		this.frameMap = new HashMap<>();
		this.dict.open();
	}
	
	public FrameNetActivityMatcher annotateFNActivities(Model model) {
		List<String> labelsToAnnotate = new ArrayList<String>();
		for (Activity a : model.getActivities()) {
			if(Objects.isNull(this.frameMap.get(this.nlpHelper.getTokenizedString(a.getLabel())))) {
				labelsToAnnotate.add(a.getLabel());
			}
		}
		try {
			this.frameMap.putAll(this.fnAnno.fetchFNResults(labelsToAnnotate));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	
	
	@Override
	public boolean test(Activity a1, Activity a2) {
		try {
			return this.matchSimple(a1, a2);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongWordspaceTypeException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean matchSimple(Activity a1, Activity a2) throws IOException, WrongWordspaceTypeException {
		/**Get all frames for the k similar sentences for the label of activity a1**/
		List<Frame> frames1 = this.getSimilarFrames(a1);
		/**Get all frames for the k similar sentences for the label of activity a2**/
		List<Frame> frames2 = this.getSimilarFrames(a2);
		for(Frame f1 : frames1) {
			for(Frame f2 : frames2) {
				if(f1.equals(f2)) {
					System.err.println("--------Matching Frame--------");
					System.out.println(f1.toString());
					System.out.println(f2.toString());
					System.err.println("------------------------------");
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean matchMajorityVote(Activity a1, Activity a2) throws IOException, WrongWordspaceTypeException {
		/**Get all frames for the k similar sentences for the label of activity a1**/
		List<Frame> frames1 = this.getSimilarFrames(a1);
		/**Get all frames for the k similar sentences for the label of activity a2**/
		List<Frame> frames2 = this.getSimilarFrames(a2);
		System.err.println("---Majority-1---");
		List<Frame> frameMaj1 = this.majorityVoteFrames(frames1);
		System.err.println(frameMaj1);
		System.err.println("---Majority-2---");
		List<Frame> frameMaj2 = this.majorityVoteFrames(frames2);
		System.err.println(frameMaj2);
		for(Frame f1 : frameMaj1) {
			for(Frame f2 : frameMaj2) {
				if(f1.equals(f2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<Frame> getSimilarFrames(Activity a) throws IOException, WrongWordspaceTypeException {
		List<String> similarLabels = getSimilarLabels(a.getLabel());
		List<Frame> frames = new ArrayList<>();
		for(String simLabel : similarLabels) {
			frames.addAll(frameMap.get(this.nlpHelper.getTokenizedString(simLabel).trim()));
		}
		return frames;
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
			}
		}
		return similarSentences;
	}
	
	/**
	 * TODO Consider extracting the stem of the verb instead of the inflated form
	 * TODO Consider the case with multiple verbs in one sentence, currently 
	 * only extracting the first verb
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
				if(this.nlpHelper.isPennTreebankVerbTag(t)) {
					return Optional.of(this.nlpHelper.getNormalized(tw.word(), POS.VERB).toLowerCase());
				}
				
			}
		}
		return Optional.empty();
	}
	
	
	public boolean isVerb(String word) {
		word = nlpHelper.getNormalized(word, POS.VERB);
		IIndexWord idxWord = dict.getIndexWord(word, POS.VERB);
		return idxWord != null;
	}
	
	public List<Frame> majorityVoteFrames(List<Frame> frames) {
		Objects.requireNonNull(frames);
		if(frames.size() == 0) {
			return new ArrayList<>();
		}
		Map<Frame,Integer> countMap = new HashMap<>();
		for (Frame frame : frames) {
			if(countMap.containsKey(frame)) {
				countMap.put(frame, countMap.get(frame) + 1);				
			} else {
				countMap.put(frame, 1);
			}
		}
		int maxValue = Integer.MIN_VALUE;
		for(Map.Entry<Frame, Integer> e : countMap.entrySet()) {
			if(e.getValue() > maxValue) {
				maxValue = e.getValue();
			}
		}
		List<Frame> bestFrames = new ArrayList<>();
		for(Map.Entry<Frame, Integer> e : countMap.entrySet()) {
			if(e.getValue() == maxValue) {
				bestFrames.add(e.getKey());
			}
		}
		return bestFrames;
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
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
	}
}
