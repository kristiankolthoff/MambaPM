package de.unima.ki.mamba.semafor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetAnnotator {

	private FrameNetXMLParser fnParser;
	private FrameNetService fnService;
	private static FrameNetAnnotator fnAnno;
	private List<String> activitiesToAnnotate;
	
	private FrameNetAnnotator() throws ParserConfigurationException, FileNotFoundException {
		this.fnService = new FrameNetService();
		this.fnParser = new FrameNetXMLParser();
		this.activitiesToAnnotate = new ArrayList<String>();
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
		activitiesToAnnotate.add(a.getLabel());
		return this;
	}
	
	public HashMap<String, List<Frame>> fetchFNResults() throws ParserConfigurationException, SAXException, IOException {
		fnService.createAnnotationFile(activitiesToAnnotate);
		fnService.runFNSemanticParsing();
		HashMap<String, List<Frame>> frameMap = fnParser.fetchFNData(FrameNetOptions.ABS_PATH_FNDATA + 
				FrameNetOptions.FN_FILE_OUT_NAME);
		fnService.cleanAll();
		activitiesToAnnotate.clear();
		return frameMap;
	}
	
	public static FrameNetAnnotator getInstance() throws ParserConfigurationException, FileNotFoundException {
		if(fnAnno == null) {
			fnAnno = new FrameNetAnnotator();
		}
		return fnAnno;
	}
}
