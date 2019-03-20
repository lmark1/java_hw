package hr.fer.zemris.java.hw03;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;


/**
 * Testing program for {@link SmartScriptParser} class. Accepts one command line
 * argument which is a path to ...\workspace\hw03-JMBAG\examples\doc1.txt.
 * Parses the string once, converts it back to String and parses it again.
 * 
 * @author Lovro Marković
 *
 */
public class SmartScriptTester {

	/**
	 * Main mehtod of the class. Executed when program is run.
	 * 
	 * @param args One argument. Path to the doc1.txt file in the examples folder.
	 */
	public static void main(String[] args) {

		String docBody = new String();
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])), StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.out.println("Cannot find given file.");
			System.exit(1);
		}

		SmartScriptParser parser = null;

		try {
			parser = new SmartScriptParser(docBody);

		} catch (SmartScriptParserException e) {
			System.out.println("Unable to parse document!");
			System.exit(-1);

		} catch (Exception e) {
			System.out.println("If this line ever executes, you have failed this class!");
			System.exit(-1);
		}

		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		System.out.println("Original document.");
		System.out.println(originalDocumentBody);

		System.out.printf("\n");
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		String docString2 = createOriginalDocumentBody(document2);
		System.out.println("Document copy.");
		System.out.println(docString2);
	}

	/**
	 * Creates a String from the node generated node tree.
	 * 
	 * @param node
	 *            Node from which the string is generated.
	 * @return String generated from node tree.
	 */
	private static String createOriginalDocumentBody(Node node) {
		ObjectStack stack = new ObjectStack();
		String originalDoc = new String();
		stack = addAllNodesToStack(node, stack);

		while (!stack.isEmpty()) {
			originalDoc = stack.pop() + originalDoc;
		}

		return originalDoc;
	}

	/**
	 * Adds all nodes and elements to stack as Strings. 
	 * 
	 * @param node Current node that is processed to String.
	 * @param stack Current state of stack.
	 * @return Updated stack state.
	 */
	private static ObjectStack addAllNodesToStack(Node node, ObjectStack stack) {

		if (node instanceof TextNode) {
			stack.push(buildText((TextNode)node));
			return stack;
		}

		if (node instanceof EchoNode) {
			stack.push(echoElementstoString((EchoNode) node));
			return stack;
		}

		if (node instanceof ForLoopNode) {
			// Construct forLoop String
			String forString = "{$ FOR " + ((ForLoopNode) node).getVariable().asText() + " "
					+ ((ForLoopNode) node).getStartExpression().asText() + " "
					+ ((ForLoopNode) node).getEndExpression().asText() + " ";

			if (((ForLoopNode) node).getStepExpression() != null) {
				forString += ((ForLoopNode) node).getStepExpression().asText();
			}
			forString += "$}";
			stack.push(forString);
			
			//Process all sub nodes in forLoop
			for (int i = 0; i <= node.numberOfChildren(); i++) {

				if (i == node.numberOfChildren()) {
					stack.push("{$ END $}");

				} else {
					stack = addAllNodesToStack(node.getChild(i), stack);
				}
			}
			
		} else {
			// Go through all other sub nodes
			for (int i = 0; i < node.numberOfChildren(); i++) {
				stack = addAllNodesToStack(node.getChild(i), stack);
			}
		}
		return stack;
	}

	/**
	 * Constructs a string from Element[] array contained in EchoNode.
	 * 
	 * @param node Node type EchoNode.
	 * @return String containing all elements in EchoNode array.
	 */
	private static String echoElementstoString(EchoNode node) {
		Element[] echoElements = node.getElements();
		String elementString = new String();
		elementString += "{$= ";

		for (Element element : echoElements) {
			if (element instanceof ElementString) {
				elementString += element.asText();
			} else if (element instanceof ElementFunction) {
				elementString += element.asText() + " ";
			} else {
				elementString += element.asText() + " ";
			}
		}

		elementString += "$}";
		return elementString;
	}

	@SuppressWarnings("javadoc")
	private static String buildText(TextNode node) {
		return node.getText().replaceAll("\\{\\$", "\\\\{\\$");
	}
}
