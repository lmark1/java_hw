package hr.fer.zemris.java.crypto;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which contains static factory methods used for conversion from
 * hexadecimal values to byte arrays and the other way around.
 * 
 * Also contains a method for splitting strings used in MyShell commands.
 * 
 * @author Lovro Marković
 *
 */
public class Util {

	/**
	 * Array of hex characters used in the converstion methods.
	 */
	private static final char[] hexChars = "0123456789abcdef".toCharArray();

	/**
	 * Converts a given String of hex characters to a byte array.
	 * 
	 * @param keyText
	 *            String of hex character.
	 * @return Array of bytes.
	 * 
	 * @throws IllegalArgumentException
	 *             Exception thrown if string is odd sized, if it has non hex
	 *             character or if it's null.
	 */
	public static byte[] hexToByte(String keyText) {

		if (keyText == null) {
			throw new IllegalArgumentException("Null value passed as string.");
		}

		int len = keyText.length();

		if (len % 2 != 0) {
			throw new IllegalArgumentException(
					"Given string for hexToByte conversion has odd size.");
		}

		byte[] bytes = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {

			if (!isHex(keyText.charAt(i))) {
				throw new IllegalArgumentException(
						"String contains invalid characters");
			}

			int value1 = Character.digit(keyText.charAt(i), 16) << 4;
			int value2 = Character.digit(keyText.charAt(i + 1), 16);

			bytes[i / 2] = (byte) (value1 + value2);
		}

		return bytes;
	}

	/**
	 * Converts a given byte array to a String of hexadecimal values.
	 * 
	 * @param byteArray
	 *            Given byte array.
	 * @return String of hexadecimal values.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed as an array.
	 */
	public static String byteToHex(byte[] byteArray) {

		if (byteArray == null) {
			throw new IllegalArgumentException("Null value passed as array.");
		}

		StringBuilder hexString = new StringBuilder();

		for (int i = 0; i < byteArray.length; i++) {
			int bits = byteArray[i] & 0xFF;

			char hexChar1 = hexChars[bits >>> 4];
			char hexChar2 = hexChars[bits & 0x0F];

			hexString.append(hexChar1);
			hexString.append(hexChar2);
		}

		return hexString.toString();
	}

	/**
	 * Check if given character c is hexadecimal.
	 * 
	 * @param c
	 *            given characetr.
	 * @return True if hexadecimal, otherwise false.
	 */
	private static boolean isHex(char c) {

		for (char hexChar : hexChars) {
			if (Character.toLowerCase(c) == hexChar)
				return true;
		}

		return false;
	}

	/**
	 * Splits string so that it supports escaped \" and treats input inside as a
	 * single token.
	 * 
	 * @param input
	 *            Given user input.
	 * @return List of String elements.
	 */
	public static List<String> split(String input) {
		List<String> splitString = new ArrayList<>();
		char[] cInput = input.trim().toCharArray();
		int currentIndex = 0;

		boolean escapeState = false;
		StringBuilder token = new StringBuilder();

		while (currentIndex < cInput.length) {

			// Escape sequence starts
			if (cInput[currentIndex] == '\"' && !escapeState) {
				escapeState = true;
				currentIndex++;
				continue;
			}

			// Escape sequence ends - check if it ended correctly
			if (cInput[currentIndex] == '\"' && escapeState) {
				escapeState = false;
				currentIndex++;

				if (currentIndex >= cInput.length
						|| Character.isWhitespace(cInput[currentIndex])) {
					splitString.add(token.toString());
					token.setLength(0);
					continue;
				}

				return null;
			}
			
			// \\ -> \
			if (currentIndex + 1 < cInput.length && currentIndex >= 1
					&& cInput[currentIndex - 1] == '\\'
					&& cInput[currentIndex] == '\\') {
				
				currentIndex++;
				continue;
			}
			
			// Append character to string builder
			if (escapeState || !escapeState
					&& !Character.isWhitespace(cInput[currentIndex])) {
				token.append(cInput[currentIndex]);
				currentIndex++;
				continue;
			}

			if (!escapeState && Character.isWhitespace(cInput[currentIndex])) {
				currentIndex++;

				if (token.length() == 0) {
					continue;
				}

				splitString.add(token.toString());
				token.setLength(0);
				continue;
			}
		}

		// String ended without escaping
		if (escapeState) {
			return null;
		}

		if (token.length() > 0) {
			splitString.add(token.toString());
			token.setLength(0);
		}

		return splitString;
	}
}
