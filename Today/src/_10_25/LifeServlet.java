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
		System.out.println(name+">init():Sservlet����ʼ����"+initVar+"��");
	}
	
	public void destroy(){
		destroyVar++;
		System.out.println(name+">destroy():Servlet��������"+destroyVar+"��");
	}
	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		serviceVar++;
		System.out.println(name+">service():Servlet����Ӧ��"+serviceVar+"������");
		
		String content1="��ʼ��������"+initVar;
		String content2="��Ӧ�ͻ��������"+serviceVar;
		String content3="���ٴ�����"+destroyVar;
		
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
