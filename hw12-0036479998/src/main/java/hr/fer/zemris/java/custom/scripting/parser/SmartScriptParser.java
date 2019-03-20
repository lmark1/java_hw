package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.custom.scripting.lexer.LexerException;

/**
 * Parser class. String value passed to a constructor will be parsed using
 * {@link SmartLexer}. Node hierarchy tree will be constructed consisting of
 * ECHO, TEXT and FORLOOP nodes. If any invalid expression occurs
 * {@link SmartScriptParserException} will be thrown.
 * 
 * @author Lovro Marković
 *
 */
public class SmartScriptParser {

	/**
	 * Instanced object type Lexer that is used by Parser.
	 */
	private SmartLexer lexer;

	/**
	 * Document node containing node tree of all nodes in the given document.
	 */
	private DocumentNode docNode;

	/**
	 * Constructor for Parser objects. Initializes documentBody variable.
	 * 
	 * @param documentBody
	 *            String containing document body that will be parsed.
	 * @throws SmartScriptParserException
	 *             Document string cant be null.
	 */
	public SmartScriptParser(String documentBody) {
		if (documentBody == null) {
			throw new SmartScriptParserException("Document can't be null");
		}
		lexer = new SmartLexer(documentBody);
		docNode = generateDocumentNode();
	}

	/**
	 * Returns generated document node.
	 * 
	 * @return Generated document node.
	 */
	public DocumentNode getDocumentNode() {
		return docNode;
	}

	/**
	 * Return whole document node of the input string.
	 * 
	 * @return Document node tree.
	 */
	private DocumentNode generateDocumentNode() {
		ObjectStack stack = new ObjectStack();
		DocumentNode docNode = new DocumentNode();

		stack.push(docNode);
		stack = getAllNodes(stack);

		Node upper = (Node) stack.peek();
		if (!(upper instanceof DocumentNode)) {
			throw new SmartScriptParserException("Document node is not last node left on stack.");
		}

		return (DocumentNode) stack.pop();
	}

	/**
	 * Returns updated stack with all the elements in the document.
	 * 
	 * @param stack
	 *            Current stat of stack.
	 * @return State of stack after going through all the nodes and elements.
	 * @throws SmartScriptParserException
	 *             Exception thrown when illegal syntax or invalid tokens are
	 *             detected.
	 */
	private ObjectStack getAllNodes(ObjectStack stack) {

		boolean tagOpened = false;
		while (true) {

			// Try to get next lexer token
			SmartToken newToken;
			try {
				newToken = lexer.nextToken();

			} catch (LexerException e) {
				if (lexer.getToken().getType() == SmartTokenType.EOF) {
					break;
				}
				throw new SmartScriptParserException("No valid tokens found");

			}

			if (newToken.getType() == SmartTokenType.START_TAG) {

				// If a tag is opened two times in a row
				if (tagOpened) {
					throw new SmartScriptParserException("Tag has to close before another opens.");
				}

				tagOpened = true;
				continue;
			}

			if (newToken.getType() == SmartTokenType.END_TAG) {

				// If a tag is closed two times in a row
				if (!tagOpened) {
					throw new SmartScriptParserException("Tag has to be opened before it is closed.");
				}

				tagOpened = false;
				continue;
			}

			// Process text tag
			if (!tagOpened && newToken.getType() == SmartTokenType.TEXT) {
				TextNode newNode = new TextNode(newToken.getValue().toString());
				stack = addSubNodes(newNode, stack);
				continue;
			}

			// Process equals tag
			if (tagOpened && newToken.getType() == SmartTokenType.EQUALS) {
				tagOpened = false;
				stack = processEqualsTag(stack);
				continue;
			}

			// Process for tag
			if (tagOpened && newToken.getType() == SmartTokenType.FOR) {
				tagOpened = false;
				stack = processForTag(stack);
				continue;
			}

			// Process end tag
			if (tagOpened && newToken.getType() == SmartTokenType.END) {
				stack = processEndTag(stack);
				continue;
			}

			// Process EOF tag
			if (newToken.getType() == SmartTokenType.EOF) {
				continue;
			}

			// If none of the above
			throw new SmartScriptParserException("Unexpected token. Token type: " + newToken.getType().toString()
					+ " and value " + newToken.getValue().toString());
		}

		return stack;
	}

	/**
	 * Process END tag.
	 * 
	 * @param stack
	 *            Current state of stack.
	 * @return Updated state of stack.
	 * @throws SmartScriptParserException
	 *             Exception thrown if there are no more Nodes left in stack.
	 */
	private ObjectStack processEndTag(ObjectStack stack) {
		stack.pop();

		if (stack.isEmpty()) {
			throw new SmartScriptParserException("There are more END tags than needed.");
		}

		return stack;
	}

	/**
	 * Process FOR tag. If there is enough elements in the for tag and
	 * expressions are valid, create a new ForNode and push it to stack.
	 * 
	 * @param stack
	 *            Current state of stack.
	 * @return Updated state of stack.
	 * @throws SmartScriptParserException
	 *             Exception thrown when there is too many or too few elements
	 *             in FOR tag or expressions type String can't be parsed as
	 *             integer, or if ElementFunction object is found inside.
	 */
	private ObjectStack processForTag(ObjectStack stack) {
		ArrayIndexedCollection forElements = new ArrayIndexedCollection();

		while (true) {
			Element newElement = getNextElement();

			// Cant be a function in for
			if (newElement instanceof ElementFunction) {
				throw new SmartScriptParserException("Element in for can't be a function");
			}

			if (newElement == null) {
				break;
			}
			forElements.add(newElement);
		}

		// Too many / too few elements in for
		if (forElements.size() < 3 || forElements.size() > 4) {
			throw new SmartScriptParserException("For loop has " + forElements.size() + "; Maximum number is 3.");
		}

		// First element has to be a variable
		if (!(forElements.get(0) instanceof ElementVariable)) {
			throw new SmartScriptParserException(
					"First element of for loop is " + ((Element) forElements.get(0)).asText() + "; Variable expected.");
		}

		forElements = forElementsToInteger(forElements);

		ForLoopNode forNode = generateForLoopNode(forElements);
		stack = addSubNodes(forNode, stack);
		stack.push(forNode);

		return stack;
	}

	/**
	 * Generate {@link ForLoopNode} from given for elements.
	 * 
	 * @param forElements
	 *            Elements of the for loop.
	 * @return ForLoopNode constructed from the given elements.
	 */
	private ForLoopNode generateForLoopNode(ArrayIndexedCollection forElements) {
		ForLoopNode forNode;

		if (forElements.size() == 3) {
			forNode = new ForLoopNode((ElementVariable) forElements.get(0), (Element) forElements.get(1),
					(Element) forElements.get(2), null);

		} else {
			forNode = new ForLoopNode((ElementVariable) forElements.get(0), (Element) forElements.get(1),
					(Element) forElements.get(2), (Element) forElements.get(3));
		}

		return forNode;
	}

	/**
	 * Attempt to parse any StringElements in for loop as integer. Replace the
	 * StringElements with {@link ElementConstantInteger}.
	 * 
	 * @param forElements
	 *            Current array of for elements.
	 * @return Updated array of for elements.
	 */
	private ArrayIndexedCollection forElementsToInteger(ArrayIndexedCollection forElements) {

		for (int i = 0; i < forElements.size(); i++) {

			if (forElements.get(i) instanceof ElementString) {

				int intValue;
				try {
					intValue = Integer.parseInt(((ElementString) forElements.get(i)).getName());

				} catch (NumberFormatException e) {
					throw new SmartScriptParserException(
							"Cannot parse " + ((Element) forElements.get(i)).getClass() + " as a number.");
				}

				ElementConstantInteger newInteger = new ElementConstantInteger(intValue);
				forElements.remove(i);
				forElements.insert(newInteger, i);
			}
		}

		return forElements;
	}

	/**
	 * Process equals tag, update state of stack.
	 * 
	 * @param stack
	 *            Current state of stack.
	 * @return Updated state of stack with the EchoNode.
	 */
	private ObjectStack processEqualsTag(ObjectStack stack) {
		ArrayIndexedCollection echoElements = new ArrayIndexedCollection();

		while (true) {
			Element newElement = getNextElement();

			if (newElement == null) {
				break;
			}
			echoElements.add(newElement);
		}

		Element[] echoValues = toElementArray(echoElements);
		EchoNode newNode = new EchoNode(echoValues);

		stack = addSubNodes(newNode, stack);
		return stack;
	}

	/**
	 * Convert {@link ArrayIndexedCollection} to element array.
	 * 
	 * @param elems
	 *            Array collection.
	 * @return Element[] array.
	 */
	private Element[] toElementArray(ArrayIndexedCollection elems) {
		Element[] tempArray = new Element[elems.size()];

		for (int i = 0; i < elems.size(); i++) {
			tempArray[i] = (Element) elems.get(i);
		}

		return tempArray;
	}

	/**
	 * Gets the next element in a tag environment.
	 * 
	 * @return Next element in the tag.
	 * @throws SmartScriptParserException
	 *             Excpetion thrown if unexpected element occurs.
	 */
	private Element getNextElement() {

		// Try to get next lexer token
		SmartToken newToken;
		try {
			newToken = lexer.nextToken();
		} catch (LexerException e) {
			return null;
		}

		SmartTokenType type = newToken.getType();
		if (type == SmartTokenType.END_TAG) {
			return null;
		}

		if (type == SmartTokenType.CONSTANT_DOUBLE) {
			return new ElementConstantDouble((double) newToken.getValue());
		}

		if (type == SmartTokenType.CONSTANT_INTEGER) {
			return new ElementConstantInteger((int) newToken.getValue());
		}

		if (type == SmartTokenType.VARIABLE) {
			return new ElementVariable((String) newToken.getValue());
		}

		if (type == SmartTokenType.FUNCTION) {
			return new ElementFunction((String) newToken.getValue());
		}

		if (type == SmartTokenType.OPERATOR) {
			return new ElementOperator(Character.toString((char) newToken.getValue()));
		}

		if (type == SmartTokenType.STRING) {
			return new ElementString((String) newToken.getValue());
		}

		// If its any other type cause an exception
		throw new SmartScriptParserException("Unexpected element detected.");
	}

	/**
	 * Add nodes to the array of the first element on the stack.
	 * 
	 * @param newNode
	 *            Node that will be added to the array.
	 * @param stack
	 *            Current state of stack.
	 * @return Updated state of stack.
	 */
	private ObjectStack addSubNodes(Node newNode, ObjectStack stack) {
		Node upperNode = (Node) stack.pop();
		upperNode.addChildNode(newNode);
		stack.push(upperNode);

		return stack;
	}

}
