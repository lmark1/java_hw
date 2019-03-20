package hr.fer.zemris.math;

import java.util.Locale;

/**
 * This class represents an unmodifiable three-component vector.
 * 
 * @author Lovro Marković
 *
 */
public class Vector3 {

	/**
	 * X component of the vector.
	 */
	private final double x;

	/**
	 * Y component of the vector.
	 */
	private final double y;

	/**
	 * Z component of the vector.
	 */
	private final double z;

	/**
	 * Default constructor. Initializes vectro components.
	 * 
	 * @param x
	 *            X vector component.
	 * @param y
	 *            Y vector component.
	 * @param z
	 *            Z vector component.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculates vecto3 norm.
	 * 
	 * @return Norm of this vector.
	 */
	public double norm() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
	}

	/**
	 * Generates normalized vector.
	 * 
	 * @return Normalized vector.
	 */
	public Vector3 normalized() {
		double norm = this.norm();
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Adds given vector to the current.
	 * 
	 * @param other
	 *            Given vector.
	 * @return Vector representing sum.
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Subtracts given vectro from current.
	 * 
	 * @param other
	 *            Given vector.
	 * @return Subtracted vector.
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Dot product of current and given vector.
	 * 
	 * @param other
	 *            Given vector.
	 * @return Dot product.
	 */
	public double dot(Vector3 other) {
		return (x * other.x + y * other.y + z * other.z);
	}

	/**
	 * Cross product of current and given vector.
	 * 
	 * @param other
	 *            Given vector.
	 * @return Cross product.
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(y * other.z - z * other.y, z * other.x - x * other.z,
				x * other.y - y * other.x);
	}

	/**
	 * Scaling current vector with factor s.
	 * 
	 * @param s
	 *            Given factor for scaling.
	 * @return Scaled vector.
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Cosinus angle between this and given vector.
	 * 
	 * @param other Given vector.
	 * @return Cosinus of angle.
	 */
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (this.norm() * other.norm());
	}
	
	/**
	 * Getter for x componenet.
	 * 
	 * @return X component.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter for the y component.
	 * 
	 * @return Y component.
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Getter for the z component.
	 * 
	 * @return Z component.
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Generate double array from current vector.
	 * 
	 * @return Double array.
	 */
	public double[] toArray() {
		double[] array = new double[3];
		array[0] = x;
		array[1] = y;
		array[2] = z;
		
		return array;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "(%.6f, %.6f, %.6f)", x, y, z);
	}
	
}
