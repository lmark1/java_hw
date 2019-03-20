package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class describes an instance of an element - integer constant.
 * 
 * @author Lovro Marković
 *
 */
public class ElementConstantInteger extends Element{

	/**
	 * Value of the int constant.
	 */
	private int value;
	
	/**
	 * Constructor for {@link ElementConstantInteger} objects. Initializes value variable.
	 * 
	 * @param value Value of the element type int.
	 */
	public ElementConstantInteger(int value) {
		this.value = value;
	}
	/**
	 * Returns the value of the instanced int constant.
	 * 
	 * @return Value of int constant.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @return Value of Integer constant as String.
	 */
	@Override
	public String asText() {
		return Integer.toString(value);
	}
}
