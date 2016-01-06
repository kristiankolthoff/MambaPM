package de.unima.ki.mamba.pm.activitymatcher;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.lucene.index.CorruptIndexException;
import org.junit.Before;
import org.junit.Test;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.WrongWordspaceTypeException;
import de.unima.ki.mamba.pm.model.Activity;
import de.unima.ki.mamba.pm.model.Model;

public class FrameNetActivityMatcherTest {

	private FrameNetActivityMatcher fnActMatcher;
	private Model model;
	private List<Model> models;
	
	@Before
	public void init() {
		try {
			this.fnActMatcher = new FrameNetActivityMatcher();
			this.models = new ArrayList<>();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CorruptConfigFileException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void extractSingleVerbTest() {
		final String sentence1 = "Add the certificate to the documents";
		final String sentence2 = "Checking the bachelors degree";
		final String sentence3 = "The student visits the university";
		assertEquals("add", this.fnActMatcher.extractVerb(sentence1).get());
		assertEquals("check", this.fnActMatcher.extractVerb(sentence2).get());
		assertEquals("visits", this.fnActMatcher.extractVerb(sentence3).get());
	}
	
	@Test
	public void getSimilarLabelsTest() {
		try {
			final String stemSentence = " the certificate to the documents";
			List<String> similarVerbs = this.fnActMatcher.getSimilarVerbs("add", FrameNetActivityMatcher.MAX_K);
			for(String s : similarVerbs) {
				System.out.println(s);
			}
			List<String> similarLabels = this.fnActMatcher.getSimilarLabels("Add" + stemSentence);
			for(String s : similarLabels) {
				System.out.println(s);
			}
			assertEquals(20, similarLabels.size());
			assertEquals("add" + stemSentence, similarLabels.get(0));
			assertEquals("introduce" + stemSentence, similarLabels.get(1));
			assertEquals("upgrade" + stemSentence, similarLabels.get(2));
			assertEquals("attach" + stemSentence, similarLabels.get(6));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongWordspaceTypeException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void matchDifficultSimilarVerbActivitiesTest() {
		Activity a1 = new Activity("a1", "Send documents");
		Activity a2 = new Activity("a2", "Send application files to university");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.models.add(this.model);
		this.fnActMatcher.annotateFNActivities(this.models);
		assertTrue(this.fnActMatcher.match(a1, a2));
	}
	
	@Test
	public void matchDifficultActivitiesTest() {
		Activity a1 = new Activity("a1", "Send letter of rejection");
		Activity a2 = new Activity("a2", "Reject applicant");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.models.add(this.model);
		this.fnActMatcher.annotateFNActivities(this.models);
		assertTrue(this.fnActMatcher.match(a1, a2));
	}
	
	@Test
	public void noMatchActivitiesTest() {
		Activity a1 = new Activity("a1", "Send letter of rejection");
		Activity a2 = new Activity("a2", "Invite applicant to interview");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.models.add(this.model);
		this.fnActMatcher.annotateFNActivities(this.models);
		assertFalse(this.fnActMatcher.match(a1, a2));
	}
	
	@Test
	public void matchTrivialActivitiesTest() {
		Activity a1 = new Activity("a1", "The student sends the letter");
		Activity a2 = new Activity("a2", "The student sends the letter");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.models.add(this.model);
		this.fnActMatcher.annotateFNActivities(this.models);
		assertTrue(this.fnActMatcher.match(a1, a2));
	}
	

}
