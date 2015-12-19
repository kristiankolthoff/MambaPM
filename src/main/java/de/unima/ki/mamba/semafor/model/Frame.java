package de.unima.ki.mamba.semafor.model;

import java.util.List;

public class Frame {

	private String sentence;
	private String name;
	private String target;
	private List<FElement> fElements;
	
	public Frame(String sentence, String name, String target, List<FElement> fElements) {
		this.sentence = sentence;
		this.target = target;
		this.name = name;
		this.fElements = fElements;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FElement> getfElements() {
		return fElements;
	}

	public void setfElements(List<FElement> fElements) {
		this.fElements = fElements;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
