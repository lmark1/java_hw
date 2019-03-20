package hr.fer.zemris.java.hw05.demo4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Program demonstrates the use of the "new" stream API used for processing data
 * from collections.
 * 
 * @author Lovro Marković
 *
 */
public class StudentDemo {

	/**
	 * Main method of the program. Executes when program is run.
	 * 
	 * @param args
	 *            Accepts no arguments.
	 */
	public static void main(String[] args) {
		List<String> lines = new ArrayList<>();

		try {
			lines = Files.readAllLines(Paths.get("./studenti.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Can't load file.");
			System.exit(1);
		}

		List<StudentRecord> records = null;

		try {
			records = stringToStudentRecord(lines);

		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		long broj = vratiBodovaViseOd25(records);
		System.out.format("Broj studenata sa vecim brojem bodova > 25: %d%n",
				broj);
		System.out.println();

		long broj5 = vratiBrojOdlikasa(records);
		System.out.format("Broj studenata sa peticom: %d%n", broj5);
		System.out.println();

		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		odlikasi.forEach(System.out::println);
		System.out.println();

		List<StudentRecord> odlikasiSortirano = vratiSortiranuListuOdlikasa(
				records);
		odlikasiSortirano.forEach(System.out::println);
		System.out.println();

		List<StudentRecord> nepolozeniJMBAGovi = vratiPopisNepolozenih(records);
		nepolozeniJMBAGovi.forEach(System.out::println);
		System.out.println();

		Map<Integer, List<StudentRecord>> mapaPoOcjenama = razvrstajStudentePoOcjenama(
				records);
		mapaPoOcjenama.forEach((t,u) -> {
			System.out.format("Ocjena: "+t+"%n");
			u.forEach(System.out::println);
		});
		System.out.println();

		Map<Integer, Integer> mapaPoOcjenama2 = vratiBrojStudenataPoOcjenama(
				records);
		mapaPoOcjenama2.forEach((t, u) -> System.out
				.format("ocjena = %d, broj studenata = %d%n", t, u));
		System.out.println();

		Map<Boolean, List<StudentRecord>> prolazNeProlaz = razvrstajProlazPad(
				records);
		prolazNeProlaz.forEach((t, u) -> {
			System.out.println(t.toString());
			u.forEach(System.out::println);
		});
	}

	/**
	 * Returns number of all student entries that have a total score sum larger
	 * than 25.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return Number of satisfying entries.
	 */
	public static long vratiBodovaViseOd25(List<StudentRecord> records) {
		return records.stream().filter(p -> p.getLabScore()
				+ p.getMidtermScore() + p.getFinalScore() > 25).count();
	}

	/**
	 * Return a number of all student entries that have a '5' grade.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return Number of students with grade '5'.
	 */
	public static long vratiBrojOdlikasa(List<StudentRecord> records) {
		List<StudentRecord> odlikasi = vratiListuOdlikasa(records);
		return odlikasi.size();
	}

	/**
	 * Returns a list of all student entries that have a '5' grade.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return List of all StudentRecord with grade '5'.
	 */
	public static List<StudentRecord> vratiListuOdlikasa(
			List<StudentRecord> records) {

		List<StudentRecord> odlikasi = records.stream()
				.filter(p -> p.getGrade() == 5).collect(Collectors.toList());

		return odlikasi;
	}

	/**
	 * Returns a sorted list by score of all students that have a grade '5'.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return List of all sorted StudentRecord values by score.
	 */
	public static List<StudentRecord> vratiSortiranuListuOdlikasa(
			List<StudentRecord> records) {
		List<StudentRecord> odlikasiSort = records.stream()
				.filter(p -> p.getGrade() == 5)
				.sorted((a, b) -> Double.compare(
						b.getLabScore() + b.getMidtermScore()
								+ b.getFinalScore(),
						a.getLabScore() + a.getMidtermScore()
								+ a.getFinalScore()))
				.collect(Collectors.toList());

		return odlikasiSort;
	}

	/**
	 * Returns a list of students with a failing grade.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return List of all failed StudentRecord entries.
	 */
	public static List<StudentRecord> vratiPopisNepolozenih(
			List<StudentRecord> records) {
		return records.stream().filter(p -> p.getGrade() == 1)
				.sorted((a, b) -> Integer.compare(
						Integer.parseInt(a.getJmbag()),
						Integer.parseInt(b.getJmbag())))
				.collect(Collectors.toList());
	}

	/**
	 * Makes a map with keys as student grades and values as lists of students
	 * with the appropriate grades.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return Map of all students, mapped by grades.
	 */
	public static Map<Integer, List<StudentRecord>> razvrstajStudentePoOcjenama(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.groupingBy(StudentRecord::getGrade));
	}

	/**
	 * Makes a map with keys as student grades and appropriate values describing
	 * number of students with that grade.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return Returns a map as described above.
	 */
	public static Map<Integer, Integer> vratiBrojStudenataPoOcjenama(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.toMap(StudentRecord::getGrade,
						(a) -> Integer.valueOf(1), (a, b) -> a + b));
	}

	/**
	 * Makes a map with true / false boolean keys with appropriate StudentRecord
	 * values. Students that passed are mapped with true keys, others are mapped
	 * with false keys.
	 * 
	 * @param records
	 *            Given list of student records.
	 * @return Returns a map as described above.
	 */
	public static Map<Boolean, List<StudentRecord>> razvrstajProlazPad(
			List<StudentRecord> records) {
		return records.stream()
				.collect(Collectors.partitioningBy(s -> s.getGrade() > 1));
	}

	/**
	 * Creates {@link StudentRecord} list out of given list of Strings.
	 * 
	 * @param lines
	 *            List of Strings.
	 * @return List of StudentRecord objects.
	 * @throws IllegalArgumentException
	 *             Exception thrown if one of the given lines is invalid.
	 */
	private static List<StudentRecord> stringToStudentRecord(
			List<String> lines) {
		List<StudentRecord> studentList = new ArrayList<>();

		for (String line : lines) {
			StudentRecord newRecord = stringToRecord(line);

			if (newRecord == null) {
				throw new IllegalArgumentException(line + " line is invalid.");
			}

			studentList.add(newRecord);
		}

		return studentList;
	}

	/**
	 * Creates a single StudentRecord entry based on the given String input.
	 * 
	 * @param line
	 *            String containing student records.
	 * @return StudentRecord object, or null if given line is invalid.
	 * 
	 */
	private static StudentRecord stringToRecord(String line) {
		String[] studentRecords = line.split("\\t");
		int len = studentRecords.length;

		if (len != 7) {
			return null;
		}

		Integer grade;
		Double finalScore;
		Double midtermScore;
		Double labScore;

		try {
			grade = Integer.parseInt(studentRecords[len - 1]);
			labScore = Double.parseDouble(studentRecords[len - 2]);
			finalScore = Double.parseDouble(studentRecords[len - 3]);
			midtermScore = Double.parseDouble(studentRecords[len - 4]);

		} catch (NumberFormatException e) {
			return null;
		}

		String jmbag = studentRecords[0];
		String lastName = studentRecords[1];
		String firstName = studentRecords[2];

		return new StudentRecord(jmbag, lastName, firstName, midtermScore,
				finalScore, labScore, grade);
	}
}
