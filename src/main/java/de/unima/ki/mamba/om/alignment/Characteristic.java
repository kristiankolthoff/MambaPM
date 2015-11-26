// *****************************************************************************
//
// Copyright (c) 2011 Christian Meilicke (University of Mannheim)
//
// Permission is hereby granted, free of charge, to any person
// obtaining a copy of this software and associated documentation
// files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge,
// publish, distribute, sublicense, and/or sell copies of the Software,
// and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
// IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// *********************************************************************************

package de.unima.ki.mamba.om.alignment;


import java.text.DecimalFormat;




/**
* Characterises the relation between two mappings in terms of recall, precision and f-value.
*/
public class Characteristic {
	
	private int numOfRulesGold;
	private int numOfRulesMatcher;
	private int numOfRulesCorrect;
	
	/**
	* If set to false, it uses jeromes way of counting. 
	*/
	private static boolean strictEvaluation = false;
	
	/**
	* Constructs an empty characteristic which is a characteristic for an mapping of cardinality zero. 
	*
	*/
	public Characteristic() {
		this(0,0,0);
	}
	
	/**
	* Constructs a characteristic.
	* 
	* @param numOfRulesGold Number of correspondences of the reference mapping.
	* @param numOfRulesMatcher Number of correspondences in the mapping under discussion
	* (in most cases the mapping generated by a matching system). 
	* @param numOfRulesCorrect Number of correspondences that are both the reference mapping and the 
	* generated mapping.
	*/
	protected Characteristic(int numOfRulesGold, int numOfRulesMatcher, int numOfRulesCorrect) {
		this.numOfRulesGold = numOfRulesGold;
		this.numOfRulesMatcher = numOfRulesMatcher;
		this.numOfRulesCorrect = numOfRulesCorrect;
	}
	
	/**
	* Constructs a characteristic based by comparing two mappings.
	* 
	* @param mapping The mapping under discussion.
	* @param reference The reference mapping.
	* @throws ALCOMOException Thrown if the namespaces of the mappings differ.
	*/
	public Characteristic(Alignment mapping, Alignment reference) {
		// Mapping correct = reference.getIntersection(mapping); 
		Alignment correct = new Alignment();
		if (strictEvaluation) {
			for (Correspondence r : reference) {
				for (Correspondence m : mapping) {
					if (m.equals(r)) {
						correct.add(r);
					}
				}
			}
		}
		else {
			for (Correspondence r : reference) {
				for (Correspondence m : mapping) {
					if (m.getUri1().equals(r.getUri1()) && m.getUri2().equals(r.getUri2())) {
						correct.add(r);
					}
				}
			}			
		}
		
		this.numOfRulesGold = reference.size();
		this.numOfRulesMatcher = mapping.size();
		this.numOfRulesCorrect = correct.size();
	}	
	
	/**
	* Joins this mapping with another mapping by summing up relevant characteristics
	* in absolute numbers. Larger matching problems are thus to a greater extent weighted.
	*  
	* @param c The other charcteristic.
	*/
	public void join(Characteristic c) {
		this.numOfRulesCorrect += c.getNumOfRulesCorrect();
		this.numOfRulesGold += c.getNumOfRulesGold();
		this.numOfRulesMatcher += c.getNumOfRulesMatcher();
	}
	
	/**
	* Returns a string representation. 
	* 
	* @return A string representation.
	*/
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Precision: " + (100.0 * this.getPrecision()) + "%\n");
		sb.append("Recall:    " + (100.0 * this.getRecall()) + "%\n");
		sb.append("F-measure: " + (100.0 * this.getFMeasure()) + "%\n");
		sb.append("Gold: " + this.numOfRulesGold + " Matcher: " + numOfRulesMatcher +  " Correct: " + numOfRulesCorrect + "\n");
		return sb.toString();
	}

	/**
	* Returns the f-measure.
	* @return The f-measure.
	*/
	public double getFMeasure() {
		if ((this.getPrecision() == 0.0f) || (this.getRecall() == 0.0f)) { return 0.0f; }
		return (2 * this.getPrecision() * this.getRecall()) / (this.getPrecision() + this.getRecall());
	}
	

	public static double computeFFromPR(double precision, double recall) {
		if ((precision == 0.0f) || (recall == 0.0f)) { return 0.0f; }
		return (2 * precision * recall) / (precision + recall);
	}
	
	
	
	public String getF() {
		return toDecimalFormat(this.getFMeasure());
	}

	/**
	* Returns the precision.
	* 
	* @return The precision.
	*/
	public double getPrecision() {
		return (double)this.numOfRulesCorrect /  (double)this.numOfRulesMatcher;
	}
	
	public String getP() {
		return toDecimalFormat(this.getPrecision());
	}
	
	/**
	* Returns the recall.
	* 
	* @return The recall.
	*/
	public double getRecall() {
		return (double)this.numOfRulesCorrect /  (double)this.numOfRulesGold;
	}
	
	public String getR() {
		return toDecimalFormat(this.getRecall());
	}

	
	public int getNumOfRulesCorrect() {
		return numOfRulesCorrect;
	}

	public int getNumOfRulesGold() {
		return numOfRulesGold;
	}

	public int getNumOfRulesMatcher() {
		return numOfRulesMatcher;
	}

	public String toShortDesc() {
		double precision = this.getPrecision();
		double recall = this.getRecall();
		double f = this.getFMeasure();

		return toDecimalFormat(precision) + "\t" + toDecimalFormat(recall) + "\t" + toDecimalFormat(f);
	}

	private static String toDecimalFormat(double precision) {
		DecimalFormat df = new DecimalFormat("0.000");
		return df.format(precision).replace(',', '.');
	}

	public static void useDiffuseEvaluation() {
		strictEvaluation = false;
		
	}

	public static boolean strictEvaluationActive() {
		return strictEvaluation;
	}






	
}
