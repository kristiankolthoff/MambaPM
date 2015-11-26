package de.unima.ki.mamba.pm.matcher;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.Correspondence;
import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.pm.model.parser.BPMNParser;
import de.unima.ki.mamba.pm.model.parser.PNMLParser;
import de.unima.ki.mamba.pm.model.parser.Parser;

public class BasicMatcher {

	private Alignment alignment = null; 
	
	private String sourceNS = null;
	private String targetNS = null;
	
	private Parser parser = null;
	
	public BasicMatcher() {
		
		
	}
	
	public void setParser(String extension) {
		if (extension.equals("bpmn")) {
			this.parser = new BPMNParser();
		}
		if (extension.equals("pnml")) {
			this.parser = new PNMLParser();
		}
		
	}
	
	public void setNamespacePrefixes(String sourceNS, String targetNS)  {
		this.sourceNS = sourceNS;
		this.targetNS = targetNS;
	}
	
	public void match(String sourceModelPath, String targetModelPath) throws ParserConfigurationException, SAXException, IOException {
		
		Model sourceModel = parser.parse(sourceModelPath);
		Model targetModel = parser.parse(targetModelPath);
		this.alignment = new Alignment();
		for (Activity sourceActivity : sourceModel.getActivities()) {
			for (Activity targetActivity : targetModel.getActivities()) {
				if (matchActivities(sourceActivity, targetActivity)) {
					Correspondence c = new Correspondence(
							this.sourceNS + sourceActivity.getId(),
							this.targetNS + targetActivity.getId()
					);
					alignment.add(c);
					
				}
			}
		}
		this.sourceNS = null;
		this.targetNS = null;
		
	}
	
	public boolean matchActivities(Activity a1, Activity a2) {
		if (normalize(a1.getLabel()).equals(normalize(a2.getLabel()))) {
			return true;
		}
		return false;
		
	}
	
	public String normalize(String label) {
		return label.toLowerCase();
	}
	
	
	public Alignment getAlignment() {
		return this.alignment;
	}
	

}
