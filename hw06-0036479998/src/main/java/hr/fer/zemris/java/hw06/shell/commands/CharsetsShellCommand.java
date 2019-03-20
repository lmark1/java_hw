package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Class implements command for MyShell program. When run command prints out all avaliable charsets.
 * 
 * @author Lovro Marković
 *
 */
public class CharsetsShellCommand implements ShellCommand{

	/**
	 * Name of the 'charsets' command.
	 */
	public static final String name = "charsets";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Map<String, Charset> charsetMap = Charset.availableCharsets();
		
		if (!arguments.isEmpty()) {
			env.writeln("'charsets' does not accept any arguments");
			return ShellStatus.CONTINUE;
		}
		
		env.writeln("Avaliable charsets are:");
		for (Map.Entry<String, Charset> charset : charsetMap.entrySet()) {
			env.writeln(charset.getKey());
		}
		
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return name;
	}

	@Override
	public List<String> getCommandDescription() {
		List <String> description = new ArrayList<>();
		
		description.add("Prints out all avaliable sharsets.");
		
		return Collections.unmodifiableList(description);
	}

}
