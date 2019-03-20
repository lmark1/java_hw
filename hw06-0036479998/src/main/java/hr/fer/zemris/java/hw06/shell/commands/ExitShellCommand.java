package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Class implements a command for MyShell used for exiting the shell.
 * 
 * @author LovroMarković
 *
 */
public class ExitShellCommand implements ShellCommand{

	/**
	 * Name of the command.
	 */
	public static final String name = "exit";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		
		if (!arguments.isEmpty()) {
			env.writeln("'exit' does not accept any argument");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("Command used for exiting MyShell");
		description.add("Does not need any additional arguments");
		
		return Collections.unmodifiableList(description);
	}

}
