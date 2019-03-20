package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class implements an engine which executes the document obtained by
 * parsing.
 * 
 * @author Lovro Marković
 *
 */
public class SmartScriptEngine {

	/**
	 * Root of the document tree.
	 */
	private DocumentNode documentNode;

	/**
	 * Context of the program.
	 */
	private RequestContext requestContext;

	/**
	 * Stack used by the engine.
	 */
	private ObjectMultistack multistack = new ObjectMultistack();

	/**
	 * Map containing all implemented functions.
	 */
	private Map<String, IFunction> functionMap;

	/**
	 * Visitor used by the engine.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				System.out
						.println("Exception occured while writing text node.");
				System.exit(1);
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			ElementConstantInteger start = null;
			ElementConstantInteger end = null;
			ElementConstantInteger step = null;

			try {
				start = (ElementConstantInteger) node.getStartExpression();
				end = (ElementConstantInteger) node.getEndExpression();
				step = (ElementConstantInteger) node.getStepExpression();
			} catch (ClassCastException e) {
				System.out
						.println("Unexpected node encountered in the for loop");
				System.exit(1);
			}

			int startValue = start.getValue();
			int endValue = end.getValue();
			int stepValue = step.getValue();
			String iteratorName = node.getVariable().getName();

			multistack.push(iteratorName, new ValueWrapper(startValue));

			while ((int) multistack.peek(iteratorName).getValue() <= endValue) {
				visitAllChildren(node);

				ValueWrapper iterator = multistack.pop(iteratorName);
				iterator.add(stepValue);
				multistack.push(iteratorName, iterator);
			}

			multistack.pop(iteratorName);
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Element[] elements = node.getElements();

			// Objects on temp stack are either String or ValueWrapper type.
			Stack<Object> tempStack = new Stack<>();

			for (Element element : elements) {

				// If element is a constant push it onto the stack
				if (pushConstant(element, tempStack)) {
					continue;
				}

				// If element is a variable update its value on the tempStack
				if (pushVariableValue(element, tempStack)) {
					continue;
				}

				// Perform binary operation
				if (pushOperationResult(element, tempStack)) {
					continue;
				}

				// Perform function operation
				if (pushFunctionResult(element, tempStack)) {
					continue;
				}

				throw new IllegalStateException(
						"Unknown element found in EchoNode.");
			}

			String echo = new String();
			while (!tempStack.isEmpty()) {
				Object o = tempStack.pop();

				if (o instanceof String) {
					echo = (String) o + echo;

				} else {
					echo = ((ValueWrapper) o).getValue().toString() + echo;
				}
			}

			try {
				requestContext.write(echo);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			visitAllChildren(node);
		}

		/**
		 * Checks if given element is a function, pops appropriate amount of
		 * elements from the tempStack, performs the function and pushes result
		 * back on the stack.
		 * 
		 * @param element
		 *            Given element.
		 * @param tempStack
		 *            Reference to the temporary stack.
		 * @return True if function operations is performed, false otherwise.
		 */
		private boolean pushFunctionResult(Element element,
				Stack<Object> tempStack) {

			if (element instanceof ElementFunction) {
				ElementFunction fElem = (ElementFunction) element;
				String fName = fElem.getName();

				if (!functionMap.containsKey(fName)) {
					throw new IllegalStateException(
							"Unsupported function name found: "
									+ fElem.getName());
				}

				try {
					functionMap.get(fName).apply(tempStack);
				} catch (EmptyStackException e) {
					throw new IllegalStateException(
							"Illegal stack state when running @" + fName
									+ "function .");
				}

				return true;
			}

			return false;
		}

		/**
		 * Checks if given eleemnt is an operator, pops 2 elements from the
		 * temporary stack, performs an operation and pushes result back on the
		 * stack. Supported operations are +, -, *, /.
		 * 
		 * @param element
		 *            Given element.
		 * @param tempStack
		 *            Reference to the temporary stack.
		 * @return True if operation is performed, false otherwise.
		 */
		private boolean pushOperationResult(Element element,
				Stack<Object> tempStack) {

			if (element instanceof ElementOperator) {
				ElementOperator operator = (ElementOperator) element;

				ValueWrapper val1 = null;
				ValueWrapper val2 = null;

				Object o1 = tempStack.pop();
				Object o2 = tempStack.pop();

				// Extract first value
				if (o1 instanceof String) {
					val1 = getValueWrapper((String) o1);
				} else {
					val1 = new ValueWrapper(((ValueWrapper) o1).getValue());
				}

				// Extract second value
				if (o2 instanceof String) {
					val2 = getValueWrapper((String) o2);
				} else {
					val2 = new ValueWrapper(((ValueWrapper) o2).getValue());
				}

				ValueWrapper result = new ValueWrapper(val1.getValue());

				switch (operator.getName()) {
				case "+":
					result.add(val2.getValue());
					break;

				case "-":
					result.subtract(val2.getValue());
					break;

				case "*":
					result.multiply(val2.getValue());
					break;

				case "/":
					result.divide(val2.getValue());
					break;

				default:
					throw new IllegalArgumentException(
							"Unsupported operator found: "
									+ operator.getName());
				}

				tempStack.push(result);
				return true;
			}

			return false;
		}

		/**
		 * Checks if given element is a variable, pushes its value on the
		 * temporary stack.
		 * 
		 * @param element
		 *            Given element.
		 * @param tempStack
		 *            Reference to the temporary stack.
		 * @return True if variable value is pushed, false otherwise.
		 */
		private boolean pushVariableValue(Element element,
				Stack<Object> tempStack) {

			if (element instanceof ElementVariable) {
				ValueWrapper value = multistack
						.peek(((ElementVariable) element).getName());
				tempStack.push(value);
				return true;
			}

			return false;
		}

		/**
		 * If given element is a constant push its value on the temporary stack.
		 * 
		 * @param element
		 *            Element.
		 * @param tempStack
		 *            Reference to the temoporary stack.
		 * @return True if value is pushed on the stack, false otherwise.
		 */
		private boolean pushConstant(Element element, Stack<Object> tempStack) {

			if (element instanceof ElementString) {
				String name = ((ElementString) element).getName();
				tempStack.push(name);

				return true;
			}

			if (element instanceof ElementConstantDouble) {
				double value = ((ElementConstantDouble) element).getValue();
				tempStack.push(new ValueWrapper(value));
				return true;
			}

			if (element instanceof ElementConstantInteger) {
				int value = ((ElementConstantInteger) element).getValue();
				tempStack.push(new ValueWrapper(value));
				return true;
			}

			return false;
		}

		/**
		 * Visits all children of a given node.
		 * 
		 * @param node
		 *            Given node.
		 */
		private void visitAllChildren(Node node) {
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
	};

	/**
	 * Constructor for the script engine.
	 * 
	 * @param documentNode
	 *            Root node of the document.
	 * @param requestContext
	 *            Context of the program.
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {

		this.documentNode = documentNode;
		this.requestContext = requestContext;

		initFunctions();
	}

	/**
	 * Executes the program.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

	/**
	 * Initialize all the supported functions.
	 */
	private void initFunctions() {
		functionMap = new HashMap<>();

		functionMap.put("sin", (stack) -> {

			try {
				ValueWrapper valueWrapper = (ValueWrapper) stack.pop();
				Object value = valueWrapper.getValue();

				if (value instanceof Integer) {
					Integer intValue = (Integer) value;
					double rad = (double) intValue * Math.PI / 180;
					stack.push(new ValueWrapper(Math.sin(rad)));
				} else {
					double rad = (double) value * Math.PI / 180;
					stack.push(new ValueWrapper(Math.sin(rad)));
				}

			} catch (ClassCastException e) {
				throw new IllegalArgumentException(
						"Illegal arguments found on stack - sin function.");
			}

		});

		functionMap.put("decfmt", (stack) -> {
			Object f = stack.pop();

			if (!(f instanceof String)) {
				throw new IllegalArgumentException(
						"Second argument has to be String");
			}

			String fStr = (String) f;
			Object x = stack.pop();

			DecimalFormat df = new DecimalFormat(fStr);
			if (x instanceof String) {
				String strX = (String) x;

				try {
					double value = Double.parseDouble(strX);
					stack.push(df.format(value));
					return;
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"Number as String representation expected.");
				}
			}

			if (x instanceof ValueWrapper) {
				ValueWrapper value = (ValueWrapper) x;
				stack.push(df.format(value.getValue()));
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument types found in stack - dcfmt function.");
		});

		functionMap.put("dup", (stack) -> {
			Object x = stack.pop();
			stack.push(x);
			stack.push(x);
		});

		functionMap.put("swap", (stack) -> {
			Object a = stack.pop();
			Object b = stack.pop();

			stack.push(a);
			stack.push(b);
		});

		functionMap.put("setMimeType", (stack) -> {
			Object x = stack.pop();

			if (x instanceof String) {
				String xStr = (String) x;
				requestContext.setMimeType(xStr);
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - setMimeType function.");
		});

		functionMap.put("paramGet", (stack) -> {
			Object dv = stack.pop();
			Object name = stack.pop();

			if (name instanceof String) {
				String nameStr = (String) name;

				String value = requestContext.getParameter(nameStr);

				if (dv instanceof String) {
					stack.push(value == null ? getValueWrapper((String) dv)
							: getValueWrapper(value));

				} else {
					// If it's not a String it must be a value wrapper
					stack.push(value == null ? ((ValueWrapper) dv).getValue()
							: getValueWrapper(value));
				}

				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - paramGet function.");
		});

		functionMap.put("pparamGet", (stack) -> {
			Object dv = stack.pop();
			Object name = stack.pop();

			if (name instanceof String) {
				String nameStr = (String) name;

				String value = requestContext.getPersistentParameter(nameStr);
				stack.push(value == null ? dv : getValueWrapper(value));
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - pparamGet function.");
		});

		functionMap.put("pparamSet", (stack) -> {
			Object name = stack.pop();
			Object value = stack.pop();

			if (name instanceof String && value instanceof ValueWrapper) {
				String valueStr = ((ValueWrapper) value).toString();
				String nameStr = (String) name;

				requestContext.setPersistentParameter(nameStr, valueStr);
				return;

			} else if (name instanceof String && value instanceof String) {
				String valueStr = (String) value;
				String nameStr = (String) name;

				requestContext.setPersistentParameter(nameStr, valueStr);
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - pparamSet function.");
		});

		functionMap.put("pparamDel", (stack) -> {
			Object name = stack.pop();

			if (name instanceof String) {
				String nameStr = (String) name;

				requestContext.removePersistentParameter(nameStr);
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - tparamDel function.");
		});

		functionMap.put("tparamGet", (stack) -> {
			Object dv = stack.pop();
			Object name = stack.pop();

			if (name instanceof String) {
				String nameStr = (String) name;

				String value = requestContext.getTemporaryParameter(nameStr);
				stack.push(value == null ? dv : getValueWrapper(value));
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - paramGet function.");
		});

		functionMap.put("tparamSet", (stack) -> {
			Object name = stack.pop();
			Object value = stack.pop();

			if (name instanceof String && value instanceof ValueWrapper) {
				String valueStr = ((ValueWrapper) value).toString();
				String nameStr = (String) name;

				requestContext.setTemporaryParameter(nameStr, valueStr);
				return;

			} else if (name instanceof String && value instanceof String) {
				String valueStr = (String) value;
				String nameStr = (String) name;

				requestContext.setTemporaryParameter(nameStr, valueStr);
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - tparamSet function.");
		});

		functionMap.put("tparamDel", (stack) -> {
			Object name = stack.pop();

			if (name instanceof String) {
				String nameStr = (String) name;

				requestContext.removeTemporaryParameter(nameStr);
				return;
			}

			throw new IllegalArgumentException(
					"Illegal argument type found in stack - tparamDel function.");
		});
	}

	/**
	 * Converts a string value into a int / double and returns appropriate value
	 * wrapper object.
	 * 
	 * @param value
	 *            Given string value.
	 * @return Value wrapper object.
	 */
	private ValueWrapper getValueWrapper(String value) {

		try {
			int intVal = Integer.parseInt(value);
			return new ValueWrapper(intVal);
		} catch (NumberFormatException ignorable) {
		}

		try {
			double dVal = Double.parseDouble(value);
			return new ValueWrapper(dVal);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Can't parse " + value + " to a number.");
		}
	}

	/**
	 * Interface modeling a function operation.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private interface IFunction {

		/**
		 * Performs a specific operation on arguments contained in the stack.
		 * Pushes result back on the stack.
		 * 
		 * @param stack
		 *            Reference to the stack.
		 */
		void apply(Stack<Object> stack);
	}
}
