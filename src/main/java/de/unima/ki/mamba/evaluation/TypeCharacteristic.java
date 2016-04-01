package de.unima.ki.mamba.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.unima.ki.mamba.exceptions.CorrespondenceException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.Correspondence;
import de.unima.ki.mamba.om.alignment.CorrespondenceType;


public class TypeCharacteristic extends Characteristic{

	private Map<CorrespondenceType, Integer> numOfGoldMap;
	private Map<CorrespondenceType, Integer> numOfMatcherMap;
	private Map<CorrespondenceType, Alignment> alignmentGold;
	private Map<CorrespondenceType, Double> recallMap;
	
	public TypeCharacteristic(Alignment mapping, Alignment reference) throws CorrespondenceException {
		super(mapping, reference);
		this.numOfGoldMap = new HashMap<>();
		this.numOfMatcherMap = new HashMap<>();
		this.alignmentGold = new HashMap<>();
		this.recallMap = new HashMap<>();
		this.initNumOfGoldMap();
		this.initNumOfMatcherMap();
		this.init(mapping, reference);
		this.initRecallMap();
	}
	
	private void init(Alignment mapping, Alignment reference) throws CorrespondenceException {
		/**
		 * Init number of types of gold standard
		 */
		for(Correspondence cRef : reference.getCorrespondences()) {
			if(cRef.getType().isPresent()) {
				CorrespondenceType type = cRef.getType().get();
				this.numOfGoldMap.put(type, this.numOfGoldMap.get(type) + 1);
				Alignment alignType = this.alignmentGold.get(type);
				alignType.add(cRef);
				this.alignmentGold.put(type, alignType);
				
			} else {
				throw new CorrespondenceException(CorrespondenceException.MISSING_TYPE_ANNOTATION);
			}
		}
		/**
		 * Init number of types of correct mappings
		 */
		for(Correspondence cMap : this.alignmentCorrect.getCorrespondences()) {
			CorrespondenceType type = cMap.getType().get();
			this.numOfMatcherMap.put(type, this.numOfMatcherMap.get(type) + 1);
		}
	}
	
	private void initNumOfGoldMap() {
		for(CorrespondenceType type : CorrespondenceType.values()) {
			this.numOfGoldMap.put(type, 0);
			this.alignmentGold.put(type, new Alignment());
		}
	}
	
	private void initNumOfMatcherMap() {
		for(CorrespondenceType type : CorrespondenceType.values()) {
			this.numOfMatcherMap.put(type, 0);
		}
	}
	
	private void initRecallMap() {
		for(CorrespondenceType type : CorrespondenceType.values()) {
			this.recallMap.put(type, Characteristic.computeRecall(
					this.numOfMatcherMap.get(type), this.numOfGoldMap.get(type)));
		}
	}
	
	/**
	 * Returns the number of correspondences of a given type category for the matcher
	 * @param type - the type of the correspondence
	 * @return number of correspondences
	 * @throws CorrespondenceException
	 */
	public int getNumOfMatcher(CorrespondenceType type) throws CorrespondenceException {
		return this.numOfMatcherMap.get(type);
	}
	
	/**
	 * Returns the number of correspondences of a given type category for the gold standard
	 * @param type - the type of the correspondence
	 * @return number of correspondences
	 * @throws CorrespondenceException
	 */
	public int getNumOfGold(CorrespondenceType type) throws CorrespondenceException {
		return this.numOfGoldMap.get(type);
	}

	/**
	 * Returns the recall for a given type category.
	 * @param type the type of the correspondence to obtain the recall from
	 * @return the recall for the type category
	 * @throws CorrespondenceException 
	 */
	public double getRecall(CorrespondenceType type) {
		return this.getConfSumCorrect(type) / this.getConfSumReference(type);
	}
	
	private double getConfSumReference(CorrespondenceType type) {
		Alignment alignment = this.alignmentGold.get(type);
		double sum = 0;
		for(Correspondence c : alignment) {
			sum += c.getConfidence();
		}
		return sum;
	}

	
	private double getConfSumCorrect(CorrespondenceType type) {
		Alignment alignment = this.alignmentGold.get(type);
		double sumCorr = 0;
		for(Correspondence cCorr : this.alignmentCorrect) {
			for(Correspondence cRef : alignment) {
				if(cRef.equals(cCorr)) {
					sumCorr += cRef.getConfidence();
				}
			}
		}
		return sumCorr;
	}
	
	/**
	 * Returns the macro recall for a given correspondence type for mutliple
	 * <code>TypeCharacteristc</code>s.
	 * @param characteristics - the characteristics to compute the macro recall from
	 * @param type - the correpsondence type
	 * @return macro recall
	 * @throws CorrespondenceException
	 */
	public static double getRecallMacro(List<TypeCharacteristic> characteristics, CorrespondenceType type) {
		Objects.requireNonNull(characteristics);
		double sum = 0;
		int numOfOcc = 0;
		for(TypeCharacteristic c : characteristics) {
			double currRecall = c.getRecall(type);
			if(!Double.isNaN(currRecall)) {
				sum += c.getRecall(type);
				numOfOcc++;
			}
		}
		return sum / numOfOcc;
	}
	
	/**
	 * Returns the micro recall for a given correspondence type for multiple
	 * <code>TypeCharacteristic</code>s.
	 * @param characteristics - the characteristics to compute the macro recall from
	 * @param type - the correspondence type
	 * @return micro recall
	 * @throws CorrespondenceException 
	 */
	public static double getRecallMicro(List<TypeCharacteristic> characteristics, CorrespondenceType type) {
		Objects.requireNonNull(characteristics);
		double sumCorr = 0;
		double sum = 0;
		for(TypeCharacteristic c : characteristics) {
			sumCorr += c.getConfSumCorrect(type);
			sum += c.getConfSumReference(type);
		}
		return sumCorr / sum;
	}
	
	/**
	 * Returns recall standard deviation for a given correspondence type for multiple
	 * <code>TypeCharacteristic</code>s.
	 * @param characteristics - the characteristics to compute the macro recall from
	 * @param type - the correspondence type
	 * @return recall standard deviation
	 * @throws CorrespondenceException 
	 */
	public static double getRecallStdDev(List<TypeCharacteristic> characteristics, CorrespondenceType type) {
		Objects.requireNonNull(characteristics);
		double avgMacro = TypeCharacteristic.getRecallMacro(characteristics, type);
		double dev = 0;
		int numOfOcc = 0;
		for(TypeCharacteristic c : characteristics) {
			double currRecall = c.getRecall(type);
			if(!Double.isNaN(currRecall)) {
				double currDev = Math.abs(currRecall - avgMacro);
				dev += Math.pow(currDev, 2);
				numOfOcc++;
			}
		}
		return Math.sqrt(dev/numOfOcc);
	}

	public Map<CorrespondenceType, Double> getRecallMap() {
		return recallMap;
	}
	
	public static double getTypePercentage(List<TypeCharacteristic> characteristics, CorrespondenceType type) 
			throws CorrespondenceException {
		Objects.requireNonNull(characteristics);
		int sumAll = 0;
		int sumType = 0;
		for(TypeCharacteristic c : characteristics) {
			sumAll += c.getNumOfRulesGold();
			sumType += c.getNumOfGold(type);
		}
		return sumType / (double) sumAll;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<CorrespondenceType, Double> e : this.recallMap.entrySet()) {
			sb.append("R-" + e.getKey() + ": " + (100.0 * e.getValue()) + "%\n");
		}
		sb.append("Precision: " + (100.0 * this.getPrecision()) + "%\n");
		return super.toString() + sb.toString();
	}

}