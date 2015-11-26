package de.unima.ki.mamba.pm.model;

import java.util.HashSet;
import java.util.Set;

public class Activity {
	
	private Set<Flow> flowTo;
	private Set<Flow> flowFrom;
	
	private String id;
	private String label;
	private Set<String[]> labels;
	

	public Activity(String id, String label) {
		this.id = id;
		this.label = label;	
		this.labels = new HashSet<String[]>();	
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void addFlowFrom(Flow flow) {
		this.flowFrom.add(flow);
	}
	
	public void addFlowTo(Flow flow) {
		this.flowTo.add(flow);
	}
	
	public String toString() {
		return this.id + "\" " + this.label + "\"";
	}

	public void normalizeLabels() {
		String parts[] = this.label.split(" ");	
		this.labels.add(parts);
		// do the of modification
		 
		// do the ing removal
		
		// to the plural s removal
	}
	
	
	
	
	
	
}
