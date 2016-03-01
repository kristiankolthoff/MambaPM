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
import de.unima.ki.mamba.om.alignment.Correspondence;

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
		assertEquals(1/(double)2, this.typeCharacteristic.getRecall(Correspondence.TYPE_DIFFICULT),0);
	}
	
	@Test
	public void recallDifficultSimilarVerbTest() throws CorrespondenceException {
		assertEquals(1/(double)3, this.typeCharacteristic.getRecall(Correspondence.TYPE_DIFFICULT_SIMILAR_VERB),0);
	}
	
	@Test
	public void recallOneWordSimilarTest() throws CorrespondenceException {
		assertTrue(Double.isNaN(this.typeCharacteristic.getRecall(Correspondence.TYPE_ONE_WORD_SIMILAR)));
	}
	
	@Test
	public void recallTrivialTest() throws CorrespondenceException {
		assertEquals(1, this.typeCharacteristic.getRecall(Correspondence.TYPE_TRIVIAL),0);
	}
	
	@Test
	public void recallTrivialBasicNormTest() throws CorrespondenceException {
		assertEquals(1, this.typeCharacteristic.getRecall(Correspondence.TYPE_TRIVIAL_BASIC_NORM),0);
	}
	
	@Test
	public void recallTrivialExtendedNormTest() throws CorrespondenceException {
		assertEquals(0, this.typeCharacteristic.getRecall(Correspondence.TYPE_TRIVIAL_EXTENDED_NORM),0);
	}
	
	@Test
	public void recallMiscTest() throws CorrespondenceException {
		assertEquals(1/(double)3, this.typeCharacteristic.getRecall(Correspondence.TYPE_MISC),0);
	}
	
	@Test
	public void recallDifficultMicroTest() throws CorrespondenceException {
		assertEquals(0.5, TypeCharacteristic.getRecallMicro(this.characteristics, Correspondence.TYPE_DIFFICULT), 0);
	}
	
	@Test
	public void recallDifficultMacroTest() throws CorrespondenceException {
		assertEquals(0.5, TypeCharacteristic.getRecallMacro(this.characteristics, Correspondence.TYPE_DIFFICULT), 0);
	}
	
	@Test
	public void recallDifficultStdDevTest() throws CorrespondenceException {
		assertEquals(0, TypeCharacteristic.getRecallStdDev(this.characteristics, Correspondence.TYPE_DIFFICULT), 0);
	}
	
	@Test
	public void recallMiscMicroTest() throws CorrespondenceException {
		assertEquals(1/(double)3, TypeCharacteristic.getRecallMicro(this.characteristics, Correspondence.TYPE_MISC), 0);
	}
	
	@Test
	public void recallMiscMacroTest() throws CorrespondenceException {
		assertEquals(1/(double)3, TypeCharacteristic.getRecallMacro(this.characteristics, Correspondence.TYPE_MISC), 0);
	}
	
	@Test
	public void recallTrivialAfterBasicNormMicroTest() throws CorrespondenceException {
		assertEquals(1, TypeCharacteristic.getRecallMicro(this.characteristics, Correspondence.TYPE_TRIVIAL_BASIC_NORM), 0);
	}
	
	@Test
	public void recallTrivialAfterBasicNormMacroTest() throws CorrespondenceException {
		assertEquals(1, TypeCharacteristic.getRecallMacro(this.characteristics, Correspondence.TYPE_TRIVIAL_BASIC_NORM), 0);
	}
}
