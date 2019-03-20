package hr.fer.zemris.java.hw02;

import java.lang.Math;

/**
 * Class provides support for working with complex numbers. Each instance of
 * this class represents an unmodifiable complex number. Each method that
 * performs some sort of operation returns a new instance which represents a
 * modified number.
 * <p>
 * Example of constructors and static factory functions:
 * </p>
 * 
 * <pre>
 * ComplexNumber c1 = new ComplexNumber(real, imag);
 * ComplexNumber c2 = ComplexNumer.fromMagnitudeAndAngle(magnitude, angle);
 * ComplexNumber c3 = ComplexNumber.fromImaginary(imag);
 * </pre>
 * 
 * <p>
 * Example of operations:
 * </p>
 * 
 * <pre>
 * c1 = c1.add(c2);
 * c1 = c2.mul(c1);
 * c1 = c1.root(3);
 * </pre>
 * 
 * 
 * 
 * @author Lovro Marković
 *
 */
public class ComplexNumber {

	/**
	 * Unmodifiable real part of the complex number.
	 */
	private final double realPart;

	/**
	 * Unmodifiable imaginary part complex number.
	 */
	private final double imaginaryPart;

	/**
	 * Constructor for class {@link ComplexNumber}. Initializes real and
	 * imaginary values.
	 * 
	 * @param realPart
	 *            Real part of the complex number.
	 * @param imaginaryPart
	 *            Imaginary part of the complex number.
	 */
	public ComplexNumber(double realPart, double imaginaryPart) {
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}

	/**
	 * Static factory method, creates a {@link ComplexNumber} from real input.
	 * 
	 * @param real
	 *            Real part of the complex number.
	 * @return {@link ComplexNumber} containing only real part.
	 */
	public static ComplexNumber fromReal(double real) {
		ComplexNumber cNum = new ComplexNumber(real, 0);
		return cNum;
	}

	/**
	 * Static factory method, creates a {@link ComplexNumber} from imaginary
	 * input.
	 * 
	 * @param imaginary
	 *            Real part of the complex number.
	 * @return {@link ComplexNumber} containing only real part.
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		ComplexNumber cNum = new ComplexNumber(0, imaginary);
		return cNum;
	}

	/**
	 * Static factory method, creates a {@link ComplexNumber} from magnitude and
	 * angle.
	 * 
	 * @param magnitude
	 *            Magnitude of the complex number.
	 * @param angle
	 *            Angle of the complex number.
	 * @return {@link ComplexNumber} with real and imaginary parts according to
	 *         given angle and magnitude.
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		double real = magnitude * Math.cos(angle);
		double imaginary = magnitude * Math.sin(angle);
		ComplexNumber cNum = new ComplexNumber(real, imaginary);
		return cNum;
	}

	/**
	 * Parse complex number from given string. (accepts strings such as: "3.51",
	 * "-3.17", "-2.71i", "i", "1", "-2.71-3.15i")
	 * 
	 * @param s
	 *            String that contains some form of a complex number.
	 * @return {@link ComplexNumber} object containing real and / or imaginary
	 *         data from the given insput.
	 * @throws IllegalArgumentException
	 *             If the complex number is not valid.
	 */
	public static ComplexNumber parse(String s) {
		double real = 0, imag = 0;

		String newS = removeSpacesAndFirstPlus(s);
		String[] splitString = (newS + ' ').split("");
		StringBuilder makeNumber = new StringBuilder();
		int numberChecker = 0;

		for (String currentElement : splitString) {

			if (makeNumber.length() != 0
					&& (currentElement.equals("+") || currentElement.equals("-") || currentElement.equals(" "))) {

				ComplexNumber newC = parseRealOrImag(makeNumber.toString());
				numberChecker++;

				// Parse up to 2 numbers
				if (newC == null || numberChecker > 2) {
					throw new IllegalArgumentException("Invalid complex number format.");
				}

				real += newC.getReal();
				imag += newC.getImaginary();
				makeNumber.setLength(0);

				// If input is eg. 2+2 or 2i+2i
				if (numberChecker == 2 && (real == 0 || imag == 0)) {
					throw new IllegalArgumentException("Invalid complex number format.");
				}
			}
			makeNumber.append(currentElement);
		}

		ComplexNumber newC = new ComplexNumber(real, imag);
		return newC;
	}

	/**
	 * Parse real or imaginary number.
	 * 
	 * @param strCheck
	 *            String that will be parsed to a complex number.
	 * @return ComplexNumber parsed from the string.
	 */
	private static ComplexNumber parseRealOrImag(String strCheck) {

		boolean isImag = false;
		strCheck = removeSpacesAndFirstPlus(strCheck);
		// Case of i
		if ((strCheck.contains("i") && strCheck.length() == 1) ||
		// Case of -i
				(strCheck.contains("i") && strCheck.length() == 2 && strCheck.contains("-"))) {
			strCheck = strCheck.replace('i', '1');
			isImag = true;

		} else if (strCheck.contains("i")) {
			strCheck = strCheck.replace('i', '\0');
			isImag = true;
		}

		// Try to parse the number and reset buffer
		double imag = 0;
		double real = 0;
		try {
			if (isImag) {
				imag = Double.parseDouble(strCheck);
			} else {
				real = Double.parseDouble(strCheck);
			}

		} catch (NumberFormatException ex) {
			return null;
		}

		ComplexNumber newC = new ComplexNumber(real, imag);
		return newC;
	}

	/**
	 * Removes all spaces and first plus sign from the string.
	 * 
	 * @param s
	 *            Input string.
	 * @return Formatted string.
	 */
	private static String removeSpacesAndFirstPlus(String s) {
		s = s.replaceAll("\\s+", "");
		StringBuilder sNew = new StringBuilder(s);
		if (sNew.charAt(0) == '+') {
			sNew.setCharAt(0, ' ');
		}
		String newS = new String(sNew);
		newS = newS.replaceAll("\\s+", "");
		return newS;
	}

	/**
	 * Gets the real part of the instanced complex number.
	 * 
	 * @return Real part of the imaginary number.
	 */
	public double getReal() {
		return this.realPart;
	}

	/**
	 * Gets the imaginary part of the complex number.
	 * 
	 * @return Real part of the imaginary number.
	 */
	public double getImaginary() {
		return this.imaginaryPart;
	}

	/**
	 * Calculates magnitude of the complex number.
	 * 
	 * @return Magnitude of the complex number.
	 */
	public double getMagnitude() {
		double mag = Math.sqrt(Math.pow(this.realPart, 2) + Math.pow(this.imaginaryPart, 2));
		return mag;
	}

	/**
	 * Calculates angle of the complex number.
	 * 
	 * @return Angle of the complex number.
	 */
	public double getAngle() {
		double angle = Math.atan2(this.imaginaryPart, this.realPart);

		if (angle < 0) {
			return (2 * Math.PI + angle);
		} else {
			return angle;
		}
	}

	/**
	 * Adds a complex number to the instanced one.
	 * 
	 * @param c
	 *            Complex number that will be added.
	 * @throws IllegalArgumentException
	 *             Exception thrown when given {@link ComplexNumber} is null.
	 * @return New complex number as a sum of two given.
	 */
	public ComplexNumber add(ComplexNumber c) {

		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		double newReal = this.realPart + c.getReal();
		double newImag = this.imaginaryPart + c.getImaginary();

		ComplexNumber newC = new ComplexNumber(newReal, newImag);
		return newC;
	}

	/**
	 * Subtracts this complex number with the given one.
	 * 
	 * @param c
	 *            Complex number that will substrat this one.
	 * @return Substracted complex number.
	 */
	public ComplexNumber sub(ComplexNumber c) {

		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		double newReal = this.realPart - c.getReal();
		double newImag = this.imaginaryPart - c.getImaginary();

		ComplexNumber newC = new ComplexNumber(newReal, newImag);
		return newC;
	}

	/**
	 * Multiplies complex number with the given one.
	 * 
	 * @param c
	 *            Complex number which will multiply this one.
	 * @throws IllegalArgumentException
	 *             Exception thrown when given {@link ComplexNumber} is null.
	 * 
	 * @return Multiplied complex number.
	 */
	public ComplexNumber mul(ComplexNumber c) {

		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		double newReal = (this.realPart * c.getReal()) - (this.imaginaryPart * c.getImaginary());
		double newImag = (this.realPart * c.getImaginary()) + (this.imaginaryPart * c.getReal());

		ComplexNumber newC = new ComplexNumber(newReal, newImag);
		return newC;
	}

	/**
	 * Divides complex number with the given one.
	 * 
	 * @param c
	 *            Complex number that divides this one.
	 * @throws IllegalArgumentException
	 *             Exception thrown if dividing by zero or when null referenc is
	 *             given.
	 * @return Divided complex number.
	 */
	public ComplexNumber div(ComplexNumber c) {

		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		if (c.getReal() == 0 && c.getImaginary() == 0) {
			throw new IllegalArgumentException("Division by zero.");
		}

		double a = this.realPart;
		double b = this.imaginaryPart;
		double x = c.getReal();
		double y = c.getImaginary();

		double newReal = (a * x + b * y) / (Math.pow(x, 2) + Math.pow(y, 2));
		double newImag = (b * x - a * y) / (Math.pow(x, 2) + Math.pow(y, 2));

		ComplexNumber newC = new ComplexNumber(newReal, newImag);
		return newC;
	}

	/**
	 * Raises this complex number to the n-th power. n>=0.
	 * 
	 * @param n
	 *            Which power the complex number will be raised to.
	 * @return Complex number to the n-th power.
	 * @throws IllegalArgumentException
	 *             Exception thrown if n is negative.
	 */
	public ComplexNumber power(int n) throws IllegalArgumentException {

		if (n < 0) {
			throw new IllegalArgumentException("Power can't be negative");
		}

		double mag = Math.pow(this.getMagnitude(), n);
		double angle = n * this.getAngle();

		ComplexNumber newC = fromMagnitudeAndAngle(mag, angle);
		return newC;
	}

	/**
	 * Calculates the n-th root of the complex number.
	 * 
	 * @param n
	 *            Which root will be calculated.
	 * @return All N-th roots of the complex number.
	 * @throws IllegalArgumentException
	 *             Exception thrown if n is negative or 0.
	 */
	public ComplexNumber[] root(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("Power can't be negative or zero.");
		}

		double mag = Math.pow(this.getMagnitude(), (double) 1 / n);
		ComplexNumber[] cArray = new ComplexNumber[n];
		double angle = this.getAngle() / n;

		for (int i = 0; i < n; i++) {
			cArray[i] = fromMagnitudeAndAngle(mag, (angle + 2 * Math.PI * i / n));
		}

		return cArray;
	}

	/**
	 * Converts current complex number to string.
	 */
	@Override
	public String toString() {
		StringBuilder complexStr = new StringBuilder();
		String imString = String.format("%.3f", this.imaginaryPart);
		String realString = String.format("%.3f", this.realPart);

		if (this.realPart != 0) {
			complexStr.append(realString);
		}

		if (complexStr.length() != 0 && this.imaginaryPart > 0) {
			complexStr.append("+" + imString + "i");

		} else if (this.imaginaryPart < 0) {
			complexStr.append(imString + "i");

		} else if (complexStr.length() == 0) {
			complexStr.append("0");
		}

		return complexStr.toString();
	}

}
