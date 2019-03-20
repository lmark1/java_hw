package hr.fer.zemris.hw16.trazilica;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.math.VectorOperations;

/**
 * This class represents a console used for searching through given files
 * provided in the directory given as a command line argument. Console excepts
 * queries starting with keyword 'query' and a list of words. It lists documents
 * the given words appear in. It also accepts command 'type' and a number of the
 * document in the result list. It prints out the document. Command 'results'
 * prints out current result set for the latest query. Command 'exit' exits the
 * application.
 * 
 * @author Lovro Marković
 *
 */
public class Konzola {

	/**
	 * List of stopwords.
	 */
	private static List<String> stopWords;

	/**
	 * List of all document properties contining frequency vectors..
	 */
	private static List<DocumentProperties> documentVectors = new ArrayList<>();

	/**
	 * Map with vocabulary words and their inedexes.
	 */
	private static Map<String, Integer> vocabulary = new HashMap<>();

	/**
	 * Map containg vocabulary words and their document appearance count.
	 */
	private static Map<String, Integer> vocabularyCount = new HashMap<>();

	/**
	 * Set of all the query results.
	 */
	private static Set<DocumentProperties> resultSet;

	/**
	 * List of only the 10 best query results.
	 */
	private static List<DocumentProperties> resultList;

	/**
	 * Number of searchable documents.
	 */
	private static int documentCount;

	/**
	 * IDF vector.
	 */
	private static double[] idf;

	/**
	 * Index of the vocabulary.
	 */
	private static int index = 0;
	
	/**
	 * Number of results shown.
	 */
	private static final int RESULT_COUNT = 10;

	/**
	 * Path to searchable documents.
	 */
	private static Path doc;

	/**
	 * Main method of the program.
	 * 
	 * @param args
	 *            Accepts one command line argument - path to the directory
	 *            containing all the searchable documents.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Program accepts one command line argument.");
			return;
		}

		doc = Paths.get(args[0]);

		if (!Files.isDirectory(doc)) {
			System.out.println("Directory path expected.");
			return;
		}

		initializeStopwords();
		try {
			iterateFiles(doc, (p) -> addToVocabulary(p));
			iterateFiles(doc, new Command() {

				@Override
				public void execute(Path p) throws IOException {
					DocumentProperties prop = getDocumentProperties(p);
					if (prop != null) {
						documentVectors.add(prop);
					}
				}
			});
		} catch (IOException e) {
			System.out.println("Error occured while reading files.");
			e.printStackTrace();
		}
		initializeIDF();

		System.out.println(
				"Veličina riječnika je " + vocabulary.size() + " riječi.");

		Scanner s = new Scanner(System.in);
		while (true) {
			System.out.println();
			System.out.print("Enter command > ");
			String input = s.nextLine();

			if (input.toLowerCase().equals("exit")) {
				System.out.println("Doviđenja!");
				break;
			}

			try {
				parseAndExecute(input);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		s.close();
	}

	/**
	 * Parses input string and executes appropriate command.
	 * 
	 * @param input
	 *            Input string.
	 */
	private static void parseAndExecute(String input) {

		if (input.isEmpty()) {
			System.out.println("Unesite naredbu.");
			return;
		}

		String[] split = input.split("\\s+");
		String command = split[0].toLowerCase();
		String[] arguments = new String[split.length - 1];
		for (int i = 1; i < split.length; i++) {
			arguments[i - 1] = split[i];
		}

		if (command.equals("query")) {
			executeQuery(arguments);

		} else if (command.equals("type")) {
			executeType(arguments);

		} else if (command.equals("results")) {
			executeResults(arguments);

		} else {
			System.out.println("Nepoznata naredba.");
		}
	}

	/**
	 * Prints out results of the current query.
	 * 
	 * @param arguments
	 *            This command excepts no arguments.
	 */
	private static void executeResults(String[] arguments) {
		if (resultList == null || resultList.isEmpty()) {
			System.out
					.println("Potrebno je najprije izvršiti 'query' naredbu.");
			return;
		}

		if (arguments.length != 0) {
			System.out.println("Naredba 'result' ne prima argumente.");
			return;
		}

		printResults();
	}

	/**
	 * Executes 'type' command.
	 * 
	 * @param arguments
	 *            Command arguments.
	 */
	private static void executeType(String[] arguments) {

		if (resultList == null || resultList.isEmpty()) {
			System.out
					.println("Potrebno je najprije izvršiti 'query' naredbu.");
			return;
		}

		if (arguments.length != 1) {
			System.out.println("Naredba 'type' prima samo jedan argument.");
			return;
		}

		int index = 0;
		try {
			index = Integer.valueOf(arguments[0]);
		} catch (NumberFormatException e) {
			System.out.println("Argument mora biti cijeli broj > 0.");
			return;
		}

		Path path = null;
		try {
			path = Paths.get(resultList.get(index).path);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Argument mora biti broj između 0 i " + (resultList.size()-1));
			return;
		}

		String text = null;
		try {
			text = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		System.out.println(text);
	}

	/**
	 * Exectue command 'query'.
	 * 
	 * @param arguments
	 *            Query arguments.
	 */
	private static void executeQuery(String[] arguments) {

		Set<String> query = new HashSet<>();
		for (String string : arguments) {
			if (vocabulary.containsKey(string.toLowerCase())) {
				query.add(string.toLowerCase());
			}
		}

		if (query.isEmpty()) {
			System.out.println(
					"Potrebo je unijeti kljucne rijeci kao argument naredbe 'query'.");
			return;
		}

		if (resultSet != null && resultList != null) {
			resultSet.clear();
			resultList.clear();
		} else {
			resultSet = new TreeSet<>(new Comparator<DocumentProperties>() {

				@Override
				public int compare(DocumentProperties o1,
						DocumentProperties o2) {
					if (o1.sim < o2.sim) {
						return 1;
					} else if (o1.sim > o2.sim) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			resultList = new ArrayList<>();
		}

		System.out.println("Query is: " + query);

		// Calculate similarities for all the documents.
		for (DocumentProperties docProp : documentVectors) {

			int[] tf = new int[vocabulary.size()];
			for (String word : query) {
				int index = vocabulary.get(word);
				tf[index] = 1;
			}

			double[] inputTFIDF = VectorOperations.multiply(tf, idf);
			double[] docTFIDF = VectorOperations.multiply(docProp.freqVector,
					idf);

			double sim = VectorOperations.scalarMultiply(inputTFIDF, docTFIDF)
					/ (VectorOperations.norm(docTFIDF)
							* VectorOperations.norm(inputTFIDF));

			DocumentProperties newProp = new DocumentProperties();
			newProp.path = docProp.path;
			newProp.sim = sim;
			resultSet.add(newProp);
		}

		// Extract only 10 best documents.
		int count = 0;
		for (DocumentProperties docProp : resultSet) {
			if (count >= RESULT_COUNT) {
				break;
			}

			if ((docProp.sim - 0) < 10e-15) {
				continue;
			}
			count++;
			resultList.add(docProp);
		}

		System.out.println("Najboljih "+RESULT_COUNT+" rezultata:");
		printResults();
	}

	/**
	 * Print results from the result list.
	 */
	private static void printResults() {
		for (int i = 0; i < resultList.size(); i++) {
			System.out.println("[" + i + "] " + resultList.get(i));
		}
	}

	/**
	 * Add all non stop words found in a file in the path to the vocabulary.
	 * 
	 * @param doc
	 *            Path to the file.
	 * @throws IOException
	 *             Exception thrown if there is an error while reading files.
	 */
	private static void addToVocabulary(Path doc) throws IOException {

		if (!Files.isReadable(doc)) {
			return;
		}

		documentCount++;
		String[] words = getWords(doc);

		for (String word : words) {
			String lWord = word.toLowerCase();
			if (stopWords.contains(lWord) || lWord.isEmpty()) {
				continue;
			}

			if (!vocabulary.containsKey(lWord)) {
				vocabulary.put(lWord, index);
				index++;
			}
		}
	}

	/**
	 * Generates a document vector from the file given as an argument.
	 * 
	 * @param root
	 *            Path containing a readable file.
	 * @return Return document properties.
	 * @throws IOException
	 *             Exception thrown if there is an error reading from files.
	 */
	private static DocumentProperties getDocumentProperties(Path root)
			throws IOException {

		if (!Files.isReadable(root)) {
			return null;
		}

		String[] words = getWords(root);
		Map<String, Integer> wordMap = new HashMap<>();
		for (String word : words) {

			String lWord = word.toLowerCase();
			if (stopWords.contains(lWord) || lWord.isEmpty()) {
				continue;
			}

			if (wordMap.containsKey(lWord)) {
				wordMap.put(lWord, wordMap.get(lWord) + 1);
			} else {
				wordMap.put(lWord, 1);
			}
		}

		Set<String> keySet = wordMap.keySet();
		int[] vector = new int[vocabulary.size()];
		for (String key : keySet) {

			int index = vocabulary.get(key);
			int value = wordMap.get(key);
			vector[index] = value;

			// Store words appearance in the map
			if (vocabularyCount.containsKey(key)) {
				vocabularyCount.put(key, vocabularyCount.get(key) + 1);

			} else {
				vocabularyCount.put(key, 1);
			}
		}

		DocumentProperties docProp = new DocumentProperties();
		docProp.freqVector = vector;
		docProp.path = root.toString();
		return docProp;
	}

	/**
	 * Retern array of words from the given file.
	 * 
	 * @param path
	 *            Path to the file.
	 * @return Array of words.
	 * @throws IOException
	 *             Exception thrown if there is an error while reading.
	 */
	private static String[] getWords(Path path) throws IOException {

		byte[] b = Files.readAllBytes(path);
		String text = new String(b, StandardCharsets.UTF_8);
		String[] words = text.split("\\P{L}+");
		return words;
	}

	/**
	 * Iterates through files and a applies a function to each text file.
	 * 
	 * @param root
	 *            Document path.
	 * @param c
	 *            Command which will be appliead to each text file.
	 * @throws IOException
	 *             Exception thrown if there is an error while reading files.
	 */
	private static void iterateFiles(Path root, Command c) throws IOException {
		if (Files.isDirectory(root)) {

			DirectoryStream<Path> dirs = Files.newDirectoryStream(root);
			for (Path path : dirs) {
				iterateFiles(path, c);
			}

		} else {
			c.execute(root);
		}
	}

	/**
	 * Initializes stopwords.
	 */
	private static void initializeStopwords() {
		try {
			stopWords = Files.readAllLines(
					Paths.get("./src/main/resources/hrvatski_stoprijeci.txt"),
					StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Can't load file.");
			System.exit(1);
		}
	}

	/**
	 * Initialize IDF vector.
	 */
	private static void initializeIDF() {
		idf = new double[vocabulary.size()];

		Set<String> keySet = vocabularyCount.keySet();
		for (String key : keySet) {
			int index = vocabulary.get(key);
			double value = Math
					.log10((double) documentCount / vocabularyCount.get(key));
			idf[index] = value;
		}
	}

	/**
	 * This class is used for storing properties regarding each document.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class DocumentProperties {

		/**
		 * Frequency vector.
		 */
		private int[] freqVector;

		/**
		 * Path to the file.
		 */
		private String path;

		/**
		 * Similarity number.
		 */
		private double sim;

		@Override
		public String toString() {
			return String.format("(%.4f) %s", sim, path);
		}
	}

	/**
	 * This interface defines a command which is executed on a text file.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private interface Command {

		/**
		 * Executes a function on a text file with path p.
		 * 
		 * @param p
		 *            Path to the text file.
		 * @throws IOException
		 *             Exception thrown if an error occurs while reading from
		 *             file.
		 */
		public void execute(Path p) throws IOException;

	}
}
