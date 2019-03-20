package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * This class represents a complex number.
 * 
 * @author Lovro Marković
 *
 */
public class Complex {

	/**
	 * Complex number representing a zero.
	 */
	public static final Complex ZERO = new Complex(0, 0);

	/**
	 * Complex number representing one.
	 */
	public static final Complex ONE = new Complex(1, 0);

	/**
	 * Complex number representing negative one.
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/**
	 * Complex number representing an imaginary number.
	 */
	public static final Complex IM = new Complex(0, 1);

	/**
	 * Complex number representing a negative imaginary number.
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Real part of the complex number.
	 */
	private final double re;

	/**
	 * Imaginary part of the complex number.
	 */
	private final double im;

	/**
	 * Default complex number contructor. Initializes real and imaginary values
	 * on zero.
	 */
	public Complex() {
		im = 0;
		re = 0;
	}

	/**
	 * Complex number constructor. Sets class variables to given values.
	 * 
	 * @param re
	 *            Real part of complex number.
	 * @param im
	 *            Imaginary part of the complex number.
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates module of the complex number.
	 * 
	 * @return Module of the complex number.
	 */
	public double module() {
		return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
	}

	/**
	 * Multiply current complex number with the given one.
	 * 
	 * @param c
	 *            Given complex number.
	 * @return Multiplied complex number.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null reference passed as complex number.
	 */
	public Complex multiply(Complex c) {

		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		double newReal = (re * c.re) - (im * c.im);
		double newImag = (re * c.im) + (im * c.re);

		return new Complex(newReal, newImag);
	}

	/**
	 * Divides current complex number with the given complex number.
	 * 
	 * @param c
	 *            Given complex number.
	 * @return Result of division as a complex number.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null reference is passed as a complex
	 *             number, or if division by zero occurs.
	 */
	public Complex divide(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		if (c.re == 0 && c.im == 0) {
			throw new IllegalArgumentException("Division by zero.");
		}

		double a = re;
		double b = im;
		double x = c.re;
		double y = c.im;

		double newReal = (a * x + b * y) / (Math.pow(x, 2) + Math.pow(y, 2));
		double newImag = (b * x - a * y) / (Math.pow(x, 2) + Math.pow(y, 2));

		return new Complex(newReal, newImag);
	}

	/**
	 * Adds given complex number to the current one.
	 * 
	 * @param c
	 *            Given complex number.
	 * @return Result of addition.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null reference is passed as a complex
	 *             number.
	 */
	public Complex add(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		return new Complex(re + c.re, im + c.im);
	}

	/**
	 * Subtracts given complex number with the current one.
	 * 
	 * @param c
	 *            Given complex number.
	 * @return Result of subtraction.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null reference is passed as a complex
	 *             number.
	 */
	public Complex sub(Complex c) {
		if (c == null) {
			throw new IllegalArgumentException("Null reference given");
		}

		return new Complex(re - c.re, im - c.im);
	}

	/**
	 * Negate current complex number.
	 * 
	 * @return Negative value of current complex number.
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Calculates n-th power of the complex number.
	 * 
	 * @param n
	 *            Given power.
	 * @return N-th power of the current complex number.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given power is negative.
	 */
	public Complex power(int n) {

		if (n < 0) {
			throw new IllegalArgumentException("Power can't be negative");
		}
		
		double mag = Math.pow(module(), n);
		double angle = Math.atan2(im, re);

		if (angle < 0) {
			angle = 2 * Math.PI + angle;
		}
		
		angle = n * angle;
		return new Complex(mag * Math.cos(angle), mag * Math.sin(angle));
	}

	/**
	 * Returns list of n-th roots of complex number.
	 * 
	 * @param n
	 *            Root.
	 * @return List of N-th roots of the complex number.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given root is negative or zero.
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException(
					"Power can't be negative or zero.");
		}

		double mag = Math.pow(module(), (double) 1 / n);
		double angle = Math.atan2(im, re);

		if (angle < 0) {
			angle = 2 * Math.PI + angle;
		}
		angle /= n;
		
		List<Complex> cList = new ArrayList<>();	
		for (int i = 0; i < n; i++) {
			double newAngle = angle + 2 * Math.PI * i / n;
			cList.add(new Complex(mag * Math.cos(newAngle), mag * Math.sin(newAngle)));
		}
		
		return cList;
	}

	@Override
	public String toString() {
		
		if (im < 0 && re != 0) {
			return String.format(Locale.ROOT, "%.6f - %.6fi", re, -1 * im);
		} 
		
		else if (im > 0 && re != 0) {
			return String.format(Locale.ROOT, "%.6f + %.6fi", re, im);
		} 
		
		else if (re != 0) {
			return String.format(Locale.ROOT, "%.6f", re);
		} 
		
		else {
			return String.format(Locale.ROOT, "%.6fi", im);
		}
	}

}
