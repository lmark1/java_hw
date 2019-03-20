package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Mkdir command takes a single argument: directory name, and creates the
 * appropriate directory structure.
 * 
 * @author Lovro Marković
 *
 */
public class MkdirShellCommand implements ShellCommand{

	/**
	 * Name of the 'mkdir' command.
	 */
	public static final String name = "mkdir";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argsList = Util.split(arguments);
		
		if (argsList.size() != 1) {
			env.writeln("'mkdir' expects 1 argument");
			return ShellStatus.CONTINUE;
		}
		
		Path dirPath = Paths.get(argsList.get(0));
		if (Files.exists(dirPath)) {
			env.writeln("Given path already exsits.");
			return ShellStatus.CONTINUE;
		}
		
		try {
			Files.createDirectories(dirPath);
		
		} catch (IOException e) {
			env.writeln("Creating directories failed");
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
		
		description.add("Mkdir takes a single argument.");
		description.add("It creates an appropriate directory structure.");
		
		return description;
	}

}
