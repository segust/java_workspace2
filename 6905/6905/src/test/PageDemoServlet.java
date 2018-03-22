package test;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class PageDemoServlet extends HttpServlet {

	private static final long serialVersionUID = -5262205056018792243L;

	/**
	 * Constructor of the object.
	 */
	public PageDemoServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得前台的传来的当前页码和按多少条分页的参数，获取当前页的数据
		int curPageNum = Integer.parseInt(request.getParameter("curPageNum"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		
		//获取总的个数
		//long sum = 你的Service.getSum();
		//request.setAttribute("sum", sum);
		
		//把curPageNum和pageSize传到DAO的SQL语句的limit ?,? 两个参数赋予 (curPageNum-1)*pageSize,pageSize 两值
		//ArrayList<E> List = 你的Service.search**ByPage(curPageNum, pageSize);
		
		//得到的当前页的数据存到request中
		//request.setAttribute("list", list);
		
		//forward跳转回页面，加上curPageNum和pageSize参数，便于前台JSP获取后进行相关计算
		try {
			String url = "你的页面路径?curPageNum="
					+ curPageNum + "&pageSize=" + pageSize;
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
