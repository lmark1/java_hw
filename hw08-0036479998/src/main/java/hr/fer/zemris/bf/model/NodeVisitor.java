package hr.fer.zemris.bf.model;

/**
 * Interface defining metods of a node visitor.
 * 
 * @author Lovro Marković
 *
 */
public interface NodeVisitor {

	/**
	 * Visits a constant node. Perform some operations in it.
	 * 
	 * @param node Constant node.
	 */
	void visit(ConstantNode node);
	
	/**
	 * Visits a variabe node. Perform some operations in it.
	 * 
	 * @param node Variable node.
	 */
	void visit(VariableNode node);
	
	/**
	 * Visits an Unary operator node. Perform some operations in it.
	 * 
	 * @param node Unary operator node.
	 */
	void visit(UnaryOperatorNode node);
	
	/**
	 * Visits a Binary operator node.
	 * 
	 * @param node Binary operator node.
	 */
	void visit(BinaryOperatorNode node);
}
