package hr.fer.zemris.java.custom.scripting.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * This class represents a wrapper containing some values and methods which
 * perform basic arithmetic operations.
 * 
 * @author Lovro Marković
 *
 */
public class ValueWrapper {

	/**
	 * Stored value of the ValueWrapper class instances.
	 */
	private Object value;

	/**
	 * Constructor for the ValueWrapper class. Initializes the value variable.
	 * 
	 * @param value
	 *            Initial value of the value variable.
	 */
	public ValueWrapper(Object value) {
		this.value = value;
	}

	/**
	 * Getter for the stored value.
	 * 
	 * @return Stored value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setter of the stored value.
	 * 
	 * @param value
	 *            New value of the stored value.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Adds the currently stored Object value with the given argument incValue.
	 * If either of the arguments are of type Double, result is stored as
	 * Double, else the result will be stored as Integer.
	 * 
	 * @param incValue
	 *            Value that will be added to the stored value.
	 * @throws RuntimeException
	 *             Exception thrown if stored value or given argument aren't
	 *             null or of type String, Integer or Double. Or if the given
	 *             String value can't be interpreted as an Integer or Double.
	 * 
	 */
	public void add(Object incValue) {

		List<Object> operandList = getObjectValues(incValue);
		Object value1 = operandList.get(0);
		Object value2 = operandList.get(1);

		if (value1 instanceof Integer && value2 instanceof Integer) {
			doIntegerOperation((u, t) -> u + t, value1, value2);

		} else {
			doDoubleOperation((u, t) -> u + t, value1, value2);
		}
	}

	/**
	 * Subs the currently stored Object value with the given argument incValue.
	 * If either of the arguments are of type Double, result is stored as
	 * Double, else the result will be stored as Integer.
	 * 
	 * @param decValue
	 *            Value that will be added to the stored value.
	 * @throws RuntimeException
	 *             Exception thrown if stored value or given argument aren't
	 *             null or of type String, Integer or Double. Or if the given
	 *             String value can't be interpreted as an Integer or Double.
	 * 
	 */
	public void subtract(Object decValue) {
		List<Object> operandList = getObjectValues(decValue);
		Object value1 = operandList.get(0);
		Object value2 = operandList.get(1);

		if (value1 instanceof Integer && value2 instanceof Integer) {
			doIntegerOperation((u, t) -> u - t, value1, value2);

		} else {
			doDoubleOperation((u, t) -> u - t, value1, value2);
		}
	}

	/**
	 * Multiplies the currently stored Object value with the given argument
	 * incValue. If either of the arguments are of type Double, result is stored
	 * as Double, else the result will be stored as Integer.
	 * 
	 * @param mulValue
	 *            Value that will be added to the stored value.
	 * @throws RuntimeException
	 *             Exception thrown if stored value or given argument aren't
	 *             null or of type String, Integer or Double. Or if the given
	 *             String value can't be interpreted as an Integer or Double.
	 * 
	 */
	public void multiply(Object mulValue) {
		List<Object> operandList = getObjectValues(mulValue);
		Object value1 = operandList.get(0);
		Object value2 = operandList.get(1);

		if (value1 instanceof Integer && value2 instanceof Integer) {
			doIntegerOperation((u, t) -> u * t, value1, value2);

		} else {
			doDoubleOperation((u, t) -> u * t, value1, value2);
		}
	}

	/**
	 * Divides the currently stored Object value with the given argument
	 * incValue. If either of the arguments are of type Double, result is stored
	 * as Double, else the result will be stored as Integer.
	 * 
	 * @param divValue
	 *            Value that will be added to the stored value.
	 * @throws RuntimeException
	 *             Exception thrown if stored value or given argument aren't
	 *             null or of type String, Integer or Double. Or if the given
	 *             String value can't be interpreted as an Integer or Double, or
	 *             if dividing by zero.
	 *              
	 */
	public void divide(Object divValue) {
		List<Object> operandList = getObjectValues(divValue);
		Object value1 = operandList.get(0);
		Object value2 = operandList.get(1);

		// If value2 is zero
		if (value2 instanceof Double && ((Double) value2).equals(0)
				|| value1 instanceof Integer && ((Integer) value2).equals(0)) {
			throw new RuntimeException("Division by zero.");
		}

		if (value1 instanceof Integer && value2 instanceof Integer) {
			doIntegerOperation((u, t) -> u / t, value1, value2);

		} else {
			doDoubleOperation((u, t) -> u / t, value1, value2);
		}
	}

	/**
	 * Compares the currently stored object with the given variable.
	 * 
	 * @param withValue
	 *            Value that the stored variable will be compared with.
	 * @return Integer less than zero if the currently stored value is smaller
	 *         than the argument, an integer greater than zero if currently
	 *         stored value is larger than the argument or an integer 0 if they
	 *         are both equal.
	 * @throws RuntimeException
	 *             Exception thrown if stored value or given argument aren't
	 *             null or of type String, Integer or Double. Or if the given
	 *             String value can't be interpreted as an Integer or Double.
	 */
	public int numCompare(Object withValue) {
		List<Object> operandList = getObjectValues(withValue);
		Object value1 = operandList.get(0);
		Object value2 = operandList.get(1);

		Double doubleValue1 = objectToDouble(value1);
		Double doubleValue2 = objectToDouble(value2);

		return doubleValue1.compareTo(doubleValue2);
	}

	/**
	 * Returns converted this.value and incValue variables to Integer or Double
	 * type. Also checks if the inital values are valid.
	 * 
	 * @param incValue
	 *            Value that will perform operation with this.value.
	 * @return List containing converted object values.
	 * @throws RuntimeException
	 *             Exception thrown if stored value or given argument aren't
	 *             null or of type String, Integer or Double. Or if the given
	 *             String value can't be interpreted as an Integer or Double.
	 */
	private List<Object> getObjectValues(Object incValue) {

		if (!isCorrectClass(incValue) || !isCorrectClass(value)) {
			throw new RuntimeException("Values are not correct types or null.");
		}

		Object value1 = objectConverter(value);
		Object value2 = objectConverter(incValue);

		if (value1 == null || value2 == null) {
			throw new RuntimeException(
					"Conversion of String types to Integer or Double failed");
		}

		List<Object> newList = new ArrayList<>();
		newList.add(value1);
		newList.add(value2);

		return newList;
	}

	/**
	 * Method performs operations on operands type Double and stores the result
	 * in this value.
	 * 
	 * @param function
	 *            Function specifiying the operation.
	 * @param value1
	 *            First operand of the function.
	 * @param value2
	 *            Second operand of the function.
	 */
	private void doDoubleOperation(BiFunction<Double, Double, Double> function,
			Object value1, Object value2) {

		Double doubleVal1 = objectToDouble(value1);
		Double doubleVal2 = objectToDouble(value2);

		this.value = function.apply(doubleVal1, doubleVal2);
	}

	/**
	 * Method performs operations on operands type Integer and stores the result
	 * in this value.
	 * 
	 * @param function
	 *            Function specifiying the operation.
	 * @param value1
	 *            First operand of the function.
	 * @param value2
	 *            Second operand of the function.
	 */
	private void doIntegerOperation(
			BiFunction<Integer, Integer, Integer> function, Object value1,
			Object value2) {

		this.value = function.apply((Integer) value1, (Integer) value2);
	}

	/**
	 * Convert object to Double value type. Assume object will be either Integer
	 * or Double.
	 * 
	 * @param value
	 *            Given object.
	 * @return Object converted to Double, if it's not either Integer or Double
	 *         return null.
	 */
	private static Double objectToDouble(Object value) {

		if (value instanceof Double) {
			return (Double) value;
		}

		if (value instanceof Integer) {
			Integer intValue = (Integer) value;
			Double doubleValue = Double.valueOf(intValue.intValue());
			return doubleValue;
		}

		return null;
	}

	/**
	 * Checks if the given object is a type of approved classes. Approved
	 * classes are Integer, Double or String. Value can also be null.s
	 * 
	 * @param value
	 *            Given object.
	 * @return True if given value is an instance of approved classes or null,
	 *         otherwise false.
	 */
	private boolean isCorrectClass(Object value) {

		return value == null || value instanceof Integer
				|| value instanceof Double || value instanceof String;
	}

	/**
	 * Converts objects according to the following rules. If value is null, than
	 * it will be treated as an Integer of value 0. If given Object is String it
	 * will be parsed int Integer, or if it contains 'E' or '.' into Double.
	 * 
	 * @param value
	 *            Value type Object.
	 * @return Appropriabe value type according to the mentioned rules, or null
	 *         if the value does not satisfy any condition.
	 */
	private static Object objectConverter(Object value) {

		if (value == null) {
			Integer newInteger = Integer.valueOf(0);
			return newInteger;
		}

		if (!(value instanceof String)) {
			return value;
		}

		String strValue = (String) value;

		// Try to parse string according to its type
		if (isDecimal(strValue)) {
			Double doubleValue;

			try {
				doubleValue = Double.parseDouble(strValue);
				return doubleValue;

			} catch (NumberFormatException e) {
				return null;
			}

		} else {
			Integer intValue;

			try {
				intValue = Integer.parseInt(strValue);
				return intValue;

			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	/**
	 * Assume that the given String contains some numers. Check if the given
	 * string can be interpreted as a decimal number. (i.e. if it has a symbol
	 * '.' or 'e'.)
	 * 
	 * @param value
	 *            Given value type String.
	 * @return True if given string is a decimal number, otherwise false.
	 */
	private static boolean isDecimal(String value) {
		boolean decimalFlag = false;

		for (int i = 0, len = value.length(); i < len; i++) {
			char newChar = value.charAt(i);

			if (newChar == 'E' || newChar == '.')
				decimalFlag = true;

			// only viable decimal format is with 'E' and '.'
			if (newChar == 'e' || newChar == ',')
				return false;
		}

		return decimalFlag;
	}
}
