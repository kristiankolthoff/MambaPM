package de.unima.ki.mamba.pm.matcher;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.index.CorruptIndexException;
import org.xml.sax.SAXException;

import de.linguatools.disco.CorruptConfigFileException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.Correspondence;
import de.unima.ki.mamba.pm.activitymatcher.ActivityMatcher;
import de.unima.ki.mamba.pm.activitymatcher.FrameNetActivityMatcher;
import de.unima.ki.mamba.pm.activitymatcher.SimpleSyntacticActivityMatcher;
import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;

public class BasicMatcher {

	private String sourceNS = null;
	private String targetNS = null;
	private List<ActivityMatcher> activityMatchers;
	
	public BasicMatcher() throws FileNotFoundException, CorruptIndexException, IOException, 
			CorruptConfigFileException, ParserConfigurationException {
		this.activityMatchers = new ArrayList<>();
		registerActivityMatcher();
	}
	
	private void registerActivityMatcher() throws FileNotFoundException, CorruptIndexException, IOException, 
			CorruptConfigFileException, ParserConfigurationException {
		this.activityMatchers.add(new FrameNetActivityMatcher());
	}
	
	public void setNamespacePrefixes(String sourceNS, String targetNS)  {
		this.sourceNS = sourceNS;
		this.targetNS = targetNS;
	}
	
	public Alignment match(Model sourceModel, Model targetModel) throws ParserConfigurationException, SAXException, IOException {
		Alignment alignment = new Alignment();
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
		return alignment;
	}
	
	public boolean matchActivities(Activity a1, Activity a2) {
		for(ActivityMatcher am : this.activityMatchers) {
			if(am.test(a1, a2)) {
				return true;
			}
		}
		return false;
	}
	
	public String normalize(String label) {
		return label.toLowerCase();
	}
}	