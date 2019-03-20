package hr.fer.zemris.java.hw04.db;

/**
 * Class describing various types of getters for {@link StudentRecord} objects.
 * 
 * @author Lovro Marković
 *
 */

public class FieldValueGetters {

	/**
	 * Getter for returning student's first name.
	 */
	public static final IFieldValueGetter FIRST_NAME = (record) -> {
		
		if (record == null) {
			throw new IllegalArgumentException("Given record can't be null");
		}
		
		return record.getFirstName();
	};
	
	/**
	 * Getter for returning student's last name.
	 */
	public static final IFieldValueGetter LAST_NAME = (record) -> {
		
		if (record == null) {
			throw new IllegalArgumentException("Given record can't be null");
		}
		
		return record.getLastName();
	};
	
	/**
	 * Getter for the student's jmbag.
	 */
	public static final IFieldValueGetter JMBAG = (record) -> {
		
		if (record == null) {
			throw new IllegalArgumentException("Given record can't be null");
		}
		
		return record.getJmbag();
	};
}
