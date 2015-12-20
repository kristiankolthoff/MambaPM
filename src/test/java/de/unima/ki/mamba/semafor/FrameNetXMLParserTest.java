package de.unima.ki.mamba.semafor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.unima.ki.mamba.semafor.model.FElement;
import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetXMLParserTest {

	private FrameNetXMLParser fnParser;
	
	@Before
	public void init() {
		try {
			this.fnParser = new FrameNetXMLParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void fetchFNDataTest() {
		final String testXMLPath = FrameNetOptions.ABS_PATH_SEMAFOR + "/test/framenet-parsing-test.txt.out";
		try {
			HashMap<String, List<Frame>> frameMap = this.fnParser.fetchFNData(testXMLPath);
			assertEquals(3, frameMap.size());
			List<Frame> frames = frameMap.get("The student sends the letter to his girlfriend .");
			assertEquals(4, frames.size());
			Frame educationFrame = frames.get(0);
			assertEquals(0, educationFrame.getRank());
			Frame sendingFrame = frames.get(1);
			Frame relationshipFrame = frames.get(2);
			assertEquals("Sending", sendingFrame.getName());
			assertEquals("Personal_relationship", relationshipFrame.getName());
			
			List<FElement> fElements = sendingFrame.getfElements();
			assertEquals("Sender", fElements.get(0).getName());
			assertEquals("Theme", fElements.get(1).getName());
			assertEquals("Recipient", fElements.get(2).getName());
			assertEquals("The student", fElements.get(0).getContent());
			assertEquals("the letter", fElements.get(1).getContent());
			assertEquals("to his girlfriend", fElements.get(2).getContent());
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
