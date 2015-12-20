package de.unima.ki.mamba.semafor;

import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetAnnotator {

	private FrameNetXMLParser fnParser;
	private FrameNetService fnService;
	private static FrameNetAnnotator fnAnno;
	private HashMap<String, String> labelsToAnnotate;
	
	private FrameNetAnnotator() throws ParserConfigurationException {
		this.fnService = new FrameNetService();
		this.fnParser = new FrameNetXMLParser();
		this.labelsToAnnotate = new HashMap<String, String>();
	}
	
	public FrameNetAnnotator annotate(List<Model> models) {
		for(Model m : models) {
			annotate(m);
		}
		return this;
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
		fnService.runFNSemanticParsing();
		
		return null;
	}
	
	public static FrameNetAnnotator getInstance() throws ParserConfigurationException {
		if(fnAnno == null) {
			fnAnno = new FrameNetAnnotator();
		}
		return fnAnno;
	}
}
