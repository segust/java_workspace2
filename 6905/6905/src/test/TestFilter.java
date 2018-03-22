package test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestFilter implements Filter {

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		 HttpServletRequest req = (HttpServletRequest)request;
	        HttpSession session = req.getSession();
		if(session.getAttribute("name")!=null && session.getAttribute("pwg")!=null){
			System.out.print(request.getParameter("name")+"******");
			 chain.doFilter(request, response);
		}else{
			HttpServletResponse res = (HttpServletResponse)response;
			res.sendRedirect(req.getContextPath()+"/jsp/login/login.jsp");
			System.out.print("拦截------");
		}
		
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.print("adfasf");
	}

}
