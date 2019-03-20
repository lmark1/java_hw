package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class describes an instance of an element - double constant.
 * 
 * @author Lovro Marković
 *
 */
public class ElementConstantDouble extends Element{
	
	/**
	 * Value of double constant.
	 */
	private double value;
	
	/**
	 * Constructor for the {@link ElementConstantDouble}. Initializes value of the element.
	 *  
	 * @param value Value of the element, type Double.
	 */
	public ElementConstantDouble(double value) { 
		this.value = value;
	}
	
	/**
	 * Returns value of current double constant.
	 * 
	 * @return Double constant value.
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return Value of double constant as String.
	 */
	@Override
	public String asText() {
		return Double.toString(value);
	}
}
