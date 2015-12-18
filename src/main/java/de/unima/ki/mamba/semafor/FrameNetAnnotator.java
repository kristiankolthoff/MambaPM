package de.unima.ki.mamba.semafor;

import java.util.HashMap;
import java.util.List;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetAnnotator {

	private FrameNetOptions fnOpt;
	private FrameNetXMLParser fnParser;
	private FrameNetExecutor fnExec;
	private static FrameNetAnnotator fnAnno;
	private HashMap<String, String> labelsToAnnotate;
	
	private FrameNetAnnotator() {
		this.fnOpt = FrameNetOptions.getStandardOpt();
		this.fnParser = new FrameNetXMLParser();
		this.fnExec = new FrameNetExecutor();
		this.labelsToAnnotate = new HashMap<String, String>();
	}
	
	public FrameNetAnnotator annotate(Model m) {
		for(Activity a : m.getActivities()) {
			this.annotate(a);
		}
		return this;
	}
		
	public FrameNetAnnotator annotate(Activity a) {
		labelsToAnnotate.put(a.getId(), a.getLabel());
		return this;
	}
	
	public List<Frame> execute() {
		return null;
	}
	
	public static FrameNetAnnotator getInstance() {
		return fnAnno;
	}

	public FrameNetOptions getFnOpt() {
		return fnOpt;
	}

	public void setFnOpt(FrameNetOptions fnOpt) {
		this.fnOpt = fnOpt;
	}
}
