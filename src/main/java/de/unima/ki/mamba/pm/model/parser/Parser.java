package de.unima.ki.mamba.pm.model.parser;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.unima.ki.mamba.pm.model.Model;

public interface Parser {
	
	public Model parse(String filepath) throws ParserConfigurationException, SAXException, IOException;

}
