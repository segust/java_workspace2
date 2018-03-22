package _10_20;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.*;

@SuppressWarnings("serial")
public abstract class GenericServlet implements Servlet, ServletConfig, java.io.Serializable {
	private transient ServletConfig config;

	public GenericServlet() {

	}

	public void destroy() {

	}

	public String getInitParameter(String name) {
		return getServletConfig().getInitParameter(name);
	}


	public Enumeration<String> getInitParameterNames() {
		return getServletConfig().getInitParameterNames();
	}

	public ServletConfig getServletConfig() {
		return config;
	}

	public ServletContext getServletContext() {
		return getServletConfig().getServletContext();
	}

	public String getServletInfo() {
		return " ";
	}

	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		this.init(config);
		;
	}

	public void init() throws ServletException {
	}

	public void log(String msg) {
		getServletContext().log(getServletName() + ":" + msg);
	}

	public void log(String message, Throwable t) {
		getServletContext().log(getServletName() + ":" + message, t);
	}

	public abstract void service(ServletRequest req, ServletResponse res) throws ServletException, IOException;

	public String getServletName() {
		return config.getServletName();
	}
}
