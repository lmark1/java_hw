package hr.fer.zemris.java.custom.scripting.nodes;

/**
 * Node representing a piece of textual data.
 * 
 * @author Lovro Marković
 *
 */
public class TextNode extends Node {

	/**
	 * Text propery of the TextNode instance.
	 */
	private String text;

	/**
	 * Constructor of objects type TextNode. Initializes string variable.
	 * 
	 * @param text
	 *            Variable type string.
	 */
	public TextNode(String text) {
		this.text = text;
	}

	/**
	 * Returns the stored text as String.
	 * 
	 * @return Text as String.
	 */
	public String getText() {
		return text;
	}
}
