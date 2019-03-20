package hr.fer.zemris.math;

/**
 * This class implements some vector operations.
 * 
 * @author Lovro Marković
 *
 */
public class VectorOperations {

	/**
	 * Multiply two vectors by elements.
	 * 
	 * @param v1
	 *            First vector.
	 * @param v2
	 *            Second vector.
	 * @return Multiplied vector or null if vectors are different length.
	 * @throws IllegalArgumentException
	 *             Exception thrown if the vectors are not the same length.
	 */
	public static double[] multiply(int[] v1, double[] v2) {

		if (v1.length != v2.length) {
			throw new IllegalArgumentException(
					"Vectors are different lengths.");
		}

		double[] result = new double[v1.length];
		for (int i = 0, len = v1.length; i < len; i++) {
			result[i] = v1[i] * v2[i];
		}

		return result;
	}

	/**
	 * Scalar multiplication of two vectors.
	 * 
	 * @param v1 First vector.
	 * @param v2 Second vector.
	 * @return Scalar multiplication.
	 */
	public static double scalarMultiply(double[] v1, double[] v2) {
		
		if (v1.length != v2.length) {
			throw new IllegalArgumentException(
					"Vectors are different lengths.");
		}
		
		double result = 0;
		for (int i = 0, len = v1.length; i < len; i++) {
			result += (double) v1[i] * v2[i];
		}
		
		return result;
	}
	
	/**
	 * Calculate vector norm.
	 * 
	 * @param v Vector.
	 * @return Norm.
	 */
	public static double norm(double[] v) {
		return Math.sqrt(scalarMultiply(v, v));
	}
}
