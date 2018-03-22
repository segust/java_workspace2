package cn.edu.cqupt.filter;

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

/**
 * 检测用户是否登陆的过滤器
 * 
 * @ClassName: CheckLoginFilter
 * @Description: TODO
 * @author liweiguo
 */
public class CheckLoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String path = req.getRequestURI();
		String servletPath = req.getServletPath();

		// 关于登录的都无需过滤
		if (path.indexOf("login.html") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		if (path.indexOf("css/login.css") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		if (path.indexOf("img") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		if (path.indexOf("js/login.js") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		if (path.indexOf("js/jquery-1.9.1.min.js") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		if (path.indexOf("ConstantHTML/css/xcConfirm.css") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		if (path.indexOf("ConstantHTML/js/xcConfirm.js") > -1) {
			chain.doFilter(request, response);
			return;
		}
		
		//登录UserLoginServlet无需过滤
		if(servletPath.indexOf("/UserLoginServlet") > -1){
			chain.doFilter(request, response);
			System.out.println("过滤器");
			return;
		}

		// 判断是否登录
       //change by lynn 2015/06/19
		if (session.getAttribute("username") == null) {

			// 重定向到登录页面
			resp.sendRedirect(req.getContextPath() + "/jsp/login/login.html");
		} else {
			// 已登录,顺利跳转
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

}