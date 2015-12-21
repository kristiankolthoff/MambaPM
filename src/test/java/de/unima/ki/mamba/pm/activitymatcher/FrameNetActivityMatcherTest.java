package de.unima.ki.mamba.pm.activitymatcher;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.junit.Before;
import org.junit.Test;

import de.linguatools.disco.CorruptConfigFileException;
import de.linguatools.disco.WrongWordspaceTypeException;

public class FrameNetActivityMatcherTest {

	private FrameNetActivityMatcher fnActMatcher;
	
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
		}
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
}
