package hr.fer.zemris.java.custom.collections;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ArrayIndexedCollectionTest {

	@Test
	public void addCoupleObjects() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		assertEquals(col1.get(0),obj1);
		assertEquals(col1.get(1),obj2);
		assertEquals(col1.get(2),obj3);
	}
	
	@Test
	public void addMoreThanCapacity() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		assertEquals(col1.get(0),obj1);
		assertEquals(col1.get(1),obj2);
		assertEquals(col1.get(2),obj3);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addNullException() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		col1.add(null);
	}
	
	@Test
	public void addAllMethodTest() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1, 1);
		Integer obj4 = new Integer(4);
		col2.add(obj4);
		
		assertEquals(col2.get(0),obj1);
		assertEquals(col2.get(1),obj2);
		assertEquals(col2.get(2),obj3);
		assertEquals(col2.get(3),obj4);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getIndexOutofBoundsUpper(){
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.get(2);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getIndexOutofBoundsLower(){
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.get(-1);
	}
	 
	@Test(expected = IndexOutOfBoundsException.class)
	public void clearWhenFull() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(3);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		col1.clear();
		col1.get(0);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void clearWhenEmpty() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		
		col1.clear();
		col1.get(0);
	}
	
	@Test 
	public void insertAtStartMiddleEnd() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(6);
		Integer obj5 = new Integer(1);
		Integer obj6 = new Integer(2);
		
		//Insert at start
		col1.insert(obj1,0);
		col1.insert(obj2,0);
		col1.insert(obj3,0);
		//Insert in the middle
		col1.insert(obj4, 1);
		col1.insert(obj6, 2);
		//Insert at the end
		col1.insert(obj5, 5);
		
		assertEquals(col1.get(0),obj3);
		assertEquals(col1.get(1),obj4);
		assertEquals(col1.get(2),obj6);
		assertEquals(col1.get(3),obj2);
		assertEquals(col1.get(4),obj1);
		assertEquals(col1.get(5),obj5);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void insertNullValue() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		col1.insert(null,0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void insertAtInvalidIndex() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		col1.insert(obj1,0);
		col1.insert(obj1,1);
		col1.insert(obj1,3);
	}
	
	@Test
	public void indexOfNull() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		assertEquals(col1.indexOf(null), -1);
	}
	
	@Test
	public void indexOfEmptyArray() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		assertEquals(col1.indexOf(obj1),-1);
	}
	
	@Test 
	public void indexOfElementInArray() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(3);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		assertEquals(col1.indexOf(obj1),0);
		assertEquals(col1.indexOf(obj2),1);
		assertEquals(col1.indexOf(obj3),2);
	}
	
	@Test
	public void indexofElementNotInCollection() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(3);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(11);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		assertEquals(col1.indexOf(obj4),-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeIndexOutOfBoundsLower() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(3);
		Integer obj1 = new Integer(5);

		col1.add(obj1);
		col1.remove(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeIndexOutOfBoundsUpper() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		Integer obj1 = new Integer(5);

		col1.add(obj1);
		col1.remove(2);
	}
	
	@Test
	public void removeAtStartMiddleEnd() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		Integer obj4 = new Integer(6);
		Integer obj5 = new Integer(1);
		Integer obj6 = new Integer(2);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		col1.add(obj4);
		col1.add(obj5);
		col1.add(obj6);
		
		col1.remove(0);
		col1.remove(4);
		col1.remove(2);

		assertEquals(col1.get(0), obj2);
		assertEquals(col1.get(1), obj3);
		assertEquals(col1.get(2), obj5);	
		
		col1.remove(0);
		col1.remove(0);
		col1.remove(0);
		
		assertEquals(col1.size(), 0);
	}
	
	@Test
	public void containsTest(){
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		Integer obj1 = new Integer(5);
		assertEquals(col1.contains(obj1), false);
	}
	
	@Test
	public void containsEmpty() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(1);
		assertEquals(col1.contains(new Integer(5)), false);
	}
	
	@Test
	public void containsElementInCollection() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection(3);
		Integer obj1 = new Integer(5);
		Integer obj2 = new Integer(3);
		Integer obj3 = new Integer(10);
		
		col1.add(obj1);
		col1.add(obj2);
		col1.add(obj3);
		
		assertEquals(col1.contains(obj3), true);
	}
}
