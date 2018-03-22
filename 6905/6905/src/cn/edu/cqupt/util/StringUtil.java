package cn.edu.cqupt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

public class StringUtil {
	
	public static final String UPLOAD_FOLDER= "uploadFilePlace";
	
	/**
	 * 新入库
	 */
	public static final String NEW_IN = "新入库";
	
	/**
	 * 更新入库
	 */
	public static final String UPDATE_IN = "更新入库";
	
	/**
	 * 轮换入库
	 */
	public static final String BORROW_IN = "轮换入库";
	
	/**
	 * 调拨出库,直接出库
	 */
	public static final String DIRECT_OUT = "调拨出库";
	
	/**
	 * 更新出库
	 */
	public static final String UPDATE_OUT = "更新出库";
	
	/**
	 * 轮换出库
	 */
	public static final String BORROW_OUT = "轮换出库";
	
	/**
	 * 发料调拨出库
	 */
	public static final String DIRECT_OUTLIST = "发料调拨出库";
	
	/**
	 * 发料轮换出库
	 */
	public static final String BORROW_OUTLIST = "发料轮换出库";
	
	/**
	 * 发料更新出库
	 */
	public static final String UPDATE_OUTLIST = "发料更新出库";
	
	/**
	 * 导出excel的后缀名
	 */
	public static final String SUFFIX_EXECL = "whms";
	
	/**
	 * 已入库
	 */
	public static final String IN_WARED = "已入库";
	
	/**
	 * 已出库
	 */
	public static final String OUT_WARED = "已出库";
	
	public static boolean isEmpty(String str)
	{
		if("".equals(str)||str==null||str.trim().equals("")){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isNotEmpty(String str)
	{
		if(!"".equals(str)&&str!=null&&!"null".equals(str)&&!"/*/*".equals(str)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 组合sql语句
	 * @param tableName 数据库表的名字
	 * @param headline 数据库表的标题行
	 * @return
	 * @author LiangYH
	 */
	public static String combineSQLString(String tableName,ArrayList<String> headline){
		
		StringBuilder sqlBuilder = new StringBuilder();
//		sqlBuilder.append("Insert Into "+tableName+" Values(");
		sqlBuilder.append("Insert Into "+tableName+"(");
		
		int columnLen = headline.size();
		for(int i = 0; i  < columnLen-1; i++){
			sqlBuilder.append(headline.get(i)+",");
		}
		//单独处理最后一个
		sqlBuilder.append(headline.get(columnLen-1)+")Values(");
		for(int i = 0; i < columnLen-1; i++){
			sqlBuilder.append("?,");
		}
		//单独处理最后一个
		sqlBuilder.append("?)On DUPLICATE KEY UPDATE ");
		String tempString = null;
		ArrayList<String> tempArray = headline;
		for(int i = 0; i < columnLen-1; i++){
			tempString = tempArray.get(i);
			sqlBuilder.append(tempString+"=VALUES("+tempString+"),");
		}
		//单独处理最后一个
		String lastString = tempArray.get(columnLen-1);
		sqlBuilder.append(lastString+"=VALUES("+lastString+")");
				
		return sqlBuilder.toString();
	}
	
	/**
	 * 解析JSON
	 * @param content 前台使用json传来的二维数组
	 * @return 解析之后的List<ArrayList<String>>
	 * @author LiangYH
	 */
	public static List<ArrayList<String>> resolveJSONToDyadic(String content){
		List<ArrayList<String>> targetDyadic = new ArrayList<ArrayList<String>>();
		JSONArray ja = JSONArray.fromObject(content);
		int size = ja.size();
		for(int i = 0; i < size; i++){
			JSONArray jaTemp = ja.getJSONArray(i);
			ArrayList<String> tempList = new ArrayList<String>();
			int tempSize = jaTemp.size();
			for(int k = 0; k < tempSize; k++){
				tempList.add(jaTemp.getString(k));
			}
			targetDyadic.add(tempList);
		}
		return targetDyadic;
	}
	
	/**
	 * 将路径中的'\'转换为'/'
	 * */
	public static String translateAbsolutely(String text){
		String info = new String();
		for (int i = 0; i < text.length(); i++) {
			if(text.charAt(i)=='\\'){
				info+="/";
			}else{
				info+=text.charAt(i);
			}
		}
		return info;
	}
	
	/**
	 * 在服务器目录下生成文件夹
	 * 如果foldName是null或者是“”，则使用默认文件夹名字
	 * @param request Servlet请求对象
	 * @param foldName 文件夹名字
	 * @return 生成的文件夹绝对路径
	 * @author LiangYH
	 */
	public static String createFold(HttpServletRequest request, String foldName){
		String tempFilePath = "";
		if(foldName == null || "".equals(foldName)){
			foldName = StringUtil.UPLOAD_FOLDER;
		}
		tempFilePath = request.getSession().getServletContext().getRealPath("/")
				+ foldName;
		
		File tempFile = new File(tempFilePath);
		if (!tempFile.exists() && !tempFile.isDirectory()) {
			//生成文件夹
			tempFile.mkdir();
		}
		return tempFilePath;
	}
	
}

