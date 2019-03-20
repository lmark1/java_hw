package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.db.lexer.LexerException;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.Token;
import hr.fer.zemris.java.hw04.db.lexer.TokenType;
import hr.fer.zemris.java.hw04.db.lexer.WildcardException;

/**
 * Class represents a parser of query statements and it gets everything after
 * the query keyword as a string through constructor
 * 
 * @author Lovro Marković
 *
 */
public class QueryParser {

	/**
	 * List of tokens extracted from the parser.
	 */
	private List<Token> tokenList;

	/**
	 * Lexer which generates tokens for parsing.
	 */
	private QueryLexer lexer;

	/**
	 * Constructor for the QueryParser objects. Expects a string which it will
	 * immediately parse.
	 * 
	 * @param query
	 *            String that will be parsed.
	 * @throws IllegalArgumentException
	 *             Exception thrown if invalid query is passed.
	 */
	public QueryParser(String query) {
		lexer = new QueryLexer(query);
		tokenList = new ArrayList<>();

		generateTokens();
	}

	/**
	 * Checks if the given query is of direct type. Direct query for is as
	 * follows: jmbag="xxx".
	 * 
	 * @return True if it is a direct query, otherwise false.
	 */
	public boolean isDirectQuery() {

		if (tokenList.size() == 3) {
			if (tokenList.get(1).getType() == TokenType.COMPARISON_OPERATOR && tokenList.get(1).getValue().equals("=")
					&& tokenList.get(0).getType() == TokenType.ATTRIBUTE_NAME
					&& tokenList.get(0).getValue().equals("jmbag")
					&& tokenList.get(2).getType() == TokenType.STRING_LITERAL) {

				return true;
			}
		}

		return false;
	}

	/**
	 * Method returns the string literal which was given in equality comparison
	 * in direct query.
	 * 
	 * @return String literal of the direct query expression.
	 * @throws IllegalStateException
	 *             Exception thrown if this query is not direct.
	 */
	public String getQueriedJMBAG() {

		if (!isDirectQuery()) {
			throw new IllegalStateException("Query is not direct");
		}

		return tokenList.get(2).getValue().toString();
	}

	/**
	 * Return a list of all conditional expressions constructed from generated
	 * tokens.
	 * 
	 * @return List of all conditional expressions.
	 */
	public List<ConditionalExpression> getQuery() {
		List<ConditionalExpression> expressionList = new ArrayList<>();

		for (int i = 2; i < tokenList.size(); i += 4) {

			String stringLiteral = tokenList.get(i).getValue().toString();
			IComparisonOperator comparisonOperator = getOperator(tokenList.get(i - 1));
			IFieldValueGetter fieldGetter = getFieldGetter(tokenList.get(i - 2));

			expressionList.add(new ConditionalExpression(fieldGetter, stringLiteral, comparisonOperator));
		}

		return expressionList;
	}

	/**
	 * Return field value getter based on the given token.
	 * 
	 * @param token
	 *            Given token.
	 * @return Field value getter, or null if wrong token is given.
	 */
	private static IFieldValueGetter getFieldGetter(Token token) {

		if (token.getType() != TokenType.ATTRIBUTE_NAME) {
			return null;
		}

		switch (token.getValue().toString()) {
		case "jmbag":
			return FieldValueGetters.JMBAG;

		case "firstName":
			return FieldValueGetters.FIRST_NAME;

		case "lastName":
			return FieldValueGetters.LAST_NAME;

		default:
			return null;
		}
	}

	/**
	 * Return comparison operator based on the given token.
	 * 
	 * @param token
	 *            Given token.
	 * @return Comparison operator, or null if wrong token is given.
	 */
	private static IComparisonOperator getOperator(Token token) {

		if (token.getType() != TokenType.COMPARISON_OPERATOR) {
			return null;
		}

		switch (token.getValue().toString().toLowerCase()) {
		case "<":
			return ComparisonOperators.LESS;

		case "<=":
			return ComparisonOperators.LESS_OR_EQUALS;

		case ">":
			return ComparisonOperators.GREATER;

		case ">=":
			return ComparisonOperators.GREATER_OR_EQUALS;

		case "=":
			return ComparisonOperators.EQUALS;

		case "!=":
			return ComparisonOperators.NOT_EQUALS;

		case "like":
			return ComparisonOperators.LIKE;

		default:
			return null;
		}
	}

	/**
	 * Generates tokens using the initated lexer variable.
	 * 
	 * @throws IllegalArgumentException
	 *             If query sequence is invalid.
	 * @throws WildcardException
	 *             Exception thrown if too many WildCard characters are found in
	 *             like comparison string literal.
	 */
	private void generateTokens() {
		boolean attributeExpected = true;
		boolean operatorExpected = false;
		boolean stringExpected = false;

		while (true) {
			Token newToken;

			try {
				newToken = lexer.nextToken();

			} catch (LexerException e) {

				if (lexer.getToken() != null && lexer.getToken().getType() == TokenType.EOF) {
					break;
				}

				throw new IllegalArgumentException("Invalid query sequence");
			}

			if (attributeExpected && !operatorExpected && !stringExpected
					&& newToken.getType() == TokenType.ATTRIBUTE_NAME) {
				tokenList.add(newToken);
				attributeExpected = false;
				operatorExpected = true;
				continue;
			}

			if (!attributeExpected && operatorExpected && !stringExpected
					&& newToken.getType() == TokenType.COMPARISON_OPERATOR) {
				tokenList.add(newToken);
				operatorExpected = false;
				stringExpected = true;
				continue;
			}

			if (!attributeExpected && !operatorExpected && stringExpected
					&& newToken.getType() == TokenType.STRING_LITERAL) {
				tokenList.add(newToken);
				stringExpected = false;
				continue;
			}

			if (!attributeExpected && !operatorExpected && !stringExpected
					&& newToken.getType() == TokenType.AND_OPERATOR) {
				tokenList.add(newToken);
				attributeExpected = true;
				continue;
			}

			if (!attributeExpected && !operatorExpected && !stringExpected && newToken.getType() == TokenType.EOF) {
				continue;
			}

			throw new IllegalArgumentException("Invalid query input");
		}

		// If there is somehow only 2 tokens and no exception is thrown
		if (tokenList.size() < 3) {
			throw new IllegalArgumentException("Less than 3 tokens found");
		}

		// Check for wildcard characters after LIKE operators
		for (int i = 1; i < tokenList.size(); i++) {

			if (tokenList.get(i - 1).getType() == TokenType.COMPARISON_OPERATOR
					&& tokenList.get(i - 1).getValue().toString().toLowerCase().equals("like")
					&& tokenList.get(i).getType() == TokenType.STRING_LITERAL) {

				int wildCardNumber = howManyWildCards(tokenList.get(i).getValue().toString());

				if (wildCardNumber > 1) {
					throw new WildcardException(
							"Too many wild card characters in " + tokenList.get(i).getValue().toString());
				}
			}
		}
	}

	/**
	 * Counts the number of wildcard(*) characters in a string.
	 * 
	 * @param value
	 *            String given for analysis.
	 * @return Number of wildcard characters in a string.
	 */
	private static int howManyWildCards(String value) {
		int wildCardCount = 0;

		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '*')
				wildCardCount++;
		}

		return wildCardCount;
	}
}
