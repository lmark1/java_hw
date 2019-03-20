package hr.fer.zemris.java.hw04.db;

/**
 * Interface used for determining does the given record pass the set condition.
 * 
 * @author Lovro Marković
 *
 */
public interface IFilter {
	
	/**
	 * Method checks if the given record passes a certain condition.
	 * 
	 * @param record Record given for analysis.
	 * @return True if the record passes the condition, otherwise false.
	 */
	public boolean accepts(StudentRecord record);
}
