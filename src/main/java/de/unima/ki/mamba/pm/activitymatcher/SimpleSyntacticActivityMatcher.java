package de.unima.ki.mamba.pm.activitymatcher;

import java.util.function.BiPredicate;

import de.unima.ki.mamba.pm.model.Activity;

public class SimpleSyntacticActivityMatcher implements BiPredicate<Activity, Activity>{

	
	public SimpleSyntacticActivityMatcher() {
	}
	
	@Override
	public boolean test(Activity a1, Activity a2) {
		String label1 = a1.getLabel();
		String label2 = a2.getLabel();
		System.out.println(label1 + "  " + label2);
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
