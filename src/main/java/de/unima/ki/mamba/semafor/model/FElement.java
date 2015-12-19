package de.unima.ki.mamba.semafor.model;

public class FElement {

	private String name;
	private String content;
	
	public FElement(String name, String content) {
		this.name = name;
		this.content = content;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
