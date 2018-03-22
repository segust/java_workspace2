package cn.edu.cqupt.util;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

public class CrossPageCheck {
	/**
	 * 将每次前台提交的已经选择的id保存，注意首次进入没有该参数，也就不用执行后面比较选择id和当前页id
	 * @param request
	 * @param curPageIdArray(int[] need to cast into long[])
	 * @return checkedIdStr
	 * @author Liuhs
	 */
	public static String getCheckedIdStr(HttpServletRequest request,long[] curPageIdArray) {
		String checkedIdStr = "";
		String unCheckedIdStr = "";
		if (request.getParameter("checkedIdStr") != null) {
			checkedIdStr = request.getParameter("checkedIdStr").trim();
			unCheckedIdStr = request.getParameter("unCheckedIdStr").trim();
			
			// 在后台去掉最后一个逗号,同时考虑空串的情况
			if (!checkedIdStr.equals("")) {
				checkedIdStr += "]";
				checkedIdStr = checkedIdStr.replace(",]", "");
			} else {
				checkedIdStr = "-1";
			}
			if (!unCheckedIdStr.equals("")) {
				unCheckedIdStr += "]";
				unCheckedIdStr = unCheckedIdStr.replace(",]", "");
			} else {
				unCheckedIdStr = "-2";
			}
			
			// 将前台传来的已选择的字符串id切分成数组
			String[] checkedIdStrArray = checkedIdStr.split(",");
			// 将前台传来的当前页没有选择的字符串id切分成数组
			String[] unCheckedIdStrArray = unCheckedIdStr.split(",");

			// 去除重复元素和当前页没选择的元素
			long[] checkedIdArray = CrossPageCheck.deleteRepeatElements(
					checkedIdStrArray, unCheckedIdStrArray);

			// 比较当前页Id(curPageIdArray)和选择id(checkedIdArray)是否一致，得到1,0,1...数组
			String checkedIdFlagStr = CrossPageCheck.getCheckedIdFlagStr(
					checkedIdArray, curPageIdArray);
			request.setAttribute("checkedIdFlagStr", checkedIdFlagStr);
			
			// 将已选择元素放回前台
			checkedIdStr = "";
			if (checkedIdArray.length != 0) {
				for (int i = 0; i < checkedIdArray.length; i++) {
					checkedIdStr += checkedIdArray[i] + ",";
				}
				checkedIdStr += "]";
				checkedIdStr = checkedIdStr.replace(",]", "");
			}
		}
		return checkedIdStr;
	}
	
	/**
	 * 删除数组重复的和当前页没选择的id号
	 * 
	 * @param checkedId
	 *            已经选择的id数组
	 * @return
	 * @author Liuhs
	 */
	public static long[] deleteRepeatElements(String[] checkedId,
			String[] unCheckedId) {
		// String[]转成long[]
		long[] checkedIdArray = new long[checkedId.length];
		for (int i = 0; i < checkedIdArray.length; i++) {
			checkedIdArray[i] = Long.parseLong(checkedId[i]);
		}
		long[] unCheckedIdArray = new long[unCheckedId.length];
		for (int i = 0; i < unCheckedIdArray.length; i++) {
			unCheckedIdArray[i] = Long.parseLong(unCheckedId[i]);
		}

		Arrays.sort(checkedIdArray);
		ArrayList<Long> unrepeatCheckedIdList = new ArrayList<Long>();

		// 去除相同的id
		long curId = checkedIdArray[0];
		for (int i = 0; i < checkedIdArray.length; i++) {
			// 最后一个数不管什么情况直接加进来，避免退出循坏不加
			if (i == checkedIdArray.length - 1) {
				// 如果最后一个数和标志数不同，标志数也需要加进来
				if (curId != checkedIdArray[i])
					unrepeatCheckedIdList.add(curId);
				unrepeatCheckedIdList.add(checkedIdArray[i]);
			} else {
				if (curId == checkedIdArray[i])
					continue;
				else {
					unrepeatCheckedIdList.add(curId);
					curId = checkedIdArray[i];
				}
			}
		}

		// 从unrepeatCheckedIdList中去除当前页没选择的id号
		Arrays.sort(unCheckedIdArray);
		int j = 0;
		for (int i = 0; i < unrepeatCheckedIdList.size(); ) {
			if (j == unCheckedIdArray.length)
				break;
			if (unrepeatCheckedIdList.get(i) == unCheckedIdArray[j]) {
				unrepeatCheckedIdList.set(i, (long) -3);
				j++;
				i++;
			}
			else if(unrepeatCheckedIdList.get(i) > unCheckedIdArray[j])
				j++;
			else
				i++;
		}
		ArrayList<Long> finalCheckedIdList = new ArrayList<Long>();
		for (int i = 0; i < unrepeatCheckedIdList.size(); i++) {
			if (unrepeatCheckedIdList.get(i) != -3)
				finalCheckedIdList.add(unrepeatCheckedIdList.get(i));
		}

		// list转成long[]
		long[] finalCheckedIdArray = new long[finalCheckedIdList.size()];
		for (int i = 0; i < finalCheckedIdArray.length; i++) {
			finalCheckedIdArray[i] = finalCheckedIdList.get(i);
		}
		return finalCheckedIdArray;
	}

	/**
	 * 跨页选取时已选择的id和当前页每条记录的id比较，返回比如1,0,0,1...数组
	 * 
	 * @param checkedId
	 *            已经选择的id数组
	 * @param curPageId
	 *            当前页的id数组
	 * @return
	 * @author Liuhs
	 */
	public static String getCheckedIdFlagStr(long[] checkedId, long[] curPageId) {
		String checkedIdFlagStr = "";
		// 保证两个比较对象都已排好序
		// checkedId在上一步已经排好序
		Arrays.sort(curPageId);

		// 比较,得到比如1,0,0...,长度为当前页id长度的字符串
		int i = 0;
		if (checkedId.length != 0) {
			for (int j = 0; j < curPageId.length;) {
				if (i == checkedId.length)
					break;
				if (curPageId[j] == checkedId[i]) {
					checkedIdFlagStr += "1,";
					i++;
					j++;
					continue;
				} else if (curPageId[j] > checkedId[i]) {
					i++;
					continue;
				} else {
					checkedIdFlagStr += "0,";
					j++;
					continue;
				}
			}
			checkedIdFlagStr += "]";
			checkedIdFlagStr = checkedIdFlagStr.replace(",]", "");
		}

		return checkedIdFlagStr;
	}
}
