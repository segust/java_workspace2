package cn.edu.cqupt.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetError {
	public static void limitVisit(HttpServletRequest request,HttpServletResponse response) {
		String lastPage = request.getHeader("Referer");
		request.setAttribute("limitVisitFlag", 1);
		request.setAttribute("lastPage", lastPage);
		try {
			request.getRequestDispatcher("error.jsp").forward(request,
					response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
