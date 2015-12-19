package de.unima.ki.mamba.semafor;

import org.junit.Before;
import org.junit.Test;

public class FrameNetServiceTest {

	private FrameNetService fnService;
	
	@Before
	public void init() {
		fnService = new FrameNetService();
	}
	
	@Test
	public void runFNSemanticParsingTest() {
		fnService.runFNSemanticParsing();
	}
}
