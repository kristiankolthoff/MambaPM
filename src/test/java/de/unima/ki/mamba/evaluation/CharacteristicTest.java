package de.unima.ki.mamba.evaluation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.unima.ki.mamba.evaluation.Characteristic;
import de.unima.ki.mamba.om.alignment.Alignment;
import de.unima.ki.mamba.om.alignment.Correspondence;

public class CharacteristicTest {

	private Characteristic characteristic;
	private Characteristic characteristic2;
	private Characteristic characteristic3;
	private List<Characteristic> characteristicsZero;
	private List<Characteristic> characteristics;
	
	@Before
	public void init() {
		this.characteristic = new Characteristic(new Alignment(), 
				new Alignment());
		final Alignment align11 = new Alignment();
		align11.add(new Correspondence("11", "1", 0.8));
		align11.add(new Correspondence("11", "2", 0.2));
		final Alignment align12 = new Alignment();
		align12.add(new Correspondence("11", "1", 0.9));
		this.characteristic2 = new Characteristic(align12, align11);
		final Alignment align21 = new Alignment();
		align21.add(new Correspondence("21", "1", 0.7));
		align21.add(new Correspondence("21", "2", 0.1));
		final Alignment align22 = new Alignment();
		align22.add(new Correspondence("21", "2", 0.6));
		this.characteristic3 = new Characteristic(align22, align21);
		this.characteristicsZero = new ArrayList<>();
		this.characteristicsZero.add(characteristic);
		this.characteristics = new ArrayList<Characteristic>();
		this.characteristics.add(this.characteristic2);
		this.characteristics.add(this.characteristic3);
	}
	
	@Test
	public void nonBinaryRecallTest() {
		assertEquals(0.8d, this.characteristic2.getRecall(), 0d);
		assertEquals(0.1/0.8, this.characteristic3.getRecall(), 0.0001d);
	}
	
	@Test
	public void nonBinaryPrecisionTest() {
		assertEquals(0.8/1, this.characteristic2.getPrecision(), 0d);
	}
	
	@Test
	public void zeroRulesMatcherPrecisionTest() {
		assertTrue(Double.isNaN(this.characteristic.getPrecision()));
	}
	
	@Test
	public void zeroRulesGoldRecallTest() {
		assertTrue(Double.isNaN(this.characteristic.getRecall()));
	}
	
	@Test
	public void zeroPrecisionAndRecallFMeasureTest() {
		assertTrue(Double.isNaN(this.characteristic.getFMeasure()));
	}
	
	@Test
	public void computeZeroInputRecallTest() {
		assertTrue(Double.isNaN(Characteristic.computeRecall(0, 0)));
	}
	
	@Test
	public void computeZeroPrecisionAndRecallFMeasureTest() {
		assertEquals(0, Characteristic.computeFFromPR(0, 0), 0);
	}
	
	@Test
	public void getFMeasureTest() {
		assertEquals(0.8, this.characteristic2.getFMeasure(), 0.0001d);
		assertEquals(0.11, this.characteristic3.getFMeasure(), 0.1d);
	}
	
	@Test
	public void getRecallMacroZeroTest() {
		assertTrue(Double.isNaN(Characteristic.getRecallMacro(characteristicsZero)));
	}
	
	@Test
	public void getRecallMacroTest() {
		assertEquals(0.4625, Characteristic.getRecallMacro(this.characteristics),0);
	}
	
	@Test
	public void getRecallMicroTest() {
		assertEquals(0.5, Characteristic.getRecallMicro(this.characteristics), 0.00001d);
	}
	
	@Test
	public void getPrecisionMacroTest() {
		assertEquals(0.9/2, Characteristic.getPrecisionMacro(this.characteristics), 0);
	}
	
	@Test
	public void getPrecisionMicroTest() {
		assertEquals(0.45, Characteristic.getPrecisionMicro(this.characteristics), 0);
	}
	
	@Test
	public void getFMeasureMacroTest() {
		assertEquals(0.45, Characteristic.getFMeasureMacro(this.characteristics), 0.1d);
	}
	
	@Test
	public void getFMeasureMicroTest() {
//		assertEquals(Characteristic.computeFFromPR(0.45, 0.5), 
//				Characteristic.getFMeasureMicro(this.characteristics), 0);
	}
}
