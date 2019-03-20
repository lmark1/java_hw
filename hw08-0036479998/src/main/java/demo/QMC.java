package demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.qmc.Minimizer;
import hr.fer.zemris.bf.utils.Util;

/**
 * Demonstrative program for the QMC minimizer. Expected user input is as
 * follows e.g. "function (var1, var2, var3) = var1 and var2 or var3". Program
 * will print out minimal forms of the function.
 * 
 * @author Lovro Marković
 *
 */
public class QMC {

	/**
	 * Main method of the program, executed when program is run.
	 * 
	 * @param args
	 *            No input argzments expected.
	 */
	public static void main(String[] args) {
		Scanner inputScanner = new Scanner(System.in);

		while (true) {
			System.out.print(">");
			String newInput = inputScanner.nextLine();

			if (newInput.equals("quit"))
				break;
			if (!newInput.contains("=")) {
				System.out.println("Pogreška: funkcija nije ispravno zadana.");
				continue;
			}

			String[] splitString = newInput.split("=");
			if (splitString.length != 2) {
				System.out.println("Pogreška: funkcija nije ispravno zadana");
				continue;
			}

			// Try to get variables from the first part of input
			List<String> variables = null;
			try {
				variables = getVariables(splitString[0]);
			} catch (IllegalStateException e) {
				System.out.println(e.getMessage());
				continue;
			}

			// Get both parts of the expression
			String[] expression = null;
			if (splitString[1].contains("|")) {
				expression = splitString[1].split("\\|");

				// If there is more than one separator
				if (expression.length != 2) {
					System.out.println("Izraz nije ispravno zadan.");
					continue;
				}

			} else {
				expression = new String[1];
				expression[0] = splitString[1];
			}

			Set<Integer> minterms = null;
			if (expression[0].trim().charAt(0) == '[') {

				// Get minterns defined with indexes
				try {
					minterms = getIndexes(expression[0].trim());
				} catch (IllegalStateException e) {
					System.out.println(e.getMessage());
					continue;
				}

			} else {

				// Get minternes defined with expression
				Parser newParser = null;
				try {
					newParser = new Parser(expression[0].trim());
					minterms = Util.toSumOfMinterms(variables,
							newParser.getExpression());

				} catch (Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
			}

			Set<Integer> dontCare = null;
			if (expression.length == 2) {

				try {
					if (expression[1].trim().charAt(0) == '[') {

						// Get dont cares defined with indexes
						dontCare = getIndexes(expression[1].trim());

					} else {

						// Get dont cares defined with expression
						Parser newParser = new Parser(expression[1].trim());
						dontCare = Util.toSumOfMinterms(variables,
								newParser.getExpression());

					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					continue;
				}

			} else {
				// If dont cares are not given return an empty set
				dontCare = new TreeSet<>();
			}

			Minimizer newMin = null;
			try{
				newMin = new Minimizer(minterms, dontCare, variables);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
			
			int index = 1;
			for (String string : newMin.getMinimalFormsAsString()) {
				System.out.print(index++ +". ");
				System.out.println(string);
				
			}

		}

		inputScanner.close();
	}

	/**
	 * Extracts indexes from the given string. Expected string is [1, 2, 3].
	 * 
	 * @param string
	 *            Given string containing indexes.
	 * @return Indexes.
	 */
	private static Set<Integer> getIndexes(String string) {
		Set<Integer> indexes = new TreeSet<>();

		int currentIndex = 0;
		char[] data = string.toCharArray();
		boolean brackets = false;

		while (currentIndex < data.length) {

			if (data[currentIndex] == '[' && !brackets) {
				currentIndex++;
				brackets = true;
				continue;
			}

			if (data[currentIndex] == ']' && brackets) {
				currentIndex++;
				brackets = false;
				continue;
			}

			if (Character.isWhitespace(data[currentIndex])) {
				currentIndex++;
				continue;
			}

			if (data[currentIndex] == ',' && brackets) {
				currentIndex++;
				continue;
			}

			if (Character.isDigit(data[currentIndex]) && brackets) {
				StringBuilder newVar = new StringBuilder();

				newVar.append(data[currentIndex]);
				currentIndex++;

				while (currentIndex < data.length
						&& Character.isDigit(data[currentIndex])) {
					newVar.append(data[currentIndex]);
					currentIndex++;
				}

				indexes.add(Integer.parseInt(newVar.toString()));
				continue;
			}

			throw new IllegalStateException(
					"Pogreška prilikom dohvaćanja minterma ili dont carova zadanim indexima.");
		}

		return indexes;
	}

	/**
	 * Extracts variables from the given function header. Function header
	 * example "function(A, B, C)"
	 * 
	 * @param string
	 *            Given function header.
	 * @return Variables.
	 */
	private static List<String> getVariables(String string) {
		List<String> variables = new ArrayList<>();

		int currentIndex = 0;
		char[] data = string.toCharArray();
		boolean brackets = false;

		while (currentIndex < data.length) {

			if (Character.isWhitespace(data[currentIndex])) {
				currentIndex++;
				continue;
			}

			if (Character.isLetter(data[currentIndex]) && !brackets) {

				// Consume all data
				currentIndex++;
				while (currentIndex < data.length
						&& Character.isLetterOrDigit(data[currentIndex])) {
					currentIndex++;
				}
				continue;
			}

			if (data[currentIndex] == '(' && !brackets) {
				brackets = true;
				currentIndex++;
				continue;
			}

			if (Character.isLetter(data[currentIndex]) && brackets) {
				StringBuilder newVar = new StringBuilder();

				newVar.append(data[currentIndex]);
				currentIndex++;

				while (currentIndex < data.length
						&& Character.isLetterOrDigit(data[currentIndex])) {
					newVar.append(data[currentIndex]);
					currentIndex++;
				}

				variables.add(newVar.toString().toUpperCase());
				continue;
			}

			if (data[currentIndex] == ',' && brackets) {
				currentIndex++;
				continue;
			}

			if (data[currentIndex] == ')' && brackets) {
				currentIndex++;
				brackets = false;
				continue;
			}

			throw new IllegalStateException(
					"Pogrešno definiranje variabli funkcije.");
		}

		return variables;
	}
}
