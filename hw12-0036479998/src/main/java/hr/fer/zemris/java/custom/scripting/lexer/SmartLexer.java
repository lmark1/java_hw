package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;

/**
 * Lexer used for generating tokens. Tokens are used by
 * {@link SmartScriptParser} which generates Nodes and Elements.
 * {@link LexerException} is thrown when unrecognized token appears. Tokens are
 * defined by {@link SmartTokenType} enumeration.
 * 
 * @author Lovro Marković
 *
 */
public class SmartLexer {

	/**
	 * Lexer data stored as an array of characters.
	 */
	private char[] data;

	/**
	 * Current index of the character that will be processed.
	 */
	private int currentIndex;

	/**
	 * Current token extracted from data.
	 */
	private SmartToken token;

	/**
	 * Current state of the SmartLexer instance.
	 */
	private SmartLexerState state;

	/**
	 * Constructor for the Lexer objects. Accepts text from which tokens will be
	 * extracted.
	 * 
	 * @param text
	 *            Text that will be analysed.
	 * @throws IllegalArgumentException
	 *             Exception thrown if the passed String is null.
	 */
	public SmartLexer(String text) {

		if (text == null) {
			throw new IllegalArgumentException("Text string cannot be null.");
		}

		data = text.toCharArray();
		currentIndex = 0;
		state = SmartLexerState.TEXT;
	}

	/**
	 * Toggles the current state of Lexer.
	 */
	private void toggleLexerState() {

		if (state == SmartLexerState.TAG) {
			state = SmartLexerState.TEXT;

		} else {
			state = SmartLexerState.TAG;
		}

	}

	/**
	 * Generates and return next token.
	 * 
	 * @return Next token.
	 * @throws LexerException
	 *             Exception thrown if next token cannot be generated.
	 */
	public SmartToken nextToken() {
		findNextToken();
		return token;
	}

	/**
	 * Get last generated token. It does not start generating the next token.
	 * The method can be run multiple times.
	 * 
	 * @return Last generated token.
	 */
	public SmartToken getToken() {
		return token;
	}

	/**
	 * Searches for the next token in data.
	 * 
	 * @throws LexerException
	 *             Exception thrown if there are no more tokens left to analyse.
	 */
	private void findNextToken() {

		// If there are no more tokens, throw exception
		if (token != null && token.getType() == SmartTokenType.EOF) {
			throw new LexerException("Current token is EOF; No more tokens left.");
		}

		// If processing a TAG skip all empty spaces
		if (state == SmartLexerState.TAG) {
			skipEmptySpaces();
		}

		// If there is no more data left to process generate EOF token
		if (currentIndex >= data.length) {
			token = new SmartToken(SmartTokenType.EOF, null);
			return;
		}

		if (isNextTokenText()) {
			return;
		}

		if (isStartTag()) {
			toggleLexerState();
			token = new SmartToken(SmartTokenType.START_TAG, new String(data, currentIndex, 2));
			currentIndex += 2;
			return;
		}

		if (isEndTag()) {
			toggleLexerState();
			token = new SmartToken(SmartTokenType.END_TAG, new String(data, currentIndex, 2));
			currentIndex += 2;
			return;
		}

		if (checkTagType()) {
			return;
		}

		if (checkTagContents()) {
			return;
		}

		throw new LexerException("Do not recognize next token.");
	}

	/**
	 * Checks if there are some TAG contents.
	 * 
	 * @return True if some TAG content is found, otherwise false.
	 */
	private boolean checkTagContents() {

		// TAG is encountered and its type is found -> check for things inside
		return state == SmartLexerState.TAG && token.getType() != SmartTokenType.START_TAG && (isNextFunction()
				|| isNextOperator() || isNextString() || isNextVariable() || isNextIntOrDoubleConstant());

	}

	/**
	 * Checks if the next token inside TAG is string. if it is, an appropriate
	 * token is created and stored.
	 * 
	 * @return True if next token is string, otherwise false.
	 */
	private boolean isNextString() {

		if (currentIndex < data.length && data[currentIndex] == '"') {
			currentIndex++;
			
			boolean escape = false;
			StringBuilder string = new StringBuilder();
			while (currentIndex < data.length) {

				if (escape && data[currentIndex] != '\\' && data[currentIndex] != '"' && 
						data[currentIndex] != 'r' && data[currentIndex] != 'n' &&
						data[currentIndex] != 't') {
					throw new LexerException("Invalid escape sequence");
				}

				if (!escape && data[currentIndex] == '"'){
					break;
				}

				// Set Escape sequence
				if (!escape && data[currentIndex] == '\\') {
					currentIndex++;
					escape = true;
					continue;
				}
				
				if (escape) {
					if  (data[currentIndex] == '\\') {
						string.append("\\");
					
					} else if (data[currentIndex] == '"') {
						string.append("\"");
					
					} else if (data[currentIndex] == 'n') {
						string.append("\n");
					
					} else if (data[currentIndex] == 'r') {
						string.append("\r");
					
					} else if (data[currentIndex] == 't') {
						string.append("\t");
					}
					
					escape = false;
				} else if (!escape) {
					string.append(data[currentIndex]);
				}
				
				currentIndex++;
			}

			currentIndex++;
			token = new SmartToken(SmartTokenType.STRING, string.toString());
			return true;
		}

		return false;
	}

	/**
	 * Check if the next token inside TAG is int or double constant. If it is,
	 * an appropriate token is created and stored.
	 * 
	 * @return True if next token is int or double constant, ohterwise false.
	 * @throws LexerException
	 *             Exception thrown if the number can't be parsed as integer or
	 *             double.
	 */
	private boolean isNextIntOrDoubleConstant() {

		if (Character.isDigit(data[currentIndex])) {
			StringBuilder number = new StringBuilder();
			number.append(data[currentIndex]);
			currentIndex++;

			while (currentIndex < data.length && (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.'
					|| data[currentIndex] == ',')) {
				number.append(data[currentIndex]);
				currentIndex++;
			}

			// Try parsing number as integer
			String parseNumber = number.toString().replaceAll(",", ".");
			int numberInt;
			try {
				numberInt = Integer.parseInt(parseNumber);
				token = new SmartToken(SmartTokenType.CONSTANT_INTEGER, numberInt);
				return true;
			} catch (NumberFormatException ignore) {
			}

			// Try parsing as double
			double numberDouble;
			try {
				numberDouble = Double.parseDouble(number.toString());
				token = new SmartToken(SmartTokenType.CONSTANT_DOUBLE, numberDouble);
				return true;
			} catch (NumberFormatException e) {
				throw new LexerException("Cannot parse " + number.toString() + " as Double or Integer.");
			}
		}

		return false;
	}

	/**
	 * Checks if the next token is variable. If it is, an appropriate token is
	 * created and stored.
	 * 
	 * @return True if next token is variable, otherwise false.
	 */
	private boolean isNextVariable() {

		if (Character.isLetter(data[currentIndex])) {

			StringBuilder var = new StringBuilder();

			while (currentIndex < data.length && (Character.isLetter(data[currentIndex])
					|| Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
				var.append(data[currentIndex]);
				currentIndex++;
			}

			token = new SmartToken(SmartTokenType.VARIABLE, var.toString());
			return true;
		}

		return false;
	}

	/**
	 * Checks if current TAG contains an operato element at current position. If
	 * it does an appropriate token is created and stored.
	 * 
	 * @return True if tag contains an operator element at current position,
	 *         otherwise false.
	 */
	private boolean isNextOperator() {
		char symbol = data[currentIndex];

		if (symbol == '+' || symbol == '*' || symbol == '/'
				|| (symbol == '-' && !Character.isDigit(data[currentIndex + 1])) || symbol == '^') {
			token = new SmartToken(SmartTokenType.OPERATOR, symbol);
			currentIndex++;
			return true;
		}

		return false;
	}

	/**
	 * Checks if current TAG contains a function element at current position. If
	 * it does an approprite token is created and stored.
	 * 
	 * @return True if tag contains a function element at current position,
	 *         otherwise false.
	 */
	private boolean isNextFunction() {

		// If it starts with @ + letter
		if ((currentIndex + 1) < data.length && data[currentIndex] == '@'
				&& Character.isLetter(data[currentIndex + 1])) {
			currentIndex++;

			StringBuilder function = new StringBuilder();

			while (currentIndex < data.length && (Character.isLetter(data[currentIndex])
					|| Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
				function.append(data[currentIndex]);
				currentIndex++;
			}

			token = new SmartToken(SmartTokenType.FUNCTION, function.toString());
			return true;
		}

		return false;
	}

	/**
	 * Checks which type of TAG is currently open. An appropriate token is
	 * created for the type of tag found.
	 * 
	 * @return True if a tag is currently open and a certain type is found,
	 *         otherwise false.
	 */
	private boolean checkTagType() {

		return state == SmartLexerState.TAG && token.getType() == SmartTokenType.START_TAG
				&& (isNextEnd() || isNextEquals() || isNextFor());
	}

	/**
	 * Check if current char is '='. If it is, make a new current token with the
	 * appropriate type and value.
	 * 
	 * @return True if current char is '=' otherwise false.
	 */
	private boolean isNextEquals() {

		if (data[currentIndex] == '=') {
			token = new SmartToken(SmartTokenType.EQUALS, data[currentIndex]);
			currentIndex++;
			return true;
		}

		return false;
	}

	/**
	 * Check if current tag is FOR. If it is, make a new current token with the
	 * appropriate type and value.
	 * 
	 * @return True if current tag is FOR, otherwise false.
	 * @throws LexerException
	 *             Exception thrown if for is misspelled.
	 */
	private boolean isNextFor() {

		if ((currentIndex + 2) < data.length) {
			String forCheck = new String(data, currentIndex, 3);

			if (forCheck.toLowerCase().equals("for")) {
				currentIndex += 3;

				if (isCharBlank(data[currentIndex])) {
					token = new SmartToken(SmartTokenType.FOR, forCheck);
					return true;
				}

				throw new LexerException("Invalid tag type" + forCheck + data[currentIndex]);
			}
		}

		return false;
	}

	/**
	 * check if current tag is END. If it is, make a new current token with the
	 * appropriate type and value.
	 * 
	 * @return True if current tag is END, otherwise false.
	 * @throws LexerException
	 *             exception thrown if end is misspelled.
	 */
	private boolean isNextEnd() {

		if ((currentIndex + 3) < data.length) {
			String endCheck = new String(data, currentIndex, 3);

			if (endCheck.toLowerCase().equals("end")) {
				currentIndex += 3;

				if (isCharBlank(data[currentIndex]) || isEndTag()) {
					token = new SmartToken(SmartTokenType.END, endCheck);
					return true;
				}

				throw new LexerException("Illegal tag definition " + endCheck + data[currentIndex]);
			}
		}

		return false;
	}

	/**
	 * Determines if the next token is Text type. If it is make a new current
	 * token with the appropriate type and value.
	 * 
	 * @return True if the token is text, otherwise false.
	 * @throws LexerException
	 *             Exception thrown if escape is invalid.
	 */
	private boolean isNextTokenText() {

		StringBuilder text = new StringBuilder();
		boolean escapeState = false;

		if (state == SmartLexerState.TEXT) {

			while (currentIndex < data.length) {
				escapeState = setTextEscape(escapeState);

				if (isStartTag() && !escapeState) {
					break;
				}

				text.append(data[currentIndex]);
				currentIndex++;
			}

			if (text.length() > 0) {
				token = new SmartToken(SmartTokenType.TEXT, text.toString());
				return true;
			}

		}
		return false;
	}

	/**
	 * Checks if next token is start tag '{$'. If it is make a new current token
	 * with the appropriate type and value.
	 * 
	 * @return True if the next token is start tag, otherwise false.
	 */
	private boolean isStartTag() {

		if (state == SmartLexerState.TEXT && (currentIndex + 1) < data.length && data[currentIndex] == '{'
				&& data[currentIndex + 1] == '$') {

			return true;
		}

		return false;
	}

	/**
	 * Checks if next token is start tag '$}'. If it is make a new current token
	 * with the appropriate type and value.
	 * 
	 * @return True if the next token is start tag, otherwise false.
	 */
	private boolean isEndTag() {

		if (state == SmartLexerState.TAG && (currentIndex + 1) < data.length && data[currentIndex] == '$'
				&& data[currentIndex + 1] == '}') {

			return true;
		}

		return false;
	}

	/**
	 * Checks if the text escape operator '\\' is in a legal syntax '\{' or
	 * '\\'.
	 * 
	 * @param escapeState
	 *            Current escape state.
	 * @return True if the text escape is legal, false if the escape state is
	 *         over, otherwise do not change escape state.
	 * @throws LexerException
	 *             Exception thrown if escape is invalid.
	 */
	private boolean setTextEscape(boolean escapeState) {

		if (data[currentIndex] == '\\') {

			if ((currentIndex + 1) < data.length && (data[currentIndex + 1] == '{' || data[currentIndex + 1] == '\\')) {
				currentIndex++;
				return true;
			}

			throw new SmartScriptParserException("Found illegal escape.");

		} else if (isCharBlank(data[currentIndex])) {
			return false;
		}

		return escapeState;
	}

	/**
	 * Moves curentIndex to the first character in data array that contains a
	 * symbol.
	 */
	private void skipEmptySpaces() {

		while (currentIndex < data.length) {
			char c = data[currentIndex];

			if (isCharBlank(c)) {
				currentIndex++;
				continue;
			}
			break;
		}
	}

	/**
	 * Checks if given character is blank.
	 * 
	 * @param c
	 *            Character the check is performed on.
	 * @return True if the character is blank, otherwise false.
	 */
	private boolean isCharBlank(Character c) {
		if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
			return true;
		}

		return false;
	}
}
