package hr.fer.zemris.bf.qmc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.utils.Util;

/**
 * Instances of this class represent specification of (possibly incomplete)
 * products. If the boolean function is defined with variables A,B,C,D then the
 * product ABC'D will be represented with the mask 1101. If variable is not
 * represented in the product, it will be marked with '2'. e.g. B' is
 * represented with 2022.
 * 
 * @author Lovro Marković
 *
 */
public class Mask {

	/**
	 * Mask values represented by an array of bytes.
	 */
	private byte[] values;

	/**
	 * Set of mintermn indexes.
	 */
	private Set<Integer> indexes;

	/**
	 * Defines if the given product is normal or "don't care" type.
	 */
	private boolean dontCare;

	/**
	 * Hash code of mask value array.
	 */
	private int maskHash;

	/**
	 * Is product used in combination with another product to generate a third
	 * product.
	 */
	private boolean combined = false;

	/**
	 * Constructor for Mask object. Initiliazes all object variables using given
	 * input information.
	 * 
	 * @param index
	 *            Given index of mask.
	 * @param numberOfVariables
	 *            Number of variables.
	 * @param dontCare
	 *            Is given mask dont care type.
	 * @throws IllegalArgumentException
	 *             Exception thrown if given number of variables isn't atleast
	 *             1.
	 */
	public Mask(int index, int numberOfVariables, boolean dontCare) {

		if (numberOfVariables < 1) {
			throw new IllegalArgumentException(
					"Number of variables has to be atleast 1.");
		}

		this.dontCare = dontCare;
		values = Util.indexToByteArray(index, numberOfVariables);
		maskHash = values.hashCode();

		indexes = new TreeSet<>();
		indexes.add(index);
		indexes = Collections.unmodifiableSet(indexes);
	}

	/**
	 * Constructor for Mask object. Initializes all object variables.
	 * 
	 * @param values
	 *            Array of bytes contataining mask values.
	 * @param indexes
	 *            Indexes of minterms.
	 * @param dontCare
	 *            Is given product dont care type.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null value passed as argument.
	 */
	public Mask(byte[] values, Set<Integer> indexes, boolean dontCare) {

		if (values == null || indexes == null) {
			throw new IllegalArgumentException(
					"'null' value passed as argument.");
		}

		this.dontCare = dontCare;
		this.indexes = Collections.unmodifiableSet(new TreeSet<>(indexes));

		this.values = new byte[values.length];
		for (int i = 0; i < values.length; i++) {
			this.values[i] = values[i];
		}

		this.maskHash = Arrays.hashCode(values);
	}

	@Override
	public int hashCode() {
		return maskHash;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Mask)) {
			return false;
		}

		Mask objMask = (Mask) obj;
		if (objMask.hashCode() == maskHash) {
			return true;
		}

		return Arrays.equals(objMask.values, values);
	}

	@Override
	public String toString() {
		StringBuilder newString = new StringBuilder();

		for (byte b : values) {
			if (b == 2) {
				newString.append("-");
			} else {
				newString.append(b);
			}
		}
		newString.append(dontCare ? " D" : " .");
		newString.append(combined ? " * " : "   ");
		newString.append(indexes.toString());

		return newString.toString();
	}

	/**
	 * Getter for combined class variable.
	 * 
	 * @return Combined class variable value.
	 */
	public boolean isCombined() {
		return combined;
	}

	/**
	 * Setter for the combined class variable.
	 * 
	 * @param combined
	 *            New combined variable value.
	 */
	public void setCombined(boolean combined) {
		this.combined = combined;
	}

	/**
	 * Getter for the 'dont care' class variable.
	 * 
	 * @return Dont care class variable value.
	 */
	public boolean isDontCare() {
		return dontCare;
	}

	/**
	 * Getter for the unmodifiable indexes variable.
	 * 
	 * @return Indexes variable value.
	 */
	public Set<Integer> getIndexes() {
		return indexes;
	}

	/**
	 * How many times does value '1' occur in mask values array.
	 * 
	 * @return Count of '1' occuernces.
	 */
	public int countOfOnes() {
		byte one = (byte) 1;
		int count = 0;

		for (byte b : values) {
			if (b == one) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Mask is combined with the given product using the theroem of
	 * simplification. If the combination is possible then the appropriate mask
	 * is returned.
	 * 
	 * @param other
	 *            Mask given to combine with this mask.
	 * @return Combined mask if combination is possible, otherwise "no result".
	 * @throws IllegalArgumentException
	 *             Exception thrown if null is passed as mask or if masks are
	 *             not the same length.s
	 */
	public Optional<Mask> combineWith(Mask other) {

		if (other == null) {
			throw new IllegalArgumentException("'null' value passed as mask.");
		}

		if (this.values.length != other.values.length) {
			throw new IllegalArgumentException("Masks are not the same length");
		}

		byte[] newValues = new byte[this.values.length];
		int differenceCount = 0;
		for (int i = 0, len = this.values.length; i < len; i++) {

			if (values[i] == 2 && !(other.values[i] == 2)
					|| !(values[i] == 2) && other.values[i] == 2) {
				return Optional.empty();
			}

			if (values[i] != other.values[i]) {
				differenceCount++;
				if (differenceCount > 1)
					return Optional.empty();

				newValues[i] = (byte) 2;
			} else
				newValues[i] = values[i];
		}

		Set<Integer> newMintermSet = new TreeSet<>();
		newMintermSet.addAll(indexes);
		newMintermSet.addAll(other.indexes);
		Mask newMask = new Mask(newValues, newMintermSet,
				dontCare && other.dontCare);

		this.setCombined(true);
		other.setCombined(true);

		return Optional.of(newMask);
	}

	/**
	 * Getter for size of the values array.
	 * 
	 * @return Size of values array.
	 */
	public int size() {
		return values.length;
	}

	/**
	 * Getter for the value at index position.
	 * 
	 * @param position
	 *            Index of the value array.
	 * @return Byte at the given index.
	 * @throws IllegalArgumentException
	 *             Exception thrown when given index is out of bounds of array.
	 */
	public byte getValueAt(int position) {

		if (position >= values.length) {
			throw new IllegalArgumentException(
					"Index must be less than " + values.length);
		}

		return values[position];
	}
}
