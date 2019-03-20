package hr.fer.zemris.bf.model;

/**
 * This class implements Node interface and models a "Variable node" type.
 * 
 * @author Lovro Marković
 *
 */
public class VariableNode implements Node{

	/**
	 * Value of the variable node object, type boolean.
	 */
	private String value;
	
	/**
	 * Variable node constructor. Initializes value variable.
	 * 
	 * @param value Initial value.
	 */
	public VariableNode(String value) {
		this.value = value;
	}
	
	/**
	 * Getter for the variable node value.
	 * 
	 * @return Value of constant node.
	 */
	public String getValue() {
		return value;
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);		
	}
	
}
