package cn.edu.cqupt.beans;

import java.util.ArrayList;

public class Page {
	private ArrayList<Temperature> tempatureRecords;//当前页面的温度记录
	private ArrayList<Humidity> humidityRecords;//当前页面的温度记录
	private int pageSize;//每页容纳的记录条数
	private int startIndex;//起始索引   用来设置数据库中limit ?,?第一个?
	private int currentPageNum;//当前页码
	private int totalRecords;//总记录条数
	private int totalPage;//总页面数量
	private String url;
	public Page(int currentPageNum, int totalRecords,int pageSize) {
		this.pageSize = pageSize;
		this.currentPageNum = currentPageNum;
		this.totalRecords = totalRecords;
		//计算开始记录的索引
		startIndex = (currentPageNum-1)*pageSize;
		//计算总页面数量
		totalPage = totalRecords%pageSize==0?totalRecords/pageSize:(totalRecords/pageSize+1);
	}

	public ArrayList<Temperature> getTempatureRecords() {
		return tempatureRecords;
	}


	public void setTempatureRecords(ArrayList<Temperature> tempatureRecords) {
		this.tempatureRecords = tempatureRecords;
	}


	public ArrayList<Humidity> getHumodityRecords() {
		return humidityRecords;
	}


	public void setHumidityRecords(ArrayList<Humidity> humodityRecords) {
		this.humidityRecords = humodityRecords;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public int getStartIndex() {
		return startIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPageNum() {
		return currentPageNum;
	}

	public void setCurrentPageNum(int currentPageNum) {
		this.currentPageNum = currentPageNum;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
}
