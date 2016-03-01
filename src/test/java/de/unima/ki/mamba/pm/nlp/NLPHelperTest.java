package de.unima.ki.mamba.pm.nlp;


import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import de.unima.ki.mamba.pm.nlp.NLPHelper;


public class NLPHelperTest {

	@Test
	public void getPOSTest() {
		assertEquals("noun",NLPHelper.getPOS("documents").iterator().next().toString());
	}
	
	@Test
	public void sanitizeLabelsTest() {
		final String label = " Send letter of rejection   ";
		assertEquals("send letter of rejection", NLPHelper.getSanitizeLabel(label));
		final String label2 = "The student attends the university.";
		assertEquals("the student attends the university .", NLPHelper.getSanitizeLabel(label2));
		final String label3 = "Send letter of \nprovisional acceptance";
		assertEquals("send letter of provisional acceptance", NLPHelper.getSanitizeLabel(label3));
		final String label4 = "Attach bachelor's certificate";
		assertEquals("attach bachelor 's certificate", NLPHelper.getSanitizeLabel(label4));
		final String label5 = "Find Results IN the documents";
		assertEquals("find results in the documents", NLPHelper.getSanitizeLabel(label5));
	}
	
	public void getWordStemsTest() {
		List<String> stems = NLPHelper.getStemmedTokens("Sending bachelors documents to universities");
		assertEquals("send", stems.get(0));
		assertEquals("document", stems.get(2));
	}
	
	public static void main(String[] args) {
		
		String x = "run";
		String x1 = x.substring(0, x.length() - 1);
		System.out.println(x1);
		System.out.println(x1 + "e");


		System.out.println(NLPHelper.getPOS("documents"));
		
	}
	
	


}
