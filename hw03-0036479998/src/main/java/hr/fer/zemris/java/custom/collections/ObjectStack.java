package hr.fer.zemris.java.custom.collections;

/**
 * Class implements a stack like collection using
 * {@link ArrayIndexedCollection}. It implements the usual methods for using a
 * stack collection such as push, pop, peek, etc which are realised using
 * functionalities of {@link ArrayIndexedCollection} methods such as add,
 * insert, etc. Elements on stack are of type Object. Duplicate elements are
 * allowed and storage of null references is not allowed. If user attempts to
 * add or insert null references {@link IllegalArgumentException} is thrown.
 * 
 * @author lmark
 *
 */
public class ObjectStack {

	/**
	 * Array type collection which immitates stack.
	 */
	private ArrayIndexedCollection stack;

	/**
	 * Default constructor.
	 */
	public ObjectStack() {
		this.stack = new ArrayIndexedCollection();
	}

	/**
	 * Check if the stack is empty.
	 * 
	 * @return True if the stack is empty, otherwise false.
	 */
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	/**
	 * Current number of elements stored in the collection.
	 * 
	 * @return Returns current number of elements stored in the collection.
	 */
	public int size() {
		return this.stack.size();
	}

	/**
	 * Pushes given value on the stack. null value must not be allowed to be
	 * placed on stack. Complexity O(1).
	 * 
	 * @param value
	 *            Value that will be pushed on the stack.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given value is null.
	 */
	public void push(Object value) {
		this.stack.add(value);
	}

	/**
	 * Removes last value pushed on stack from stack and returns it. Complexity
	 * O(1).
	 * 
	 * @return Last value pushed on the stack.
	 * @throws EmptyStackException
	 *             Exception thrown if the stack is empty.
	 */
	public Object pop(){

		if (this.size() == 0) {
			throw new EmptyStackException("Stack is empty.");
		}

		Object lastValue = this.stack.get(this.size() - 1);
		this.stack.remove(this.size() - 1);

		return lastValue;
	}

	/**
	 * Returns last element placed on stack but does not delete it from stack.
	 * 
	 * @return Last element placed on the stack.
	 * @throws EmptyStackException
	 *             Exception thrown if stack is empty.
	 */
	public Object peek() {

		if (this.size() == 0) {
			throw new EmptyStackException("Stack is empty.");
		}

		return this.stack.get(this.size() - 1);
	}

	/**
	 * Removes all elements from stack.
	 */
	public void clear() {
		this.stack.clear();
	}
}
