package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * This class allows users to store multiple values of the same key and it
 * provides a stack like abstraction. Keys forObjectMultistack are Strings, and
 * values associated with those keys are instances of class ValueWrapper.
 * 
 * @author Lovro Markoivić
 *
 */
public class ObjectMultistack {

	/**
	 * Internal map. Stores key type String, with their appropriate values type
	 * {@link MultiStackEntry}.
	 */
	private Map<String, MultiStackEntry> stackMap;

	/**
	 * Node of a single linked list.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class MultiStackEntry {

		/**
		 * Value of the node.
		 */
		private ValueWrapper value;

		/**
		 * Reference to the next entry.
		 */
		private MultiStackEntry next;
	}

	/**
	 * Default constructor for ObjectMultistack objects. Initializes the
	 * internal map variable.
	 */
	public ObjectMultistack() {
		stackMap = new HashMap<>();
	}

	/**
	 * Pushes valueWrapper value to the internal stack with the argument name
	 * type String as a key to that stack.
	 * 
	 * @param name
	 *            Key of the stack.
	 * @param valueWrapper
	 *            New value that will be pushed on the stack.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed for the key argument.
	 */
	public void push(String name, ValueWrapper valueWrapper) {

		if (name == null) {
			throw new IllegalArgumentException(
					"Key defined by first argument can't be null.");
		}

		MultiStackEntry head = stackMap.get(name);

		MultiStackEntry newEntry = new MultiStackEntry();
		newEntry.value = valueWrapper;

		if (head == null) {
			stackMap.put(name, newEntry);

		} else {
			newEntry.next = head;
			head = newEntry;
			stackMap.put(name, head);
		}
	}

	/**
	 * Pops the last element pushed to the stack at the location in the internal
	 * map defined by the given key type String.
	 * 
	 * @param name
	 *            Key type String defining the stack in the map.
	 * @return First object pushed on the appropriate stack.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed as the key argument or if
	 *             stack corresponding do the given key value is empty.
	 */
	public ValueWrapper pop(String name) {

		if (name == null) {
			throw new IllegalArgumentException(
					"Key defined by the first argument can't be null.");
		}

		if (isEmpty(name)) {
			throw new IllegalArgumentException(
					"Stack at the given position is empty.");
		}

		MultiStackEntry head = stackMap.get(name);

		MultiStackEntry poppedEntry = head;
		head = head.next;
		stackMap.put(name, head);

		return poppedEntry.value;
	}

	/**
	 * Returns the reference to the last element pushed on the stack at the
	 * location in the internal map defined by the given key type String.
	 * 
	 * @param name
	 *            Key type String defining the stack in the map.
	 * @return Reference to the last element
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed as the key argument or if
	 *             stack corresponding do the given key value is empty.
	 */
	public ValueWrapper peek(String name) {

		if (name == null) {
			throw new IllegalArgumentException(
					"Key defined by the first argument can't be null.");
		}

		if (isEmpty(name)) {
			throw new IllegalArgumentException(
					"Stack at the given position is empty.");
		}
		
		MultiStackEntry head = stackMap.get(name);

		return head.value;
	}

	/**
	 * Checks if the stack at the position in the map defined by the given key is empty.
	 * 
	 * @param name Key type String defining the stack in the map.
	 * @return True if the stack is empty, otherwise false.
	 * @throws IllegalArgumentException Exception thrown if njll is passed as the key argument. 
	 */
	public boolean isEmpty(String name) {
		
		if (name == null) {
			throw new IllegalArgumentException("Key defined by the first argument can't be null.");
		}
		
		MultiStackEntry head = stackMap.get(name);
		if (head == null) return true;
		
		return false;
	}
	
}
