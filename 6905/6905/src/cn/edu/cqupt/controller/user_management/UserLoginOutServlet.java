package cn.edu.cqupt.controller.user_management;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLoginOutServlet extends HttpServlet {

	private static final long serialVersionUID = 6219963099815322722L;

	public UserLoginOutServlet() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//点击退出或关闭页面，清除session
		HttpSession session = request.getSession();
		if(session != null){
			session.removeAttribute("username");
			session.removeAttribute("password");
		}
		response.getWriter().write("1");
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
