package hr.fer.zemris.math;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ComplexTest {

	@Test
	public void testModule() {
		Complex c = new Complex(1, 1);
		assertEquals(c.module(), Math.sqrt(2), 0.0001);
	}

	@Test
	public void testMultiply() {
		Complex c = new Complex(0, 1);
		c = c.multiply(c);
		
		assertEquals(c.toString(), "-1.000000");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDivide() {
		Complex c1 = new Complex(1, 0);
		c1.divide(new Complex());
	}
	
	@Test
	public void testDivide2() {
		Complex c1 = new Complex(1, 0);
		c1 = c1.divide(new Complex(0, 1));
		
		assertEquals(c1.toString(), "-1.000000i");
	}

	@Test
	public void testAdd() {
		Complex c1 = new Complex(1, 0);
		c1 = c1.add(new Complex(0, 1));
		
		assertEquals(c1.toString(), "1.000000 + 1.000000i");
	}

	@Test
	public void testSub() {
		Complex c1 = new Complex(1, 0);
		c1 = c1.sub(new Complex(0, 1));
		
		assertEquals(c1.toString(), "1.000000 - 1.000000i");
	}

	@Test
	public void testNegate() {
		Complex c1 = new Complex(1, 0);
		assertEquals(c1.negate().toString(), "-1.000000");
	}

	@Test
	public void testPower() {
		Complex c = new Complex(1, 1);
		assertEquals(c.power(2).toString(), "0.000000 + 2.000000i");
	}

	@Test
	public void testRoot() {
		Complex c = new Complex(0, 2);
		List<Complex> roots = c.root(2);
		List<Complex> testRoots = new ArrayList<Complex>();
		testRoots.add(new Complex(1, 1));
		testRoots.add(new Complex(-1, -1));
		
		assertThat(roots, is(testRoots));
	}

	@Test
	public void testToString() {
		Complex c = new Complex(0, -1);
		assertEquals(c.toString(), "-1.000000i");
	}

}
