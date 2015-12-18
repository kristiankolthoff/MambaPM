package de.unima.ki.mamba.semafor;

import java.util.ArrayList;
import java.util.List;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetAnnotator {

	private FrameNetOptions fnOpt;
	private FrameNetXMLParser fnParser;
	private FrameNetExecutor fnExec;
	private static FrameNetAnnotator fnAnno;
	private List<String> labelsToAnnotate;
	
	public FrameNetAnnotator(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
		this.fnParser = new FrameNetXMLParser();
		this.fnExec = new FrameNetExecutor();
		this.labelsToAnnotate = new ArrayList<String>();
	}
	
	public FrameNetAnnotator annotate(Model m) {
		return this;
	}
		
	public FrameNetAnnotator annotate(Activity a) {
		return this;
	}
	
	public FrameNetAnnotator annotate(String s) {
		return this;
	}
	
	public List<Frame> execute() {
		return null;
	}
	
	public FrameNetAnnotator getInstance() {
		//TODO: Build instance + options etc.
		return fnAnno;
	}

	public FrameNetOptions getFnOpt() {
		return fnOpt;
	}

	public void setFnOpt(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
	}
}
