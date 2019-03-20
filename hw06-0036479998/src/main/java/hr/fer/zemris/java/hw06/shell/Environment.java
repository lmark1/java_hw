package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * This interface will be passed to every defined command. It defines basic
 * functionalities of commands.
 * 
 * @author Lovro Marković
 *
 */
public interface Environment {

	/**
	 * Method used for reading user input.
	 * 
	 * @return String containing user input.
	 * @throws ShellIOException
	 *             Exception thrown if user input can't be read.
	 */
	public String readLine() throws ShellIOException;

	/**
	 * Method used for writing to user.
	 * 
	 * @param text
	 *            String that will be written to the user.
	 * @throws ShellIOException
	 *             Exception thrown if given String can't be written to user.
	 */
	public void write(String text) throws ShellIOException;

	/**
	 * Method used for writing lines to the user.
	 * 
	 * @param text
	 *            String that will be written to the user.
	 * @throws ShellIOException
	 *             Exception thrown if the given String can't be written to
	 *             user.
	 */
	public void writeln(String text) throws ShellIOException;

	/**
	 * Generates an unmodifiable command map so that the client can't delete
	 * commands by clearing the map.
	 * 
	 * @return Returns Map of commands.
	 */
	public SortedMap<String, ShellCommand> commands();

	/**
	 * Returns the current MULTILINE symbol.
	 * 
	 * @return MULTILINE symobol.
	 */
	public Character getMultilineSymbol();

	/**
	 * Sets a new MULTILINE symbol.
	 * 
	 * @param symbol New MULTILINE symbol.
	 */
	public void setMultilineSymbol(Character symbol);

	/**
	 * Returns the current prompt symbol.
	 * 
	 * @return Current prompt symbol.
	 */
	public Character getPromptSymbol();

	/**
	 * Sets a new prompt symbol.
	 * 
	 * @param symbol New prompt symbol.
	 */
	public void setPromptSymbol(Character symbol);

	/**
	 * Returns the current MORELINES symbol.
	 * 
	 * @return Current MORELINES symbol.
	 */
	public Character getMorelinesSymbol();

	/**
	 * Sets a new MORELINES symbol.
	 * 
	 * @param symbol New MORELINES symbol.
	 */
	public void setMorelinesSymbol(Character symbol);
}
