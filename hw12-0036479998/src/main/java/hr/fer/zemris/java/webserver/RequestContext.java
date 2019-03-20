package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a Request Context object. It is used for writing
 * request context data into the stream initialized in the constructor. It also
 * maintains a read only map of parameters and a two read/write maps:
 * temporaryParameters and persistentParameters.
 * 
 * @author Lovro Marković
 *
 */
public class RequestContext {

	/**
	 * Dispatcher.
	 */
	private IDispatcher dispatcher;

	/**
	 * Output stream of the request context.
	 */
	private OutputStream outputStream;

	/**
	 * Charset for interpreting bytes written in output stream.
	 */
	private Charset charset;

	/**
	 * Write only property - Encoding of the request.
	 */
	public String encoding = "UTF-8";

	/**
	 * Write only property - Status code of the request.
	 */
	public int statusCode = 200;

	/**
	 * Write only property - Status text of the request.
	 */
	public String statusText = "OK";

	/**
	 * Type of the request.
	 */
	public String mimeType = "text/html";

	/**
	 * Private collection of parameters.
	 */
	private Map<String, String> parameters;

	/**
	 * Private collection of temporary parameters.
	 */
	private Map<String, String> temporaryParameters;

	/**
	 * Private collection of persistent paremeters.
	 */
	private Map<String, String> persistentParameters;

	/**
	 * List of output cookies.
	 */
	private List<RCCookie> outputCookies;

	/**
	 * Flag checking if the header is generated, defaults to false.
	 */
	private boolean headerGenerated = false;

	/**
	 * Constructor for RequestContext objects.
	 * 
	 * @param outputStream
	 *            Output stream.
	 * @param parameters
	 *            Map of parameters.
	 * @param persistentParameters
	 *            Map of persistent parameters.
	 * @param outputCookies
	 *            List of output cookies.
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies) {
		
		this(outputStream, parameters, persistentParameters, outputCookies,
				new HashMap<>(), null);
	}

	/**
	 * Additionl constructor for the request contex. Initializes
	 * temporaryParameters and dispatcher.
	 * 
	 * @param outputStream
	 *            Output strea.
	 * @param parameters
	 *            Map of parameters.
	 * @param persistentParameters
	 *            Map of persistent parameters.
	 * @param outputCookies
	 *            List of output cookies.
	 * @param temporaryParameters
	 *            Map of temporaray parameters.
	 * @param dispatcher
	 *            Reference to the dispatcher.
	 */
	public RequestContext(OutputStream outputStream,
			Map<String, String> parameters,
			Map<String, String> persistentParameters,
			List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher) {

		this.outputStream = outputStream;

		this.parameters = new HashMap<>();
		this.parameters.putAll(parameters);

		this.persistentParameters = persistentParameters;
		//this.persistentParameters.putAll(persistentParameters);

		this.temporaryParameters = new HashMap<>();

		this.outputCookies = new ArrayList<>();
		this.outputCookies.addAll(outputCookies);

		this.temporaryParameters = temporaryParameters;
		this.dispatcher = dispatcher;
	}

	/**
	 * @return Returns Dispatcher reference.
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Method retrieves value of the given key from the parameters map.
	 * 
	 * @param name
	 *            Key of parameters map.
	 * @return Value associated with the given key or null if there is no such
	 *         key.
	 */
	public String getParameter(String name) {

		if (!parameters.containsKey(name)) {
			return null;
		}

		return parameters.get(name);
	}

	/**
	 * @return Returns set of all names contained in parameter map as a read -
	 *         only set.
	 */
	public Set<String> getParameterNames() {
		Set<String> nameSet = parameters.keySet();
		return Collections.unmodifiableSet(nameSet);
	}

	/**
	 * Method retrieves value of the given key from the persistent parameters
	 * map.
	 * 
	 * @param name
	 *            Key of persistent parameters map.
	 * @return Value associated with the given key or null if there is no such
	 *         key.
	 */
	public String getPersistentParameter(String name) {

		if (!persistentParameters.containsKey(name)) {
			return null;
		}

		return persistentParameters.get(name);
	}

	/**
	 * @return Returns set of all names contained in persistent parameter map as
	 *         a read - only set.
	 */
	public Set<String> getPersistentParameterNames() {
		Set<String> nameSet = persistentParameters.keySet();
		return Collections.unmodifiableSet(nameSet);
	}

	/**
	 * Stores the given value in the persistent parameters map.
	 * 
	 * @param name
	 *            Key of the given value.
	 * @param value
	 *            Value that will be stored in map.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null value is passed as a key.
	 */
	public void setPersistentParameter(String name, String value) {

		if (name == null) {
			throw new IllegalArgumentException("Null value passed as key.");
		}

		persistentParameters.put(name, value);
	}

	/**
	 * Removes value associate with the given key in the persistent parameters
	 * map.
	 * 
	 * @param name
	 *            Key at which the value will be removed.
	 */
	public void removePersistentParameter(String name) {

		if (name == null) {
			return;
		}

		persistentParameters.remove(name);
	}

	/**
	 * Method retrieves value of the given key from the temporary parameters
	 * map.
	 * 
	 * @param name
	 *            Key of temporary parameters map.
	 * @return Value associated with the given key or null if there is no such
	 *         key.
	 */
	public String getTemporaryParameter(String name) {

		if (!temporaryParameters.containsKey(name)) {
			return null;
		}

		return temporaryParameters.get(name);
	}

	/**
	 * @return Returns set of all names contained in temporary parameter map as
	 *         a read - only set.
	 */
	public Set<String> getTemporaryParameterNames() {
		Set<String> nameSet = temporaryParameters.keySet();
		return Collections.unmodifiableSet(nameSet);
	}

	/**
	 * Stores the given value in the temporary parameters map.
	 * 
	 * @param name
	 *            Key of the given value.
	 * @param value
	 *            Value that will be stored in map.
	 * @throws IllegalArgumentException
	 *             Exception thrown if null value is passed as a key.
	 */
	public void setTemporaryParameter(String name, String value) {

		if (name == null) {
			throw new IllegalArgumentException("Null value passed as key.");
		}

		temporaryParameters.put(name, value);
	}

	/**
	 * Removes value associate with the given key in the temporary parameters
	 * map.
	 * 
	 * @param name
	 *            Key at which the value will be removed.
	 */
	public void removeTemporaryParameter(String name) {

		if (name == null) {
			return;
		}

		temporaryParameters.remove(name);
	}

	/**
	 * Add given cookie to the output cookies list.
	 * 
	 * @param cookie
	 *            Given cookie.
	 */
	public void addRCCookie(RCCookie cookie) {

		if (cookie == null) {
			throw new IllegalArgumentException("Null value passed as cookie.");
		}

		outputCookies.add(cookie);
	}

	/**
	 * Writes given data into the output stream.
	 * 
	 * @param data
	 *            Array of bytes.
	 * @return This {@link RequestContext} object.
	 * @throws IOException
	 *             Exception thrown if an error occurs during writing.
	 */
	public RequestContext write(byte[] data) throws IOException {
		writeHeaderToStream();
		outputStream.write(data, 0, data.length);
		return this;
	}

	/**
	 * Writes given text into the output stream.
	 * 
	 * @param text
	 *            Data type String.
	 * @return This {@link RequestContext} object.
	 * @throws IOException
	 *             Exception thrown if an error occures during writing.
	 */
	public RequestContext write(String text) throws IOException {
		writeHeaderToStream();
		outputStream.write(text.getBytes(charset), 0,
				text.getBytes(charset).length);
		return this;
	}

	/**
	 * Writes header to the stream if it is not already written.
	 * 
	 * @throws IOException
	 *             Exception thrown if an error occurs during writing to stream.
	 */
	private void writeHeaderToStream() throws IOException {

		if (headerGenerated) {
			return;
		}

		charset = Charset.forName(encoding);
		byte[] header = generateHeader();
		outputStream.write(header);
		headerGenerated = true;

	}

	/**
	 * Generates header using currently set header data: encoding, status code,
	 * status text and mime type.
	 * 
	 * @return Returns header as a byte array.
	 */
	private byte[] generateHeader() {
		String appendCharset = mimeType.startsWith("text/")
				? ";charset=" + charset + "\r\n" : "\r\n";
		
		StringBuilder sb = new StringBuilder();
		for (RCCookie cookie : outputCookies) {
			sb.append("Set-Cookie: " + cookie.getName() + "=\""
					+ cookie.getValue() + "\"");

			if (cookie.getDomain() != null) {
				sb.append("; Domain=" + cookie.getDomain());
			}

			if (cookie.getPath() != null) {
				sb.append("; Path=" + cookie.getPath());
			}

			if (cookie.getMaxAge() != null) {
				sb.append("; Max-Age=" + cookie.getMaxAge());
			}

			sb.append("\r\n");
		}
		String cookies = sb.toString();

		String newStr = "HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
				+ "Content-Type: " + mimeType + appendCharset + cookies + "\r\n";
		
//		System.out.println("vracam odgovor: ");
//		System.out.println(newStr);
		return newStr.getBytes(StandardCharsets.ISO_8859_1);
	}

	/**
	 * Sets new value of the mime type variable.
	 * 
	 * @param mimeType
	 *            New mime type value.
	 * @throws RuntimeException
	 *             Exception thrown if user attempts to change this value when
	 *             the header was already generated.
	 */
	public void setMimeType(String mimeType) {

		if (headerGenerated) {
			throw new RuntimeException("Header has already been generated.");
		}

		this.mimeType = mimeType;
	}

	/**
	 * Sets new value to status text variable.
	 * 
	 * @param statusText
	 *            New status text value.
	 * @throws RuntimeException
	 *             Exception thrown if user attempts to change this value when
	 *             the header was already generated.
	 */
	public void setStatusText(String statusText) {

		if (headerGenerated) {
			throw new RuntimeException("Header has already been generated.");
		}

		this.statusText = statusText;
	}

	/**
	 * Sets new value to status code variable.
	 * 
	 * @param statusCode
	 *            New status code value.
	 * @throws RuntimeException
	 *             Exception thrown if user attempts to change this value when
	 *             the header was already generated.
	 */
	public void setStatusCode(int statusCode) {

		if (headerGenerated) {
			throw new RuntimeException("Header has already been generated.");
		}

		this.statusCode = statusCode;
	}

	/**
	 * Sets new encoding value.
	 * 
	 * @param encoding
	 *            New encoding value.
	 * @throws RuntimeException
	 *             Exception thrown if user attempts to change this value when
	 *             the header was already generated.
	 */
	public void setEncoding(String encoding) {

		if (headerGenerated) {
			throw new RuntimeException("Header has already been generated.");
		}

		this.encoding = new String(encoding);
	}

	/**
	 * This static class represents cookies of the request context.
	 * 
	 * @author Lovro Marković
	 *
	 */
	public static class RCCookie {

		/**
		 * Name of the cookie.
		 */
		private String name;

		/**
		 * Value of the cookie.
		 */
		private String value;

		/**
		 * Domain of the cookie.
		 */
		private String domain;

		/**
		 * Path of the cookie.
		 */
		private String path;

		/**
		 * Maximum cookie age
		 */
		private Integer maxAge;

		/**
		 * Constructor for the Cookie object.
		 * 
		 * @param name
		 *            Cookie name.
		 * @param value
		 *            Cookie value.
		 * @param maxAge
		 *            Maximum age of the cookie.
		 * @param domain
		 *            Cookie domain.
		 * @param path
		 *            Cookie path.
		 */
		public RCCookie(String name, String value, Integer maxAge,
				String domain, String path) {
			this.name = name;
			this.value = value;
			this.maxAge = maxAge;
			this.domain = domain;
			this.path = path;
		}

		/**
		 * @return Returns name of the cookie.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return Returns domain of the cookie.
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * @return Returns maximum age of the cookie.
		 */
		public Integer getMaxAge() {
			return maxAge;
		}

		/**
		 * @return Returns path of the cookie.
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @return Returns value of the cookie.
		 */
		public String getValue() {
			return value;
		}
	}
}
