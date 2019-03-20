package hr.fer.zemris.java.webserver;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * This class implements a simple HTTP server.
 * 
 * @author Lovro Marković
 *
 */
public class SmartHTTPServer {

	/**
	 * Server address.
	 */
	private String address;

	/**
	 * Server port.
	 */
	private int port;

	/**
	 * Number of worker threads.
	 */
	private int workerThreads;

	/**
	 * User session timeout, measured in seconds.
	 */
	private int sessionTimeout;

	/**
	 * Map of available mime types.
	 */
	private Map<String, String> mimeTypes = new HashMap<>();

	/**
	 * Main thread of the server.
	 */
	private ServerThread serverThread;

	/**
	 * Executor service of the thread.
	 */
	private ExecutorService threadPool;

	/**
	 * Root of the document.
	 */
	private Path documentRoot;

	/**
	 * Maps worker names with their appropriate classes.
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();

	/**
	 * Map of all the sessions.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<String, SessionMapEntry>();

	/**
	 * Random SID generator.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Task that cleans expired sessions from the sessions map.
	 */
	private final TimerTask sessionCleaner = new TimerTask() {

		@Override
		public void run() {

			Set<String> sessionKey = sessions.keySet();

			for (String key : sessionKey) {

				SessionMapEntry entry = sessions.get(key);
				if (isTooOld(entry)) {
					sessions.remove(key);
				}
			}
		}
	};

	/**
	 * Timer used for scheduling tasks on a timer.
	 */
	private final Timer timer = new Timer();

	/**
	 * Time period defined for sessionCleaner.
	 */
	private static final long CLEAN_TIME = 5 * 60 * 1000;

	/**
	 * Main method of the program. Executes when program starts.
	 * 
	 * @param args
	 *            Accepts one command line argument - path to the server config
	 *            file.
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Path to the config file expected.");
			return;
		}

		SmartHTTPServer server = new SmartHTTPServer(args[0]);
		server.start();
	}

	/**
	 * Constructor of the HTTP server,
	 * 
	 * @param configFileName
	 *            Name of the config file.
	 */
	public SmartHTTPServer(String configFileName) {

		Properties serverProp = null;

		try {
			serverProp = new Properties();
			FileInputStream in = new FileInputStream(configFileName);
			serverProp.load(in);
			in.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		address = serverProp.getProperty("server.address");
		port = Integer.parseInt(serverProp.getProperty("server.port"));
		workerThreads = Integer
				.parseInt(serverProp.getProperty("server.workerThreads"));
		documentRoot = Paths.get(serverProp.getProperty("server.documentRoot"));
		sessionTimeout = Integer
				.parseInt(serverProp.getProperty("session.timeout"));

		// Load mime properties
		Properties mimeProp = null;
		try {
			mimeProp = new Properties();
			FileInputStream in = new FileInputStream(
					serverProp.getProperty("server.mimeConfig"));
			mimeProp.load(in);
			in.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		// Initialize mime types
		Set<Object> mimeKeys = mimeProp.keySet();
		for (Object key : mimeKeys) {
			String value = mimeProp.getProperty((String) key);
			mimeTypes.put((String) key, value);
		}

		// Load worker properties
		Properties workerProp = null;
		try {
			workerProp = new Properties();
			FileInputStream in = new FileInputStream(
					serverProp.getProperty("server.workers"));
			workerProp.load(in);
			in.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		// Initialize workers
		Set<Object> workerPaths = workerProp.keySet();

		try {
			for (Object wPath : workerPaths) {

				String fqcn = workerProp.getProperty((String) wPath);
				IWebWorker iww = generateWWClass(fqcn);
				workersMap.put((String) wPath, iww);
			}
		} catch (Exception ignorable) {
		}

	}

	/**
	 * Starts server thread if it's not already running, also starts a thread
	 * that removes expired session entries.
	 */
	protected synchronized void start() {

		if (serverThread != null && serverThread.isAlive()) {
			return;
		}

		serverThread = new ServerThread();
		threadPool = Executors.newFixedThreadPool(workerThreads);
		serverThread.run();

		// Schedule session cleaner
		timer.schedule(sessionCleaner, CLEAN_TIME, CLEAN_TIME);
	}

	/**
	 * Method used to stop the server. Kills the server thread, shuts down the
	 * thread pool and cancels timer task.
	 */
	protected synchronized void stop() {
		serverThread.kill();
		threadPool.shutdown();
		sessionCleaner.cancel();
	}

	/**
	 * Generates a Web worker class.
	 * 
	 * @param fqcn
	 *            Fully qualified class name.
	 * @return Web worker object.
	 * @throws Exception
	 *             Exception thrown if an error occurs while loading the class
	 *             reference.
	 */
	private IWebWorker generateWWClass(String fqcn) throws Exception {
		Class<?> referenceToClass = null;
		referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
		Object newObject = referenceToClass.newInstance();
		IWebWorker iww = (IWebWorker) newObject;
		return iww;
	}

	/**
	 * This class implements a thread for the server.
	 * 
	 * @author Lovro Marković
	 *
	 */
	protected class ServerThread extends Thread {

		/**
		 * Variable signaling if server thread is running.
		 */
		private boolean isRunning = true;

		/**
		 * Server socket.
		 */
		public ServerSocket serverSocket;

		@SuppressWarnings("resource")
		@Override
		public void run() {

			// open serverSocket on specified port
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket();
				serverSocket.setSoTimeout(sessionTimeout * 1000);
				serverSocket.bind(new InetSocketAddress(
						InetAddress.getByName(address), port));

				while (isRunning) {
					Socket client = serverSocket.accept();
					ClientWorker cw = new ClientWorker(client);

					// submit cw to threadpool for execution
					threadPool.submit(cw);
				}

			} catch (IOException e) {
				System.out.println(e.getMessage());
				return;
			}

		}

		/**
		 * Method used to stop the thread.
		 */
		public void kill() {
			isRunning = false;
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * Worker for the HTTP server.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {

		/**
		 * Request context.
		 */
		private RequestContext context = null;

		/**
		 * Server socket.
		 */
		private Socket csocket;

		/**
		 * Input stream of the server.
		 */
		private PushbackInputStream istream;

		/**
		 * Output stream of the server.
		 */
		private OutputStream ostream;

		/**
		 * Version.
		 */
		private String version;

		/**
		 * Method used in the request.
		 */
		private String method;

		/**
		 * Parameters of the request.
		 */
		private Map<String, String> params = new HashMap<String, String>();

		/**
		 * Temporary parameters of the request.
		 */
		private Map<String, String> tempParams = new HashMap<String, String>();

		/**
		 * Permanent parameters of the request.
		 */
		private Map<String, String> permPrams = new HashMap<String, String>();

		/**
		 * Ouput cookies of the request.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();

		/**
		 * Session ID.
		 */
		private String SID;

		/**
		 * Path to worker class.
		 */
		private static final String PATH_TO_WORKER = "hr.fer.zemris.java.webserver.workers.";

		/**
		 * Length of a session ID.
		 */
		private static final int SID_LENGTH = 20;

		/**
		 * Constructor for the ClientWorker object.
		 * 
		 * @param csocket
		 *            Initialize socket variable.
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;
		}

		@Override
		public void run() {
			// obtain input stream from socket and wrap it to pushback input
			// stream
			// obtain output stream from socket
			// Then read complete request header from your client in separate
			// method...
			List<String> request = null;

			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = csocket.getOutputStream();

			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error while reading from socket.");
				System.exit(1);
			}

			if (istream == null) {
				return;
			}

			try {
				request = readRequest(istream);
			} catch (IOException ignorable) {
				// Exception is thrown only if header is completly empty
				return;
			}

			// If header is invalid (less then a line at least) return response
			// status 400
			if (request == null) {
				sendError(ostream, 400, "Bad request header.");
				return;
			}

			// Extract (method, requestedPath, version) from firstLine
			// if method not GET or version not HTTP/1.0 or HTTP/1.1 return
			// response status 400
			String[] firstLine = request.isEmpty() ? null
					: request.get(0).split(" ");

			method = firstLine[0].toUpperCase();
			version = firstLine[2];

			if (firstLine == null || firstLine.length != 3
					|| !"GET".equals(method) || !("HTTP/1.0".equals(version)
							|| "HTTP/1.1".equals(version))) {

				sendError(ostream, 400, "Bad request header");
				return;
			}

			// Check current session for cookies
			checkSession(request);

			// (path, paramString) = split requestedPath to path and
			// parameterString
			String path = firstLine[1];

			String[] pathSplit = path.split("[?]");
			String paramString = pathSplit.length == 2 ? pathSplit[1] : "";

			// parseParameters(paramString); ==> your method to fill map
			// parameters
			if (!paramString.isEmpty()) {
				parseParameters(paramString);
			}

			try {
				internalDispatchRequest(pathSplit[0], true);

			} catch (Exception e1) {
				System.err.println(e1.getMessage());
				System.exit(1);
			}

		}

		@Override
		public void DispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Internal dispatch request.
		 * 
		 * @param urlPath
		 *            URL path.
		 * @param directCall
		 *            Boolean variable signalling if it's a direct call.
		 * @throws Exception
		 *             Exception thrown if there is an error during writing or
		 *             reading from streams.
		 */
		public void internalDispatchRequest(String urlPath, boolean directCall)
				throws Exception {

			if (urlPath.startsWith("/private") && directCall) {
				sendError(ostream, 404, "Restricted access");
				return;
			}

			// Configuration - based approach
			if (workersMap.containsKey(urlPath)) {
				IWebWorker iww = workersMap.get(urlPath);

				if (context == null) {
					context = initializeContext("text/plain");
				}

				iww.processRequest(context);
				csocket.close();
				return;
			}

			if (urlPath.startsWith("/ext")) {
				IWebWorker iww = generateWWClass(PATH_TO_WORKER
						+ urlPath.substring(5, urlPath.length()));

				if (context == null) {
					context = initializeContext("text/html");
				}

				iww.processRequest(context);
				return;
			}

			// requestedPath = resolve path with respect to documentRoot
			// if requestedPath is not below documentRoot, return response
			// status 403 forbidden
			Path relPath = Paths.get(urlPath.substring(1, urlPath.length()));
			Path requestedPath = documentRoot.toAbsolutePath().resolve(relPath);
			if (!requestedPath.startsWith(documentRoot)) {
				sendError(ostream, 403, "Forbidden path given");
				return;
			}

			// check if requestedPath exists, is file and is readable; if not,
			// return status 404
			String extension = null;
			if (!(Files.exists(requestedPath)
					&& Files.isRegularFile(requestedPath)
					&& Files.isReadable(requestedPath))) {

				sendError(ostream, 404, "Requsted file path is invalid.");
				return;

			} else {
				// else extract file extension
				int dotIndex = requestedPath.toString().lastIndexOf(".");
				extension = requestedPath.toString().substring(dotIndex + 1);
			}

			// Add scripting funtionality
			if ("smscr".equals(extension)) {
				processScript(requestedPath);
				return;
			}

			// find in mimeTypes map appropriate mimeType for current file
			// extension
			// (you filled that map during the construction of SmartHttpServer
			// from mime.properties)
			String mimeType = mimeTypes.get(extension);

			// if no mime type found, assume application/octet-stream
			if (mimeType == null) {
				mimeType = new String("application/octet-stream");
			}

			// create a rc = new RequestContext(...); set mime-type; set status
			// to 200
			// If you want, you can modify RequestContext to allow you to add
			// additional headers
			// so that you can add “Content-Length: 12345” if you know that file
			// has 12345 bytes

			if (context == null) {
				context = initializeContext(mimeType);
			}

			// open file, read its content and write it to rc (that will
			// generate header and send
			// file bytes to client)
			try {

				// TODO sample.txt downloada... format je text/plain. Skripte
				// isto budu u text/plain pa ih lijepo prikaze
				InputStream is = new FileInputStream(requestedPath.toFile());
				byte[] buf = new byte[1024];
				while (true) {
					int r = is.read(buf);
					if (r < 0)
						break;
					context.write(buf);
				}

				ostream.flush();
				is.close();
				csocket.close();

				return;
			} catch (IOException e) {
				System.out.println("Error writing to request context.");
				System.exit(1);
			}
		}

		/**
		 * Processes header and checks it for cookies.
		 * 
		 * @param request
		 *            List of header lines.
		 */
		private void checkSession(List<String> request) {
			String tmpCandidate = null;

			for (String line : request) {

				// If a line does not start with Cookie skip
				if (!line.startsWith("Cookie")) {
					continue;
				}

				// Split all cookies
				String[] cookies = line
						.substring(new String("Cookie: ").length(),
								line.length())
						.split(";");

				for (String cookie : cookies) {

					String[] splitCookie = cookie.split("=");
					String name = splitCookie[0];

					// check if cookie is SID
					if (name.toLowerCase().equals("sid")) {
						String value = splitCookie[1].replaceAll("[\"]", "");
						tmpCandidate = value;
					}
				}
			}
			SessionMapEntry activeEntry = null;
			// Check if sid is found or not - nekad je string = "null" ?
			if (tmpCandidate == null || tmpCandidate.equals("null")) {
				activeEntry = addSessionEntry();

			} else {
				SessionMapEntry entry = sessions.get(tmpCandidate);

				if (entry == null) {
					activeEntry = addSessionEntry();

				} else if (isTooOld(entry)) {
					sessions.remove(SID);
					activeEntry = addSessionEntry();
				} else {
					entry.validUntil = sessionTimeout
							+ System.currentTimeMillis() / 1000;
					activeEntry = entry;
				}
			}

			this.permPrams = activeEntry.map;
		}

		/**
		 * Generate a new Session map entry with a sid value consisting of 20
		 * randomly generated uppercase letters. Initialize sid String value.
		 * Also adds an appropriate cookie in the outputCookies list.
		 * 
		 * @return Returns newly generated session map entry.
		 * 
		 */
		private synchronized SessionMapEntry addSessionEntry() {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < SID_LENGTH; i++) {
				char c = (char) (sessionRandom.nextInt(26) + 'a');
				sb.append(String.valueOf(c).toUpperCase());
			}

			SID = sb.toString();
			SessionMapEntry newEntry = new SessionMapEntry();
			newEntry.sid = SID;
			newEntry.validUntil = sessionTimeout
					+ System.currentTimeMillis() / 1000;
			newEntry.map = new ConcurrentHashMap<>();

			sessions.put(newEntry.sid, newEntry);
			// Make new sid cookie
			RCCookie sidCookie = new RCCookie("sid", SID, null, address, "/");

			// TODO Kako napraviti cookie HTTP only?
			outputCookies.add(sidCookie);

			return newEntry;
		}

		/**
		 * @param mimeType
		 *            Intializes mimeTye.
		 * @return Returns initialized context object.
		 */
		private RequestContext initializeContext(String mimeType) {
			RequestContext rc = new RequestContext(ostream, params, permPrams,
					outputCookies, tempParams, this);

			if (mimeType != null) {
				rc.setMimeType(mimeType);
			}

			rc.setStatusCode(200);
			rc.setStatusText("OK");

			return rc;
		}

		/**
		 * Processes script. Prints its output on the internet.
		 * 
		 * @param requestedPath
		 *            Path to the script file.
		 * @throws IOException
		 *             Exception thrown if there is an error writing to and from
		 *             streams.
		 * 
		 */
		private void processScript(Path requestedPath) throws IOException {
			byte[] bytes = Files.readAllBytes(requestedPath);
			String script = new String(bytes, StandardCharsets.UTF_8);
			SmartScriptParser parser = new SmartScriptParser(script);

			if (context == null) {
				context = initializeContext("text/html");
			}

			SmartScriptEngine engine = new SmartScriptEngine(
					parser.getDocumentNode(), context);
			engine.execute();

			ostream.flush();
			csocket.close();
		}

		/**
		 * Method used to fill the parameters map with the String containing
		 * parameters.
		 * 
		 * @param paramString
		 *            String containig parameters.
		 */
		private void parseParameters(String paramString) {
			String[] mapEntries = paramString.split("[&]");

			for (String entry : mapEntries) {
				String[] splitEntry = entry.split("[=]");
				if (splitEntry.length != 2) {
					continue;
				}
				params.put(splitEntry[0], splitEntry[1]);
			}
		}

		/**
		 * Obtains input and output streams from socket. Reads request header.
		 * 
		 * @param is
		 *            Input stream.
		 * @return Lines of the request header.
		 * @throws IOException
		 *             Exception thrown if header is empty.
		 */
		private List<String> readRequest(InputStream is) throws IOException {
			byte[] requestBytes = null;

			try {
				requestBytes = readRequestBytes(is);
			} catch (IOException e) {
				throw new IOException(e.getMessage());
			}

			if (requestBytes == null) {
				return null;
			}

			String requestString = new String(requestBytes,
					StandardCharsets.US_ASCII);

			// System.out.println("dobio sam headere: ");
			// System.out.println(requestString);
			return extractHeaders(requestString);
		}

		/**
		 * @param requestHeader
		 *            String containing request header.
		 * @return Returns List of all lines in the header.
		 */
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for (String s : requestHeader.split("\n")) {
				if (s.isEmpty())
					break;
				char c = s.charAt(0);
				if (c == 9 || c == 32) {
					currentLine += s;
				} else {
					if (currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if (!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		/**
		 * Reads request header.
		 * 
		 * @param is
		 *            InputStream
		 * @return Byte array contining the header.
		 * @throws IOException
		 *             Exception thrown if an error occurs while reading.
		 */
		private byte[] readRequestBytes(InputStream is) throws IOException {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			boolean isRead = false;

			l: while (true) {
				int b = 0;

				try {
					b = is.read();
					isRead = true;
				} catch (IOException e) {

					if (!isRead) {
						// If nothing is read do not print an error message.
						throw new IOException("No data found in header");

					} else {
						// If something is read but an error occured later print
						// message.
						System.out
								.println("Error occured while reading header.");
						return null;
					}
				}

				if (b == -1)
					return null;
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			return bos.toByteArray();
		}

		/**
		 * Auxiliary method for sending a response without a body.
		 * 
		 * @param cos
		 *            Socket output stream.
		 * @param statusCode
		 *            Status code of error.
		 * @param statusText
		 *            Status text of error.
		 */
		private void sendError(OutputStream cos, int statusCode,
				String statusText) {

			String response = "<html><head><title>" + statusText
					+ "</title></head>" + "<body><b>" + statusCode + " "
					+ statusText + "</b></body><html>";

			try {
				cos.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
						+ "Server: simple java server\r\n"
						+ "Content-Type: text/html;charset=UTF-8\r\n"
						+ "Content-Length: " + response.length() + "\r\n"
						+ "Connection: close\r\n" + "\r\n" + response)
								.getBytes(StandardCharsets.US_ASCII));
				cos.flush();
			} catch (IOException e) {
				System.out.println("Error occured while writing error message");
			}

		}
	}

	/**
	 * Check if session is too old.
	 * 
	 * @param entry
	 *            Session entry.
	 * @return True if the session is too old, otherwise false.
	 */
	private boolean isTooOld(SessionMapEntry entry) {
		return Math.abs(System.currentTimeMillis() / 1000
				- entry.validUntil) > sessionTimeout;
	}

	/**
	 * Structure of session entry.
	 * 
	 * @author Lovro Marković
	 *
	 */
	private static class SessionMapEntry {

		/**
		 * Session ID.
		 */
		String sid;

		/**
		 * Time until session expires.
		 */
		long validUntil;

		/**
		 * Current map of permanent parameters of this session.s
		 */
		Map<String, String> map;
	}
}
