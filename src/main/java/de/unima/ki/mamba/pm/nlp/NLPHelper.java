package de.unima.ki.mamba.pm.nlp;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unima.ki.mamba.pm.utils.Settings;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;

public class NLPHelper {
	
	private static IRAMDictionary dict;
	private static HashSet<String> stopwords;
	
	private static final String[] STOP_WORDS;
	
	static {
		dict = new RAMDictionary(new File(Settings.getWordnetDirectory()) , ILoadPolicy.NO_LOAD); 
		try {
			dict.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		STOP_WORDS = new String[]{
				"a","able","about","across","after","all","almost","also","am","among","an","and","any","are","as","at","be","because","been","but","by","can","cannot",
				"could","dear","did","do","does","either","else","ever","every","for","from","get","got","had","has","have","he","her","hers","him","his","how","however",
				"i","if","in","into","is","it","its","just","least","let","like","likely","may","me","might","most","must","my","neither","no","nor","not","of","off",
				"often","on","only","or","other","our","own","rather","said","say","says","she","should","since","so","some","than","that","the","their","them",
				"then","there","these","they","this","tis","to","too","twas","us","wants","was","we","were","what","when","where","which","while","who","whom","why",
				"will","with","would","yet","you","your"};
		stopwords = new HashSet<String>();
		stopwords.addAll(Arrays.asList(STOP_WORDS));
	}
	

	private NLPHelper() {
		
	}
	
	public static Set<POS> getPOS(String w) {
		IIndexWord indexWord;
		Set<ISynset> synsets;
		Set<POS> pos = new HashSet<POS>();
		POS[] all = new POS[]{POS.NOUN, POS.VERB, POS.ADVERB, POS.ADJECTIVE};
		for (POS p : all) {
			indexWord = dict.getIndexWord(w, p);
			synsets = getSynsetsByIndexWord(indexWord);
			if (synsets.size() > 0) {
				pos.add(p);
			}
		}
		// do some basic stemming for verbs
		if (w.endsWith("ing") && !pos.contains(POS.VERB)) {
			String w1 = w.substring(0, w.length() - 3);
			String w2 = w1 + "e";
			indexWord = dict.getIndexWord(w1, POS.VERB);
			synsets = getSynsetsByIndexWord(indexWord);
			if (synsets.size() > 0) {
				pos.add(POS.VERB);
			}
			indexWord = dict.getIndexWord(w2, POS.VERB);
			synsets = getSynsetsByIndexWord(indexWord);
			if (synsets.size() > 0) {
				pos.add(POS.VERB);
			}
			if (w1.endsWith("nn") || w1.endsWith("tt")) {
				String w3 = w1.substring(0, w.length() - 1);
				indexWord = dict.getIndexWord(w3, POS.VERB);
				synsets = getSynsetsByIndexWord(indexWord);
				if (synsets.size() > 0) {
					pos.add(POS.VERB);
				}
			}
		}
		// do some basic stemming for plural nouns
		if (w.endsWith("s") && !pos.contains(POS.NOUN)) {
			String w1 = w.substring(0, w.length() - 1);
			indexWord = dict.getIndexWord(w1, POS.NOUN);
			synsets = getSynsetsByIndexWord(indexWord);
			if (synsets.size() > 0) {
				pos.add(POS.NOUN);
			}
		}
		
		return pos;
	}
	
	//TODO Does not remove s in verbs like: visits
	public static String getNormalized(String w, POS p) {
		IIndexWord indexWord;
		Set<ISynset> synsets;
		if (p == POS.ADVERB) {
			return w;
		}
		if (p == POS.ADJECTIVE) {
			return w;
		}
		if (p == POS.NOUN) {
			indexWord = dict.getIndexWord(w, POS.NOUN);
			synsets = getSynsetsByIndexWord(indexWord);
			if (synsets.size() == 0 && w.endsWith("s")) {
				return w.substring(0, w.length()-1);
			}			
			else {
				return w;
			}
		}
		if (p == POS.VERB) {
			indexWord = dict.getIndexWord(w, POS.VERB);
			synsets = getSynsetsByIndexWord(indexWord);
			if (synsets.size() > 0) {
				return w;
			}
			if (w.endsWith("ing")) {
				String w1 = w.substring(0, w.length() - 3);
				String w2 = w1 + "e";
				indexWord = dict.getIndexWord(w1, POS.VERB);
				synsets = getSynsetsByIndexWord(indexWord);
				if (synsets.size() > 0) return w1;
				indexWord = dict.getIndexWord(w2, POS.VERB);
				synsets = getSynsetsByIndexWord(indexWord);		
				if (synsets.size() > 0) return w1;
				
			}			
			else {
				return w;
			}			
		}
		return w;
	}
	
		
	public static boolean isStopword(String w) {
		return stopwords.contains(w);
	}
	
	public static void showSenses(String w, POS p) {

		IIndexWord indexWord = dict.getIndexWord(w, p);
		Set<ISynset> synsets = getSynsetsByIndexWord(indexWord);
		for (ISynset synset : synsets) {
			System.out.println(synset);
		}
	}
	
	
	private static Set<ISynset> getSynsetsByIndexWord(IIndexWord indexWord) {
		Set<ISynset> synsets = new HashSet<ISynset>();
		if (indexWord != null && indexWord.getWordIDs() != null) {
			for (IWordID wordId : indexWord.getWordIDs()) {
				ISynsetID synsetid = wordId.getSynsetID();
				ISynset synset = dict.getSynset(synsetid);
				synsets.add(synset);
			}
		}
		return synsets;
	}
	
	public POS getWordType() {
		
		
		return POS.ADJECTIVE;
	}
	
	public static boolean isPennTreebankVerbTag(String t) {
		return t.equals("VB") || t.equals("VBZ") || t.equals("VBD") || t.equals("VBG")
				|| t.equals("VBN") || t.equals("VBP");
	}
	
	public static String getTokenizedString(String sentence) {
		PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<CoreLabel>(new StringReader(sentence), 
				new CoreLabelTokenFactory(), "");
		List<CoreLabel> tokens = tokenizer.tokenize();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i).originalText());
			if(i!=tokens.size()-1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public static List<String> getTokens(String sentence) {
		PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<CoreLabel>(new StringReader(sentence),
				new CoreLabelTokenFactory(), "");
		List<CoreLabel> tokens = tokenizer.tokenize();
		List<String> sTokens = new ArrayList<>();
		for(CoreLabel token : tokens) {
			sTokens.add(token.originalText());
		}
		return sTokens;
	}
	
	public static List<String> getStemmedTokens(String sentence) {
		List<String> tokens = getTokens(sentence);
		List<String> stemmedTokens = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			List<String> possibleStems = getWordStem(tokens.get(i));
			if(possibleStems.size() >= 1) {
				stemmedTokens.add(possibleStems.get(0));				
			} else {
				stemmedTokens.add(tokens.get(i));
			}
		}
		return stemmedTokens;
	}
	
	public static List<String> getWordStem(String word) {
		WordnetStemmer ws = new WordnetStemmer(dict);
		return ws.findStems(word, null);
	}
	
	public static String getSanitizeLabel(String label) {
		label = label.trim();
		label = label.toLowerCase();
		label = getTokenizedString(label);
		label = label.replace("\n", "");
		return label;
	}
}
