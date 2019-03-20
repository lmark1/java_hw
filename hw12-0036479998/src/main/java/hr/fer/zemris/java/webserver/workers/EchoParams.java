package hr.fer.zemris.java.webserver.workers;

import java.util.Set;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class implements IWebWorker. It displays an HTML table from the given
 * context parameters.
 * 
 * @author Lovro Marković
 *
 */
public class EchoParams implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		context.setMimeType("text/html");
		StringBuilder sb = new StringBuilder();

		sb.append("<html><body><table style=\"width:100%\">");
		sb.append("<tr><th>Keys</th><th>Values</th></tr>");

		Set<String> keySet = context.getParameterNames();
		for (String key : keySet) {
			sb.append("<tr>");
			sb.append("<td>" + key + "</td>");
			sb.append("<td>" + context.getParameter(key) + "</td");
			sb.append("<tr>");
		}

		sb.append("</table></body><html>");
		context.write(sb.toString());
	}

}
