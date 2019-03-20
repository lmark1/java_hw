package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * This class implement help command. If started with no arguments, it lists
 * names of all supported commands. If started with single argument, it prints
 * name and the description of selected command (or print appropriate error
 * message if no such command exists).
 * 
 * @author Lovro Marković
 *
 */
public class HelpShellCommand implements ShellCommand {

	/**
	 * Name of 'help' command.
	 */
	public static final String name = "help";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argList = Util.split(arguments);
		Map<String, ShellCommand> map = env.commands();
		
		if (argList.isEmpty()) {
			for (Map.Entry<String, ShellCommand> entry: map.entrySet()) {
				env.writeln(entry.getValue().getCommandName());
			}
			
			return ShellStatus.CONTINUE;
		
		} else if (argList.size() == 1) {
			if (!map.containsKey(argList.get(0))){
				env.writeln("Given keyword not found");
				return ShellStatus.CONTINUE;
			}
			
			ShellCommand comm = map.get(argList.get(0));
			List<String> desc = comm.getCommandDescription();
			
			env.writeln(comm.getCommandName());
			for (String string : desc) {
				env.writeln(string);
			}
		
		} else {
			env.writeln("Too many arguments given");
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		
		description.add("If started with no arguments, it lists names of all supported commands.");
		description.add("If started with single argument, it prints name and the description of selected command");
		
		return Collections.unmodifiableList(description);
	}

}
