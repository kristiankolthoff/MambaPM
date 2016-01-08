package de.unima.ki.mamba.pm.activitymatcher;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.nlp.NLPHelper;

public class SimpleSyntacticActivityMatcher implements ActivityMatcher{

	private NLPHelper nlpHelper;
	
	public SimpleSyntacticActivityMatcher() {
		this.nlpHelper = new NLPHelper();
	}
	
	@Override
	public boolean test(Activity a1, Activity a2) {
		String label1 = a1.getLabel();
		String label2 = a2.getLabel();
		return simpleStringEquality(label1, label2);
	}
	
	public boolean simpleStringEquality(String l1, String l2) {
		if(l1.toLowerCase().equals(l2.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}



}
