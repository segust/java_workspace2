package cn.edu.cqupt.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFile {

	/**
	 * 解决不同浏览器文件下载乱码
	 * @param request
	 * @param fileName下载时出现的文件名
	 * @author Liuhs
	 * @return
	 */
	public static String getNormalFilename(HttpServletRequest request,String fileName) {
		String agent = request.getHeader("User-Agent");
		try {
			//System.out.println("agent = "+agent);
			if (agent != null &&(agent.indexOf("Edge") != -1 ||
									agent.indexOf("MSIE") != -1 ||
									agent.indexOf("Trident") != -1)) {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	}
	
	/**
	 * 设置响应头（response head），向用户传送下载流
	 * @param response
	 * @param path
	 * @author lyh
	 */
	public static void launchDownloadStream(HttpServletResponse response, String absolutePath, String fileName){
		
		FileInputStream in = null;
		ServletOutputStream out = null;
		try{
			in = new FileInputStream(absolutePath);
			out = response.getOutputStream();
			
			// 设置响应头，控制浏览器下载文件
			response.setHeader("content-disposition","attachment;filename=" + fileName);
			response.setContentType("application/vnd.ms-excel");
			
			// 创建缓冲区
			byte buff[] = new byte[1024];
			int len = 0;
			// 循环将输入流输入到缓冲区中
			while ((len = in.read(buff)) > 0) {
				out.write(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
