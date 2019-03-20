package hr.fer.zemris.java.hw11.jnotepadpp;

/**
 * Interface used for passing functions that operate on strings.
 * 
 * @author Lovro Marković
 *
 */
public interface StringOperation {

	/**
	 * @param s
	 *            Passed string.
	 * @return Return string after performing an operation on the original.
	 */
	String apply(String s);
}

