package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IDispatcher;
import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class implements WebWorker ineterface. It calculates a sum of two
 * numbers and displays operands and the result of operation in a HTTP table.
 * 
 * @author Lovro Marković
 *
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		int a = 0;
		int b = 0;

		try {
			a = Integer.parseInt(context.getParameter("a"));
		} catch (Exception e) {
			a = 1;
		}

		try {
			b = Integer.parseInt(context.getParameter("b"));
		} catch (Exception e) {
			b = 2;
		}

		int result = a + b;
		String sResult = String.valueOf(result);
		String sA = String.valueOf(a);
		String sB = String.valueOf(b);

		// Set temporary parameters
		context.setTemporaryParameter("result", sResult);
		context.setTemporaryParameter("a", sA);
		context.setTemporaryParameter("b", sB);

		// Call dispatcher
		IDispatcher d = context.getDispatcher();
		d.DispatchRequest("/private/calc.smscr");
	}

}
