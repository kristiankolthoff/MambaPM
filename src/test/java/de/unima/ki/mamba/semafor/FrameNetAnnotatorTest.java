package de.unima.ki.mamba.semafor;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.pm.model.parser.BPMNParser;
import de.unima.ki.mamba.pm.model.parser.Parser;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetAnnotatorTest {

	private FrameNetAnnotator fnAnno;
	private Activity a;
	private Parser bpmnParser;
	private Model m;
	
	@Before
	public void init() {
		try {
			try {
				this.fnAnno = FrameNetAnnotator.getInstance();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
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
	public void annotateActivityTest() {
		fnAnno.annotate(a);
		try {
			HashMap<String, List<Frame>> frameMap = fnAnno.fetchFNResults();
			assertEquals(1, frameMap.size());
			List<Frame> frames = frameMap.get(a.getLabel());
			assertEquals(4, frames.size());
			assertEquals("Education_teaching", frames.get(0).getName());
			assertEquals("Sending", frames.get(1).getName());
			assertEquals("Text", frames.get(2).getName());
			assertEquals("Locale_by_use", frames.get(3).getName());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void annotateModelTest() {
		fnAnno.annotate(m);
		try {
			HashMap<String, List<Frame>> frameMap = fnAnno.fetchFNResults();
			List<Frame> frames = frameMap.get("Send letter of acceptance");
			assertEquals("Sending", frames.get(2).getName());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Activity a : m.getActivities()) {
			System.out.println(a.getId() + " " + a.getLabel());
		}
	}
}
