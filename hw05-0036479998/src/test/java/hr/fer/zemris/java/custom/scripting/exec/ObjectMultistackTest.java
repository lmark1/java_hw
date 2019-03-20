package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.Assert.*;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class ObjectMultistackTest {

	@Test
	public void pushTest() {
		ObjectMultistack mStack = new ObjectMultistack();
		ValueWrapper newWrapper = new ValueWrapper(new Integer(3));
		
		mStack.push("mirko", newWrapper);
		mStack.push("mirko", new ValueWrapper(new Integer(5)));
		mStack.push("Marko", new ValueWrapper(new Double(3)));
		
		assertEquals(mStack.isEmpty("mirko"), false);
		assertEquals(mStack.isEmpty("Ivana"), true);
		
		assertEquals(mStack.peek("mirko").getValue(), Integer.valueOf(5));
		assertEquals(mStack.pop("mirko").getValue(), Integer.valueOf(5));
		
		assertEquals(mStack.peek("mirko").getValue(), Integer.valueOf(3));
	}
	
	@Test
	public void isEmptyTest() {
		ObjectMultistack mStack = new ObjectMultistack();
		assertEquals(mStack.isEmpty("bla"), true);
		
		mStack.push("bla", new ValueWrapper(new Double(3)));
		assertEquals(mStack.isEmpty("bla"), false);
		
		mStack.peek("bla");
		assertEquals(mStack.isEmpty("bla"), false);
		
		mStack.pop("bla");
		assertEquals(mStack.isEmpty("bla"), true);
	}
	
	@Test(expected = Exception.class)
	public void popTooMuch() {
		ObjectMultistack mStack = new ObjectMultistack();
		
		mStack.pop("bla");
	}
	
	@Test(expected = Exception.class)
	public void peekTooMuch() {
		ObjectMultistack mStack = new ObjectMultistack();
		
		mStack.peek("bla");
	}
	
	@Test(expected = Exception.class)
	public void popNull() {
		ObjectMultistack mStack = new ObjectMultistack();
		mStack.pop(null);
	}
	
	@Test(expected = Exception.class)
	public void peekNull() {
		ObjectMultistack mStack = new ObjectMultistack();
		mStack.peek(null);
	}
	
	@Test(expected = Exception.class)
	public void pushNull() {
		ObjectMultistack mStack = new ObjectMultistack();
		mStack.push(null, new ValueWrapper(new Integer(3)));
	}
}
