package hr.fer.zemris.bf.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

import hr.fer.zemris.bf.model.Node;

/**
 * This class contains some auxiliary methods.
 * 
 * @author Lovro Marković
 *
 */
public class Util {

	/**
	 * For a given list of variables generates all combinations of values and
	 * the given consumer will be called on each combination of values.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param consumer
	 *            Given consumer.
	 */
	public static void forEach(List<String> variables,
			Consumer<boolean[]> consumer) {

		List<boolean[]> combinations = generateCombinations(variables.size());
		for (boolean[] boolArray : combinations) {
			consumer.accept(boolArray);
		}
	}

	/**
	 * Generated all combinations of ones and zeros of n size.
	 * 
	 * @param n
	 *            Size of each combination.
	 * @return List of Lists of boolean values.
	 */
	private static List<boolean[]> generateCombinations(int n) {
		List<boolean[]> combinations = new ArrayList<>();

		int numberOfCombinations = (int) Math.pow(2, n);
		for (int i = 0; i < numberOfCombinations; i++) {

			boolean[] newCombination = new boolean[n];
			int index = n - 1;
			int combinationNumber = i;

			while (combinationNumber > 0) {
				newCombination[index] = combinationNumber % 2 == 1;
				combinationNumber /= 2;
				index--;
			}

			// Add remaining zeros if number is too small
			while (index >= 0) {
				newCombination[index] = false;
				index--;
			}

			combinations.add(newCombination);
		}

		return combinations;
	}

	/**
	 * This method is used for checking if given string list is equal to list
	 * generated from the text file given by path.
	 * 
	 * @param testList
	 *            Generated list from output.
	 * @param correctListPath
	 *            Path to text file.
	 * @return True if list is valid.
	 * @throws IOException
	 *             Exception thrown if path is incorrect.
	 */
	public static boolean checkLists(List<String> testList,
			Path correctListPath) throws IOException {
		boolean isValid = true;

		// Check if lists are equal
		List<String> lines = Files.readAllLines(correctListPath,
				StandardCharsets.UTF_8);

		Iterator<String> itLines = lines.iterator();
		Iterator<String> itTest = testList.iterator();
		int line = 0;

		while (itLines.hasNext() && itTest.hasNext()) {
			line++;
			String s1 = itLines.next();
			String s2 = itTest.next();

			if (!s1.equals(s2)) {
				System.out.println("Error in line: " + line);
				System.out.println(s1);
				System.out.println(s2);

				isValid = false;
			}
		}

		if (itLines.hasNext() || itTest.hasNext()) {
			System.out.println("List sizes not equal!");
			isValid = false;
		} else {
			System.out.println("Equal!");
		}

		return isValid;
	}

	/**
	 * Generates a set of boolean arrays that generate the wanted value from the
	 * given expression.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            A boolean expression tree.
	 * @param expressionValue
	 *            Given result of the expression.
	 * @return Returns a set of boolean values that satisfy
	 */
	public static Set<boolean[]> filterAssignments(List<String> variables,
			Node expression, boolean expressionValue) {

		ExpressionEvaluator eval = new ExpressionEvaluator(variables);
		Set<boolean[]> returnSet = new TreeSet<>(new Comparator<boolean[]>() {

			@Override
			public int compare(boolean[] o1, boolean[] o2) {

				for (int i = 0, length = o1.length; i < length; i++) {

					if (o1[i] && !o2[i]) {
						return 1;
					}

					if (!o1[i] && o2[i]) {
						return -1;
					}
				}

				return 0;
			}
		});

		forEach(variables, new Consumer<boolean[]>() {

			@Override
			public void accept(boolean[] t) {
				eval.setValues(t);
				expression.accept(eval);

				if (eval.getResult() == expressionValue) {
					returnSet.add(t);
				}
			}
		});

		return returnSet;
	}

	/**
	 * Method accepts array of boolean values and returns index in the list
	 * where the given values are.
	 * 
	 * @param values
	 *            Array of boolean values.
	 * @return Index of row in combinations.
	 */
	public static int booleanArrayToInt(boolean[] values) {

		int rowNumber = 0;
		for (int i = values.length - 1, power = 0; i >= 0; i--) {

			if (values[i]) {
				rowNumber += Math.pow(2, power);
			}
			power++;
		}
		return rowNumber;
	}

	/**
	 * Returns minterms as a set of integer.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            Given epxression.
	 * @return Set of minterms.
	 */
	public static Set<Integer> toSumOfMinterms(List<String> variables,
			Node expression) {

		return filterMinMaxTerm(true, variables, expression);
	}

	/**
	 * Returns maxterms as a set of integer.
	 * 
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            Given epxression.
	 * @return Set of minterms.
	 */
	public static Set<Integer> toSumOfMaxterms(List<String> variables,
			Node expression) {

		return filterMinMaxTerm(false, variables, expression);
	}

	/**
	 * Caluculates minterms or maxterms based on the toggle input parameter.
	 * True for minterms, False for maxeterms.
	 * 
	 * @param toggle
	 *            True for minterms, false for maxterms.
	 * @param variables
	 *            List of variables.
	 * @param expression
	 *            Root of expression tree.
	 * @return Set of integer minterm / maxterm.
	 */
	private static Set<Integer> filterMinMaxTerm(boolean toggle,
			List<String> variables, Node expression) {
		Set<Integer> integerSet = new TreeSet<>();

		Set<boolean[]> boolSet = filterAssignments(variables, expression,
				toggle);
		for (boolean[] set : boolSet) {
			integerSet.add(booleanArrayToInt(set));
		}

		return integerSet;
	}

	/**
	 * Convert a whole number to binary with n - bits. Position 0 in the array
	 * holds the highest bit. If n is too small to contain all the bytes, an
	 * appropriate array will be returned, however information will be lost.
	 * 
	 * @param x
	 *            Number that will be converted.
	 * @param n
	 *            Size of the byte array.
	 * @return Byte array containing binary numbers.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given size of byte array is negative.
	 */
	public static byte[] indexToByteArray(int x, int n) {

		if (n < 0) {
			throw new IllegalArgumentException(
					"Size of byte array has to be positive");
		}

		byte[] binaryNumber = new byte[n];
		int index = n - 1;
		boolean isNegative = false;

		if (x < 0) {
			isNegative = true;
			x = -1 * x;
		}

		// Binary conversion
		while (index >= 0 && x > 0) {
			binaryNumber[index] = (byte) (x % 2 == 0 ? 0 : 1);
			x /= 2;
			index--;
		}

		// Two's complement
		if (isNegative) {
			byte residue = 1;

			for (int i = n - 1; i >= 0; i--) {
				binaryNumber[i] = (byte) (binaryNumber[i] == 1 ? 0 : 1);
				
				byte sum = (byte) (binaryNumber[i] + residue);
				binaryNumber[i] = (byte) (sum % 2);
							
				if (sum > 1) {
					residue = 1;
				} else {
					residue = 0;
				}
			}
		}
		
		return binaryNumber;
	}
}
