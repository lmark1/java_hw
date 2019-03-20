package hr.fer.zemris.java.hw06.shell;

/**
 * This enumeration defines the current status of the shell.
 * 
 * @author Lovro Marković
 *
 */
public enum ShellStatus {

	/**
	 * If command other than exit is given, this Shell state will be active.
	 */
	CONTINUE,

	/**
	 * If an "exit" command is given or a {@link ShellIOException} occurs this
	 * state becomes active and shell will terminate.
	 */
	TERMINATE
}
