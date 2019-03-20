package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Class describing String element.
 * 
 * @author Lovro Marković
 *
 */
public class ElementString extends Element {

	/**
	 * Name of element type string.
	 */
	private String name;

	/**
	 * Constructor for the {@link ElementString} objects. Initializes name of
	 * the element.
	 * 
	 * @param name
	 *            Name of the element, type String.
	 */
	public ElementString(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the String element.
	 * 
	 * @return Name of the String element.
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return Name of element type String.
	 */
	@Override
	public String asText() {
		return "\"" + name.replace("\\", "\\\\").replace("\n", "\\n")
				.replace("\r", "\\r").replace("\t", "\\t")
				.replace("\"", "\\\"") + "\"";
	}
}
