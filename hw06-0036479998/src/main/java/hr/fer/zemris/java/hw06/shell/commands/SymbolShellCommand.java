package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * This class implements a command for MyShell program. Command can check
 * current symbol values and set new ones for PROMPTSYMBOL, MORELINESYMBOL and
 * MULTILINESYMBOL.
 * 
 * @author Lovro Marković
 *
 */
public class SymbolShellCommand implements ShellCommand {

	/**
	 * Name of the 'symbol' command.
	 */
	public static final String name = "symbol";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] argList = arguments.trim().split("\\s+");

		// Check if keyword is valid
		Character currentChar = null;
		if (argList.length > 0) {
			currentChar = getCurrentValue(env, argList[0]);

			if (currentChar == null) {
				env.writeln("Unsupported symbol keyword passed");
				return ShellStatus.CONTINUE;
			}
		}

		// Value getter
		if (argList.length == 1) {
			env.writeln(String.format("Symbol for %s is '%c'", argList[0],
					currentChar));

		// Value setter
		} else if (argList.length == 2) {

			if (argList[1].length() != 1) {
				env.writeln("Character value expected as a second argument");
				return ShellStatus.CONTINUE;
			}
			
			Character oldValue = getCurrentValue(env, argList[0]);
			Character newValue = argList[1].charAt(0);
			setValue(env, argList[0], newValue);
			env.writeln(String.format("Symbol for %s changed from '%c' to '%c'",
					argList[0], oldValue , newValue));
		
		} else {
			env.writeln("Too much arguments given.");
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns the curent value for the specified keyword input;
	 * 
	 * @param env
	 *            Environmnet reference of the shell.
	 * @param input
	 *            User keyword input.
	 * @return Character corresponding to that keyword.
	 */
	private Character getCurrentValue(Environment env, String input) {
		Character charSymbol = null;

		switch (input) {
		case "PROMPT":
			charSymbol = env.getPromptSymbol();
			break;

		case "MORELINE":
			charSymbol = env.getMultilineSymbol();
			break;

		case "MULTILINE":
			charSymbol = env.getMultilineSymbol();
			break;
		}

		return charSymbol;
	}

	/**
	 * Sets the value of the given keyword.
	 * 
	 * @param env
	 *            Environment reference of the shell.
	 * @param input
	 *            User keyword input;
	 * @param newChar
	 *            New value of of the given keyword.
	 */
	private void setValue(Environment env, String input, Character newChar) {

		switch (input) {
		case "PROMPT":
			env.setPromptSymbol(newChar);
			break;

		case "MORELINE":
			env.setMorelinesSymbol(newChar);
			break;

		case "MULTILINE":
			env.setMultilineSymbol(newChar);
			break;
		}
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Command used for checking current values for "
				+ "PROMPTSYMBOL, MORELINESYMBOL, MULTILINESYMBOL.");
		description
				.add("If one arguments is passed (keyword: PROMPT, MORELINES, MULTILINE)"
						+ " it will return it's value.");
		description
				.add("If two arguments are passed first is expected to be a keyword, "
						+ "and second one should be a new value for that keyword.");

		return Collections.unmodifiableList(description);
	}

}
