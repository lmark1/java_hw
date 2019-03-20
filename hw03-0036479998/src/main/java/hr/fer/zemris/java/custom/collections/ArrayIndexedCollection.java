package hr.fer.zemris.java.custom.collections;

/**
 * This class implements a resizeable collection of objects using array. Each
 * instance of this class manages private variable size, capacity and array of
 * Objects - elements. When adding or inserting more elements than the current
 * capacity of the array, the array is then reallocated with doubled capacity.
 * This class also extends {@link Collection} and overrides all of its methods
 * that are not implemented. Duplicate elements are allowed and storage of null
 * references is not allowed. If user attempts to add or insert null references
 * {@link IllegalArgumentException} is thrown.
 * 
 * @author Lovro Marković
 *
 */
public class ArrayIndexedCollection extends Collection {

	/**
	 * Current number of elements stored in the collection.
	 */
	private int size;

	/**
	 * Current capacity of alocated array of object references.
	 */
	private int capacity;

	/**
	 * Array of object references which length is determined by the capacity
	 * variable.
	 */
	private Object[] elements;

	/**
	 * Default constructor. Sets initial capacity to 16.
	 */
	public ArrayIndexedCollection() {
		this(16);
	}

	/**
	 * Constructor which specifies the initial capacity of elements array
	 * 
	 * @param initialCapacity
	 *            Initial capacity of the elements array.
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		this(null, initialCapacity);
	}

	/**
	 * Constructor which which copies all objects from input collection to
	 * elements array.
	 * 
	 * @param collection
	 *            Collection which will be copied to elements array.
	 */
	public ArrayIndexedCollection(Collection collection) {
		this(collection, 16);
	}

	/**
	 * Constructor which initializes elements of aray with initial capacity, and
	 * a collection which will be copied to that array
	 * 
	 * @param collection
	 *            Collection which will be copied to elements array.
	 * @param initialCapacity
	 *            Initial capacity of the elements array.
	 * @throws IllegalArgumentException
	 *             Exception thrown when initial capacity is less than 1.
	 */
	public ArrayIndexedCollection(Collection collection, int initialCapacity) throws IllegalArgumentException {

		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Initial capacity of elements array can't be less than 1.");
		}

		// If there is no exception initialize variables
		this.capacity = initialCapacity;
		this.elements = new Object[this.capacity];
		this.size = 0;

		// If collection is recieved, add to elements array
		if (collection != null) {
			this.addAll(collection);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return Returns current number of elements in array.
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * <p>Adds element to first empty place in the elements array. If
	 * the array is full then double its capacity. O(1) complexity unless array
	 * needs to be extended.</p>
	 * 
	 * @throws IllegalArgumentException
	 *             Exception thrown if the passed value is null.
	 */
	@Override
	public void add(Object value) {

		if (value == null) {
			throw new IllegalArgumentException("The value was passed as null.");
		}

		if (this.size == this.capacity) {
			doubleCapacity();
		}

		this.elements[this.size] = value;
		this.size++;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return True if the collection contains given value, otherwise false.
	 */
	@Override
	public boolean contains(Object value) {

		if (value == null || this.isEmpty()) {
			return false;
		}

		for (Object object : this.elements) {
			if (object.equals(value)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Object[] toArray() {

		Object[] tempArray = new Object[this.size];
		
		for (int i = 0; i < this.size; i++) {
			tempArray[i] = this.elements[i];
		}

		return tempArray;
	}

	/**
	 * Removes all elements from this collection.
	 */
	@Override
	public void clear() {

		for (int i = 0; i < this.size; i++) {
			this.elements[i] = null;
		}

		this.size = 0;
	}

	@Override
	public void forEach(Processor processor) {

		for (int i = 0; i < this.size; i++) {
			processor.process(this.elements[i]);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return True if the value is removed, else return false.
	 * @throws IllegalArgumentException Exception thrown when null value is passed.
	 */
	@Override
	public boolean remove(Object value) {
		
		if(value == null) {
			throw new IllegalArgumentException("No null values to remove");
		}
		
		int index = indexOf(value);
		
		if(index == -1) {
			return false;
		}
		
		remove(index);
		return true;
	}
	
	/**
	 * Returns the object that is stored in array of elements at position index.
	 * Valid indexes are 0 to size-1. O(1) complexity.
	 * 
	 * @param index
	 *            Position at which the object is returned.
	 * @return Object at position index.
	 * @throws IndexOutOfBoundsException
	 *             Thrown if index is not valid.
	 */
	public Object get(int index) {

		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException("Given index is not valid.");
		}

		return this.elements[index];
	}

	/**
	 * Inserts (does not overwrite) the given value at the given position in
	 * array of elements. Valid position are 0 to size. O(n) complexity.
	 * 
	 * @param value
	 *            Value that will be inserted into the array of elements.
	 * @param position
	 *            Position at which the value will be inserted.
	 * @throws IllegalArgumentException
	 *             Exception thrown if passed value is null or if position is
	 *             not valid.
	 */
	public void insert(Object value, int position) throws IllegalArgumentException {

		if (value == null) {
			throw new IllegalArgumentException("The value was passed as null.");
		}

		if (position < 0 || position > this.size) {
			throw new IllegalArgumentException("Position argument is not valid.");
		}

		// If there isn't room to insert - increase capacity
		if (this.size == this.capacity) {
			doubleCapacity();
		}

		Object[] tempArray = new Object[this.capacity];

		// Copy all elements before position
		for (int i = 0; i <= position; i++) {
			if (i == position) {
				tempArray[i] = value;
			} else {
				tempArray[i] = this.elements[i];
			}
		}

		// Copy all elements after position
		for (int i = position + 1; i < (this.size + 1); i++) {
			tempArray[i] = this.elements[i - 1];
		}

		this.elements = tempArray;
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

		for (int i = 0; i < this.size; i++) {
			if (this.elements[i].equals(value)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Removes element at specified index from collection. Valid indexes are 0
	 * to size-1.
	 * 
	 * @param index
	 *            Index at which the element will be removed.
	 * @throws IllegalArgumentException
	 *             Thrown if passed index is not valid.
	 */
	public void remove(int index) {

		if (index < 0 || index > (this.size - 1)) {
			throw new IllegalArgumentException("Index is invalid.");
		}

		// If element is at the end, remove it with O(1) complexity - for later
		// implementation
		if (index == (this.size - 1)) {
			this.elements[index] = null;

		} else {
			Object[] tempArray = new Object[this.capacity];

			// Copy all elements before index
			for (int i = 0; i < index; i++) {
				tempArray[i] = this.elements[i];
			}

			// Copy all elements after index
			for (int i = index + 1; i < this.size; i++) {
				tempArray[i - 1] = this.elements[i];
			}

			this.elements[index] = null;
			this.elements = tempArray;
		}

		this.size--;
	}

	/**
	 * Method which doubles the capacity of elements array. All elements stay
	 * the same.
	 */
	private void doubleCapacity() {
		Object[] tempArray = new Object[2 * this.capacity];

		for (int i = 0; i < this.capacity; i++) {
			tempArray[i] = this.elements[i];
		}

		this.elements = tempArray;
		this.capacity *= 2;
	}
}
