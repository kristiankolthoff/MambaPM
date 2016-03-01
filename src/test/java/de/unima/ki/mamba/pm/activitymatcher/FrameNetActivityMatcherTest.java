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
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetActivityMatcherTest {

	private FrameNetActivityMatcher fnActMatcher;
	private Model model;
	
	@Before
	public void init() {
		try {
			this.fnActMatcher = new FrameNetActivityMatcher();
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
		this.fnActMatcher.annotateFNActivities(this.model);
		assertEquals(1, this.fnActMatcher.apply(a1, a2), 0.2);
	}
	
	@Test
	public void matchDifficultActivitiesTest() {
		Activity a1 = new Activity("a1", "Send letter of rejection");
		Activity a2 = new Activity("a2", "Reject applicant");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.fnActMatcher.annotateFNActivities(this.model);
		assertEquals(1, this.fnActMatcher.apply(a1, a2), 0.2);
	}
	
	@Test
	public void noMatchActivitiesTest() {
		Activity a1 = new Activity("a1", "Send letter of rejection");
		Activity a2 = new Activity("a2", "Invite applicant to interview");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.fnActMatcher.annotateFNActivities(this.model);
		assertEquals(0, this.fnActMatcher.apply(a1, a2), 0);
	}
	
	@Test
	public void noFramesInvokedTest() {
		Activity a1 = new Activity("a1", "asd gda");
		Activity a2 = new Activity("a2", "Accepted");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.fnActMatcher.annotateFNActivities(this.model);
		assertEquals(0, this.fnActMatcher.apply(a1, a2), 0);
	}
	
	@Test
	public void matchTrivialActivitiesTest() {
		Activity a1 = new Activity("a1", "The student sends the letter");
		Activity a2 = new Activity("a2", "The student sends the letter");
		this.model = new Model();
		this.model.addActivity(a1);
		this.model.addActivity(a2);
		this.fnActMatcher.annotateFNActivities(this.model);
		System.out.println(this.fnActMatcher.apply(a1, a2));
		assertEquals(1, this.fnActMatcher.apply(a1, a2), 0);
	}
	
	@Test
	public void majorityVoteFrameNullTest() {
		List<Frame> frames = null;
		assertEquals(new ArrayList<>(), this.fnActMatcher.majorityVoteFrames(frames));
		List<Frame> framesZero = new ArrayList<>();
		assertEquals(new ArrayList<>(), this.fnActMatcher.majorityVoteFrames(framesZero));
	}
	
	@Test
	public void majorityVoteSingleFrameTest() {
		List<Frame> frames = new ArrayList<>();
		frames.add(new Frame("test3", "Text", 5));
		frames.add(new Frame("tes3t3", "Text", 5));
		frames.add(new Frame("test3", "Text", 543));
		frames.add(new Frame("test1", "Sending", 1));
		frames.add(new Frame("test4", "Sending", 0));
		frames.add(new Frame("test4", "Respond_to_proposal", 2));
		assertEquals(new Frame("testtest", "Text", 0), this.fnActMatcher.majorityVoteFrames(frames).get(0));
	}
	
	@Test
	public void majorityVoteMultipleFramesTest() {
		List<Frame> frames = new ArrayList<>();
		frames.add(new Frame("test3", "Text", 5));
		frames.add(new Frame("tes3t3", "Text", 5));
		frames.add(new Frame("test1", "Sending", 1));
		frames.add(new Frame("test4", "Sending", 0));
		frames.add(new Frame("test4", "Respond_to_proposal", 2));
		List<Frame> majFrames = new ArrayList<>();
		majFrames.add(new Frame("tes3t3", "Text", 5));
		majFrames.add(new Frame("test1", "Sending", 1));
		assertEquals(majFrames, this.fnActMatcher.majorityVoteFrames(frames));
	}
	

}
