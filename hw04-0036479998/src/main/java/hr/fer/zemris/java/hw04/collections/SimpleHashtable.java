package hr.fer.zemris.java.hw04.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Class implements a simple hashtable with some basic methods such as adding,
 * removing and getting table entries.
 * 
 * @author Lovro Marković
 *
 * @param <K>
 *            Type of key.
 * @param <V>
 *            Type of value assigned to key.
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {

	/**
	 * Array of references to all heads of table entry slots.s
	 */
	private TableEntry<K, V>[] table;

	/**
	 * Number of currently stored TableEntries in table array;
	 */
	private int size;

	/**
	 * This variable gets incremented every time a modification to the hastable
	 * happens.
	 */
	private int modificationCount;

	/**
	 * Static class which represents a structure of a single hastable entry.
	 * 
	 * @author Lovro Marković
	 *
	 * @param <K>
	 *            Type of key.
	 * @param <V>
	 *            Type of value assigned to the key.
	 */
	public static class TableEntry<K, V> {

		/**
		 * Value of the key.
		 */
		private K key;

		/**
		 * Value assigned to the key.
		 */
		private V value;

		/**
		 * Reference to the next TableEntry.
		 */
		private TableEntry<K, V> next;

		/**
		 * Returns the key of the hashtable entry.
		 * 
		 * @return Key of the hashtable entry.
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Returns value assigned to the hashtable entry.
		 * 
		 * @return Value assigned to the hashtable entry.
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Sets the value assigned to the table entry.
		 * 
		 * @param value
		 *            Value of the table entry.
		 */
		public void setValue(V value) {
			this.value = value;
		}
	}

	/**
	 * Default constructor for SimpleHashtable objects. Initializes tableSlots
	 * field of 16 table entries.
	 */
	public SimpleHashtable() {
		this(16);
	}

	/**
	 * Constructor for SimpleHastable objects. Recieves the inital capacity and
	 * calculates a new one so that the actual capacity will be first power of 2
	 * which is greater or equal to the given initial capacity.
	 * 
	 * @param capacity
	 *            Initial capacity of the tableSlots field.
	 * @throws IllegalArgumentException
	 *             Exception thrown when given initial capcity is less than 1.
	 */
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int capacity) {

		if (capacity < 1) {
			throw new IllegalArgumentException("Given inital capacity is less than 1");
		}

		int powersOfTwo = 1;
		while (powersOfTwo < capacity) {
			powersOfTwo *= 2;
		}

		table = (TableEntry<K, V>[]) new TableEntry[powersOfTwo];
		size = 0;
		modificationCount = 0;
	}

	/**
	 * Puts another tableEntry in the existing table array. If an entry with the
	 * same key already exists then the value assigned to that key will be
	 * updated with the new given value.
	 * 
	 * @param key
	 *            Key of the new TableEntry.
	 * @param value
	 *            Value of the new TableEntry.
	 * @throws IllegalArgumentException
	 *             Exception thrown if passed key value is null.
	 */
	public void put(K key, V value) {

		if (key == null) {
			throw new IllegalArgumentException("Key can't be null");
		}

		TableEntry<K, V> newEntry = new TableEntry<>();
		newEntry.key = key;
		newEntry.value = value;

		addTableEntry(newEntry);
	}

	/**
	 * Return the value assigned to the given key.
	 * 
	 * @param key
	 *            Key whose value will be returnd.
	 * @return Value assigned to the key or null if key doesn't exist.
	 */
	public V get(Object key) {

		if (key == null) {
			return null;
		}

		int index = calculateSlotIndex(key);
		TableEntry<K, V> head = table[index];

		while (head != null) {

			if (head.key.equals(key)) {
				return head.value;
			}

			head = head.next;
		}

		return null;
	}

	/**
	 * Return current number of TableEntries in the table.
	 * 
	 * @return Nummber of TableEntries.
	 */
	public int size() {
		return size;
	}

	/**
	 * Checks if there is a TableEntry containig the given key.
	 * 
	 * @param key
	 *            Key given to check if it's a part of table.
	 * @return True if the table contains the given key, otherwise false.
	 */
	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}

		int index = calculateSlotIndex(key);
		TableEntry<K, V> head = table[index];

		while (head != null) {

			if (head.key.equals(key)) {
				return true;
			}

			head = head.next;
		}

		return false;
	}

	/**
	 * Checks if there is a TableEntry containing the given value.
	 * 
	 * @param value
	 *            Value given to check if it's a part of table.
	 * @return True if the table contains the given value, otherwise false.
	 */
	public boolean containsValue(Object value) {
		for (TableEntry<K, V> tableEntry : table) {

			while (tableEntry != null) {

				try {
					if (tableEntry.value.equals(value)) {
						return true;
					}

					// If comparing null values
				} catch (NullPointerException e) {

					if (tableEntry.value == value) {
						return true;
					}
				}

				tableEntry = tableEntry.next;
			}
		}

		return false;
	}

	/**
	 * Checks if the hastable is empty.
	 * 
	 * @return True if the hashtable is empty otherwise false.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Removes a table entry containing given key.
	 * 
	 * @param key
	 *            Given key which entry will be removed.
	 */
	public void remove(Object key) {

		if (key == null) {
			throw new IllegalArgumentException("There are no null keys in the hashtable.");
		}

		// If the slot is empty
		int index = calculateSlotIndex(key);
		if (table[index] == null) {
			return;

			// If the first needs to be removed
		} else if (table[index].getKey().equals(key)) {
			table[index] = table[index].next;
			modificationCount++;
			size--;
			return;

		}

		// If it's somewhere in the middle
		TableEntry<K, V> head = table[index];
		while (true) {
			if (head.next == null || head.next.key.equals(key)) {
				break;
			}

			head = head.next;
		}

		if (head.next != null) {
			head.next = head.next.next;
			modificationCount++;
			size--;
			return;
		}

	}

	/**
	 * Constructs a String from all table entry values and keys in order, from
	 * first to last table slot and from first to last slot entry.
	 * 
	 * @return Returns a string in the following format:
	 *         <code> "[key1=value1, key2=value2, key3=value3]" </code>
	 */
	@Override
	public String toString() {
		String hashString = new String("[");
		boolean firstAddition = true;

		for (TableEntry<K, V> tableEntry : table) {

			while (tableEntry != null) {

				if (!firstAddition) {
					hashString += ", ";
				}

				hashString += tableEntry.key.toString() + "=";

				if (tableEntry.value == null) {
					hashString += "null";

				} else {
					hashString += tableEntry.value.toString();
				}

				tableEntry = tableEntry.next;
				firstAddition = false;
			}
		}

		return hashString + "]";
	}

	/**
	 * Delete all elements from the table.
	 */
	@SuppressWarnings("unused")
	public void clear() {
		for (TableEntry<K, V> tableEntry : table) {
			tableEntry = null;
		}
		size = 0;
	}

	/**
	 * Adds a TableEntry to a slot in the table. If there are elements there
	 * already then it will be added at the end of the linked list starting from
	 * the head node given in the table array. If an entry with the same key
	 * already exists then the value assigned to that key will be updated with
	 * the new given value.
	 * 
	 * @param newEntry
	 *            New entry that will be added to the table.
	 */
	private void addTableEntry(TableEntry<K, V> newEntry) {
		int index = calculateSlotIndex(newEntry.key);

		if (table[index] == null) {
			table[index] = newEntry;

		} else {
			TableEntry<K, V> head = table[index];
			while (true) {

				if (head.key.equals(newEntry.key)) {
					head.value = newEntry.value;
					return;
				}

				if (head.next == null) {
					break;
				}

				head = head.next;
			}

			head.next = newEntry;
		}

		size++;
		modificationCount++;
		resizeTable();
	}

	/**
	 * Return the slot in which the entry will be placed.
	 * 
	 * @param key
	 *            Key that will be added to the slot.
	 * @return Slot in which the Entry will be put.
	 */
	private int calculateSlotIndex(Object key) {
		return Math.abs(key.hashCode()) % table.length;
	}

	/**
	 * If table load exceeds 75% then the table will be resized to double its
	 * former capacity.
	 */
	@SuppressWarnings("unchecked")
	private void resizeTable() {
		if ((double) size / table.length >= 0.75) {

			// Double the size
			TableEntry<K, V>[] oldTable = table;
			int capacity = table.length;
			clear();

			table = (TableEntry<K, V>[]) new TableEntry[capacity * 2];
			addAll(oldTable);

			modificationCount++;
		}
	}

	/**
	 * Adds all elements to the current table instance from the given table.
	 * 
	 * @param table
	 *            Table of elements that will be added to the current table.
	 */
	private void addAll(TableEntry<K, V>[] table) {
		for (int i = 0; i < table.length; i++) {
			TableEntry<K, V> head = table[i];

			while (head != null) {
				TableEntry<K, V> newEntry = new TableEntry<>();
				newEntry.key = head.key;
				newEntry.value = head.value;
				addTableEntry(newEntry);

				head = head.next;
			}
		}
	}

	@Override
	public Iterator<TableEntry<K, V>> iterator() {
		return new IteratorImpl();
	}

	/**
	 * Nested class which implements an iterator.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {

		/**
		 * Current element that was return by next() method.
		 */
		private TableEntry<K, V> currentElement;

		/**
		 * If remove state is true, element can be removed from the table,
		 * otherise {@link IllegalStateException} is thrown.
		 */
		private boolean removeState;

		/**
		 * Current modification count. initialzied when IteratorImpl object is
		 * created.
		 */
		private int currentModificationCount;

		/**
		 * Current slot for getting next Pair.
		 */
		private int currentSlot;

		/**
		 * Current element in the current slot.
		 */
		private int elementInSlot;

		/**
		 * Default constructor for {@link IteratorImpl} type objects. Store the
		 * current modificationCount variable in from the hashTable instance.
		 */
		public IteratorImpl() {
			currentModificationCount = modificationCount;
			removeState = false;
			currentSlot = 0;
			elementInSlot = 0;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException
		 *             Exception thrown if collection is modified while
		 *             iterating.
		 */
		@Override
		public boolean hasNext() {

			if (currentModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Cannot modify the table while iterating.");
			}

			// Try searching in the same slot
			TableEntry<K, V> head = table[currentSlot];
			for (int i = 0; head != null && i < elementInSlot; i++) {
				head = head.next;
			}
			if (head != null) {
				return true;
			}

			// If there is no more elements in the current slot, search other
			// slots
			for (int i = currentSlot + 1; i < table.length; i++) {
				if (table[i] != null) {
					return true;
				}
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException
		 *             Exception thrown if collection is modified while
		 *             iterating.
		 */
		@Override
		public TableEntry<K, V> next() {

			if (currentModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Cannot modify the table while iterating.");
			}

			while (currentSlot < table.length) {

				// Go to the first non-empty slot
				TableEntry<K, V> head = table[currentSlot];

				// Go to the first unreturned element
				for (int i = 0; i < elementInSlot; i++) {
					head = head.next;
				}

				// If there are no more elements left in current slot find
				// another slot
				if (head == null) {
					currentSlot++;
					elementInSlot = 0;
					continue;
				}

				// If there is an element in the slot return it
				elementInSlot++;
				removeState = true;
				currentElement = head;
				return head;
			}

			throw new NoSuchElementException("No more elements in collection");
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @throws ConcurrentModificationException
		 *             Exception thrown if collection is modified while
		 *             iterating.
		 * @throws IllegalStateException
		 *             Exception thrown if remove is called outside of the
		 *             remove state.
		 */
		@Override
		public void remove() {

			if (currentModificationCount != modificationCount) {
				throw new ConcurrentModificationException("Cannot modify the table while iterating.");
			}

			if (removeState) {
				removeState = false;
				currentModificationCount++;
				elementInSlot--;
				SimpleHashtable.this.remove(currentElement.key);
				return;
			}

			throw new IllegalStateException("Cannot remove entries outside of remove state");
		}

	}

}
