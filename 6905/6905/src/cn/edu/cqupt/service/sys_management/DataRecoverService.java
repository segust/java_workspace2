package cn.edu.cqupt.service.sys_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.cqupt.db.DBConnection;
import cn.edu.cqupt.util.StringUtil;

public class DataRecoverService {
	static InputStream is = null;
	
	public boolean readSQLFile(String filename) {
		//获取url,user,password
		filename = StringUtil.translateAbsolutely(filename);
		String[] values = DBConnection.getValues();
		values = getIP(values);
		
		String cmd = "cmd /c mysql -h"+values[0]+" -u"+values[1]+" -p"+values[2]+" 6905<"+filename;
		Runtime javaRuntime = Runtime.getRuntime();
		try {
			Process p =javaRuntime.exec(cmd);
			is = p.getInputStream(); 
			BufferedReader br1 = new  BufferedReader(new  InputStreamReader(is)); 
			String line1 = null;
		    while ((line1 = br1.readLine()) != null) {  
                if (line1 != null){
                	System.out.println("=AA==========line1======"+line1);
                }  
            }  
         } catch (IOException e) {  
              e.printStackTrace();  
         }  
         finally{  
              try {  
                is.close();  
              } catch (IOException e) {  
                 e.printStackTrace();  
             }  
           } 
		boolean flag = true;
		System.gc();
		return flag;
    }
	
	public String[] getIP(String[] values){
		String[] info = values[0].split(":");
		String[] ip = info[2].split("//");
		values[0] = ip[1];
		return values;
	}
	
	public static void main(String[] args) {
		DataRecoverService dataRecoverService=new DataRecoverService();
		dataRecoverService.readSQLFile("D:\\tomcat8\\apache-tomcat-8.0.23\\webapps\\6905\\uploadFilePlace\\2015年09月08日14时25分23秒.sql");
	}
}