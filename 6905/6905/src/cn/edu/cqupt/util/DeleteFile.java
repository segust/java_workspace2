package cn.edu.cqupt.util;

import java.io.File;

public class DeleteFile {

	 /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public static boolean deleteFile(File file) {  
        boolean flag = false;  
        // 路径为文件且不为空则进行删除  
        if(file.isFile() && file.exists()){
        	flag = file.delete();
        }
        return flag;  
    }  
	
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public static boolean deleteDirectory(String sPath) {  
    	boolean flag = false;
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {  
            sPath = sPath + File.separator;  
        }  
        File dirFile = new File(sPath);  
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles(); 
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {
                flag = deleteFile(files[i]); 
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }
}
