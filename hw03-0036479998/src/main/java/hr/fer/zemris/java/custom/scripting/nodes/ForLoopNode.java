package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Node representing a single for - loop construct.
 * 
 * @author Lovro Marković
 *
 */
public class ForLoopNode extends Node {

	/**
	 * Variable property of the for loop.
	 */
	private ElementVariable variable;
	
	/**
	 * Start exprression of the for loop.
	 */
	private Element startExpression;
	
	/**
	 * End expression of the for loop.
	 */
	private Element endExpression;
	
	/**
	 * Step expression of the for loop. Cannot be null.
	 */
	private Element stepExpression;
	
	/**
	 * Constructor for {@link ForLoopNode} object. Initializes variable, start expression, end expression and stepExpression.
	 * stepExpression variable can be null.
	 * 
	 * @param variable Variable of the for loop.
	 * @param startExpression Start expression of the for loop.
	 * @param endExpression End expression of the for loop.
	 * @param stepExpression Step expression of the for loop. Can be null.
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}
	
	/**
	 * Returns the variable od the for loop.
	 * 
	 * @return Variable of the for loop.
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Returns the start expression of the for loop.
	 * 
	 * @return Start expression of the for loop.
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Returns the end expression of the for loop.
	 * 
	 * @return End expression of the for loop.
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Returns the step expression of the for loop.
	 * 
	 * @return Step expression of the for loop.
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
}
