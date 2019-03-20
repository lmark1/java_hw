package hr.fer.zemris.bf.model;

import java.util.function.UnaryOperator;

/**
 * This class implements Node interface and models a Unary operator node.
 * 
 * @author Lovro Marković
 *
 */
public class UnaryOperatorNode implements Node {

	/**
	 * Name of the unary operator node.
	 */
	private String name;
	
	/**
	 * Single child of the node.
	 */
	private Node child;
	
	/**
	 * Operation performed on the child.
	 */
	private UnaryOperator<Boolean> operator;
	
	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Constructor for Unary operator node object. It accepts a node name,
	 * reference to the operand node and strategy which describes operator
	 * behaviour.
	 * 
	 * @param name Node name.
	 * @param child Reference to the operand node.
	 * @param operator Operator which describes operator behaviour.
	 */
	public UnaryOperatorNode(String name, Node child,
			UnaryOperator<Boolean> operator) {
		
		this.child = child;
		this.name = name;
		this.operator = operator;
	}
	
	/**
	 * Getter for child.
	 * 
	 * @return Child of node.
	 */
	public Node getChild() {
		return child;
	}
	
	/**
	 * Getter for name.
	 * 
	 * @return Name of node.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for operator.
	 * 
	 * @return Operator of node.
	 */
	public UnaryOperator<Boolean> getOperator() {
		return operator;
	}
}
