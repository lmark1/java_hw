package hr.fer.zemris.bf.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.utils.ExpressionTreePrinter;
import hr.fer.zemris.bf.utils.Util;

@SuppressWarnings("javadoc")
public class ParserTest {

	@Test
	public void testParser() throws IOException {
		String[] expressions = new String[] { "0", "tRue", "Not a", "A aNd b",
				"a or b", "a xoR b", "A and b * c", "a or b or c",
				"a xor b :+: c", "not not a", "a or b xor c and d",
				"a or b xor c or d", "a xor b or c xor d",
				"(a + b) xor (c or d)", "(d or b) xor not (a or c)",
				"(c or d) mor not (a or b)", "not a not b", "a and (b or",
				"a and (b or c", "a and 10" };

		List<String> testList = new ArrayList<>();
		for (String expr : expressions) {
			System.out.println("==================================");
			System.out.println("Izraz: " + expr);
			System.out.println("==================================");

			testList.add("==================================");
			testList.add("Izraz: " + expr);
			testList.add("==================================");

			try {
				System.out.println("Stablo:");
				testList.add("Stablo:");
				
				Parser parser = new Parser(expr);
				ExpressionTreePrinter visitor = new ExpressionTreePrinter();
				parser.getExpression().accept((NodeVisitor) visitor);

				testList.addAll(visitor.getOutputList());
			} catch (ParserException ex) {
				System.out.println(
						"Iznimka: " + ex.getClass() + " - " + ex.getMessage());
				testList.add(
						"Iznimka: " + ex.getClass() + " - " + ex.getMessage());
			}
			System.out.println();
			testList.add("");
		}
		
		// Remove last entry
		testList.remove(testList.size()-1);
		
		Path izraz2 = Paths.get("./src/main/resources/Izrazi2.txt");
		assertEquals(Util.checkLists(testList, izraz2), true);	
	}

}
