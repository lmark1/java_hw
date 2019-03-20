package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class LinkedListIndexedCollectionTest {

	@Test
	public void testAdd() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);

		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);

		assertEquals(col1.get(0), obj1);
		assertEquals(col1.get(1), obj2);
		assertEquals(col1.get(2), obj3);
	}

	@Test
	public void testInsert() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(11);

		col1.insert(obj1, 0);
		col1.insert(obj2, 1);
		col1.insert(obj3, 1);
		col1.insert(obj4, 0);

		assertEquals(col1.get(0), obj4);
		assertEquals(col1.get(1), obj1);
		assertEquals(col1.get(2), obj3);
		assertEquals(col1.get(3), obj2);
	}

	@Test
	public void testRemove() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(11);

		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		col1.add(obj4);

		col1.remove(2);
		col1.remove(2);
		col1.remove(0);

		assertEquals(col1.get(0), obj2);

		col1.remove(0);

		assertEquals(col1.size(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeNull() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		col1.remove(0);
	}

	@Test
	public void testAddAll() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(11);

		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);

		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col1);

		col2.add(obj4);

		assertEquals(col2.get(0), obj1);
		assertEquals(col2.get(1), obj2);
		assertEquals(col2.get(2), obj3);
		assertEquals(col2.get(3), obj4);
	}

	@Test
	public void containsTest() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		assertEquals(col1.contains(obj1), false);
	}

	@Test
	public void containsEmpty() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		assertEquals(col1.contains(new Integer(5)), false);
	}

	@Test
	public void containsElementInCollection() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);

		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);

		assertEquals(col1.contains(obj3), true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeIndexOutOfBoundsUpper() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);

		col1.add(obj1);
		col1.remove(2);
	}

	@Test
	public void indexOfNull() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		assertEquals(col1.indexOf(null), -1);
	}

	@Test
	public void indexOfEmptyCollection() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		assertEquals(col1.indexOf(obj1), -1);
	}

	@Test
	public void indexOfElementInCollection() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);

		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);

		assertEquals(col1.indexOf(obj1), 0);
		assertEquals(col1.indexOf(obj2), 1);
		assertEquals(col1.indexOf(obj3), 2);
	}

	@Test
	public void indexofElementNotInCollection() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(11);

		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);

		assertEquals(col1.indexOf(obj4), -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeIndexOutOfBoundsLower() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		Integer obj1 = new Integer(5);

		col1.add(obj1);
		col1.remove(-1);
	}
}
