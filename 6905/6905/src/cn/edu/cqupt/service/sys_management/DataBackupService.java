package cn.edu.cqupt.service.sys_management;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.edu.cqupt.db.DBConnection;

public class DataBackupService {
	static InputStream is = null;

	public void doDataBackup(String filename){
		//获取url,user,password
		String[] values = DBConnection.getValues();
		values = getIP(values);
		
		String cmd = "cmd /c mysqldump -h"+values[0]+" -u"+values[1]+" -p"+values[2]+" 6905>"+filename+".sql";
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
	}
	
	public String[] getIP(String[] values){
		String[] info = values[0].split(":");
		String[] ip = info[2].split("//");
		values[0] = ip[1];
		return values;
	}
	
	public static void main(String[] args) {
		DataBackupService dataBackupService=new DataBackupService();
		dataBackupService.doDataBackup("d:/yes");
	}
}