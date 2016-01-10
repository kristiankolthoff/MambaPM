package de.unima.ki.mamba.pm.model;

import java.util.HashSet;
import java.util.Set;

public class Model {

	private String id;
	private Set<Activity> activities;
	private Set<Flow> flows;
	
	public Model(String id) {
		this.id = id;
		this.activities = new HashSet<>();
		this.flows = new HashSet<>();
	}
	
	public Model() {
		this.activities = new HashSet<>();
		this.flows = new HashSet<>();
	}
	
	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}
	
	
	public void addFlow(Flow flow) {
		this.flows.add(flow);
	}

	public Set<Activity> getActivities() {
		return this.activities;
	}
	
	public String getLabelById(String id) {
		for (Activity a : this.activities) {
			if (a.getId().equals(id)) {
				return a.getLabel();
			}
		}
		return "?-" + id;
	}
	
	public void normalizeLabels() {
		for (Activity a : this.activities) {
			a.normalizeLabels();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
