package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Base class for all graph nodes.
 * 
 * @author Lovro Marković
 *
 */
public class Node {
	
	/**
	 * Collection for storing nodes.
	 */
	private ArrayIndexedCollection nodes;
	
	/**
	 * Default constructor for class Node.
	 */
	public Node(){}
	
	/**
	 * Adds given child to the internall managed collection of children.
	 * 
	 * @param child Node will be added to the collection.
	 * @throws IllegalArgumentException Exception thrown if given child value is null.
	 */
	public void addChildNode(Node child) {

		if (child == null) {
			throw new IllegalArgumentException("Trying to add null value as a node");
		}
		
		if (nodes == null) {
			nodes = new ArrayIndexedCollection();
		}
		
		nodes.add(child);
	}
	
	/**
	 * Returns a number of direct children.
	 * 
	 * @return Number of direct children.
	 */
	public int numberOfChildren() {
		return nodes.size();
	}
	
	/**
	 * Returns selected child from the collection.
	 * 
	 * @param index Position at which the child is located.
	 * @return Selected child from the collection
	 * @throws IndexOutOfBoundsException thrown if given index is invalid.
	 */
	public Node getChild(int index) {
		return (Node)nodes.get(index);
	}
}
