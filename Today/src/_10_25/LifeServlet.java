package _10_25;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import _10_20.GenericServlet;

public class LifeServlet extends GenericServlet{
	private static final long serialVersionUID = -3622656956592470825L;
	private int initVar=0;
	private int serviceVar=0;
	private int destroyVar=0;
	private String name;
	
	public void init(ServletConfig config)throws ServletException{
		super.init(config);
		name=config.getServletName();
		initVar++;
		System.out.println(name+">init():Sservlet被初始化了"+initVar+"次");
	}
	
	public void destroy(){
		destroyVar++;
		System.out.println(name+">destroy():Servlet被销毁了"+destroyVar+"次");
	}
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		serviceVar++;
		System.out.println(name+">service():Servlet共响应了"+serviceVar+"次请求");
		
		String content1="初始化次数："+initVar;
		String content2="响应客户请求次数"+serviceVar;
		String content3="销毁次数："+destroyVar;
		
		response.setContentType("text/html;charset=GB2312");
		
		PrintWriter out=response.getWriter();
		out.print("<html><head><title>LifeServlet</title>");
		out.print("</head><body>");
		out.print("<h1>"+content1+"</h1>");
		out.print("<h1>"+content2+"</h1>");
		out.print("<h1>"+content3+"</h1>");
		out.print("</body></html>");
		out.close();
	}
	
}
