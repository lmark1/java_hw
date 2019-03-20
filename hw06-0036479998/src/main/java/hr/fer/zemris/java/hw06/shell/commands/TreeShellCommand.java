package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Class implements a command for MyShell. Command tree expects a single
 * argument: directory name and prints a tree (each directory level shifts
 * output two charatcers to the right).
 * 
 * @author Lovro Marković
 *
 */
public class TreeShellCommand implements ShellCommand {

	/**
	 * Name of command 'tree'.
	 */
	public static final String name = "tree";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argsList = Util.split(arguments);

		if (argsList.size() != 1) {
			env.writeln("'tree' expects one argument");
			return ShellStatus.CONTINUE;
		}

		Path newPath = Paths.get(argsList.get(0)).toAbsolutePath();

		try {
			if (Files.isDirectory(newPath)) {
				
				printAllDirs(env, newPath, 0);	
			
			} else {
				env.writeln("Given path is not a directory");
			}
		
		} catch (IOException e) {
			env.writeln("IO exception caught while printing directories.");
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints a tree (each directory level shifts output two charatcers to the
	 * right).
	 * 
	 * @param env
	 *            Environment of the shell.
	 * @param newPath
	 *            Path to the current directory or file.
	 * @param spaces
	 *            Number of spaces that the output is shifted
	 * @throws IOException
	 *             Exception thrown when new directory stream can't be created.
	 */
	private void printAllDirs(Environment env, Path newPath, int spaces)
			throws IOException {
	
		String space = new String(new char[spaces]).replace("\0", " ");
		env.write(space);
		env.writeln(newPath.getFileName().toString());

		if (Files.isDirectory(newPath)) {

			DirectoryStream<Path> dirStream = Files.newDirectoryStream(newPath);
			for (Path path : dirStream) {
				printAllDirs(env, path, spaces+2);
			}
		}
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Command tree expects a single argument: "
				+ "directory name and prints a tree");
		description.add(
				"Each directory level shifts output two charatcers to the right");

		return Collections.unmodifiableList(description);
	}

}
