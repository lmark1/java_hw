package hr.fer.zemris.java.custom.collections;

/**
 * This class represents some general collection of objects along with some
 * useful methods for managing the collection. It has no storage capabilites,
 * and most of the methods do nothing.
 * 
 * @author Lovro Marković
 *
 */
public class Collection {

	/**
	 * Default constructor for the Collection class.
	 */
	protected Collection() {
	};

	/**
	 * Checks if the collection is empty.
	 * 
	 * @return True if collection is empty and false otherwise.
	 */
	public boolean isEmpty() {

		if (this.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns number of currently stored objects in the collection.
	 * 
	 * @return Number of objects in the collection. Returns 0 in this
	 *         implementation.
	 */
	public int size() {
		return 0;
	}

	/**
	 * Adds element type Object to the collection. Does nothing in this
	 * implementation.
	 * 
	 * @param value
	 *            Element of type object that will be added to the collection.
	 */
	public void add(Object value) {
	}

	/**
	 * Checks if the collection contains a given value.
	 * 
	 * @param value
	 *            Value of type Object.
	 * @return Always returns false in this implementation.
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Removes one occurence (not specified which one) of the given value in the
	 * collection.
	 * 
	 * @param value
	 *            Value of type Object that will be removed from the collection.
	 * @return True if the method removes one occurence of the value, otherwise
	 *         false. Always returns false in this implementation.
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Allocates a new array with size equals to the size of this collection and
	 * fills it with the collection content. 
	 * 
	 * @return Array of Objects from the collection. 
	 * @throws UnsupportedOperationException
	 *             Exception thrown when method is called.
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException("An unsupported operation is requested.");
	}

	/**
	 * Method calls <code>processor.process(.)</code> for each element of this
	 * collection. The order in which elements will be sent is undefined by this
	 * class.
	 * 
	 * @param processor
	 *            Object type Processor that contains method process().
	 */
	public void forEach(Processor processor) {

	}

	/**
	 * Method adds into itself all elements from given collection. This other
	 * collection remains unchanged.
	 * 
	 * @param other
	 *            Other collection that will be added to the current collection.
	 */
	public void addAll(Collection other) {

		/**
		 * Local class which extends Processor.
		 * 
		 * @author Lovro Marković
		 *
		 */
		class LocalProcessor extends Processor {
			public LocalProcessor() {
			}

			/**
			 * Method adds every element type Object to this collection.
			 * 
			 * @param value
			 *            Object which will be added to this collection.
			 */
			@Override
			public void process(Object value) {
				add(value);
			}
		}

		LocalProcessor processor = new LocalProcessor();
		other.forEach(processor);
	}

	/**
	 * Removes all elements from this collection. Does nothing in this implementation.
	 */
	public void clear() {

	}

}
