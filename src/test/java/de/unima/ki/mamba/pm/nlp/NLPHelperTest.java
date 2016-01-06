package de.unima.ki.mamba.pm.nlp;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.unima.ki.mamba.pm.nlp.NLPHelper;


public class NLPHelperTest {

	private NLPHelper nlpHelper;
	
	@Before
	public void init() {
		this.nlpHelper = new NLPHelper();
	}

	@Test
	public void getPOSTest() {
		assertEquals("noun",this.nlpHelper.getPOS("documents").iterator().next().toString());
	}
	
	public static void main(String[] args) {
		
		String x = "run";
		String x1 = x.substring(0, x.length() - 1);
		System.out.println(x1);
		System.out.println(x1 + "e");


		NLPHelper nlphelper = new NLPHelper();
		System.out.println(nlphelper.getPOS("documents"));
		
		
	}
	
	


}
