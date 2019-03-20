package hr.fer.zemris.java.hw04.db;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw04.collections.SimpleHashtable;

/**
 * Class used for performing operations on the student database. Each instance
 * creates and stores an internal list of student records from the given string.
 * 
 * @author Lovro Marković
 *
 */
public class StudentDatabase {

	/**
	 * Simple hashtable containing all student records.
	 */
	private SimpleHashtable<String, StudentRecord> studentTable;

	/**
	 * List of all student records.
	 */
	private List<StudentRecord> studentList;

	/**
	 * Constructor which accepts the whole database string line by line as an
	 * array of Strings. Each String represents a row of the database file. It
	 * creates an internal list of student records.
	 * 
	 * @param databaseString
	 *            Array of strings representing rows of database.
	 * @throws IllegalArgumentException
	 *             Exception thrown if a certain string row is invalid.
	 */
	public StudentDatabase(String[] databaseString) {

		if (databaseString == null) {
			throw new IllegalArgumentException("Given string can't be null.");
		}
		studentTable = new SimpleHashtable<>();
		studentList = new ArrayList<>();

		for (String string : databaseString) {
			StudentRecord newEntry = stringToRecord(string);

			// Invalid newEntry
			if (newEntry == null) {
				throw new IllegalArgumentException("Illegal entry found: " + string);
			}
			
			// valid entry but already is stored 
			if (studentTable.containsKey(newEntry.getJmbag())) {
				throw new IllegalArgumentException("Duplicate jmbag found");
			}

			studentList.add(newEntry);
			studentTable.put(newEntry.getJmbag(), newEntry);
		}
	}

	/**
	 * Returns {@link StudentRecord} assigned to the given jmbag. Valid jmbag is
	 * a set of numbers.
	 * 
	 * @param jmbag
	 *            Jmbag of the student.
	 * @return {@link StudentRecord} value assigned to the jmbag.
	*/
	public StudentRecord forJMBAG(String jmbag) {
		
		if (jmbag == null) return null;
			
		return studentTable.get(jmbag);
	}

	/**
	 * Return all database entries as a List that pass a condition given by the
	 * filter.
	 * 
	 * @param filter
	 *            Object which extends the {@link IFilter} interface, determines
	 *            the condition which will be tested.
	 * @return List of all {@link StudentRecord} objects in the database that
	 *         pass the
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> tempList = new ArrayList<>();
		
		for (StudentRecord studentRecord : studentList) {
			if (filter.accepts(studentRecord)) {
				tempList.add(studentRecord);
			}
		}

		return tempList;
	}

	/**
	 * Makes a {@link StudentRecord} object from the given String.
	 * 
	 * @param databaseEntry
	 *            Given string, 1 row from the database.
	 * @return {@link StudentRecord} Object if databaseEntry is valid, otherwise
	 *         null.
	 */
	private StudentRecord stringToRecord(String databaseEntry) {
		String[] studentRecords = databaseEntry.split("\\t");
		int recordLen = studentRecords.length;
		
		//If it is not correctly seperated by tabs
		if (recordLen != 4) {
			return null;
		}
		
		Integer finalGrade; 
		try {
			finalGrade = Integer.parseInt(studentRecords[recordLen-1]);
		} catch (NumberFormatException e) {
			return null;
		}
			
		String jmbag = studentRecords[0];
		String lastName = studentRecords[1];
		String firstName = studentRecords[2];

		return new StudentRecord(jmbag, lastName, firstName, finalGrade);
	}
}
