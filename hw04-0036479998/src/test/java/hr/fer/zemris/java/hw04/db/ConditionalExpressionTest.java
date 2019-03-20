package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.ComparisonOperators;
import hr.fer.zemris.java.hw04.db.ConditionalExpression;
import hr.fer.zemris.java.hw04.db.FieldValueGetters;
import hr.fer.zemris.java.hw04.db.StudentRecord;

@SuppressWarnings("javadoc")
public class ConditionalExpressionTest {

	@Test
	public void testLikeExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"Bos*",
				ComparisonOperators.LIKE
				);
		
		StudentRecord record = new StudentRecord("0004", "Markovic", "Marko", 5);
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), 
				expr.getStringLiteral() 
				);
		assertEquals(recordSatisfies, false);
		
		record = new StudentRecord("0004", "Bosanac", "Marko", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record), 
				expr.getStringLiteral() 
				);
		assertEquals(recordSatisfies, true);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidLikeExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"*Bo*s",
				ComparisonOperators.LIKE
				);
		StudentRecord record = new StudentRecord("0004", "Markovic", "Marko", 5);
		
		expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNullRecordExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"Bos**",
				ComparisonOperators.LIKE
				);
		
		expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(null),
				expr.getStringLiteral()
				);
	}
	
	@Test	
	public void testLessExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.FIRST_NAME,
				"Ana",
				ComparisonOperators.LESS
				);
		
		StudentRecord record = new StudentRecord("0004", "Markovic", "Jasna", 5);
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, false);
		
		record = new StudentRecord("0004", "Markovic", "A", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, true);
		
		record = new StudentRecord("0004", "Markovic", "Ana", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, false);
	}
	
	@Test	
	public void testGreaterExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.LAST_NAME,
				"Ana",
				ComparisonOperators.GREATER
				);
		
		StudentRecord record = new StudentRecord("0004", "Jasna", "Jasna", 5);
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
	 	assertEquals(recordSatisfies, true);
		
		record = new StudentRecord("0004", "A", "A", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, false);
		
		record = new StudentRecord("0004", "Ana", "Ana", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, false);
	}
	
	@Test	
	public void testEqualsExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.JMBAG,
				"0004",
				ComparisonOperators.EQUALS
				);
		
		StudentRecord record = new StudentRecord("0004", "Markovic", "Jasna", 5);
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, true);
		
		record = new StudentRecord("4", "Markovic", "A", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, false);
	}
	
	@Test	
	public void testNotEqualsExpression() {
		ConditionalExpression expr = new ConditionalExpression(
				FieldValueGetters.JMBAG,
				"0004",
				ComparisonOperators.NOT_EQUALS
				);
		
		StudentRecord record = new StudentRecord("0004", "Markovic", "Jasna", 5);
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, false);
		
		record = new StudentRecord("4", "Markovic", "A", 5);
		recordSatisfies = expr.getComparisonOperator().satisfied(
				expr.getFieldGetter().get(record),
				expr.getStringLiteral()
				);
		assertEquals(recordSatisfies, true);
	}
}
