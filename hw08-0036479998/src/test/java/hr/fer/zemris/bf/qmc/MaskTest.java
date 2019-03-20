package hr.fer.zemris.bf.qmc;

import static org.junit.Assert.*;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class MaskTest {
	public Mask newMask;

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNegative() {
		newMask = new Mask(4, -1, true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorNull() {
		newMask = new Mask(null, null, true);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testModifyIndexes() {
		newMask = new Mask(5, 3, true);
		newMask.getIndexes().add(3);
	}
	
	@Test
	public void testEqualsObject() {
		newMask = new Mask(5, 3, true);
		Set<Integer> newSet = new TreeSet<>();
		newSet.add(3);
		Mask otherMask = new Mask(new byte[] {1, 0, 1}, newSet, false);
		assertEquals(newMask.equals(otherMask), true);
	}

	@Test
	public void testToString() {
		newMask = new Mask(5, 3, true);
		String newString = newMask.toString();
		assertEquals(newString, "101 D   [5]");
	}

	@Test
	public void testSetCombined() {
		newMask = new Mask(5, 3, true);
		newMask.setCombined(true);
		String newString = newMask.toString();
		assertEquals(newString, "101 D * [5]");
	}

	@Test
	public void testGetIndexes() {
		newMask = new Mask(5, 3, true);
		for (Integer i : newMask.getIndexes()) {
			assertEquals(i.equals(5), true);
		}
	}

	@Test
	public void testCountOfOnes() {
		newMask = new Mask(5, 3, true);
		assertEquals(newMask.countOfOnes(), 2);
	}

	@Test
	public void testCombineWithFalse() {
		newMask = new Mask(5, 4, false);
		Mask otherMask = new Mask(10, 4, true);
		
		Optional<Mask> combined = newMask.combineWith(otherMask);
		assertEquals(combined.isPresent(), false);
	}

	@Test
	public void testCombineWithTrue() {
		newMask = new Mask(5, 4, false);
		Mask otherMask = new Mask(4, 4, true);
		
		Optional<Mask> combined = newMask.combineWith(otherMask);
		Set<Integer> testSet = new TreeSet<>();
		testSet.add(5);
		testSet.add(4);
		
		assertEquals(combined.orElse(null).getIndexes(), testSet);
	}
	
	@Test
	public void testSize() {
		newMask = new Mask(1, 10, true);
		assertEquals(newMask.size(), 10);
	}

	@Test
	public void testGetValueAt() {
		newMask = new Mask(1, 1, true);
		assertEquals(newMask.getValueAt(0), 1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetOutOfRange() {
		newMask = new Mask(1, 1, true);
		newMask.getValueAt(10);
	}

}
