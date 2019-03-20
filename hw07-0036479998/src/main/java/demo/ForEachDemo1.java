package demo;

import java.util.Arrays;

import hr.fer.zemris.bf.utils.Util;

/**
 * Demonstrative program for method Util.foreach(...).
 * 
 * @author Lovro Marković
 *
 */
public class ForEachDemo1 {

	/**
	 * Main method of the program. Run when program is executed.
	 * 
	 * @param args Accepts no command line arguments.
	 */
	public static void main(String[] args) {
		Util.forEach(
				Arrays.asList("A","B","C","D"), 
				values -> 
					System.out.println(
						Arrays.toString(values)
							.replaceAll("true", "1")
							.replaceAll("false", "0")
					)
		);
	}
	
}
