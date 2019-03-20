package hr.fer.zemris.math;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class Vector3Test {

	@Test
	public void testVector3() {
		Vector3 v = new Vector3(1, 2, 3);
		assertEquals(v.getX(), 1, 0.0001);
		assertEquals(v.getY(), 2, 0.0001);
		assertEquals(v.getZ(), 3, 0.0001);	
	}

	@Test
	public void testNorm() {
		Vector3 v = new Vector3(1, 2, 3);
		Vector3 vNorm = v.normalized();
		
		assertEquals(vNorm.norm(), 1, 0.0001);
	}

	@Test
	public void testAdd() {
		Vector3 i = new Vector3(1,0,0);
		Vector3 j = new Vector3(0,1,0);
		
		assertEquals(i.add(j).toString(), "(1.000000, 1.000000, 0.000000)");
	}

	@Test
	public void testSub() {
		Vector3 i = new Vector3(1,1,-1);
		Vector3 j = new Vector3(0,0,-1);
		
		assertEquals(i.sub(j).toString(), "(1.000000, 1.000000, 0.000000)");
	}

	@Test
	public void testDot() {
		Vector3 i = new Vector3(1,2,1);
		Vector3 j = new Vector3(-1,1,-1);
		
		assertEquals(i.dot(j), 0, 0.0001);		
	}

	@Test
	public void testCross() {
		Vector3 i = new Vector3(1,0,0);
		Vector3 j = new Vector3(0,1,0);
		Vector3 k = i.cross(j);
		
		assertEquals(k.toString(), "(0.000000, 0.000000, 1.000000)");
	}

	@Test
	public void testScale() {
		Vector3 i = new Vector3(1,0,0);
		Vector3 j = new Vector3(0,1,0);
		Vector3 k = i.cross(j);
		Vector3 l = k.add(j).scale(5);
		
		assertEquals(l.toString(), "(0.000000, 5.000000, 5.000000)");
	}

	@Test
	public void testCosAngle() {
		Vector3 i = new Vector3(1,0,0);
		Vector3 j = new Vector3(0,1,0);
		Vector3 k = i.cross(j);
		Vector3 l = k.add(j).scale(5);
		
		assertEquals(i.add(new Vector3(0,1,0)).cosAngle(l), 0.499999, 0.0001);
	}

	@Test
	public void testToArray() {
		Vector3 i = new Vector3(1,0,0);
		double[] test = new double[3];
		test[0] = 1;
		test[1] = 0;
		test[2] = 0;
		
		assertArrayEquals(i.toArray(), test, 0.0001);
	}
	
}
