package hr.fer.zemris.java.hw06.shell.commands;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.crypto.Util;

@SuppressWarnings("javadoc")
public class SplitStringTest {

	@Test
	public void testSplit() {
		String k = "\"C:\\Documents and Settings\\Users\\javko\"  kifla "
				+ "\"bla bla\"";
		List<String> l = Util.split(k);
		
		List<String> test = new ArrayList<>();
		test.add("C:\\Documents and Settings\\Users\\javko");
		test.add("kifla");
		test.add("bla bla");
		
		assertArrayEquals(test.toArray(), l.toArray());
	}
	
	@Test
	public void test2() {
		List<String> l = Util.split("a b c");
		
		List<String> test = new ArrayList<>();
		test.add("a");
		test.add("b");
		test.add("c");
		
		assertArrayEquals(test.toArray(), l.toArray());
	}

	@Test
	public void testNull() {
		List<String> l = Util.split("a b \"C:\\fi le\".txt c");
		assertEquals(l, null);
	}
	
	@Test
	public void testNull2() {
		List<String> l = Util.split("a b \"C:\\fi le.txt c");
		assertEquals(l, null);
	}
}
