package model;

import java.util.ArrayList;

public class UserPageInfo {
	private int nextPage;
	private boolean isLastPage;
	private ArrayList<String> userlist;
	
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public boolean isLastPage() {
		return isLastPage;
	}
	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	public ArrayList<String> getUserlist() {
		return userlist;
	}
	public void setUserlist(ArrayList<String> userlist) {
		this.userlist = userlist;
	}
	
}
