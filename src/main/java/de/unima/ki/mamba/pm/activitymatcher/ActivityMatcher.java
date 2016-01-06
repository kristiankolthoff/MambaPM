package de.unima.ki.mamba.pm.activitymatcher;

import de.unima.ki.mamba.pm.model.Activity;
@FunctionalInterface
public interface ActivityMatcher {

	public boolean match(Activity a1, Activity a2);
}
