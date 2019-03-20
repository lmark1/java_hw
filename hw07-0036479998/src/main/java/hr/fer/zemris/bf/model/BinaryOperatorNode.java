package hr.fer.zemris.bf.model;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * This class implements Node interface and models a Binary operator node.
 * 
 * @author Lovro Marković
 *
 */
public class BinaryOperatorNode implements Node {

	/**
	 * Name of the node.
	 */
	private String name;
	
	/**
	 * Children of the operator node.
	 */
	private List<Node> children;
	
	/**
	 * Operator that is performed on children.
	 */
	private BinaryOperator<Boolean> operator;
	
	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	/**
	 * Constructor for binary operator node. It accepts operator name, list of
	 * references to the operand nodes and a strategy which describes operator
	 * behaviour.
	 * 
	 * @param name Node name.
	 * @param children List of references to the operand nodes.
	 * @param operator Strategy which describes operator behaviour.
	 */
	public BinaryOperatorNode(String name, List<Node> children,
			BinaryOperator<Boolean> operator) {
		
		this.name = name;
		this.children = children;
		this.operator = operator;
	}
	
	/**
	 * Getter for node children.
	 * 
	 * @return Node children.
	 */
	public List<Node> getChildren() {
		return children;
	}
	
	/**
	 * Getter for node name.
	 * 
	 * @return Node name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for node operator.
	 * 
	 * @return Node operator.
	 */
	public BinaryOperator<Boolean> getOperator() {
		return operator;
	}
}
