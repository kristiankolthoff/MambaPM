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
	private Map<CorrespondenceType, Double> recallMap;
	
	public TypeCharacteristic(Alignment mapping, Alignment reference) throws CorrespondenceException {
		super(mapping, reference);
		this.numOfGoldMap = new HashMap<>();
		this.numOfMatcherMap = new HashMap<>();
		this.recallMap = new HashMap<>();
		this.initNumOfGoldMap();
		this.initNumOfMatcherMap();
		this.init(mapping, reference);
		this.initRecallMap();
	}
	
	private void init(Alignment mapping, Alignment reference) throws CorrespondenceException {
		Alignment correct = new Alignment();
		/**
		 * Find correct correspondences in the mapping alignment
		 */
		for(Correspondence cMap : mapping.getCorrespondences()) {
			for(Correspondence cRef : reference.getCorrespondences()) {
				if(cMap.equals(cRef)) {
					correct.add(cRef);
				}
			}
		}
		/**
		 * Init number of types of gold standard
		 */
		for(Correspondence cRef : reference.getCorrespondences()) {
			if(cRef.getType().isPresent()) {
				switch (cRef.getType().get()) {
				case DIFFICULT:
					this.numOfGoldMap.put(CorrespondenceType.DIFFICULT, 
							this.numOfGoldMap.get(CorrespondenceType.DIFFICULT) + 1);
					break;
				case DIFFICULT_SIMILAR_VERB:
					this.numOfGoldMap.put(CorrespondenceType.DIFFICULT_SIMILAR_VERB, 
							this.numOfGoldMap.get(CorrespondenceType.DIFFICULT_SIMILAR_VERB) + 1);
					break;
				case ONE_WORD_SIMILAR:
					this.numOfGoldMap.put(CorrespondenceType.ONE_WORD_SIMILAR, 
							this.numOfGoldMap.get(CorrespondenceType.ONE_WORD_SIMILAR) + 1);
					break;
				case TRIVIAL:
					this.numOfGoldMap.put(CorrespondenceType.TRIVIAL, 
							this.numOfGoldMap.get(CorrespondenceType.TRIVIAL) + 1);
					break;
				case TRIVIAL_BASIC_NORM:
					this.numOfGoldMap.put(CorrespondenceType.TRIVIAL_BASIC_NORM, 
							this.numOfGoldMap.get(CorrespondenceType.TRIVIAL_BASIC_NORM) + 1);
					break;
				case TRIVIAL_EXTENDED_NORM:
					this.numOfGoldMap.put(CorrespondenceType.TRIVIAL_EXTENDED_NORM, 
							this.numOfGoldMap.get(CorrespondenceType.TRIVIAL_EXTENDED_NORM) + 1);
					break;
				case MISC:
					this.numOfGoldMap.put(CorrespondenceType.MISC, 
							this.numOfGoldMap.get(CorrespondenceType.MISC) + 1);
					break;
				}
			} else {
				throw new CorrespondenceException(CorrespondenceException.MISSING_TYPE_ANNOTATION);
			}
		}
		/**
		 * Init number of types of correct mappings
		 */
		for(Correspondence cMap : correct.getCorrespondences()) {
			switch (cMap.getType().get()) {
			case DIFFICULT:
				this.numOfMatcherMap.put(CorrespondenceType.DIFFICULT, 
						this.numOfMatcherMap.get(CorrespondenceType.DIFFICULT) + 1);
				break;
			case DIFFICULT_SIMILAR_VERB:
				this.numOfMatcherMap.put(CorrespondenceType.DIFFICULT_SIMILAR_VERB, 
						this.numOfMatcherMap.get(CorrespondenceType.DIFFICULT_SIMILAR_VERB) + 1);
				break;
			case ONE_WORD_SIMILAR:
				this.numOfMatcherMap.put(CorrespondenceType.ONE_WORD_SIMILAR, 
						this.numOfMatcherMap.get(CorrespondenceType.ONE_WORD_SIMILAR) + 1);
				break;
			case TRIVIAL:
				this.numOfMatcherMap.put(CorrespondenceType.TRIVIAL, 
						this.numOfMatcherMap.get(CorrespondenceType.TRIVIAL) + 1);
				break;
			case TRIVIAL_BASIC_NORM:
				this.numOfMatcherMap.put(CorrespondenceType.TRIVIAL_BASIC_NORM, 
						this.numOfMatcherMap.get(CorrespondenceType.TRIVIAL_BASIC_NORM) + 1);
				break;
			case TRIVIAL_EXTENDED_NORM:
				this.numOfMatcherMap.put(CorrespondenceType.TRIVIAL_EXTENDED_NORM, 
						this.numOfMatcherMap.get(CorrespondenceType.TRIVIAL_EXTENDED_NORM) + 1);
				break;
			case MISC:
				this.numOfMatcherMap.put(CorrespondenceType.MISC, 
						this.numOfMatcherMap.get(CorrespondenceType.MISC) + 1);
				break;
			}
		}
	}
	
	private void initNumOfGoldMap() {
		for(CorrespondenceType type : CorrespondenceType.values()) {
			this.numOfGoldMap.put(type, 0);
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
	public double getRecall(CorrespondenceType type) throws CorrespondenceException {
		return this.recallMap.get(type);
	}
	
	/**
	 * Returns the macro recall for a given correspondence type for mutliple
	 * <code>TypeCharacteristc</code>s.
	 * @param characteristics - the characteristics to compute the macro recall from
	 * @param type - the correpsondence type
	 * @return macro recall
	 * @throws CorrespondenceException
	 */
	public static double getRecallMacro(List<TypeCharacteristic> characteristics, CorrespondenceType type) 
			throws CorrespondenceException {
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
	public static double getRecallMicro(List<TypeCharacteristic> characteristics, CorrespondenceType type) 
			throws CorrespondenceException {
		Objects.requireNonNull(characteristics);
		int sumNumOfMatcher = 0;
		int sumNumOfGold = 0;
		for(TypeCharacteristic c : characteristics) {
			sumNumOfMatcher += c.getNumOfMatcher(type);
			sumNumOfGold += c.getNumOfGold(type);
		}
		return Characteristic.computeRecall(sumNumOfMatcher, sumNumOfGold);
	}
	
	/**
	 * Returns recall standard deviation for a given correspondence type for multiple
	 * <code>TypeCharacteristic</code>s.
	 * @param characteristics - the characteristics to compute the macro recall from
	 * @param type - the correspondence type
	 * @return recall standard deviation
	 * @throws CorrespondenceException 
	 */
	public static double getRecallStdDev(List<TypeCharacteristic> characteristics, CorrespondenceType type) 
			throws CorrespondenceException {
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