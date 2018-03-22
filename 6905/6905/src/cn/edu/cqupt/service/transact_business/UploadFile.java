package cn.edu.cqupt.service.transact_business;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.edu.cqupt.beans.Contract;
import cn.edu.cqupt.beans.InApply;
import cn.edu.cqupt.dao.InApplyDAO;
import cn.edu.cqupt.util.MyDateFormat;

public class UploadFile{
	
	/**
	 * 上传文件到服务器上面，并返回文件的绝对路径+文件名以及前段传来的parameter的name和value。
	 * 其中文件保存在uploadFilePlace下面
	 * @param request
	 * @param response
	 * @return 存放parameter的hash map，其中文件路径在hashMap中的key是"fileName"
	 * @author LiangYiHuai
	 */
	 public Map<String,String> uploadFile(HttpServletRequest request, HttpServletResponse response){
		String filePathAndName= null;
		
		Map<String, String> parameters = new HashMap<String, String>();
		
		response.setContentType("text/plain");
		
		//第一个是暂时的缓存文件名
		//第二个是存放文件的地方
		String tempFilePath = "tempUploadFilePlace";
		String tempFilePath2 = "uploadFilePlace";
		//获取绝对路径
		tempFilePath = request.getSession().getServletContext().getRealPath("/") +"/"+ tempFilePath;
		tempFilePath2 =request.getSession().getServletContext().getRealPath("/") + "/"+tempFilePath2;
		try{
			//创建一个基于硬盘的FileItem工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//设置向硬盘写数据时所用的缓冲区的大小，此处为4k
			factory.setSizeThreshold(4*1024);
			//判断是否有没有这个文件目录
			File tempFile = new File(tempFilePath);
			if(!tempFile.exists() && !tempFile.isDirectory()){
				tempFile.mkdir();
			}
			//设置暂时缓存目录
			factory.setRepository(tempFile);
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			//设置文件最大不能超过1G
			upload.setSizeMax(1024*1024*1024);
			
			List<FileItem> items = upload.parseRequest(request);
			
			Iterator<FileItem> iter = items.iterator();
			
			while(iter.hasNext()){
				FileItem item = iter.next();
				if(item.isFormField()){
					String name = item.getFieldName();
					String value = item.getString();
					parameters.put(name, value);
//					System.out.println("/************UploadFile***********/");///////////////////
//					System.out.println("name = "+name+"; value = "+value);///////////////////////
				}else{
					//获取文件名
					String fileName = item.getName();
					int index = fileName.lastIndexOf("\\");
					fileName = fileName.substring(index+1, fileName.length());
					long fileSize = item.getSize();
					
					if("".equals(fileName) && fileSize==0){
						return null;
					}
					
					//判断并创建文件夹tempFilePath2
					File temp = new File(tempFilePath2);
					if(!temp.exists()&&!temp.isDirectory()){
						temp.mkdir();
					}
					
					filePathAndName = tempFilePath2+File.separator+fileName;
					File tempFile2 = new File(filePathAndName);
					
					//将文件路径放到map中
					parameters.put("fileName", filePathAndName);
					//将上传的文件放到tempFilePath2目录下面
					try {
						item.write(tempFile2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return parameters;
	}

	 /**
		 * 上传文件到服务器上面，并返回文件的绝对路径+文件名以及前段传来的parameter的name和value。
		 * 其中文件保存在uploadFilePlace下面
		 * @param request
		 * @param response
		 * @return 存放parameter的hash map，其中文件路径在hashMap中的key是"fileName"
		 * @author LiangYiHuai
		 */
		 public Map<String,String> uploadFileInDataRecover(HttpServletRequest request, HttpServletResponse response){
			String filePathAndName= null;
			
			Map<String, String> parameters = new HashMap<String, String>();
			
			response.setContentType("text/plain");
			
			//第一个是暂时的缓存文件名
			//第二个是存放文件的地方
			String tempFilePath = "tempUploadFilePlace";
			String tempFilePath2 = "uploadFilePlace";
			//获取绝对路径
//			tempFilePath = request.getSession().getServletContext().getRealPath(tempFilePath); 
//			tempFilePath2 = request.getSession().getServletContext().getRealPath(tempFilePath2);
			
			tempFilePath = request.getSession().getServletContext().getRealPath("/") +"/"+ tempFilePath;
			tempFilePath2 =request.getSession().getServletContext().getRealPath("/") + "\\"+tempFilePath2;
			try{
				//创建一个基于硬盘的FileItem工厂
				DiskFileItemFactory factory = new DiskFileItemFactory();
				//设置向硬盘写数据时所用的缓冲区的大小，此处为4k
				factory.setSizeThreshold(4*1024);
				//判断是否有没有这个文件目录
				File tempFile = new File(tempFilePath);
				if(!tempFile.exists() && !tempFile.isDirectory()){
					tempFile.mkdir();
				}
				//设置暂时缓存目录
				factory.setRepository(tempFile);
				
				ServletFileUpload upload = new ServletFileUpload(factory);
				//设置文件最大不能超过1G
				upload.setSizeMax(1024*1024*1024);
				
				List<FileItem> items = upload.parseRequest(request);
				
				Iterator<FileItem> iter = items.iterator();
				
				while(iter.hasNext()){
					FileItem item = iter.next();
					if(item.isFormField()){
						String name = item.getFieldName();
						String value = item.getString();
						parameters.put(name, value);
//						System.out.println("/************UploadFile***********/");///////////////////
//						System.out.println("name = "+name+"; value = "+value);///////////////////////
					}else{
						//获取文件名
						String fileName = item.getName();
						int index = fileName.lastIndexOf("\\");
						fileName = fileName.substring(index+1, fileName.length());
						long fileSize = item.getSize();
						
						if("".equals(fileName) && fileSize==0){
							return null;
						}
						
						//判断并创建文件夹tempFilePath2
						File temp = new File(tempFilePath2);
						if(!temp.exists()&&!temp.isDirectory()){
							temp.mkdir();
						}
						
						filePathAndName = tempFilePath2+File.separator+fileName;
//						System.out.println("tempFilePath2 = "+tempFilePath2);///////////////////////
//						System.out.println("File.separator = "+File.separator);/////////////////////
//						System.out.println("fileName = "+fileName);/////////////////////////////////
//						System.out.println("filePath and name = "+filePathAndName);/////////////////
						File tempFile2 = new File(filePathAndName);
						
						//将文件路径放到map中
						parameters.put("fileName", filePathAndName);
						//将上传的文件放到tempFilePath2目录下面
						try {
							item.write(tempFile2);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			
			}catch(Exception e){
				e.printStackTrace();
			}
			return parameters;
		}
}
