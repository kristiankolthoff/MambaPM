package de.unima.ki.mamba.semafor;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class FrameNetServiceTest {

	private FrameNetService fnService;
	
	@Before
	public void init() {
		try {
			fnService = new FrameNetService();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void runFNSemanticParsingTest() {
		fnService.runFNSemanticParsing();
	}
}
