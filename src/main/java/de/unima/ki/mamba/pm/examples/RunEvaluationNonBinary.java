package de.unima.ki.mamba.pm.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unima.ki.mamba.evaluation.Characteristic;
import de.unima.ki.mamba.evaluation.renderer.HTMLTableNBRenderer;
import de.unima.ki.mamba.evaluation.renderer.Renderer;
import de.unima.ki.mamba.exceptions.AlignmentException;
import de.unima.ki.mamba.exceptions.CorrespondenceException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.AlignmentReader;
import de.unima.ki.mamba.om.alignment.AlignmentReaderXml;


public class RunEvaluationNonBinary {

	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, 
			IOException, AlignmentException, CorrespondenceException {
		/**
		 * Define multiple directories with matcher alginments.
		 * First entry of the array should be the directory of the reference alignment.
		 */
		final String OUTPUT_PATH = "src/main/resources/data/evaluation/mes-nonbinary-new-gs.html";
		final String RESULTS_PATH = "src/main/resources/data/results/submitted-matchers/";
		final String END_DIR = "/dataset1";
		final String[] dirs = new String[] {
				"src/main/resources/data/results/goldstandard/dataset1_goldstandard_experts",
//				"src/main/resources/data/results/basicmatcher1/dataset1-majvote",
				RESULTS_PATH + "AML-PM" + END_DIR,
				RESULTS_PATH + "BPLangMatch" + END_DIR,
				RESULTS_PATH + "KnoMa-Proc" + END_DIR,
				RESULTS_PATH + "Know-Match-SSS" + END_DIR,
				RESULTS_PATH + "Match-SSS" + END_DIR,
				RESULTS_PATH + "OPBOT" + END_DIR,
				RESULTS_PATH + "pPalm-DS" + END_DIR,
				RESULTS_PATH + "RMM-NHCM" + END_DIR,
				RESULTS_PATH + "RMM-NLM" + END_DIR,
				RESULTS_PATH + "RMM-SMSL" + END_DIR,
				RESULTS_PATH + "RMM-VM2" + END_DIR,
				RESULTS_PATH + "TripleS" + END_DIR
		};
		final String[] mappingInfo = new String[] {
//				"MambaPM",
				"AML-PM",
				"BPLangMatch",
				"KnoMa-Proc",
				"Know-Match-SSS",
				"Match-SSS",
				"OPBOT",
				"pPalm-DS",
				"RMM-NHCM",
				"RMM-NLM",
				"RMM-VM2",
				"RMM-SMSL",
				"TripleS"
		};
		/**
		 * Read all alignments from gold standard and matchers 
		 */
		final AlignmentReader alignReader = new AlignmentReaderXml();
		List<List<Alignment>> alignments = new ArrayList<>();
		for (int i = 0; i < dirs.length; i++) {
			List<Alignment> aligns = new ArrayList<>();
			Files.walk(Paths.get(dirs[i])).forEach(filePath -> {
				if(Files.isRegularFile(filePath)) {
					try {
						Alignment a = alignReader.getAlignment(filePath.toString());
						aligns.add(a);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		});
			alignments.add(aligns);
		}
		/**
		 * Compute typecharacteristcs for each alignment
		 */
		List<List<Characteristic>> characteristics = new ArrayList<>();
		for (int i = 1; i < alignments.size(); i++) {
			List<Alignment> aligns = alignments.get(i);
			List<Characteristic> tcharacteristics = new ArrayList<>();
			for (int j = 0; j < aligns.size(); j++) {
				try {
				Alignment mapping = aligns.get(j);
				Alignment reference = alignments.get(0).get(j);
				Characteristic tc = new Characteristic(mapping, reference);
				tcharacteristics.add(tc);
				} catch(IndexOutOfBoundsException ex) {
					System.err.println("Number of reference and matcher "
							+ "alignments unequal : " + ex.getMessage());
				}
			}
			characteristics.add(tcharacteristics);
		}
		/**
		 * Render alignments to HTML evaluation summary page
		 */
		Renderer renderer = new HTMLTableNBRenderer(OUTPUT_PATH);
		for (int i = 0; i < characteristics.size(); i++) {
			renderer.render(characteristics.get(i), mappingInfo[i]);
		}
		renderer.flush();
	}

}
