package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class describes an instance of element variable.
 * 
 * @author Lovro Marković
 *
 */
public class ElementVariable extends Element {

	/**
	 * Name of the element variable.
	 */
	private String name;
	
	/**
	 * Constructor for the {@link ElementVariable} objects. Initializes name of the elemen.
	 * 
	 * @param name Name of the element, type String.
	 */
	public ElementVariable(String name) {
		this.name = name;
	}
	
	/**
	 * Getter for instanced variable name.
	 * 
	 * @return Returns name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return Return name of the element variable.
	 */
	@Override
	public String asText() {
		return name;
	}
}
