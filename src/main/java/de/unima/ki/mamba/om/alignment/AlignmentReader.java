package de.unima.ki.mamba.om.alignment;

import de.unima.ki.mamba.exceptions.AlignmentException;

/**
* The MappingReader defines the interface for its implementing classes that can be used to read
* Mappings from xml as well as txt-files. 
*
*/

public interface AlignmentReader {
	
	public Alignment getAlignment(String filepath) throws AlignmentException;
		
}