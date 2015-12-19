package de.unima.ki.mamba.semafor;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.pm.model.parser.BPMNParser;
import de.unima.ki.mamba.pm.model.parser.Parser;

public class FrameNetAnnotatorTest {

	private FrameNetAnnotator fnAnno;
	private Activity a;
	private Parser bpmnParser;
	private Model m;
	
	@Before
	public void init() {
		this.fnAnno = FrameNetAnnotator.getInstance();
		this.a = new Activity("test_01", "The student sends the letter to the university");
		this.bpmnParser = new BPMNParser();
		try {
			this.m = bpmnParser.parse(new File(".").getAbsolutePath() + "/src/resources/data/dataset1/models/Cologne.bpmn");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void annotateTest() {
		for(Activity ac : m.getActivities()) {
			System.out.println(ac.getLabel());
		}
		fnAnno.annotate(a);
		fnAnno.annotate(m);
		
	}
}
