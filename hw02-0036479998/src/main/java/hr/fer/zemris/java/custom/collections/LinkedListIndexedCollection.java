package hr.fer.zemris.java.custom.collections;

/**
 *
 * Class implements a linked-list backed collection of objects using. It uses a
 * static class {@link ListNode} with references to next and previous node as
 * well as a value type Object. Each instance of the class stores variables
 * size, reference to the first element of list and reference to the last
 * element of list. This class also extends {@link Collection} and overrides all
 * of its methods that are not implemented. Duplicate elements are allowed and
 * storage of null references is not allowed. If user attempts to add or insert
 * null references {@link IllegalArgumentException} is thrown.
 * 
 * @author Lovro Marković
 *
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * This class defines each linked list node as a structure with references
	 * to previous and following nodes as well as value the current node.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class ListNode {
		/**
		 * Reference to the previous linked list node.
		 */
		private ListNode previous;
		/**
		 * Reference to the next linked list node.
		 */
		private ListNode next;
		/**
		 * Value of the node - type Object.
		 */
		private Object value;
	}

	/**
	 * Current number of stored elements in the collection.
	 */
	private int size;

	/**
	 * Referene to the first node of the linked list.
	 */
	private ListNode first;

	/**
	 * Referene to the last node of the linked list.
	 */
	private ListNode last;

	/**
	 * Default constructor. Initializes first and last list node - sets them to
	 * null.
	 */
	public LinkedListIndexedCollection() {
		this(null);
	}

	/**
	 * Initializes first and last list node and sets them to null and adds all
	 * elements from collection to the list.
	 * 
	 * @param collection
	 *            Collection of elements which will be added to the current
	 *            list.
	 */
	public LinkedListIndexedCollection(Collection collection) {
		this.first = null;
		this.last = null;
		this.size = 0;

		if (collection != null) {
			this.addAll(collection);
		}
	}

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * <p>
	 * Adds the given object into this collection at the end of the collection.
	 * Newly added element becomes the element at the biggest index. Complexity
	 * is O(1).
	 * </p>
	 * 
	 * @throws IllegalArgumentException
	 *             Exception thrown if passed value is null.
	 */
	@Override
	public void add(Object value) {

		if (value == null) {
			throw new IllegalArgumentException("Can't add null values to list.");
		}

		// Add element as last
		ListNode newNode = new ListNode();
		newNode.value = value;

		if (this.last == null && this.first == null) {
			this.first = newNode;
		} else {
			this.last.next = newNode;
			newNode.previous = this.last;
		}
		this.last = newNode;
		this.size++;
	}
	
	/**
	 * Removes all elements from this collection.
	 */
	@Override
	public void clear() {
		this.first = null;
		this.last = null;
		this.size = 0;
	}

	@Override
	public void forEach(Processor processor) {
		ListNode node = this.first;

		while (node != null) {
			processor.process(node.value);
			node = node.next;
		}
	}

	@Override
	public Object[] toArray(){
		
		Object[] tempArray = new Object[this.size];
		
		ListNode node = this.first;

		for (int i = 0; i < this.size; i++) {
			tempArray[i] = node.value;
			node = node.next;
		}

		return tempArray;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return True if object is in collection, else return false.
	 */
	@Override
	public boolean contains(Object value) {

		if (value == null || this.isEmpty()) {
			return false;
		}

		ListNode node = this.first;
		while (node != null) {

			if (node.value.equals(value)) {
				return true;
			}
			node = node.next;
		}

		return false;
	}

	/**
	 * Returns the object that is stored in linked list at position index. Valid
	 * indexes are 0 to size-1. Complexity is never greater than n/2 + 1.
	 * 
	 * @param index
	 *            Position of the wanted object.
	 * @return Object at the wanted position.
	 * @throws IndexOutOfBoundsException
	 *             Exception thrown when given index is invalid.
	 */
	public Object get(int index) {
		
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Index is out of bounds.");
		}
		
		if ( (this.size / 2) > index) {
			ListNode firstNode = this.first;
			for (int i = 0; i < index; i++, firstNode = firstNode.next);
			return firstNode.value;
		
		} else {
			ListNode lastNode = this.last;
			for (int i = this.size-1; i > index; i--, lastNode = lastNode.previous);
			return lastNode.value;
		}
	}

	/**
	 * Inserts (does not overwrite) the given value at the given position in
	 * linked-list. Elements starting from this position are shifted one
	 * position. The valid positions are 0 to size. O(n) complexity.
	 * 
	 * @param value
	 *            Value that will be inserted to the list.
	 * @param position
	 *            Position at which the value will be inserted.
	 * @throws IllegalArgumentException
	 *             Exception thrown if passed value is null or position is
	 *             invalid.
	 */
	public void insert(Object value, int position) {

		if (value == null) {
			throw new IllegalArgumentException("The value was passed as null.");
		}

		if (position < 0 || position > this.size) {
			throw new IllegalArgumentException("Position argument is not valid.");
		}

		ListNode newNode = new ListNode();
		newNode.value = value;

		if (this.first == null && this.last == null) {
			this.first = newNode;
			this.last = newNode;

		} else if (position == this.size) {
			// Add at the end of the list
			this.last.next = newNode;
			newNode.previous = this.last;
			this.last = newNode;

		} else if (position == 0) {
			// Add at the start of the list
			this.first.previous = newNode;
			newNode.next = this.first;
			this.first = newNode;

		} else {
			// Add in the middle
			ListNode head = this.first;

			for (int i = 0; i < position; i++) {
				head = head.next;
			}
			head.previous.next = newNode;
			newNode.previous = head.previous;
			head.previous = newNode;
			newNode.next = head;
		}
		this.size++;
	}

	/**
	 * Searches the collection and returns the index of the first occurrence of
	 * the given value or -1 if the value is not found. O(n) complexity.
	 * 
	 * @param value
	 *            Value whose index is searched for in the elements array.
	 * @return Index of the value or -1 if the value is not found.
	 */
	public int indexOf(Object value) {

		if (value == null || this.isEmpty()) {
			return -1;
		}

		ListNode head = this.first;
		int index = 0;
		
		while (head != null) {
			if (head.value.equals(value)) {
				return index;
			}
			index++;
			head = head.next;
		}

		return -1;
	}

	/**
	 * Removes element at specified index from collection. Element that was
	 * previously at location index+1 after this operation is on location index,
	 * etc. Invalid indexes are 0 to size-1.
	 *
	 * @param index
	 *            Position at which the element will be removed from the list.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given index is invalid.
	 */
	public void remove(int index){

		if (index < 0 || index >= this.size) {
			throw new IllegalArgumentException("Given index is invalid.");
		}

		if (this.size == 1) {
			// If it's the only item
			this.first = null;
			this.last = null;
		
		} else if (index == 0) {
			// Removing from the start
			ListNode node = this.first;
			this.first = node.next;
			this.first.previous = null;
			node = null;

		} else if (index == (this.size - 1)) {
			// Removing from the end
			ListNode node = this.last;
			this.last = node.previous;
			this.last.next = null;
			node = null;

		} else {
			// Removing from the middle
			ListNode head = this.first;
			for (int i = 0; i < index; i++) {
				head = head.next;
			}

			head.previous.next = head.next;
			head.next.previous = head.previous;
			head = null;
		}
		this.size--;
	}

}
