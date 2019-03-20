package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.custom.scripting.lexer.SmartLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartToken;
import hr.fer.zemris.java.custom.scripting.lexer.SmartTokenType;
import hr.fer.zemris.java.hw03.prob1.LexerException;

@SuppressWarnings("javadoc")
public class SmartLexerTest {

	@Test
	public void escapeTest() {
		String simpleDoc = "A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.";
		SmartLexer lexer = new SmartLexer(simpleDoc);
		SmartToken[] correctData = new SmartToken[] {
				new SmartToken(SmartTokenType.TEXT, "A tag follows "),
				new SmartToken(SmartTokenType.START_TAG, "{$"),
				new SmartToken(SmartTokenType.EQUALS, '='),
				new SmartToken(SmartTokenType.STRING, "Joe \\\"Long\\\" Smith"),
				new SmartToken(SmartTokenType.END_TAG, "$}")
		};
		
		checkTokenStream(lexer, correctData);
		
	}
	
	@Test
	public void mixedEchoNode() {
		String simpleDoc = "{$= i i * @sin \"0.000\" @decfmt $}";
		SmartLexer lexer = new SmartLexer(simpleDoc);
		SmartToken[] correctData = new SmartToken[] {
				new SmartToken(SmartTokenType.START_TAG, "{$"),
				new SmartToken(SmartTokenType.EQUALS, '='),
				new SmartToken(SmartTokenType.VARIABLE, "i"),
				new SmartToken(SmartTokenType.VARIABLE, "i"),
				new SmartToken(SmartTokenType.OPERATOR, '*'),
				new SmartToken(SmartTokenType.FUNCTION, "sin"),
				new SmartToken(SmartTokenType.STRING, "0.000"),
				new SmartToken(SmartTokenType.FUNCTION, "decfmt"),
				new SmartToken(SmartTokenType.END_TAG, "$}")
				
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void mixedEchoNodeWithoutSpaces() {
		String simpleDoc = "{$=i i*@sin\"0.000\"@decfmt$}";
		SmartLexer lexer = new SmartLexer(simpleDoc);
		SmartToken[] correctData = new SmartToken[] {
				new SmartToken(SmartTokenType.START_TAG, "{$"),
				new SmartToken(SmartTokenType.EQUALS, '='),
				new SmartToken(SmartTokenType.VARIABLE, "i"),
				new SmartToken(SmartTokenType.VARIABLE, "i"),
				new SmartToken(SmartTokenType.OPERATOR, '*'),
				new SmartToken(SmartTokenType.FUNCTION, "sin"),
				new SmartToken(SmartTokenType.STRING, "0.000"),
				new SmartToken(SmartTokenType.FUNCTION, "decfmt"),
				new SmartToken(SmartTokenType.END_TAG, "$}")
				
		};
		
		checkTokenStream(lexer, correctData);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullLexerText() {
		String simpleDoc = null;
		new SmartLexer(simpleDoc);
	}
	
	@Test(expected = LexerException.class)
	public void getMoreThanAvaliable() {
		String simpleDoc = "";
		SmartLexer lex = new SmartLexer(simpleDoc);
		
		lex.nextToken();
		lex.nextToken();
	}
	
	private void checkTokenStream(SmartLexer lexer, SmartToken[] correctData) {
		int counter = 0;
		for(SmartToken expected : correctData) {
			SmartToken actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(msg, expected.getType(), actual.getType());
			assertEquals(msg, expected.getValue(), actual.getValue());
			counter++;
		}
	}
}
