package de.unima.ki.mamba.pm.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.linguatools.disco.CorruptConfigFileException;
import de.unima.ki.mamba.exceptions.AlignmentException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.pm.matcher.BasicMatcher;
import de.unima.ki.mamba.pm.model.Model;
import de.unima.ki.mamba.pm.model.parser.Parser;
import de.unima.ki.mamba.pm.model.parser.ParserFactory;

public class RunBasicMatcher {

	private static BasicMatcher basicMatcher;
	private static Parser parser;
	private static final String[] MODELS_DATASET1;
	private static final String[] MODELS_DATASET2;
	private static final String MODELS_DATASET1_SRC;
	private static final String MODELS_DATASET2_SRC;
	private static final String RESULTS_DATASET1_SRC;
	private static final String RESULTS_DATASET2_SRC;
	
	static {
		MODELS_DATASET1 = new String[]  {
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
		MODELS_DATASET2 = new String[] {
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
		MODELS_DATASET1_SRC = "src/main/resources/data/dataset1/models/";
		MODELS_DATASET2_SRC = "src/main/resources/data/dataset2/models/";
		RESULTS_DATASET1_SRC = "src/main/resources/data/results/basicmatcher1/dataset1/";
		RESULTS_DATASET2_SRC = "src/main/resources/data/results/basicmatcher1/dataset2/";
	}
	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, 
			IOException, AlignmentException, CorruptConfigFileException {
		/**
		 * Match BPMN dataset1
		 */
		List<Model> models1 = readModels(MODELS_DATASET1, Parser.TYPE_BPMN, MODELS_DATASET1_SRC);
		matchDataset(models1, RESULTS_DATASET1_SRC);
		/**
		 * Match PNML dataset2
		 */
		List<Model> models2 = readModels(MODELS_DATASET2, Parser.TYPE_PNML, MODELS_DATASET2_SRC);
		matchDataset(models2, RESULTS_DATASET2_SRC);
		
	}
	
	public static void matchDataset(List<Model> models, String output) throws ParserConfigurationException, 
			SAXException, IOException, AlignmentException, CorruptConfigFileException {
		basicMatcher = new BasicMatcher();
		Alignment alignment;
		for (int i = 0; i < models.size() - 1; i++) {
			Model soureModel = models.get(i);
			for (int j = i+1; j < models.size(); j++) {	
				Model targetModel = models.get(j);
				String mappingId = soureModel.getId() + "-" + targetModel.getId();
				basicMatcher.setNamespacePrefixes("http://" + soureModel.getId() + "#", "http://" + targetModel.getId() + "#");
				alignment = basicMatcher.match(soureModel, targetModel);
				System.out.println(mappingId + ": " + alignment.size());
				alignment.write(output +  mappingId + ".rdf");
			}
		}
	}
	
	public static List<Model> readModels(String[] modelIds, String modelType, String src) throws ParserConfigurationException, 
			SAXException, IOException {
		List<Model> models = new ArrayList<>();
		parser = ParserFactory.getParser(modelType);
		for(String modelId : modelIds) {
			Model model = parser.parse(src + modelId + "." + modelType);
			model.setId(modelId);
			models.add(model);
		}
		return models;
	}
}
