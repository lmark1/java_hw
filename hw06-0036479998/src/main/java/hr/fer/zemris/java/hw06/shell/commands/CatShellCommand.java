package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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
 * Class implements a command for MyShell. Command 'cat' takes one or two
 * arguments. The first argument is path to some file and is mandatory. The
 * second argument is charset name that will be used to interpret chars from
 * bytes. If not provided, a default platform charset will be used. This command
 * opens given file and writes its content to console.
 * 
 * @author Lovro Marković
 *
 */
public class CatShellCommand implements ShellCommand {

	/**
	 * Name of the 'cat' command.
	 */
	public static final String name = "cat";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		if (arguments.isEmpty()) {
			env.writeln("'cat' command expects atleast 1 arugment");
			return ShellStatus.CONTINUE;
		}

		List<String> listArgs = Util.split(arguments);
		Path filePath = Paths.get(listArgs.get(0));

		if (listArgs.size() == 1) {
			readFile(env, filePath, Charset.defaultCharset());

		} else if (listArgs.size() == 2) {
			Charset newCharset = null;

			try {
				newCharset = Charset.forName(listArgs.get(1));

			} catch (Exception e) {
				env.writeln("Invalid charset given");
				return ShellStatus.CONTINUE;
			}

			readFile(env, filePath, newCharset);

		} else {
			env.writeln("Too many arguments given");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Reads given file and prints its content on the screen.
	 * 
	 * @param env
	 *            Envirnoment of the shell.
	 * @param path
	 *            Path to the file.
	 * @param charset
	 *            Charset of the file.
	 */
	private void readFile(Environment env, Path path, Charset charset) {

		try {
			BufferedInputStream inputStream = new BufferedInputStream(
					new FileInputStream(path.toFile()));

			byte[] buffer = new byte[512];
			int readSize = inputStream.read(buffer);

			if (readSize < 0) {
				env.writeln("File is empty");
				inputStream.close();
				return;
			}

			while (readSize >= 0) {
				env.writeln(new String(buffer, charset));
				readSize = inputStream.read(buffer);
			}

			inputStream.close();

		} catch (IOException e) {
			env.writeln("Error reading file.");
			return;
		}

	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Command 'cat' takes one or two arguments.");
		description.add(
				"The first argument is path to some file and is mandatory.");
		description
				.add("The second argument is charset name that will be used to "
						+ "interpret chars from bytes.");
		description.add(
				"If not provided, a default platform charset will be used.");
		description.add(
				"This command opens given file and writes its content to console.");

		return Collections.unmodifiableList(description);
	}

}
