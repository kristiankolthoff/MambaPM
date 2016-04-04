package de.unima.ki.mamba.om.alignment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.unima.ki.mamba.exceptions.AlignmentException;
import de.unima.ki.mamba.pm.model.Model;


public class Alignment implements Iterable<Correspondence>{

	
	/**
	* Simple text-based proprietary format. One line per correspondence. Easy to read by a human. 
	*/
	public static final int FORMAT_TXT = 0;
	
	/**
	* Standard format of the OAEI. The part in the header where metainformation (e.g. 1:1 mapping)
	* is described is not supported.
	*/
	public static final int FORMAT_RDF = 1;
	
	private List<Correspondence> correspondences = new ArrayList<Correspondence>();
	
	
	/**
	* Constructs an empty alignment.
	*/
	public Alignment() { }
	
	/**
	* Constructs a alignment from a alignment given in a file.
	* 
	* @param filepath The path to the file.
	* @param format The format of the alignment file.
	* @throws AlignmentException Thrown if the file is not available or caontains invalid format.
	*/
	public Alignment(String filepath, int format) throws AlignmentException {
		AlignmentReader mr;
		if (format == Alignment.FORMAT_TXT) {
			mr = new AlignmentReaderTxt();
		}
		else if (format == Alignment.FORMAT_RDF) {
			mr = new AlignmentReaderXml();
		}		
		else {
			throw new AlignmentException(AlignmentException.IO_ERROR, "chosen a not supported alignment format");
		}
		this.setCorrespondences(mr.getAlignment(filepath).getCorrespondences());
	}
	

	public Alignment(Alignment alignment, Model sourceModel, Model targetModel) {
		this.correspondences = new ArrayList<Correspondence>();
		for (Correspondence c : alignment) {
			String sourceId = null;
			String targetId = null;
			try {
				sourceId = new URI(c.getUri1()).getFragment();
				targetId = new URI(c.getUri2()).getFragment();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String sourceLabel = sourceModel.getLabelById(sourceId);
			String targetLabel = targetModel.getLabelById(targetId);
			Correspondence cLabeled = new Correspondence(sourceLabel, targetLabel);
			this.correspondences.add(cLabeled);
		}
	}

	
	/**
	* Constructs a alignment from a alignment given in a file, that has to be formated in 
	* the alignment API format.
	* 
	* @param filepath The path to the file.
	* @throws AlignmentException Thrown if the file is not available or caontains invalid format.
	*/
	public Alignment(String filepath) throws AlignmentException {
		this(filepath, FORMAT_RDF);
	}
	
	public void write(String filepath, int format) throws AlignmentException {
		AlignmentWriter mw;
		if (format == Alignment.FORMAT_TXT) {
			mw = new AlignmentWriterTxt();
		}
		else if (format == Alignment.FORMAT_RDF) {
			mw = new AlignmentWriterXml();
		}
		else {
			throw new AlignmentException(AlignmentException.IO_ERROR, "chosen a not supported mapping format");
		}
		mw.writeAlignment(filepath, this);
	}
	
	/**
	* Writes a mapping in RDF format to a specified path.
	* 
	* @param filepath
	* @throws MappingException
	*/
	public void write(String filepath) throws AlignmentException {
		this.write(filepath, FORMAT_RDF);
	}

	
	public void add(Correspondence c) {
		this.correspondences.add(c);
	}
	
	public void add(Alignment alignment) {
		for (Correspondence c : alignment) {
			this.correspondences.add(c);
		}
	}

	//TODO: override?
	//@Override
	public Iterator<Correspondence> iterator() {
		return correspondences.iterator();
	}
	
	public String toString() {
		String rep = "";
		for (Correspondence c : this) {
			rep += c + "\n";
		}
		return rep;
		
	}

	public void setCorrespondences(List<Correspondence> correspondences) {
		this.correspondences = correspondences;	
	}
	
	public List<Correspondence> getCorrespondences() {
		return this.correspondences;	
	}
	
	public int size() {
		return this.correspondences.size();
	}
	
	public void sortDescending() {
		Collections.sort(this.correspondences);
		Collections.reverse(this.correspondences);		
	}
	
	public Correspondence get(int index) {
		return this.correspondences.get(index);
	}

	
	/**
	* Computes this minus a given alignment.
	*  
	* @param alignment The alignment that is substracted.
	* @return This minus a given alignment.
	*/
	public Alignment minus(Alignment alignment) {
		Alignment result = new Alignment();
		for (Correspondence c : this) {
			boolean contained = false;
			for (Correspondence r : alignment) {
				if (c.equals(r)) {
					contained = true;
				}
	
			}
			if (!contained) {
				result.add(c);
			}
		}
		return result;
	}
	
	
	public boolean contained(Correspondence c) {
		for (Correspondence myC : this.correspondences) {
			if (myC.equals(c)) {
				return true;
			}
		}
		return false;
	}


	public void applyThreshold(double t) {
		ArrayList<Correspondence> thresholded = new ArrayList<Correspondence>();
		for (Correspondence c : this.correspondences) {
			if (c.getConfidence() >= t) {
				thresholded.add(c);
			}
			
		}
		this.correspondences = thresholded;
	}


	public boolean isEmpty() {
		return this.correspondences.isEmpty();
	}
	
	public void join(Alignment a) {
		for(Correspondence c : a) {
			if(!this.contained(c)) {
				this.add(c);
			}
		}
	}
	
	public static Alignment join(Alignment a1, Alignment a2) {
		Alignment align = new Alignment();
		for(Correspondence c : a1) {
			align.add(c);
		}
		for(Correspondence c : a2) {
			if(!align.contained(c)) {
				align.add(c);
				System.out.println(c);
			}
		}
		return align;
	}
}
