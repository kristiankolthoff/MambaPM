package de.unima.ki.mamba.pm.activitymatcher;

import java.util.function.BiFunction;

import de.unima.ki.mamba.pm.model.Activity;

public class SimpleSyntacticActivityMatcher implements BiFunction<Activity, Activity, Double>{

	
	public SimpleSyntacticActivityMatcher() {
	}

	@Override
	public Double apply(Activity a1, Activity a2) {
		String label1 = a1.getLabel();
		String label2 = a2.getLabel();
		if(label1.equals(label2)) {
			System.out.println(label1 + " :: " + label2);
			return 1d;
		}
		return this.trivialAfterExtendedNorm(label1, label2);
	}
	
	public double trivialAfterExtendedNorm(String label1, String label2) {
		return 0d;
	}

}
