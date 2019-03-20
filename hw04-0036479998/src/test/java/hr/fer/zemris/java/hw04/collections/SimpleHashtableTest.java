package hr.fer.zemris.java.hw04.collections;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.Test;


@SuppressWarnings("javadoc")
public class SimpleHashtableTest {

	@SuppressWarnings("rawtypes")
	@Test(expected = IllegalArgumentException.class)
	public void simpleHashtableNegativeCapacity() {
		new SimpleHashtable(-1);
	}

	@Test
	public void testPut() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Mirko", 5);
		table.put("Ana", 3);
		table.put("Banana", 3);
		
		assertEquals(table.size(), 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPutNullKey() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		table.put(null, 5);
	}
	
	@Test
	public void testGet() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Mirko", 5);
		table.put("Ana", 3);
		table.put("Banana", 3);
		table.put("Slavko", 10);
		
		assertEquals(table.get("Slavko"), Integer.valueOf(10));
		assertEquals(table.get("Mirko"), Integer.valueOf(5));
		assertEquals(table.get("Anabanana"), null);
	}

	
	@Test
	public void testContainsKey() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Mirko", 5);
		table.put("Ana", 3);
		table.put("Banana", null);
		table.put("Slavko", 10);
		
		assertEquals(table.containsKey("Banana"), true);
		assertEquals(table.containsKey("Slavkoo"), false);
		assertEquals(table.containsKey(null), false);
		
	}

	@Test
	public void testContainsValue() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Mirko", 5);
		table.put("Ana", 3);
		table.put("Banana", null);
		table.put("Slavko", 10);
		
		assertEquals(table.containsValue(null), true);
		assertEquals(table.containsValue(1), false);
		assertEquals(table.containsValue(10), true);
		assertEquals(table.containsValue(13), false);
	}

	@Test
	public void testRemove() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Mmirko", 5);
		table.put("Ana", 3);
		table.put("Banana", null);
		table.put("Sslavko", 10);
		
		table.remove("Slavko");
		table.remove("Mirko");
		table.remove("Ana");
		
		assertEquals(table.size(), 3);
		assertEquals(table.get("Mirko"), null);
		assertEquals(table.get("Ana"), null);
	}
	
	@Test
	public void testIsEmpty() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(2);
		table.put("Mirko", 1);
		assertEquals(table.isEmpty(), false);
		
		table.remove("Mirko");		
		assertEquals(table.isEmpty(), true);
	}

	@Test
	public void testToString() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>();
		
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Ana", null);
		

		assertEquals(table.toString(), "[Ana=null, Mirko=1, Slavko=5]");
	}
	
	@Test
	public void resizeTest() {
		SimpleHashtable<String, Integer> table = new SimpleHashtable<>(1);
		table.put("Mirko", 1);
		table.put("Slavko", 5);
		table.put("Ana", null);
		
		try {
			@SuppressWarnings("rawtypes")
			Class cls = table.getClass();
			Field internalData = cls.getDeclaredField("table");
			internalData.setAccessible(true);
			
			// TODO kako testirati resize
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testForeach() {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", null);
		examMarks.put("Ivana", 5); 
		examMarks.put("Ivanka", 5); 
		examMarks.put("Ivanaa", 5); 
		
		String actualString = examMarks.toString();
		String testString = new String();
		testString = "[";
		boolean firstAddition = true;
		
		for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
			if (!firstAddition) {
				testString += ", ";
			}

			testString += pair.getKey().toString() + "=";

			if (pair.getValue() == null) {
				testString += "null";

			} else {
				testString += pair.getValue().toString();
			}

			firstAddition = false;
		}
		testString += "]";
		
		assertEquals(testString, actualString);
	}
	
	@Test
	public void iteratorForeachRemoveAll() {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", null);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		examMarks.put("Ivanka", 5); 
		examMarks.put("Ivanaa", 5); 
	
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			@SuppressWarnings("unused")
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			iter.remove(); 
		}
		
		assertEquals(examMarks.isEmpty(), true);
	}
	
	@Test(expected = IllegalStateException.class)
	public void iteratorStateException() {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", null);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			
			if(pair.getKey().equals("Ivana")) {
				iter.remove(); 
				iter.remove();
			}
		}
	}
	
	@Test(expected = ConcurrentModificationException.class)
	public void iteratorModificationException() {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", null);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			if(pair.getKey().equals("Ivana")) {
				examMarks.remove("Ivana");
			}
		}
	}
	
	@Test
	public void iteratorTestUpdatingPairs() {
		// create collection:
		SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
		// fill data:
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", null);
		examMarks.put("Ivana", 5); // overwrites old grade for Ivana
		
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
		while(iter.hasNext()) {
			SimpleHashtable.TableEntry<String,Integer> pair = iter.next();
			
			if(pair.getKey().equals("Ivana")) {
				pair.setValue(10);
			}
			
			examMarks.put("Ante", 5);
		}
		
		assertEquals(examMarks.get("Ivana"), Integer.valueOf(10));
		assertEquals(examMarks.get("Ante"), Integer.valueOf(5));
	}
}
