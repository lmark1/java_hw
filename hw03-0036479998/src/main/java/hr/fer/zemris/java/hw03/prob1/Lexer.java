package hr.fer.zemris.java.hw03.prob1;

/**
 * A simple lexic analyser. Extracts tokens type {@link TokenType} - WORD,
 * NUMBER, SYMBOL, EOF. Supports escaping numbers with '\' so they can be parsed
 * as WORD. Class has two states defined with {@link LexerState}. When in
 * EXTENDED state all chars are parsed as WORD after '#' appears. BASIC state
 * starts again when another '#' is encountered. For invalid strings
 * {@link LexerException} is thrown.
 * 
 * @author Lovro Marković
 *
 */
public class Lexer {

	/**
	 * Input text.
	 */
	private char[] data;

	/**
	 * Current token.
	 */
	private Token token;

	/**
	 * Index of the first unprocessed token
	 */
	private int currentIndex;

	/**
	 * Current state of lexer.
	 */
	private LexerState state;

	/**
	 * Constructor for Lexer class.
	 * 
	 * @param text
	 *            Text that is turned into tokens.
	 * @throws LexerException
	 *             Exception thrown if there is an error while generating next
	 *             token.
	 * @throws IllegalArgumentException
	 *             Exception thrown if the passed String reference is null.
	 */
	public Lexer(String text) {

		if (text == null) {
			throw new IllegalArgumentException("Input text cannot be null.");
		}

		data = text.toCharArray();
		currentIndex = 0;
		state = LexerState.BASIC;
	}

	/**
	 * Sets the current state of lexer.
	 * 
	 * @param state
	 *            New lexer state.
	 * @throws IllegalArgumentException
	 *             Exception thrown if passed {@link LexerState} is null.
	 */
	public void setState(LexerState state) {

		if (state == null) {
			throw new IllegalArgumentException("Passed lexer state is null.");
		}
		this.state = state;
	}

	/**
	 * Generates and return next token.
	 * 
	 * @return Next token.
	 * @throws LexerException
	 *             Exception thrown if next token cannot be generated.
	 */
	public Token nextToken() {
		findNextToken();
		return getToken();
	}

	/**
	 * Get last generated token. It does not start generating the next token.
	 * The method can be run multiple times.
	 * 
	 * @return Last generated token.
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Searches for the next token in data.
	 * 
	 * @throws LexerException
	 *             Exception thrown if there are no more tokens to analyse.
	 */
	private void findNextToken() {

		// If nextToken is called after EOF token is detected
		if (token != null && token.getType() == TokenType.EOF) {
			throw new LexerException("Current token is EOF; No more tokens left.");
		}

		skipEmptySpaces();

		// If there is no more data left to process generate EOF token
		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}

		if (state == LexerState.BASIC && (isNextWord() || isNextNumber() || isNextSymbol())) {
			return;
		}

		if (state == LexerState.EXTENDED && (isNextWordExtended() || isNextSymbol())) {
			return;
		}

		// If none of the above then exception
		throw new LexerException("Do not recognize next token.");

	}

	/**
	 * Parse WORD when EXTENDED state is active. Update current token when
	 * parsing is finished.
	 * 
	 * @return True if there are some chars between '#' symbols, otherwise
	 *         false.
	 */
	private boolean isNextWordExtended() {
		StringBuilder word = new StringBuilder();

		while (currentIndex < data.length) {
			char c = data[currentIndex];

			if (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '#') {
				break;
			}

			word.append(c);
			currentIndex++;
		}

		if (word.length() == 0) {
			return false;
		}

		token = new Token(TokenType.WORD, word.toString());

		return true;
	}

	/**
	 * Check if next token type is SYMBOL and create new current token object
	 * with its value.
	 * 
	 * @return True if the next token is SYMBOL, otherwise false.
	 */
	private boolean isNextSymbol() {
		char symbol = data[currentIndex];

		if (Character.isDigit(symbol) || Character.isLetter(symbol) || symbol == '\\') {
			return false;
		}

		token = new Token(TokenType.SYMBOL, symbol);
		currentIndex++;
		return true;
	}

	/**
	 * Check if next token is a NUMBER and create new current token object with
	 * its value.
	 * 
	 * @return True if it's a number, otherwise false.
	 * @throws LexerException
	 *             Exception thrown when number cannot be parsed.
	 */
	private boolean isNextNumber() {

		if (Character.isDigit(data[currentIndex])) {
			StringBuilder word = new StringBuilder();

			while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				word.append(data[currentIndex]);
				currentIndex++;
			}

			long numberValue;
			try {
				numberValue = Long.parseLong(word.toString());
			} catch (NumberFormatException ex) {
				throw new LexerException("Cannot parse " + word.toString() + ".");
			}

			token = new Token(TokenType.NUMBER, numberValue);
			return true;
		}

		return false;
	}

	/**
	 * Check if token is WORD and create new current token object with its
	 * value.
	 * 
	 * @return True if next token is WORD, otherwise false.
	 */
	private boolean isNextWord() {

		if (Character.isLetter(data[currentIndex]) || isEscapeBackslash()) {
			StringBuilder word = new StringBuilder();

			// Do not add escape backslashes to string
			if (!isEscapeBackslash()) {
				word.append(data[currentIndex]);
			}
			currentIndex++;

			while (currentIndex < data.length && (Character.isLetter(data[currentIndex])) || isEscapedDigitOrBackslash()
					|| isEscapeBackslash()) {

				// Do not add escape backslashes to string
				if (!isEscapeBackslash()) {
					word.append(data[currentIndex]);
				}

				currentIndex++;
			}

			token = new Token(TokenType.WORD, word.toString());
			return true;
		}

		return false;
	}

	/**
	 * Checks if the character at the current index is an escaped digit or a
	 * backslash.
	 * 
	 * @return True if the digit is escaped, otherwise false.
	 */
	private boolean isEscapedDigitOrBackslash() {
		if (currentIndex < data.length && data[currentIndex - 1] == '\\'
				&& (Character.isDigit(data[currentIndex]) || data[currentIndex] == '\\')) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if '\\' is used for escaping a number in string.
	 * 
	 * @return True if '\\' is used for escaping a number in string, otherwise
	 *         false.
	 */
	private boolean isEscapeBackslash() {
		if ((currentIndex + 1) < data.length && data[currentIndex] == '\\'
				&& (Character.isDigit(data[currentIndex + 1]) || data[currentIndex + 1] == '\\')) {
			return true;
		}

		return false;
	}

	/**
	 * Moves curentIndex to the first character in data array that contains a
	 * symbol.
	 */
	private void skipEmptySpaces() {

		while (currentIndex < data.length) {
			char c = data[currentIndex];

			if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
				currentIndex++;
				continue;
			}
			break;
		}
	}
}
