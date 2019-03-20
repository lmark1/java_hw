package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.ComparisonOperators;
import hr.fer.zemris.java.hw04.db.IComparisonOperator;

@SuppressWarnings("javadoc")
public class ComparisonOperatorsTest {

	@Test
	public void testLikeOperator() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		
		assertEquals(oper.satisfied("Zagreb", "Aba*"), false); 
		assertEquals(oper.satisfied("AAA", "AA*AA"), false); 
		assertEquals(oper.satisfied("AAAA", "AA*AA"), true);
		assertEquals(oper.satisfied("ABCD", "*"), true);
		assertEquals(oper.satisfied("Akšamović", "*ić"), true);
		assertEquals(oper.satisfied("Marko", "M*o"), true);
	}
	
	@Test
	public void noWildCard() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		assertEquals(oper.satisfied("Zagreb", "Zagreb"), true);
		assertEquals(oper.satisfied("Zagreb", "Zagrebb"), false);
	}
	
	@Test(expected = Exception.class)
	public void tooManyWildcards() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		oper.satisfied("asdasd", "asd**");
	}

	@Test(expected = Exception.class)
	public void wildcardsInFirstString() {
		IComparisonOperator oper = ComparisonOperators.LIKE;
		oper.satisfied("as*dasd", "asd*");
	}
	
	@Test
	public void testLess() {
		IComparisonOperator oper = ComparisonOperators.LESS;
		
		assertEquals(oper.satisfied("Ana", "Jasna"), true);
		assertEquals(oper.satisfied("Ana", "A"), false);
		assertEquals(oper.satisfied("Ana", "Ana"), false);
	}
	
	@Test
	public void testLessOrEquals() {
		IComparisonOperator oper = ComparisonOperators.LESS_OR_EQUALS;
		
		assertEquals(oper.satisfied("Ana", "Jasna"), true);
		assertEquals(oper.satisfied("Ana", "A"), false);
		assertEquals(oper.satisfied("Ana", "Ana"), true);
	}
	
	@Test
	public void testGreater() {
		IComparisonOperator oper = ComparisonOperators.GREATER;
		
		assertEquals(oper.satisfied("Ana", "Jasna"), false);
		assertEquals(oper.satisfied("Ana", "A"), true);
		assertEquals(oper.satisfied("Ana", "Ana"), false);
	}
	
	@Test
	public void testGreaterOrEquals() {
		IComparisonOperator oper = ComparisonOperators.GREATER_OR_EQUALS;
		
		assertEquals(oper.satisfied("Ana", "Jasna"), false);
		assertEquals(oper.satisfied("Ana", "A"), true);
		assertEquals(oper.satisfied("Ana", "Ana"), true);
	}
	
	@Test
	public void testEquals() {
		IComparisonOperator oper = ComparisonOperators.EQUALS;
		
		assertEquals(oper.satisfied("a", "b"), false);
		assertEquals(oper.satisfied("asd", "asd"), true);
	}
	
	@Test
	public void testNotEquals() {
		IComparisonOperator oper = ComparisonOperators.NOT_EQUALS;
		
		assertEquals(oper.satisfied("a", "b"), true);
		assertEquals(oper.satisfied("asd", "asd"), false);
	}
}
