package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class describing function of the element.
 * 
 * @author Lovro Marković
 *
 */
public class ElementFunction extends Element{

	/**
	 * Name of element function.
	 */
	private String name;
	
	/**
	 * Constructor for the {@link ElementFunction} objects. Inisializes name of the elemen.
	 * 
	 * @param name Name of the element, type String.
	 */
	public ElementFunction(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the name of the element function.
	 * 
	 * @return Name of the element function.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return Name of element function.
	 */
	@Override
	public String asText() {
		return  "@" + name;
	}
}
