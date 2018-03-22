package cn.edu.cqupt.service.fare_management;

import java.util.ArrayList;

import cn.edu.cqupt.beans.Attach;
import cn.edu.cqupt.dao.AttachDAO;

/**
 * 经费管理
 * 
 * @author lsy&yg
 * 
 */
public class AttachService {
	AttachDAO attachDAO = new AttachDAO();

	/** 
	 * 通过一个经费Id查找附件
	 * 
	 * @param fareId
	 * @return
	 */
	public ArrayList<Attach> getAllAttachInAFare(long fareId) {
		return attachDAO.getAllAttachInAFare(fareId);
	}

	/**
	 * 向一个经费管理项目中添加一个附件
	 * 
	 * @param attach
	 * @param fareId
	 * @return
	 */
	public boolean addAttach(Attach attach, int fareId) {
		return attachDAO.addAttach(attach, fareId);
	}

	/**
	 * 删除一个附件
	 * 
	 * @param title
	 * @return
	 */
	public boolean deleteAttachByAttachTitle(int attachId) {
		return attachDAO.deleteAttachByAttachTitle(attachId);
	}

	/**
	 * 通过fareId删除附件
	 * 
	 * @param fareId
	 * @return
	 */
	public boolean deleteAttach(long fareId) {
		return attachDAO.deleteAttachByFareId(fareId);
	}

	/**
	 * 通过fareId 得到所有的title(uuid名)用来删除本地文件
	 * 
	 * @param fareId
	 * @return
	 */
	public ArrayList<String> getAllTitleInAFare(long fareId) {
		ArrayList<String> allTitle = new ArrayList<String>();
		allTitle = attachDAO.getAllTitleInAFare(fareId);
		return allTitle;
	}
	
	/**
	 * 通过附件id获取附件路径
	 * @param attachId
	 * @return
	 */
	public String getAttachPathById(int attachId){
		String attachPath="";
		attachPath=attachDAO.getAttachPathById(attachId);
		return attachPath;
	}

}
