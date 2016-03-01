package de.unima.ki.mamba.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.unima.ki.mamba.exceptions.CorrespondenceException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.Correspondence;


public class TypeCharacteristic extends Characteristic{

	private HashMap<String, Integer> numOfGoldMap;
	private HashMap<String, Integer> numOfMatcherMap;
	private HashMap<String, Double> recallMap;
	
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
				case Correspondence.TYPE_DIFFICULT:
					this.numOfGoldMap.put(Correspondence.TYPE_DIFFICULT, 
							this.numOfGoldMap.get(Correspondence.TYPE_DIFFICULT) + 1);
					break;
				case Correspondence.TYPE_DIFFICULT_SIMILAR_VERB:
					this.numOfGoldMap.put(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB, 
							this.numOfGoldMap.get(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB) + 1);
					break;
				case Correspondence.TYPE_ONE_WORD_SIMILAR:
					this.numOfGoldMap.put(Correspondence.TYPE_ONE_WORD_SIMILAR, 
							this.numOfGoldMap.get(Correspondence.TYPE_ONE_WORD_SIMILAR) + 1);
					break;
				case Correspondence.TYPE_TRIVIAL:
					this.numOfGoldMap.put(Correspondence.TYPE_TRIVIAL, 
							this.numOfGoldMap.get(Correspondence.TYPE_TRIVIAL) + 1);
					break;
				case Correspondence.TYPE_TRIVIAL_BASIC_NORM:
					this.numOfGoldMap.put(Correspondence.TYPE_TRIVIAL_BASIC_NORM, 
							this.numOfGoldMap.get(Correspondence.TYPE_TRIVIAL_BASIC_NORM) + 1);
					break;
				case Correspondence.TYPE_TRIVIAL_EXTENDED_NORM:
					this.numOfGoldMap.put(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM, 
							this.numOfGoldMap.get(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM) + 1);
					break;
				case Correspondence.TYPE_MISC:
					this.numOfGoldMap.put(Correspondence.TYPE_MISC, 
							this.numOfGoldMap.get(Correspondence.TYPE_MISC) + 1);
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
			case Correspondence.TYPE_DIFFICULT:
				this.numOfMatcherMap.put(Correspondence.TYPE_DIFFICULT, 
						this.numOfMatcherMap.get(Correspondence.TYPE_DIFFICULT) + 1);
				break;
			case Correspondence.TYPE_DIFFICULT_SIMILAR_VERB:
				this.numOfMatcherMap.put(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB, 
						this.numOfMatcherMap.get(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB) + 1);
				break;
			case Correspondence.TYPE_ONE_WORD_SIMILAR:
				this.numOfMatcherMap.put(Correspondence.TYPE_ONE_WORD_SIMILAR, 
						this.numOfMatcherMap.get(Correspondence.TYPE_ONE_WORD_SIMILAR) + 1);
				break;
			case Correspondence.TYPE_TRIVIAL:
				this.numOfMatcherMap.put(Correspondence.TYPE_TRIVIAL, 
						this.numOfMatcherMap.get(Correspondence.TYPE_TRIVIAL) + 1);
				break;
			case Correspondence.TYPE_TRIVIAL_BASIC_NORM:
				this.numOfMatcherMap.put(Correspondence.TYPE_TRIVIAL_BASIC_NORM, 
						this.numOfMatcherMap.get(Correspondence.TYPE_TRIVIAL_BASIC_NORM) + 1);
				break;
			case Correspondence.TYPE_TRIVIAL_EXTENDED_NORM:
				this.numOfMatcherMap.put(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM, 
						this.numOfMatcherMap.get(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM) + 1);
				break;
			case Correspondence.TYPE_MISC:
				this.numOfMatcherMap.put(Correspondence.TYPE_MISC, 
						this.numOfMatcherMap.get(Correspondence.TYPE_MISC) + 1);
				break;
			}
		}
	}
	
	private void initNumOfGoldMap() {
		this.numOfGoldMap.put(Correspondence.TYPE_DIFFICULT, 0);
		this.numOfGoldMap.put(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB, 0);
		this.numOfGoldMap.put(Correspondence.TYPE_ONE_WORD_SIMILAR, 0);
		this.numOfGoldMap.put(Correspondence.TYPE_TRIVIAL, 0);
		this.numOfGoldMap.put(Correspondence.TYPE_TRIVIAL_BASIC_NORM, 0);
		this.numOfGoldMap.put(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM, 0);
		this.numOfGoldMap.put(Correspondence.TYPE_MISC, 0);
	}
	
	private void initNumOfMatcherMap() {
		this.numOfMatcherMap.put(Correspondence.TYPE_DIFFICULT, 0);
		this.numOfMatcherMap.put(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB, 0);
		this.numOfMatcherMap.put(Correspondence.TYPE_ONE_WORD_SIMILAR, 0);
		this.numOfMatcherMap.put(Correspondence.TYPE_TRIVIAL, 0);
		this.numOfMatcherMap.put(Correspondence.TYPE_TRIVIAL_BASIC_NORM, 0);
		this.numOfMatcherMap.put(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM, 0);
		this.numOfMatcherMap.put(Correspondence.TYPE_MISC, 0);
	}
	
	private void initRecallMap() {
		this.recallMap.put(Correspondence.TYPE_DIFFICULT, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_DIFFICULT), 
						this.numOfGoldMap.get(Correspondence.TYPE_DIFFICULT)));
		this.recallMap.put(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB), 
						this.numOfGoldMap.get(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB)));
		this.recallMap.put(Correspondence.TYPE_ONE_WORD_SIMILAR, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_ONE_WORD_SIMILAR), 
						this.numOfGoldMap.get(Correspondence.TYPE_ONE_WORD_SIMILAR)));
		this.recallMap.put(Correspondence.TYPE_TRIVIAL, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_TRIVIAL), 
						this.numOfGoldMap.get(Correspondence.TYPE_TRIVIAL)));
		this.recallMap.put(Correspondence.TYPE_TRIVIAL_BASIC_NORM, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_TRIVIAL_BASIC_NORM), 
						this.numOfGoldMap.get(Correspondence.TYPE_TRIVIAL_BASIC_NORM)));
		this.recallMap.put(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM), 
						this.numOfGoldMap.get(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM)));
		this.recallMap.put(Correspondence.TYPE_MISC, 
				Characteristic.computeRecall(this.numOfMatcherMap.get(Correspondence.TYPE_MISC), 
						this.numOfGoldMap.get(Correspondence.TYPE_MISC)));
	}
	
	/**
	 * Returns the number of correspondences of a given type category for the matcher
	 * @param type - the type of the correspondence
	 * @return number of correspondences
	 * @throws CorrespondenceException
	 */
	public int getNumOfMatcher(String type) throws CorrespondenceException {
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
		return this.numOfMatcherMap.get(type);
	}
	
	/**
	 * Returns the number of correspondences of a given type category for the gold standard
	 * @param type - the type of the correspondence
	 * @return number of correspondences
	 * @throws CorrespondenceException
	 */
	public int getNumOfGold(String type) throws CorrespondenceException {
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
		return this.numOfGoldMap.get(type);
	}

	/**
	 * Returns the recall for a given type category.
	 * @param type the type of the correspondence to obtain the recall from
	 * @return the recall for the type category
	 * @throws CorrespondenceException 
	 */
	public double getRecall(String type) throws CorrespondenceException {
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
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
	public static double getRecallMacro(List<TypeCharacteristic> characteristics, String type) 
			throws CorrespondenceException {
		Objects.requireNonNull(characteristics);
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
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
	public static double getRecallMicro(List<TypeCharacteristic> characteristics, String type) 
			throws CorrespondenceException {
		Objects.requireNonNull(characteristics);
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
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
	public static double getRecallStdDev(List<TypeCharacteristic> characteristics, String type) 
			throws CorrespondenceException {
		Objects.requireNonNull(characteristics);
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
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

	public HashMap<String, Double> getRecallMap() {
		return recallMap;
	}
	
	public static double getTypePercentage(List<TypeCharacteristic> characteristics, String type) throws CorrespondenceException {
		Objects.requireNonNull(characteristics);
		if(!Correspondence.isSupportedType(type)) {
			throw new CorrespondenceException(CorrespondenceException.UNSUPPORTED_TYPE, type);
		}
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
		for(Map.Entry<String, Double> e : this.recallMap.entrySet()) {
			sb.append("R-" + e.getKey() + ": " + (100.0 * e.getValue()) + "%\n");
		}
		sb.append("Precision: " + (100.0 * this.getPrecision()) + "%\n");
		return super.toString() + sb.toString();
	}

}