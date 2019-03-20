package hr.fer.zemris.bf.model;

/**
 * Interface modeling a tree node used in Parser.
 * 
 * @author Lovro Marković
 *
 */
public interface Node {

	/**
	 * Method accepts a node visitor which will perform some operation within
	 * the node.
	 * 
	 * @param visitor
	 *            Node visitor.
	 */
	public void accept(NodeVisitor visitor);

}
