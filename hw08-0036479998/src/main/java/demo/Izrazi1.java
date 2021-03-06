package demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.LexerException;
import hr.fer.zemris.bf.lexer.Token;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.utils.Util;

/**
 * This class compares given input from 'izrazi1.txt' with generated lexer
 * output.
 * 
 * @author Lovro Marković
 *
 */
public class Izrazi1 {

	/**
	 * Main method of the class. Run when program is exectued.
	 * 
	 * @param args No arguments accepted.
	 * @throws IOException Exception thrown if reading given file contents fails.
	 */
	public static void main(String[] args) throws IOException {
		String[] expressions = new String[] { "0", "tRue", "Not a", "A aNd b",
				"a or b", "a xoR b",

				"A and b * c", "a or b or c", "a xor b :+: c", "not not a",
				"a or b xor c and d", "a or b xor c or d", "a xor b or c xor d",
				"(a + b) xor (c or d)", "(d or b) xor not (a or c)",
				"(c or d) mor not (a or b)", "not a not b", "a and (b or",
				"a and (b or c", "a and 10" };

		List<String> testList = new ArrayList<>();
		for (String expr : expressions) {
			testList.add("==================================");
			testList.add("Izraz: " + expr);
			testList.add("==================================");

			System.out.println("==================================");
			System.out.println("Izraz: " + expr);
			System.out.println("==================================");

			try {
				System.out.println("Tokenizacija:");
				testList.add("Tokenizacija:");

				Lexer lexer = new Lexer(expr);
				Token token = null;
				do {
					token = lexer.nextToken();
					System.out.println(token);
					testList.add(token.toString());

				} while (token.getTokenType() != TokenType.EOF);

			} catch (LexerException ex) {
				System.out.println(
						"Iznimka: " + ex.getClass() + " - " + ex.getMessage());
				testList.add(
						"Iznimka: " + ex.getClass() + " - " + ex.getMessage());
			}
			System.out.println();
			testList.add("");
		}

		// Remove last entry
		testList.remove(testList.size() - 1);

		Path izraz1 = Paths.get("./src/main/resources/Izrazi1.txt");
		Util.checkLists(testList, izraz1);
	}
}
