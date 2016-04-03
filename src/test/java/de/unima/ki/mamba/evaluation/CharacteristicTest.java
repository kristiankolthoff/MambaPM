package de.unima.ki.mamba.evaluation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class CharacteristicTest {

	private Characteristic characteristic;
	private Characteristic characteristic2;
	private Characteristic characteristic3;
	private List<Characteristic> characteristicsZero;
	private List<Characteristic> characteristics;
	
	@Before
	public void init() {
		this.characteristic = new Characteristic();
		this.characteristic2 = new Characteristic(100, 50, 25);
		this.characteristic3 = new Characteristic(10,5,5);
		this.characteristicsZero = new ArrayList<>();
		this.characteristicsZero.add(characteristic);
		this.characteristics = new ArrayList<Characteristic>();
		this.characteristics.add(this.characteristic2);
		this.characteristics.add(this.characteristic3);
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
		assertEquals(1/(double)3, this.characteristic2.getFMeasure(), 0);
		assertEquals(2/(double)3, this.characteristic3.getFMeasure(), 0);
	}
	
	@Test
	public void getRecallMacroZeroTest() {
		assertTrue(Double.isNaN(Characteristic.getRecallMacro(characteristicsZero)));
	}
	
	@Test
	public void getRecallMacroTest() {
		assertEquals(0.375, Characteristic.getRecallMacro(this.characteristics),0);
	}
	
	@Test
	public void getRecallMicroTest() {
		assertEquals(30/(double)110, Characteristic.getRecallMicro(this.characteristics), 0);
	}
	
	@Test
	public void getPrecisionMacroTest() {
		assertEquals(0.75, Characteristic.getPrecisionMacro(this.characteristics), 0);
	}
	
	@Test
	public void getPrecisionMicroTest() {
		assertEquals(30/(double)55, Characteristic.getPrecisionMicro(this.characteristics), 0);
	}
	
	@Test
	public void getFMeasureMacroTest() {
		assertEquals(0.5, Characteristic.getFMeasureMacro(this.characteristics), 0);
	}
	
	@Test
	public void getFMeasureMicroTest() {
		assertEquals(Characteristic.computeFFromPR(30/(double)55, 30/(double)110), 
				Characteristic.getFMeasureMicro(this.characteristics), 0);
	}
}
