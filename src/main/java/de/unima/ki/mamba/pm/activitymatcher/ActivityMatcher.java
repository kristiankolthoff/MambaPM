package de.unima.ki.mamba.pm.activitymatcher;

import java.util.function.BiPredicate;

import de.unima.ki.mamba.pm.model.Activity;
@FunctionalInterface
public interface ActivityMatcher extends BiPredicate<Activity, Activity>{

	/**
	 * Inherits the test method from its superclass.
	 * Used for marking a class implementing an activity
	 * matcher using a parametrized BiPredicate
	 */
}
