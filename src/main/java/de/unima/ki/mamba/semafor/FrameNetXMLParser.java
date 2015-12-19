package de.unima.ki.mamba.semafor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unima.ki.mamba.semafor.model.Frame;

public class FrameNetXMLParser {

	private FrameNetService fnService;
	private DocumentBuilder db;
	private Document doc;
	
	public FrameNetXMLParser(FrameNetService fnService) throws ParserConfigurationException {
		this.fnService = fnService;
		this.db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	}
	
	public List<Frame> fetchFNData() throws ParserConfigurationException, SAXException, IOException {
		doc = db.parse(FrameNetOptions.ABS_PATH_FNDATA + FrameNetOptions.FN_FILE_OUT_NAME);
		Element rootElem = doc.getDocumentElement();
		NodeList nList = doc.getElementsByTagName("sentence");
		/**Iterate over each sentence**/
		for (int i = 0; i < nList.getLength(); i++) {
			Node n =  nList.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				Element eSentence = (Element) n;
				String currSentence = eSentence.getElementsByTagName("text").item(0).getTextContent();
				System.out.println("----Sentence-----" + currSentence);
				Element eAnnotationSet = (Element) eSentence.getElementsByTagName("annotationSets").item(0);
				NodeList nFrameSets = eAnnotationSet.getElementsByTagName("annotationSet");
				/**Iterate over each invoked frame for the current sentence**/
				for (int j = 0; j < nFrameSets.getLength(); j++) {
					Element currAnnoSet = (Element) nFrameSets.item(j);
					System.out.println(currAnnoSet.getAttribute("frameName"));
					Element eLayers = (Element) currAnnoSet.getElementsByTagName("layers").item(0);
					NodeList nLayers = eLayers.getElementsByTagName("layer");
					/**Extract Target indices from layer id=1**/
					Element eTarget = (Element) nLayers.item(0);
					Element eTargetLabels = (Element) eTarget.getElementsByTagName("labels").item(0);
					Element eTargetLabel = (Element) eTargetLabels.getElementsByTagName("label").item(0);
					int targetStart = Integer.valueOf(eTargetLabel.getAttribute("start"));
					int targetEnd = Integer.valueOf(eTargetLabel.getAttribute("end"));
					/**Extract frame elements from layer id=2**/
					Element eFrameElements = (Element) nLayers.item(1);
					Element eFrameLabels = (Element) eFrameElements.getElementsByTagName("labels").item(0);
					NodeList nFrameLabels = eFrameLabels.getElementsByTagName("label");
					for (int k = 0; k < nFrameLabels.getLength(); k++) {
						Element eFrameElement = (Element) nFrameLabels.item(k);
						System.out.println(eFrameElement.getAttribute("name"));
					}
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		try {
			FrameNetXMLParser fnParser = new FrameNetXMLParser(new FrameNetService());
			try {
				fnParser.fetchFNData();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
