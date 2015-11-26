package de.unima.ki.mamba.om.alignment;


import de.unima.ki.mamba.exceptions.AlignmentException;

/**
* The MappingReader defines the interface for its implementing classes that can be used to write
* Mappings to xml as well as txt-files. 
*
*/
public interface AlignmentWriter {
	
	public void writeAlignment(String filepath, Alignment alignment) throws AlignmentException;
	
	
		
}