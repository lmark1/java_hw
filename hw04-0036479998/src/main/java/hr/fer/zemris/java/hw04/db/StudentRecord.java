package hr.fer.zemris.java.hw04.db;

/**
 * Each instance of this class representes record for a student. There are no
 * multiple recods for the same student.
 * 
 * @author Lovro Marković
 *
 */
public class StudentRecord {

	/**
	 * Student's jmbag.
	 */
	private String jmbag;

	/**
	 * Student's last name.
	 */
	private String lastName;

	/**
	 * Student's first name.
	 */
	private String firstName;

	/**
	 * Student's final grade.
	 */
	private int finalGrade;

	/**
	 * Getter for the jmbag variable.
	 * 
	 * @return Student's jmbag.
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter for the final grade variable.
	 * 
	 * @return Student's jmbag.
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Getter for the jmbag variable.
	 * 
	 * @return Student's jmbag.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for the jmbag variable.
	 * 
	 * @return Student's jmbag.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Constructor for StudentRecord objects. Initialzes all variables.
	 * 
	 * @param jmbag
	 *            Student's jmbag.
	 * @param lastName
	 *            Student's last name.
	 * @param firstName
	 *            Student's first name.
	 * @param finalGrade
	 *            Student's final grade.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Two students are equal if their jmbag's are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof StudentRecord && ((StudentRecord) obj).getJmbag().equals(jmbag) ) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return jmbag.hashCode();
	}

}
