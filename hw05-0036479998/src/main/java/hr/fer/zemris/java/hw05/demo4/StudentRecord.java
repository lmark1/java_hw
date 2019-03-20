package hr.fer.zemris.java.hw05.demo4;

/**
 * This class models each student entry in the database.
 * 
 * @author Lovro Marković
 *
 */
public class StudentRecord {

	/**
	 * Students jmbag.
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
	 * Student's midterm score.
	 */
	private double midtermScore;
	
	/**
	 * Student's final score.
	 */
	private double finalScore;
	
	/**
	 * Student's lab score.
	 */
	private double labScore;
	
	/**
	 * Student's grade.
	 */
	private int grade;

	/**
	 * Constructor initializing all class variables.
	 * 
	 * @param jmbag Student's jmbag.
	 * @param lastName Student's last name.
	 * @param firstName Student's first name.
	 * @param midtermScore Student's midterm score.
	 * @param finalScore Student's final score.
	 * @param labScore Student's lab score.
	 * @param grade Student's grade.
	 */
	public StudentRecord(String jmbag, String lastName, String firstName,
			double midtermScore, double finalScore, double labScore,
			int grade) {
		super();
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.midtermScore = midtermScore;
		this.finalScore = finalScore;
		this.labScore = labScore;
		this.grade = grade;
	}
	
	/**
	 * Getter for the final score.
	 * 
	 * @return Student's final score.
	 */
	public double getFinalScore() {
		return finalScore;
	}
	
	/**
	 * Getter for the first name.
	 * 
	 * @return Student's first name.
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Getter for the grade.
	 * 
	 * @return Student's grade.
	 */
	public int getGrade() {
		return grade;
	}
	
	/**
	 * Getter for the jmbag.
	 * 
	 * @return Student's jmbag.
	 */
	public String getJmbag() {
		return jmbag;
	}
	
	/**
	 * Getter for the lab score.
	 * 
	 * @return Student's lab score.
	 */
	public double getLabScore() {
		return labScore;
	}
	
	/**
	 * Getter for the last name.
	 * 
	 * @return Student's last name.
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Getter for the midterm score.
	 * 
	 * @return Student's midterm score.
	 */
	public double getMidtermScore() {
		return midtermScore;
	}
	
	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();
		
		newString.append(jmbag+" ");
		newString.append(lastName+" ");
		newString.append(firstName+" ");
		newString.append(midtermScore+" ");
		newString.append(finalScore+" ");
		newString.append(labScore+" ");
		newString.append(grade+" ");
		
		return newString.toString();
	}
}
