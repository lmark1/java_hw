package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * This program must recieve a path as a command line argument, parse the
 * document with smart script parser and write it back using a node visitor.
 * 
 * @author Lovro Marković
 *
 */
public class TreeWriter {

	/**
	 * Main method of the program, executes when program is run.
	 * 
	 * @param args
	 *            Accepts only 1 argument, path to the text file.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("1 argument expected.");
			System.exit(1);
		}

		String docBody = new String();
		try {
			docBody = new String(Files.readAllBytes(Paths.get(args[0])),
					StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.out.println("Cannot find given file.");
			System.exit(1);
		}

		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}

	/**
	 * This class implements INodeVisitor interface. It is used for reproducing
	 * original parsed document.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			String startExpression = buildForText(node);
			System.out.print(startExpression);

			for (int i = 0, childCount = node
					.numberOfChildren(); i <= childCount; i++) {

				if (i == childCount) {
					System.out.print("{$ END $}");

				} else {
					visitNode(node.getChild(i));
				}
			}
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Element[] echoElements = node.getElements();
			StringBuilder sb = new StringBuilder();
			sb.append("{$= ");

			for (Element element : echoElements) {
				if (element instanceof ElementString) {
					sb.append(element.asText());
				} else if (element instanceof ElementFunction) {
					sb.append(element.asText() + " ");
				} else {
					sb.append(element.asText() + " ");
				}
			}

			sb.append("$}");
			
			System.out.print(sb.toString());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, childCount = node
					.numberOfChildren(); i < childCount; i++) {
				visitNode(node.getChild(i));
			}
		}

		/**
		 * Identifies and visits the given node.
		 * 
		 * @param child
		 *            Unknown node.
		 * @throws IllegalArgumentException
		 *             Exception thrown if given node cannot be identified.
		 */
		private void visitNode(Node child) {
			if (child instanceof TextNode) {
				((TextNode) child).accept(this);

			} else if (child instanceof ForLoopNode) {
				((ForLoopNode) child).accept(this);

			} else if (child instanceof EchoNode) {
				((EchoNode) child).accept(this);

			} else {
				throw new IllegalArgumentException("Can't recognize this node");
			}
		}

		/**
		 * Builds starting expression of the for loop.
		 * 
		 * @param node
		 *            Given for loop node.
		 * @return Start expression.
		 */
		private String buildForText(ForLoopNode node) {
			String startExpression = "{$ FOR " + node.getVariable().asText()
					+ " " + node.getStartExpression().asText() + " "
					+ node.getEndExpression().asText() + " ";

			if (node.getStepExpression() != null) {
				startExpression += node.getStepExpression().asText();
			}

			startExpression += "$}";
			return startExpression;
		}

	}
}
