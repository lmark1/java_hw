package hr.fer.zemris.bf.model;

/**
 * This class implements Node interface and models "Constant node". 
 * 
 * @author Lovro Marković
 *
 */
public class ConstantNode implements Node{
	
	/**
	 * Value of the constant node object, type boolean.
	 */
	private boolean value;
	
	/**
	 * Constant node constructor. Initializes value variable.
	 * @param value Initial value.
	 */
	public ConstantNode(boolean value) {
		this.value = value;
	}
	
	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * Getter for the constant node value.
	 * 
	 * @return Value of constant node.
	 */
	public boolean getValue() {
		return value;
	}
}
