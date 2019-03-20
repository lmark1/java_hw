package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

@SuppressWarnings("javadoc")
public class DemoSmartScriptEngine {

	public static void main(String[] args) {
		demo1("./src/main/resources/osnovni.smscr");
		System.out.println("");
		demo2("./src/main/resources/zbrajanje.smscr");
		System.out.println("");
		demo3("./src/main/resources/brojPoziva.smscr");
		System.out.println("");
		demo4("./src/main/resources/fibonacci.smscr");
	}

	private static void demo1(String filepath) {
		String docBody = readFromDisk(filepath);
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();

		// create engine and execute it
		new SmartScriptEngine(new SmartScriptParser(docBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters,
						cookies)).execute();
	}

	private static void demo2(String filepath) {
		String documentBody = readFromDisk(filepath);
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		parameters.put("a", "4");
		parameters.put("b", "2");
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters,
						cookies)).execute();
	}

	private static void demo3(String filepath) {
		String documentBody = readFromDisk(filepath);
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		//persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters,
				persistentParameters, cookies);
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc)
						.execute();
		System.out.println("Vrijednost u mapi: "
				+ rc.getPersistentParameter("brojPoziva"));
	}

	private static void demo4(String filepath) {
		String documentBody = readFromDisk(filepath);
		Map<String, String> parameters = new HashMap<String, String>();
		Map<String, String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(System.out, parameters, persistentParameters,
						cookies)).execute();
	}

	private static String readFromDisk(String filepath) {
		String docBody = new String();
		try {
			docBody = new String(Files.readAllBytes(Paths.get(filepath)),
					StandardCharsets.UTF_8);
		} catch (IOException e1) {
			System.out.println("Cannot find given file.");
			System.exit(1);
		}

		return docBody;
	}
}
