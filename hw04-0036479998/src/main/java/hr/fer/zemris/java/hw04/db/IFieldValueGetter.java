package hr.fer.zemris.java.hw04.db;

/**
 * Interface used for getting data from {@link StudentRecord} objects.
 * 
 * @author Lovro Marković
 *
 */
public interface IFieldValueGetter {
	
	/**
	 * Method determining which Student record data will be returned.
	 * 
	 * @param record Given student record data.
	 * @return Certain data from the StudentRecord object.
	 */
	public String get(StudentRecord record);

}
