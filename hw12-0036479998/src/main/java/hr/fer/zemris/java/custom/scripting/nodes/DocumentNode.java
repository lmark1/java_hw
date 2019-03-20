package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node representing an entire document.
 * 
 * @author Lovro Marković
 *
 */
public class DocumentNode extends Node{
	
	/**
	 * Method used for accepting visitors into document node.
	 * 
	 * @param visitor Node visitor object.
	 */
	public void accept(INodeVisitor visitor) {
		visitor.visitDocumentNode(this);
	}
}
