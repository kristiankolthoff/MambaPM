package de.unima.ki.mamba.pm.examples;


import de.unima.ki.mamba.pm.nlp.NLPHelper;
import edu.mit.jwi.item.POS;



public class WordNetTests {

	

	
	public static void main(String[] args) {
		
		String x = "run";
		String x1 = x.substring(0, x.length() - 1);
		System.out.println(x1);
		System.out.println(x1 + "e");


		NLPHelper nlphelper = new NLPHelper();
		System.out.println(nlphelper.getPOS("documents"));
		
		
	}
	
	


}
