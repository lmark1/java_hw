package hr.fer.zemris.java.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.hw04.db.lexer.WildcardException;

/**
 * Demonstrative program for the database emulator. Recieves query through the
 * command line and responds by printing appropriate database entries.
 * 
 * @author Lovro Marković
 *
 */
public class StudentDB {

	/**
	 * Main method of this program.
	 * 
	 * @param args
	 *            Accepts no arguments.
	 */
	public static void main(String[] args) {
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(Paths.get("./database.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Can't load file.");
			System.exit(1);
		}

		String[] stringLine = lines.toArray(new String[lines.size()]);
		
		// Try to get database
		StudentDatabase newDatabase = null;
		try{
			newDatabase = new StudentDatabase(stringLine);
		
		} catch (IllegalArgumentException e) {
			System.out.println("Error while reading database. Exiting...");
			System.exit(1);
		}

		Scanner inputScan = new Scanner(System.in);
		while (true) {
			System.out.print(">");
			
			String newInput = inputScan.nextLine();

			if (newInput.toLowerCase().equals("exit")) {
				System.out.println("Goodbye");
				break;
			}

			Scanner lineScanner = new Scanner(newInput);

			// If it's completely empty 
			if (!lineScanner.hasNext()) {
				System.out.println("Please write \"query\" along with some conditions.");
				continue;
			}

			// If it's not empty but the first word isn't query
			if (!lineScanner.next().equals("query")) {
				System.out.println("Invalid input - no query keyword.");
				continue;
			}
			
			// If there is no more string input after query
			if (!lineScanner.hasNext()) {
				System.out.println("Please input some conditions after query keyword.");
				continue;
			}

			// Try to get parse input
			QueryParser parser;
			try{ 
				parser = new QueryParser(lineScanner.nextLine());
			
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid query input.");
				continue;
			
			} catch (WildcardException e1) {
				System.out.println("Too many wildcards found during like operation.");
				continue;
			
			} catch (Exception e2) {
				System.out.println("Something went wrong!");
				continue;
			}
			
			List<StudentRecord> studentList = new ArrayList<>();

			// Make a StudentRecord list
			if (parser.isDirectQuery()) {
				StudentRecord r = newDatabase.forJMBAG(parser.getQueriedJMBAG());
				studentList.add(r);

			} else {
				studentList = newDatabase.filter(new QueryFilter(parser.getQuery()));
			}

			if (studentList.isEmpty()){
				System.out.println("No database entries found");
				continue;
			} 
			
			System.out.println(makeRecordTable(studentList));

			lineScanner.close();
		}

		inputScan.close();
	}

	/**
	 * Makes a table of all the given records.
	 * 
	 * @param studentList
	 *            Given records, can be 1 or more.
	 * @return Table-like string representation of given records.
	 */
	private static String makeRecordTable(List<StudentRecord> studentList) {
		String tableString = new String();
		List<Integer> length = findMaxDimensions(studentList);
		
		int jmbagLength = length.get(0);
		int firstNameLength = length.get(1);
		int lastNameLength = length.get(2);
		int finalGradeLength = length.get(3);

		String borderLine = generateBorders(length);
		tableString += borderLine + "\n";
		
		for (StudentRecord	record: studentList) {
			String jmbagString = "| " + String.format("%-" + jmbagLength + "s", record.getJmbag()) + " ";
			String firstNameString = "| " + String.format("%-" + firstNameLength + "s", record.getFirstName()) + " ";
			String lastNameString = "| " + String.format("%-" + lastNameLength + "s", record.getLastName()) + " ";
			String finalGradeString = "| " + String.format("%-" + finalGradeLength + "d", record.getFinalGrade()) + " |";
			
			tableString += jmbagString + firstNameString + lastNameString + finalGradeString + "\n";
		}	
		
		tableString += borderLine;
		
		return tableString;
	}

	/**
	 * Generate border lines for the table. Border line example:
	 * "+============+===========+===========+===+".
	 * 
	 * @param length
	 *            List of lengths for each
	 * @return generater Border string.
	 */
	private static String generateBorders(List<Integer> length) {
		return  "+" + nCharacterString('=', length.get(0) + 2) + "+" + nCharacterString('=', length.get(1) + 2)
		+ "+" + nCharacterString('=', length.get(2) + 2) + "+" + nCharacterString('=', length.get(3) + 2) + "+";
	}

	/**
	 * Create a string consisting of n characters.
	 * 
	 * @param c
	 *            Given character to multiply.
	 * @param n
	 *            How many times charater needs to be multiplied.
	 * @return String of n same characters c
	 */
	private static String nCharacterString(char c, int n) {
		StringBuilder newString = new StringBuilder();

		for (int i = 0; i < n; i++) {
			newString.append(Character.toString(c));
		}

		return newString.toString();
	}

	/**
	 * Find maximum dimensions of given student list inputs and store them in an
	 * integer list.
	 * 
	 * @param studentList
	 *            Given student list.
	 * @return Stored maximum lengths in a Integer list.
	 */
	private static List<Integer> findMaxDimensions(List<StudentRecord> studentList) {
		List<Integer> maxLength = new ArrayList<>();

		maxLength.add(0);
		maxLength.add(0);
		maxLength.add(0);
		maxLength.add(0);

		for (StudentRecord studentRecord : studentList) {
			int currJmbagLen = studentRecord.getJmbag().length();
			int currFirstNameLen = studentRecord.getFirstName().length();
			int currLastNameLen = studentRecord.getLastName().length();
			int currFinalGradeLen = Integer.toString(studentRecord.getFinalGrade()).length();
					
			int oldJmbagLen = maxLength.get(0);
			int oldFirstNameLen = maxLength.get(1);
			int oldLastNameLen = maxLength.get(2);
			int oldFinalGradeLen = maxLength.get(3);

			if (currJmbagLen > oldJmbagLen) {
				maxLength.set(0, currJmbagLen);
			}

			if (currFirstNameLen > oldFirstNameLen) {
				maxLength.set(1, currFirstNameLen);
			}

			if (currLastNameLen > oldLastNameLen) {
				maxLength.set(2, currLastNameLen);
			}

			if (currFinalGradeLen > oldFinalGradeLen) {
				maxLength.set(3, currFinalGradeLen);
			}
		}

		return maxLength;
	}

}
