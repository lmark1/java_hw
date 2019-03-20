package hr.fer.zemris.math;

/**
 * This class represents a polynomial of complex numbers : f(z) = (z - z1) * (z
 * - z2) * ... * (z - zn)
 * 
 * @author Lovro Marković
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Roots of polynomial.
	 */
	private final Complex[] roots;

	/**
	 * Consturctor initializing polynomial root.
	 * 
	 * @param roots
	 *            Roots of polynomial.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null value is passed as argument.
	 */
	public ComplexRootedPolynomial(Complex... roots) {

		if (roots == null) {
			throw new IllegalArgumentException("Null value passed as argument");
		}

		this.roots = new Complex[roots.length];

		for (int i = 0; i < roots.length; i++) {
			Complex newComplex = new Complex();
			this.roots[i] = newComplex.add(roots[i]);
		}
	}

	/**
	 * Computes polynomial value at the given point z.
	 * 
	 * @param z
	 *            Given complex point.
	 * @return Polynomial value at point z.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ONE;

		for (Complex root : roots) {
			result = result.multiply(z.sub(root));
		}

		return result;
	}

	/**
	 * Convert this polynomial representation to {@link ComplexPolynomial} type.
	 * 
	 * @return Complex Polynomial representation.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(
				new Complex[] { Complex.ONE });

		for (int i = 0; i < roots.length; i++) {
			result = result.multiply(new ComplexPolynomial(
					new Complex[] { roots[i].negate(), Complex.ONE }));
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder polyString = new StringBuilder();

		polyString.append("f(z) = ");
		for (int i = 0; i < roots.length; i++) {
			polyString.append("(z - " + roots[i].toString() + ")");

			if (i + 1 != roots.length) {
				polyString.append("*");
			}
		}

		return polyString.toString();
	}

	/**
	 * Find index that of the root that is within the given treshold.
	 * 
	 * @param z
	 *            Given complex number.
	 * @param treshold
	 *            Given treshold.
	 * @return If given complex number exists in the roots return inex,
	 *         otherwise -1.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		for (int i = 0; i < roots.length; i++) {
			double distance = z.sub(roots[i]).module();
			if (distance <= treshold)
				return i;
		}

		return -1;
	}
}
