package de.unima.ki.mamba.semafor.model;

import java.util.List;

public class Frame {

	private String sentence;
	private String name;
	private String target;
	private List<FElement> fElements;
	private int rank;
	
	public Frame(String sentence, String name, String target, List<FElement> fElements, int rank) {
		this.sentence = sentence;
		this.target = target;
		this.name = name;
		this.fElements = fElements;
		this.rank = rank;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	
}
