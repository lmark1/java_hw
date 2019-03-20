package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class describing element operator.
 * 
 * @author Lovro Marković
 *
 */
public class ElementOperator extends Element{

	/**
	 * Element operator type String.
	 */
	private String symbol;
	
	/**
	 * Constructor for the {@link ElementOperator} objects. Initializes variable symbol as the operator type.
	 * 
	 * @param symbol Operator, type String.
	 */
	public ElementOperator(String symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Returns the symbol.
	 * 
	 * @return Operator symbol type string.
	 */
	public String getName() {
		return symbol;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return Operator symbol as String.
	 */
	@Override
	public String asText() {
		return symbol;
	}
}
