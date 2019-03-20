package hr.fer.zemris.java.hw06.shell;

import java.nio.file.InvalidPathException;
import java.util.Collections;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw06.shell.commands.CatShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CharsetsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.CopyShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HelpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.HexdumpShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.LsShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.MkdirShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.SymbolShellCommand;
import hr.fer.zemris.java.hw06.shell.commands.TreeShellCommand;

/**
 * This class implements a command line program. Supported commands are:
 * charsets, cat, ls, tree, copy, mkdir, hexdump. Commands can span across
 * multiple lines by using PROMPTSYMBOL (default is >) and MORELINESYMBOL
 * (default is \). Each multi-line command writes a MULTILINESYMBOL (default is
 * |) at the start.
 * 
 * @author Lovro Marković
 *
 */
public class MyShell {

	/**
	 * Unmodifiable sorted map of all commands currently available in the shell.
	 */
	private static SortedMap<String, ShellCommand> commands;

	/**
	 * Current shell status.
	 */
	private static ShellStatus status = ShellStatus.CONTINUE;

	/**
	 * Scanner used by the readline method.
	 */
	private static Scanner readingScanner = new Scanner(System.in);

	/**
	 * Main method of the program. Executed when program is run.
	 * 
	 * @param args
	 *            Does not except command line arguments.
	 */
	public static void main(String[] args) {
		initializeShell();
		Environment shellEnv = new ShellEnvironment();

		System.out.println("Welcome to MyShell v 1.0");

		do {

			try {
				String lines = shellEnv.readLine();

				String commandName = getCommandName(lines);
				if (commandName.isEmpty()) {
					shellEnv.writeln("No command is given");
					continue;
				}

				String arguments = getArguments(lines);
				
				ShellCommand command = commands.get(commandName);
				if (command == null) {
					shellEnv.writeln("Unsupported command given");
					continue;
				}
				status = command.executeCommand(shellEnv, arguments);

			} catch (ShellIOException e) {
				shellEnv.write(e.getMessage());
				status = ShellStatus.TERMINATE;
			
			} catch (InvalidPathException e) {
				shellEnv.writeln("Path given is invalid.");
				status = ShellStatus.CONTINUE;
			
			} catch (Exception e) {
				shellEnv.writeln(e.getMessage());
				shellEnv.writeln("Unexpected exception occured.");
				status = ShellStatus.CONTINUE;
			}

		} while (status != ShellStatus.TERMINATE);

		readingScanner.close();
	}

	/**
	 * Extracts arguments from the given string representing user input.
	 * 
	 * @param lines
	 *            User input.
	 * @return Arguments extracted from the input.
	 */
	private static String getArguments(String lines) {
		String[] stringSplit = lines.trim().split("\\s+", 2);

		try {
			return stringSplit[1];

		} catch (IndexOutOfBoundsException e) {
			return new String("");
		}
	}

	/**
	 * Extracts command name from the given string representing user input.
	 * 
	 * @param lines
	 *            String representing user input.
	 * @return Command name.
	 */
	private static String getCommandName(String lines) {
		String[] stringSplit = lines.trim().split("\\s+", 2);
		return stringSplit[0];
	}

	/**
	 * Initializes the shell by adding commands to the pro
	 */
	private static void initializeShell() {
		commands = new TreeMap<>();

		// Add all commands to the map
		commands.put(ExitShellCommand.name, new ExitShellCommand());
		commands.put(SymbolShellCommand.name, new SymbolShellCommand());
		commands.put(CharsetsShellCommand.name, new CharsetsShellCommand());
		commands.put(CatShellCommand.name, new CatShellCommand());
		commands.put(LsShellCommand.name, new LsShellCommand());
		commands.put(TreeShellCommand.name, new TreeShellCommand());
		commands.put(CopyShellCommand.name, new CopyShellCommand());
		commands.put(MkdirShellCommand.name, new MkdirShellCommand());
		commands.put(HexdumpShellCommand.name, new HexdumpShellCommand());
		commands.put(HelpShellCommand.name, new HelpShellCommand());
		
		commands = Collections.unmodifiableSortedMap(MyShell.commands);
	}

	/**
	 * This class defines current environment of the shell. Implements methods
	 * of the Environemnt interface.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class ShellEnvironment implements Environment {

		/**
		 * Symbol that defines prompts in MyShell.
		 */
		private Character PROMPTSYMBOL;

		/**
		 * Symbol that signals that the current command is a multi - line
		 * command. Found at the end of the command.
		 */
		private Character MORELINESYMBOL;

		/**
		 * Symbol printed at the start of each multi - line command.
		 */
		private Character MULTILINESYMBOL;

		/**
		 * Default constructor for the Shell environment class. Initializes
		 * default values of MORELINESYMBOL, PROMPTSYMBOL and MULTILINESYMBOL.
		 */
		public ShellEnvironment() {
			PROMPTSYMBOL = '>';
			MORELINESYMBOL = '\\';
			MULTILINESYMBOL = '|';
		}

		@Override
		public String readLine() throws ShellIOException {
			StringBuilder inputString = new StringBuilder();
			write(String.valueOf(PROMPTSYMBOL) + " ");
			
			try {
				while (true) {
					String line = readingScanner.nextLine().trim();
	
					// If it has more than 1 character ensure that there is atleast
					// 1
					// blanskapce behind more line symbol
					if (line.length() == 1
							&& line.charAt(line.length() - 1) == MORELINESYMBOL
							|| (line.length() > 1
									&& line.charAt(line.length() - 2) == ' '
									&& line.charAt(
											line.length() - 1) == MORELINESYMBOL)) {
	
						String userInput = line.substring(0, line.length() - 1);
						inputString.append(userInput);
						write(String.valueOf(MULTILINESYMBOL) + " ");
						continue;
	
					} else {
						inputString.append(line);
					}
	
					return inputString.toString();
				}
				
			} catch(Exception e) {
				throw new ShellIOException("Exception occured during reading.");
			}

		}

		@Override
		public void write(String text) throws ShellIOException {
			
			try {
				System.out.print(text);
			
			} catch(Exception e) {
				throw new ShellIOException("Exception occured while writing.");
			}
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			
			try {
				System.out.println(text);
			
			} catch(Exception e) {
				throw new ShellIOException("Exception occured while writing.");
			}
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		@Override
		public Character getMultilineSymbol() {
			return MULTILINESYMBOL;
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			MULTILINESYMBOL = symbol;
		}

		@Override
		public Character getPromptSymbol() {
			return PROMPTSYMBOL;
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			PROMPTSYMBOL = symbol;
		}

		@Override
		public Character getMorelinesSymbol() {
			return MORELINESYMBOL;
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			MORELINESYMBOL = symbol;
		}

	}
}
