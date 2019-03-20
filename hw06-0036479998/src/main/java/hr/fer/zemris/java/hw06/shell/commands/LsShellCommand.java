package hr.fer.zemris.java.hw06.shell.commands;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.crypto.Util;
import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Class implements a command for MyShell. Command ls takes a single argument -
 * a directory - and writes a directory listing. E.g. -rw- 53412 2009-03-15
 * 12:59:31 azuriraj.ZIP
 * 
 * @author Lovro Marković
 *
 */
public class LsShellCommand implements ShellCommand {

	/**
	 * Name of the 'ls' command.
	 */
	public static final String name = "ls";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {

		List<String> argsList = Util.split(arguments);

		if (argsList.size() != 1) {
			env.writeln("'ls' expects one argument - a directory");
			return ShellStatus.CONTINUE;
		}

		Path dirPath = Paths.get(argsList.get(0));

		if (!Files.isDirectory(dirPath)) {
			env.writeln("Given path does not represent directory");
			return ShellStatus.CONTINUE;
		}

		DirectoryStream<Path> dirStream;
		try {
			dirStream = Files.newDirectoryStream(dirPath);
		} catch (IOException e) {
			env.writeln("Cannot generate direcotry stream");
			return ShellStatus.CONTINUE;
		}
		
		for (Path path : dirStream) {
			env.writeln(writeDetails(path));
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Prints out file details of the given file.
	 * 
	 * @param path
	 *            Given file path.
	 * @return Formatted row containing file details.
	 */
	private String writeDetails(Path path) {
		
		String col1 = getFirstColumn(path);
		String col2 = String.valueOf(path.toFile().length());
		String col3 = getThirdColumn(path);
		String col4 = path.getFileName().toString();
		
		return String.format("%s %10s %s %s", col1, col2, col3, col4);	
	}

	/**
	 * Generates a String containing date and time of the file.
	 * 
	 * @param path
	 *            Given file path.
	 * @return String representing file date and time.
	 */
	private String getThirdColumn(Path path) {
		String formattedDateTime = null;
		
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
			);
			BasicFileAttributes attributes = faView.readAttributes();
			FileTime fileTime = attributes.creationTime();
			formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		
		} catch (IOException e) {
			return new String("");
		}
		
		return formattedDateTime;
	}

	/**
	 * Returns a String representing if the given file is a directory (d),
	 * readable (r), writable (w) and executable (x).
	 * 
	 * @param path
	 *            Given file.
	 * @return String representing file properties.
	 */
	private String getFirstColumn(Path path) {
		StringBuilder newStr = new StringBuilder();

		if (Files.isDirectory(path)) {
			newStr.append("d");
		} else {
			newStr.append("-");
		}

		if (Files.isReadable(path)) {
			newStr.append("r");
		} else {
			newStr.append("-");
		}

		if (Files.isWritable(path)) {
			newStr.append("w");
		} else {
			newStr.append("-");
		}

		if (Files.isExecutable(path)) {
			newStr.append("x");
		} else {
			newStr.append("-");
		}

		return newStr.toString();
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Accepts a single argument - a directory");
		description.add("Writes a directory listing.");
		description.add("E.g. -rw- 53412 2009-03-1512:59:31 azuriraj.ZIP");

		return Collections.unmodifiableList(description);
	}

}
