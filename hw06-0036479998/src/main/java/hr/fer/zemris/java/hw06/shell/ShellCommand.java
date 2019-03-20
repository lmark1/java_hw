package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Every command class used by MyShell has to implement this interface. 
 * It has basic functionalities needed by commands.
 * 
 * @author Lovro Marković
 *
 */
public interface ShellCommand {

	/**
	 * Executes a certain command.
	 * 
	 * @param env Defines the command environment.
	 * @param arguments Everything user entered after the command.
	 * @return New state of ShellStatus.
	 */
	public ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns the name of the command.
	 * 
	 * @return Name of the command.
	 */
	public String getCommandName();
	
	/**
	 * Returns usage instructions of the command.
	 * 
	 * @return List of usage instructions for the command.
	 */
	public List<String> getCommandDescription();
}
