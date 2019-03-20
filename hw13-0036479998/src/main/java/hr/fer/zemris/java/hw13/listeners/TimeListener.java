package hr.fer.zemris.java.hw13.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class implements Context listener. It listens for how long this web
 * application is running.
 * 
 * @author Lovro Marković
 *
 */
@WebListener
public class TimeListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Long val = System.currentTimeMillis();
		sce.getServletContext().setAttribute("start", val);
		System.out.println("Hello from listener");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// Do nothing
	}
}