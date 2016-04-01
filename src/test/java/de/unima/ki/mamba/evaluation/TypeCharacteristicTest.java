package de.unima.ki.mamba.evaluation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.unima.ki.mamba.evaluation.TypeCharacteristic;
import de.unima.ki.mamba.exceptions.AlignmentException;
import de.unima.ki.mamba.exceptions.CorrespondenceException;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.CorrespondenceType;

public class TypeCharacteristicTest {

	private Alignment mapping;
	private Alignment reference;
	private TypeCharacteristic typeCharacteristic;
	private TypeCharacteristic typeCharacteristic2;
	private List<TypeCharacteristic> characteristics;
	
	@Before
	public void init() throws AlignmentException, CorrespondenceException {
		final String mappingPath = "src/test/resources/type-characteristic-test-matcher.rdf";
		final String referencePath = "src/test/resources/type-characteristic-test-reference.rdf";
		this.characteristics = new ArrayList<>();
		try {
			this.mapping = new Alignment(mappingPath);
			this.reference = new Alignment(referencePath);
		} catch (AlignmentException e) {
			e.printStackTrace();
		}
		this.typeCharacteristic = new TypeCharacteristic(mapping, reference);
		this.typeCharacteristic2 = new TypeCharacteristic(mapping, reference);
		this.characteristics.add(this.typeCharacteristic);
		this.characteristics.add(this.typeCharacteristic2);
	}
	
	@SuppressWarnings("unused")
	@Test(expected=CorrespondenceException.class)
	public void missingTypeTagTest() throws AlignmentException, CorrespondenceException {
		final String alginmentExceptionPath = "src/test/resources/type-characteristic-test-exception.rdf";
		Alignment alignmentTest = new Alignment(alginmentExceptionPath);
		TypeCharacteristic typeCharacteristic = new TypeCharacteristic(this.reference, alignmentTest);
	}
	
	@Test
	public void recallDifficultTest() throws CorrespondenceException {
		assertEquals(0.75, this.typeCharacteristic.getRecall(CorrespondenceType.DIFFICULT),0);
	}
	
	@Test
	public void recallDifficultSimilarVerbTest() throws CorrespondenceException {
		assertEquals(0.225, this.typeCharacteristic.getRecall(CorrespondenceType.DIFFICULT_SIMILAR_VERB),0.01);
	}
	
	@Test
	public void recallOneWordSimilarTest() throws CorrespondenceException {
		assertTrue(Double.isNaN(this.typeCharacteristic.getRecall(CorrespondenceType.ONE_WORD_SIMILAR)));
	}
	
	@Test
	public void recallTrivialTest() throws CorrespondenceException {
		assertEquals(1, this.typeCharacteristic.getRecall(CorrespondenceType.TRIVIAL),0);
	}
	
	@Test
	public void recallTrivialBasicNormTest() throws CorrespondenceException {
		assertEquals(1, this.typeCharacteristic.getRecall(CorrespondenceType.TRIVIAL_BASIC_NORM),0);
	}
	
	@Test
	public void recallTrivialExtendedNormTest() throws CorrespondenceException {
		assertEquals(0, this.typeCharacteristic.getRecall(CorrespondenceType.TRIVIAL_EXTENDED_NORM),0);
	}
	
	@Test
	public void recallMiscTest() throws CorrespondenceException {
		assertEquals(1/1.9, this.typeCharacteristic.getRecall(CorrespondenceType.MISC),0.01);
	}
	
	@Test
	public void recallDifficultMicroTest() throws CorrespondenceException {
		assertEquals(0.75, TypeCharacteristic.getRecallMicro(this.characteristics, CorrespondenceType.DIFFICULT), 0);
	}
	
	@Test
	public void recallDifficultMacroTest() throws CorrespondenceException {
		assertEquals(0.75, TypeCharacteristic.getRecallMacro(this.characteristics, CorrespondenceType.DIFFICULT), 0);
	}
	
	@Test
	public void recallDifficultStdDevTest() throws CorrespondenceException {
		assertEquals(0, TypeCharacteristic.getRecallStdDev(this.characteristics, CorrespondenceType.DIFFICULT), 0);
	}
	
	@Test
	public void recallMiscMicroTest() throws CorrespondenceException {
		assertEquals(1/1.9, TypeCharacteristic.getRecallMicro(this.characteristics, CorrespondenceType.MISC), 0.01);
	}
	
	@Test
	public void recallMiscMacroTest() throws CorrespondenceException {
		assertEquals(1/1.9, TypeCharacteristic.getRecallMacro(this.characteristics, CorrespondenceType.MISC), 0.01);
	}
	
	@Test
	public void recallTrivialAfterBasicNormMicroTest() throws CorrespondenceException {
		assertEquals(1, TypeCharacteristic.getRecallMicro(this.characteristics, CorrespondenceType.TRIVIAL_BASIC_NORM), 0);
	}
	
	@Test
	public void recallTrivialAfterBasicNormMacroTest() throws CorrespondenceException {
		assertEquals(1, TypeCharacteristic.getRecallMacro(this.characteristics, CorrespondenceType.TRIVIAL_BASIC_NORM), 0);
	}
	
	@Test
	public void typePercentageTest() throws CorrespondenceException {
		assertEquals(0.25, TypeCharacteristic.getTypePercentage(this.characteristics, CorrespondenceType.MISC), 0);
		assertEquals(1/12d, TypeCharacteristic.getTypePercentage(this.characteristics, CorrespondenceType.TRIVIAL), 0);
		assertEquals(1/6d, TypeCharacteristic.getTypePercentage(this.characteristics, CorrespondenceType.DIFFICULT), 0);
	}
	
}
