package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Node representing a command which generates some textual output dynamically.
 * 
 * @author Lovro Marković
 *
 */
public class EchoNode extends Node{
	
	/**
	 * Elements that will be outputed as text;
	 */
	private Element[]  elements;
	
	/**
	 * Constructor for the {@link EchoNode} object. Initializes elements array.
	 * 
	 * @param elements Value of the element array.
	 */
	public EchoNode(Element[] elements) {
		this.elements = elements;
	}
	
	/**
	 * Returns stored elemend in this EchoNode instance.
	 * 
	 * @return Stored array of elements.
	 */
	public Element[] getElements() {
		return elements;
	}
	
	/**
	 * Method used for accepting visitors into echo node.
	 * 
	 * @param visitor Node visitor object.
	 */
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}
}
