package hr.fer.zemris.math;

/**
 * This class representes a polynomial of complex numbers : f(z) = zn * z^n +
 * ... + z2 * z^2 + z1 * z + z0
 * 
 * @author Lovro Marković
 *
 */
public class ComplexPolynomial {

	/**
	 * Factors of the polynomial.
	 */
	private final Complex[] factors;

	/**
	 * Default constructor. Initializes class variables.
	 * 
	 * @param factors
	 *            Initial value of factors.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null value is passed as argument.
	 */
	public ComplexPolynomial(Complex... factors) {

		if (factors == null) {
			throw new IllegalArgumentException("Null value passed as argument");
		}
		this.factors = new Complex[factors.length];

		for (int i = 0; i < factors.length; i++) {
			Complex newComplex = new Complex();
			this.factors[i] = newComplex.add(factors[i]);
		}
	}

	/**
	 * Returns order of the polynomial.
	 * 
	 * @return Polynomial order.
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiply current polynomial with given one.
	 * 
	 * @param p
	 *            Given polynomial.
	 * @return Product of polynomials.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] resultFactors = new Complex[factors.length + p.factors.length
				- 1];

		for (int i = 0; i < factors.length; i++) {

			for (int j = 0; j < p.factors.length; j++) {

				if (resultFactors[i + j] == null)
					resultFactors[i + j] = new Complex();

				resultFactors[i + j] = resultFactors[i + j]
						.add(factors[i].multiply(p.factors[j]));
			}
		}

		return new ComplexPolynomial(resultFactors);
	}

	/**
	 * Calculate the first derivative of polynomial.
	 * 
	 * @return First derviative.
	 */
	public ComplexPolynomial derive() {

		if (factors.length == 0) {
			return new ComplexPolynomial(Complex.ZERO);
		}

		Complex[] newFactors = new Complex[factors.length - 1];
		for (int i = 1; i < factors.length; i++) {

			Complex newComp = new Complex();
			newFactors[i - 1] = newComp.add(factors[i]
					.multiply((Complex.ONE.multiply(new Complex(i, 0)))));

		}

		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Evaluate polynomial at the given point z.
	 * 
	 * @param z
	 *            Given point z.
	 * @return Polynomial value at z.
	 * @throws IllegalArgumentException
	 *             Exception thrown if passed argument is null.
	 */
	public Complex apply(Complex z) {

		if (z == null) {
			throw new IllegalArgumentException("Argument is null.");
		}

		Complex result = new Complex();
		for (int i = 0, n = order(); i <= n; i++) {
			result = result.add(factors[i].multiply(z.power(i)));
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder polyString = new StringBuilder();

		polyString.append("f(z) = ");
		for (int i = 0; i < factors.length; i++) {
			polyString.append("(" + factors[i] + ")");

			if (i != 0) {
				polyString.append(String.format("*z^%d", i));
			}

			if (i + 1 != factors.length) {
				polyString.append(" + ");
			}
		}

		return polyString.toString();
	}
}
