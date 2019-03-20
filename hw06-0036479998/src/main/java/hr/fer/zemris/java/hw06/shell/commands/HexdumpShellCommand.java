package hr.fer.zemris.java.hw06.shell.commands;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
 * hexdump command expects a single argument: file name, and produces
 * appropriate hex output of all bytes.
 * 
 * @author Lovro Marković
 *
 */
public class HexdumpShellCommand implements ShellCommand {

	/**
	 * Name of the 'hexdump' command.
	 */
	public static final String name = "hexdump";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		List<String> argList = Util.split(arguments);

		if (argList.size() != 1) {
			env.writeln("'hexdump' expects only 1 argument");
			return ShellStatus.CONTINUE;
		}

		Path dirPath = Paths.get(argList.get(0));
		if (Files.isDirectory(dirPath)) {
			env.writeln("Given path has to be a file");
			return ShellStatus.CONTINUE;
		}

		if (!Files.isReadable(dirPath)) {
			env.writeln("Invalid file path given");
			return ShellStatus.CONTINUE;
		}

		try {
			constructHexTable(dirPath, env);
			
		} catch (IOException e) {
			env.writeln("Problm happened during hex-table construction.");
		}

		return ShellStatus.CONTINUE;
	}

	/**
	 * Constructs a hex table for the given file path.
	 * 
	 * @param dirPath
	 *            Given file path.
	 * @param env Environment of shell
	 * @throws IOException Exception thrown if there is an error while reading table.
	 */
	private void constructHexTable(Path dirPath, Environment env)
			throws IOException {
		
		BufferedInputStream inputStream = new BufferedInputStream(
				new FileInputStream(dirPath.toFile()));
		byte[] inputBuffer = new byte[16];

		int adress = 0x00;
		int currentSize = inputStream.read(inputBuffer);
		while (currentSize >= 0) {
			env.writeln(getHexRow(inputBuffer, adress, currentSize));
			adress += 0x10;
			currentSize = inputStream.read(inputBuffer);
		}
		
		inputStream.close();
	}

	/**
	 * Generate one row of a hex table.
	 * 
	 * @param inputBuffer Buffer containing bytes.
	 * @param adress Starting adress.
	 * @param size Number of elements in buffer.
	 * @return Row represented as string.
	 */
	private String getHexRow(byte[] inputBuffer, int adress, int size) {
		StringBuilder row = new StringBuilder();
		
		// Append adress
		row.append(String.format("%08X", adress));
		row.append(": ");
		
		StringBuilder charStr = new StringBuilder();
		for (int i = 0; i < 16; i++) {
			
			if (i < size) {
				row.append(String.format("%02X", inputBuffer[i]));
				
				if (inputBuffer[i] < 32 || inputBuffer[i] > 127) {
					charStr.append(".");
				
				} else {
					charStr.append((char)inputBuffer[i]);
				}
			
			} else {
				row.append("  ");
				charStr.append(" ");
			}
			
			if (i == 7) {
				row.append("|");
			
			} else {
				row.append(" ");
			}
		}
		
		row.append("| ");
		row.append(charStr.toString());
		
		return row.toString();
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();

		description.add("Hexdump command expects a single argument: file name");
		description.add("roduces appropriate hex output of all bytes.");

		return Collections.unmodifiableList(description);
	}

}
