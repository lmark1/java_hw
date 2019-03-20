package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.QueryParser;
import hr.fer.zemris.java.hw04.db.lexer.WildcardException;

@SuppressWarnings("javadoc")
public class QueryParserTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullQueryTest() {
		new QueryParser(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void emptyStringTest() {
		new QueryParser("            ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incompleteQueryTooMuch() {
		new QueryParser("   jmbag=\"123\" aNd ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incompleteQueryTooFew() {
		new QueryParser("   jmbag=");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incompleteStringInQueryAtEnd() {
		new QueryParser("  firstName=\"Banana");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incompleteStringInQueryMiddle() {
		new QueryParser(" firstName<=\"Krivo anD jmbag!=\"090909\" ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void reverseAttributeAndLiteral() {
		new QueryParser(" lastName     >= \"Opet krivo\"  And \"1*23\" LIKE jmbag");
	}
	
	@Test
	public void lowerCaseLike() {
		try{
			new QueryParser("firstName<=\"Krivo\" anD jmbag Like \"090909\" ");	
		} catch(Exception e) {
			fail();
		}
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void misspelledFirstName() {
		new QueryParser("firstname<=\"Krivo\" anD jmbag LIKE \"090909\" ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void misspelledJmbag() {
		new QueryParser("firstName<=\"Krivo\" anD jmba LIKE \"090909\" ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongOperator() {
		new QueryParser("firstName=<\"Krivo\" anD jmbag LIKE \"090909\" ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongOperatorNotEquals() {
		new QueryParser("firstName!=\"Krivo\" anD jmbag =!\"090909\" ");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void attributeEqualsAttribute() {
		new QueryParser(" jmbag != firstName");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void literalEqualsLiteral() {
		new QueryParser(" \"jmbag\" = \"firstName\" ");
	}
	
	@Test(expected = WildcardException.class)
	public void tooManyWildcards() {
		new QueryParser(" jmbag != \"******\" and firstName LIKE \"Mir*ko*\"");
	}
	
	@Test
	public void isDirectTest() {
		QueryParser newParser = new QueryParser(" firstName=\"Banana\"");
		assertEquals(newParser.isDirectQuery(), false);
		
		newParser = new QueryParser(" jmbag = \"123\" ");
		assertEquals(newParser.isDirectQuery(), true); 
		
		newParser = new QueryParser("jmbag >= \"123\" AND firstName=\"Banana\"");
		assertEquals(newParser.isDirectQuery(), false);
	}
	
	@Test(expected = IllegalStateException.class)
	public void getQueriedJmbagException() {
		QueryParser newParser = new QueryParser(" firstName=\"Banana\"");
		newParser.getQueriedJMBAG();
	}
	
	@Test
	public void getQueriedJmbagAndQuery() {
		QueryParser newParser = new QueryParser(" jmbag=\"Banana\"");
		assertEquals(newParser.getQueriedJMBAG(), "Banana");
		assertEquals(newParser.getQuery().size(), 1);
	}
	
	@Test
	public void getQueryMultiple() {
		QueryParser newParser = new QueryParser(" jmbag=\"Banana\" and jmbag >= \"123\" And jmbag LIKE \"090909\"");
		assertEquals(newParser.getQuery().size(), 3);
	}
	
	@Test(expected = IllegalStateException.class)
	public void notADirectQuery() {
		QueryParser newParser = new QueryParser("     jmbag  <= \"Maki\" ");
		newParser.getQueriedJMBAG();
	}
	
	@Test(expected = IllegalStateException.class)
	public void notADirectQuery2() {
		QueryParser newParser = new QueryParser("     jmbag  like \"Maki\" ");
		newParser.getQueriedJMBAG();
	}
}
