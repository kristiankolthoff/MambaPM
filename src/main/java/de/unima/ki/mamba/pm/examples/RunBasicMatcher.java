package de.unima.ki.mamba.pm.examples;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unima.ki.mamba.exceptions.AlignmentException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.pm.matcher.BasicMatcher;

public class RunBasicMatcher {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws AlignmentException 
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, AlignmentException {
		System.out.println(new File(".").getCanonicalPath());
		matchDataset2();
		
	}
	
	public static void matchDataset1() throws ParserConfigurationException, SAXException, IOException, AlignmentException {
		
		String[] modelIds = new String[]{
				"Cologne",
				"Frankfurt",
				"FU_Berlin",
				"Hohenheim",
				"IIS_Erlangen",
				"Muenster",
				"Potsdam",
				"TU_Munich",
				"Wuerzburg"
		};
		
		BasicMatcher bm = new BasicMatcher();
		bm.setParser("bpmn");
		
		Alignment alignment;
		for (int i = 0; i < modelIds.length - 1; i++) {
			String sourceId = modelIds[i];
			for (int j = i+1; j < modelIds.length; j++) {	
				String targetId = modelIds[j];
				String mappingId = sourceId + "-" + targetId;
				bm.setNamespacePrefixes("http://" + sourceId+ "#", "http://" + targetId+ "#");
				bm.match("src/resources/data/dataset1/models/" + sourceId + ".bpmn", "src/resources/data/dataset1/models/" + targetId + ".bpmn");
				alignment = bm.getAlignment();
				System.out.println(mappingId + ": " + alignment.size());
				alignment.write("src/resources/data/results/basicmatcher1/dataset1/" +  mappingId + ".rdf");
			}
		}
		
	}
	
	public static void matchDataset2() throws ParserConfigurationException, SAXException, IOException, AlignmentException {
		
		String[] modelIds = new String[]{
				"birthCertificate_p31",
				"birthCertificate_p32",
				"birthCertificate_p33",
				"birthCertificate_p34",
				"birthCertificate_p246",
				"birthCertificate_p247",
				"birthCertificate_p248",
				"birthCertificate_p249",
				"birthCertificate_p250"
		};
		
		BasicMatcher bm = new BasicMatcher();
		bm.setParser("pnml");
		
		
		Alignment alignment;
		for (int i = 0; i < modelIds.length - 1; i++) {
			String sourceId = modelIds[i];
			for (int j = i+1; j < modelIds.length; j++) {	
				String targetId = modelIds[j];
				String mappingId = sourceId + "-" + targetId;
				bm.setNamespacePrefixes("http://" + sourceId+ "#", "http://" + targetId+ "#");
				bm.match("src/resources/data/dataset2/models/" + sourceId + ".pnml", "src/resources/data/dataset2/models/" + targetId + ".pnml");
				alignment = bm.getAlignment();
				System.out.println(mappingId + ": " + alignment.size());
				alignment.write("src/resources/data/results/basicmatcher1/dataset2/" +  mappingId + ".rdf");
			}
		}
		
		
	}
	
	
		
	
	

}
