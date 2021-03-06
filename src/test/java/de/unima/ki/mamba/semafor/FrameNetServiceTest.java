package de.unima.ki.mamba.semafor;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FrameNetServiceTest {

	private FrameNetService fnService;
	private List<String> sentences;
	
	@Before
	public void init() {
		final String javaHomePath = "/usr/lib/jvm/java-8-oracle/bin";
		this.fnService = new FrameNetService(FrameNetOptions.getStandardOpt(javaHomePath));
		this.sentences = new ArrayList<String>();
		this.sentences.add("Send invitation to the student");
	}
	
	@Test
	public void createAnnotationFileTest() {
		this.fnService.createAnnotationFile(sentences);
		File f = new File(FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME);
		assertEquals(true, f.exists());
	}
	
	@Test
	public void runFNSemanticParsingTest() {
		this.fnService.runFNSemanticParsing();
		File f = new File(FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_OUT_NAME);
		assertEquals(true, f.exists());
	}
	
	@Test
	public void runDirCleanerTest() {
		this.fnService.runFNSemanticParsing();
		File fIn = new File(FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_NAME);
		File fOut = new File(FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_OUT_NAME);
		this.fnService.cleanAll();
		assertEquals(false, fIn.exists());
		assertEquals(false, fOut.exists());
	}
	
	@After
	public void clean() {
		this.fnService.cleanAll();
	}
	
}
