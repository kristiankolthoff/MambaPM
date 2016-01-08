package de.unima.ki.mamba.om.alignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;




public class Correspondence implements Comparable<Correspondence> {

	private String uri1;
	private String uri2;
	private double confidence;
	private SemanticRelation relation;
	private Optional<String> type;
	
	public static final String TYPE_DIFFICULT = "difficult";
	public static final String TYPE_DIFFICULT_SIMILAR_VERB = "difficult-similar-verb";
	public static final String TYPE_ONE_WORD_SIMILAR = "one-word-similar";
	public static final String TYPE_TRIVIAL = "trivial";
	public static final String TYPE_TRIVIAL_BASIC_NORM = "trivial-after-basic-normalization";
	public static final String TYPE_TRIVIAL_EXTENDED_NORM = "trivial-after-extended-normalization";
	public static final String TYPE_MISC = "misc";
	
	private static HashMap<SemanticRelation, String> symbolTable = new HashMap<SemanticRelation, String>();
	private static List<String> types = new ArrayList<String>();;
	
	static {
		symbolTable.put(SemanticRelation.EQUIV, "=");
		symbolTable.put(SemanticRelation.SUB, "<");
		symbolTable.put(SemanticRelation.SUPER, ">");
		symbolTable.put(SemanticRelation.DISJOINT, "!=");
		symbolTable.put(SemanticRelation.NA, "?");
		types.add(TYPE_DIFFICULT);
		types.add(TYPE_DIFFICULT_SIMILAR_VERB);
		types.add(TYPE_ONE_WORD_SIMILAR);
		types.add(TYPE_TRIVIAL);
		types.add(TYPE_TRIVIAL_BASIC_NORM);
		types.add(TYPE_TRIVIAL_EXTENDED_NORM);
		types.add(TYPE_MISC);
	}
	
	public Correspondence(String uri1, String uri2, SemanticRelation relation, double confidence, String type) {
		this.uri1 = uri1;
		this.uri2 = uri2;
		this.relation = relation;
		this.confidence = confidence;
		this.type = Optional.of(type);
	}
	
	public Correspondence(String uri1, String uri2, SemanticRelation relation, double confidence) {
		this.uri1 = uri1;
		this.uri2 = uri2;
		this.relation = relation;
		this.confidence = confidence;
		this.type = Optional.empty();
	}
	
	public Correspondence(String uri1, String uri2, SemanticRelation relation) {
		this(uri1, uri2, relation, 1.0d);
	}

	public Correspondence(String uri1, String uri2, double confidence) {
		this(uri1, uri2, SemanticRelation.EQUIV, confidence);
	}
	
	public Correspondence(String uri1, String uri2) {
		this(uri1, uri2, SemanticRelation.EQUIV, 1.0d);
	}
	
	public String getUri1() {
		return this.uri1;
	}
	
	public String getUri2() {
		return this.uri2;
	}
	
	public double getConfidence() {
		return this.confidence;
	}
	
	public SemanticRelation getRelation() {
		return this.relation;
	}
	
	public String getRelationSymbol() {
		return Correspondence.symbolTable.get(this.relation);
	}
	
	public String toString() {
		String relationSymbol = Correspondence.symbolTable.get(this.relation);
		return uri1 + " " + relationSymbol + " " + uri2 + ", " + this.confidence;
	}


	public int compareTo(Correspondence correspondence) {
		Correspondence that = (Correspondence)correspondence;
		if (this.getConfidence() > that.getConfidence()) { return 1; }	
		else if (this.getConfidence() < that.getConfidence()) { return -1; }
		else {
			if (this.getUri1().compareTo(that.getUri1()) > 0) { return 1; }	
			else if	(this.getUri1().compareTo(that.getUri1()) < 0) { return -1; }	
			else {
				if (this.getUri2().compareTo(that.getUri2()) > 0) { return 1; }	
				else if	(this.getUri2().compareTo(that.getUri2()) < 0) { return -1; }	
				else { return 0; }
			}
		}
	}	
	
	public boolean equals(Object correspondence) {
		if (correspondence instanceof Correspondence) {
			Correspondence that = (Correspondence)correspondence;
			if (this.getUri1().equals(that.getUri1()) && this.getUri2().equals(that.getUri2())) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
		
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}

	public Optional<String> getType() {
		return type;
	}

	public void setType(Optional<String> type) {
		this.type = type;
	}
	
	public static List<String> getTypes() {
		return Correspondence.types;
	}

	public static boolean isSupportedType(String type) {
		return Correspondence.types.contains(type);
	}
}
