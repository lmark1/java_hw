package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ValueWrapperTest {

	@Test
	public void addingNull() {
		ValueWrapper v1 = new ValueWrapper(null);
		assertEquals(v1.getValue(), null);
		
		ValueWrapper v2 = new ValueWrapper(null);
		v1.add(v2.getValue());
		
		assertEquals(v1.getValue(), Integer.valueOf(0));
		assertEquals(v2.getValue(), null);
	}
	
	@Test
	public void addDouble() {
		ValueWrapper v3 = new ValueWrapper("1.2E1");
		ValueWrapper v4 = new ValueWrapper(Integer.valueOf(1));
		v3.add(v4.getValue());
		
		assertEquals(v3.getValue(), Double.valueOf(13));
		assertEquals(v4.getValue(), Integer.valueOf(1));
	}
	
	@Test
	public void addSmallDouble() {
		ValueWrapper v1 = new ValueWrapper("0.0015");
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.add(v2.getValue());
		assertEquals(v1.getValue(), Double.valueOf(0.0015));
		
		v2.add(v1.getValue());
		assertEquals(v2.getValue(), Double.valueOf(0.0015));
	}
	
	@Test(expected = RuntimeException.class)
	public void invalidDoubleFormat() {
		ValueWrapper v1 = new ValueWrapper("1.5e-3");
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.add(v2.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void decimalCommaTest() {
		ValueWrapper v1 = new ValueWrapper("1,5");
		ValueWrapper v2 = new ValueWrapper(null);
		
		v1.add(v2.getValue());
	}
	
	@Test
	public void addInteger() {
		ValueWrapper v5 = new ValueWrapper("12");
		ValueWrapper v6 = new ValueWrapper(Integer.valueOf(1));
		v5.add(v6.getValue());
		
		assertEquals(v5.getValue(), Integer.valueOf(13));
		assertEquals(v6.getValue(), Integer.valueOf(1));
	}
	
	@Test(expected = RuntimeException.class)
	public void addCharacters() {
		ValueWrapper v1 = new ValueWrapper(new Character('1'));
		ValueWrapper v2 = new ValueWrapper(new Integer(3));
		
		v1.add(v2.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void divisionWithIntegerZero() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(new Integer(3));
		
		v2.divide(v1.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void divisionWithDoubleZero() {
		ValueWrapper v1 = new ValueWrapper("3");
		ValueWrapper v2 = new ValueWrapper("0.00E-10");
		
		v1.divide(v2.getValue());
	}
	
	@Test
	public void divisionIntegerTest() {
		ValueWrapper v1 = new ValueWrapper("2");
		ValueWrapper v2 = new ValueWrapper("1");
		
		v1.divide(v2.getValue());
		assertEquals(v1.getValue(), Integer.valueOf(2));
		
		v2.divide(v1.getValue());
		assertEquals(v2.getValue(), Integer.valueOf(0));
	}
	
	@Test
	public void divisionDoubleTest() {
		ValueWrapper v1 = new ValueWrapper("2.0");
		ValueWrapper v2 = new ValueWrapper("1.0");
		
		v1.divide(v2.getValue());
		assertEquals(v1.getValue(), Double.valueOf(2));
		
		v2.divide(v1.getValue());
		assertEquals(v2.getValue(), Double.valueOf(0.5));
	}
	
	@Test
	public void nullComparisonTest() {
		ValueWrapper v1 = new ValueWrapper(null);
		ValueWrapper v2 = new ValueWrapper(null);
		
		assertEquals(v1.numCompare(v2.getValue()), 0);		
		
		assertEquals(v1.getValue(), null);
		assertEquals(v2.getValue(), null);
		
		v1.add(Integer.valueOf(1));
		assertEquals(v1.numCompare(v2.getValue()) > 0 , true);
		assertEquals(v2.numCompare(v1.getValue()) < 0, true);
	}
	
	@Test
	public void doubleComparisonTest() {
		ValueWrapper v1 = new ValueWrapper("3.4");
		ValueWrapper v2 = new ValueWrapper("3");
		
		assertEquals(v1.numCompare(v2.getValue()) > 0, true);
		
		v1.subtract(Double.valueOf(0.4));
		assertEquals(v1.numCompare(v2.getValue()) == 0, true); 
	}
	
	@Test
	public void doubleComparisonTest2() {
		ValueWrapper v1 = new ValueWrapper("0.0015");
		ValueWrapper v2 = new ValueWrapper("1.5E-3");
		
		assertEquals(v1.numCompare(v2.getValue()) == 0, true);
	}
	
	@Test(expected = RuntimeException.class)
	public void ankicaAddition() {
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		v7.add(v8.getValue());
	}
	
	@Test(expected = RuntimeException.class)
	public void ankicaComparison() {
		ValueWrapper v7 = new ValueWrapper("Ankica");
		ValueWrapper v8 = new ValueWrapper(Integer.valueOf(1));
		v7.numCompare(v8.getValue());
	}
	
	
}
