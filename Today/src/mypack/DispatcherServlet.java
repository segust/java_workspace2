package mypack;

import javax.servlet.*;
import java.io.*;

@SuppressWarnings("serial")
public class DispatcherServlet extends GenericServlet{
	private String target="/hello.jsp";
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		request.setAttribute("USER", username);
		request.setAttribute("PASSWORD", password);
		
		ServletContext context=getServletContext();
		RequestDispatcher dispatcher=context.getRequestDispatcher(target);
		dispatcher.forward(request, response);
	}

}
