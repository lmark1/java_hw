package hr.fer.zemris.bf.lexer;

/**
 * This class generates tokens from the given String.
 * 
 * @author Lovro Marković
 *
 */
public class Lexer {

	/**
	 * Array of characters representing the input String.
	 */
	private char[] data;

	/**
	 * Current index of the character that is being analysed.
	 */
	private int currentIndex;

	/**
	 * Current token found.
	 */
	private Token currentToken;

	/**
	 * Constructor for the Lexer object. Initializes the input string.
	 * 
	 * @param expression
	 *            Given expression that will be tokenized.
	 * @throws IllegalArgumentException
	 *             Exception thrown when input String is null.
	 */
	public Lexer(String expression) {

		if (expression == null) {
			throw new IllegalArgumentException("Input String can't be null.");
		}

		data = expression.toCharArray();
		currentIndex = 0;
		currentToken = null;
	}

	/**
	 * Generates next token from the input String.
	 * 
	 * @return Next token.
	 * @throws LexerException
	 *             Exception thrown if there is no more tokens to be generated,
	 *             or an unable to recognize a token sequence.
	 */
	public Token nextToken() {
		return generateToken();
	}

	/**
	 * Generates next token from the data array.
	 * 
	 * @return Next token.
	 * @throws LexerException
	 *             Exception thrown if an invalid array of characters appears in
	 *             the sequence.
	 */
	private Token generateToken() {

		if (currentToken != null
				&& currentToken.getTokenType() == TokenType.EOF) {
			throw new LexerException(
					"There are no more tokens in the given String.");
		}

		if (currentIndex >= data.length) {
			currentToken = new Token(TokenType.EOF, null);
			return currentToken;
		}

		while (Character.isWhitespace(data[currentIndex])) {
			currentIndex++;
		}

		if (Character.isLetter(data[currentIndex])) {
			currentToken = getIndentificator();
			return currentToken;
		}

		if (Character.isDigit(data[currentIndex])) {
			currentToken = getNumerical();
			return currentToken;
		}

		return getSymbolTokens();
	}

	/**
	 * Checks for symbol tokens such as '(', ')', '*', '+', '!', ':+:'.
	 * 
	 * @return Returns an appropriate single character token.
	 * @throws LexerException
	 *             Exception thrown if no such token found.
	 */
	private Token getSymbolTokens() {
		switch (data[currentIndex]) {

		case '(':
			currentIndex++;
			return new Token(TokenType.OPEN_BRACKET, '(');

		case ')':
			currentIndex++;
			return new Token(TokenType.CLOSED_BRACKET, ')');

		case '+':
			currentIndex++;
			return new Token(TokenType.OPERATOR, "or");

		case '*':
			currentIndex++;
			return new Token(TokenType.OPERATOR, "and");

		case '!':
			currentIndex++;
			return new Token(TokenType.OPERATOR, "not");
		}

		if (currentIndex + 2 < data.length) {
			String xor = new String(data, currentIndex, 3);

			if (xor.equals(":+:")) {
				currentIndex += 3;
				return new Token(TokenType.OPERATOR, "xor");
			}
		}

		throw new LexerException("No valid token found.");
	}

	/**
	 * Finds numerical tokens - 1 or more numbers in a row.
	 * 
	 * @return Appropriate numerical token.
	 * @throws LexerException
	 *             Exception thrown if numerical token found is not 0 or 1.
	 */
	private Token getNumerical() {
		StringBuilder newString = new StringBuilder();

		newString.append(data[currentIndex]);
		currentIndex++;

		while (currentIndex < data.length
				&& Character.isDigit(data[currentIndex])) {
			newString.append(data[currentIndex]);
			currentIndex++;
		}

		String tokenValue = newString.toString();

		if (tokenValue.equals("0") || tokenValue.equals("1")) {
			return new Token(TokenType.CONSTANT, tokenValue.equals("1"));
		}

		throw new LexerException(
				"Unexpected number: " + tokenValue + ".");
	}

	/**
	 * Finds identificator tokens (Operators, constants or variables)
	 * 
	 * @return Identificator token.
	 */
	private Token getIndentificator() {
		StringBuilder newString = new StringBuilder();

		newString.append(data[currentIndex]);
		currentIndex++;

		while (currentIndex < data.length
				&& (Character.isLetterOrDigit(data[currentIndex])
						|| data[currentIndex] == '_')) {

			newString.append(data[currentIndex]);
			currentIndex++;
		}

		String tokenValue = newString.toString();

		if (tokenValue.toLowerCase().equals("and")
				|| tokenValue.toLowerCase().equals("or")
				|| tokenValue.toLowerCase().equals("not")
				|| tokenValue.toLowerCase().equals("xor")) {
			
			return new Token(TokenType.OPERATOR, tokenValue.toLowerCase());
		}

		if (tokenValue.toLowerCase().equals("true")
				|| tokenValue.toLowerCase().equals("false")) {
			
			return new Token(TokenType.CONSTANT,
					tokenValue.toLowerCase().equals("true"));
		}

		return new Token(TokenType.VARIABLE, tokenValue.toUpperCase());
	}
}
