package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * The copy command expects two arguments: source file name and destination file
 * name (i.e. paths and names). If destination file exists, user is asked is
 * overwriting is allowed. Command only works with files. If the second argument
 * is directory, then it copies the original file into that directory using the
 * original file name.
 * 
 * @author Lovro Markovieć
 *
 */
public class CopyShellCommand implements ShellCommand {

	/**
	 * Name of command 'copy'.
	 */
	public static final String name = "copy";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argsList = Util.split(arguments);

		if (argsList.size() != 2) {
			env.writeln("'copy' expects 2 arguments");
			return ShellStatus.CONTINUE;
		}

		Path source = Paths.get(argsList.get(0)).toAbsolutePath();
		Path dest = Paths.get(argsList.get(1)).toAbsolutePath();

		// Can't read from directory
		if (!Files.isRegularFile(source)) {
			env.writeln("Source has to be a regular file.");
			return ShellStatus.CONTINUE;
		}

		// Can't read if not readable
		if (!Files.isReadable(source)) {
			env.writeln("Do not have permission to read source file");
			return ShellStatus.CONTINUE;
		}

		try {

			// Write only to directories that actually exist
			if (Files.isDirectory(dest) && Files.exists(dest)) {
				copyToDirectory(source, dest);

				// If it's not a directory check if you have permission to write
			} else if (!Files.isDirectory(dest) && Files.isWritable(dest)) {

				if (checkUserPermission(env)) {
					copyToDest(source, dest);
				}

				// If given source file does not exist, only copy if its parent
				// exists and is a directory
			} else if (Files.notExists(dest)) {

				if (Files.exists(dest.getParent())
						&& Files.isDirectory(dest.getParent())) {
					copyToDest(source, dest);
				
				} else {
					env.writeln("Parent directory does not exist");
				}

			} else {
				env.writeln("Invalid paths given");
			}

		} catch (IOException e) {
			env.writeln(e.getMessage());
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Copies file contents from source to a file in directory by the same name.
	 * 
	 * @param source
	 *            Source file that will be copied.
	 * @param dest
	 *            Destination directory.
	 * @throws IOException
	 *             Exception thrown if there is an error during copying.
	 */
	private void copyToDirectory(Path source, Path dest) throws IOException {
		Path destPath = Paths
				.get(dest.toString() + "\\" + source.getFileName());

		copyToDest(source, destPath);
	}

	/**
	 * Copies contents of the given source file to the destination file.
	 * 
	 * @param source
	 *            Source file which contents will be copied.
	 * @param dest
	 *            Destination of the copy.
	 * @throws IOException
	 *             Exception thrown if there is an error during file copying.
	 */
	private void copyToDest(Path source, Path dest) throws IOException {

		if (Files.exists(dest) && Files.isSameFile(source, dest)) {
			throw new IOException("Source and dest are same files.");
		}
		
		BufferedInputStream inputStream = new BufferedInputStream(
				new FileInputStream(source.toFile()));
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(dest.toFile()));

		byte[] inputBuffer = new byte[512];

		int currentSize = inputStream.read(inputBuffer);
		while (currentSize >= 0) {
			outputStream.write(inputBuffer);
			currentSize = inputStream.read(inputBuffer);
		}

		inputStream.close();
		outputStream.close();
	}

	/**
	 * Checks user permission for overwriting destination file.
	 * 
	 * @param env
	 *            Shell environment.
	 * @return True if user grants permission, otherwise false.
	 */
	private boolean checkUserPermission(Environment env) {
		env.writeln(
				"Do you want to overwrite the given destination file? [y/n]");

		while (true) {
			String userInput = env.readLine();

			if (userInput.equals("y")) {
				env.writeln("File will be overwritten");
				return true;
			}

			if (userInput.equals("n")) {
				env.writeln("File won't be overwritten");
				return false;
			}

			env.writeln("Please input 'y' or 'n'");
		}
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("The copy command expects two arguments: "
				+ "source file name and destination file name (i.e. paths and names)");
		description.add("Command only works with files.");
		description.add("If the second argument is directory,");
		description.add(
				"then it copies the original file into that directory using the original file name");

		return Collections.unmodifiableList(description);
	}

}
