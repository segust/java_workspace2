package model;

import util.Query;

public class User {
	private String uuid;
	private String username;
	private String info;
	private int work;
	private int follow;
	private int fans;
	private int videoWork;
	private int articleWork;
	private int compilation;
	private boolean done;
	private boolean relation;
	
	public int saveUserInfo(){
		String sql = null;
		Object[] params = null;
		if(this.done){
			sql = "UPDATE `page_rank`.`usr` SET `usrname`=?, `infom`=?, `work`=?, `follow`=?, `fans`=?, "
					+ "`video_work`=?, `article_work`=?, `compilation`=?, `done`='1' WHERE `id`=?";
			params = new Object[]{this.username, this.info, this.work, this.follow, this.fans, this.videoWork, 
					this.articleWork, this.compilation, this.getUuid() };
		}else{
			sql = "UPDATE `page_rank`.`usr` SET `done`='0' WHERE `id`=?";
			params = new Object[]{this.uuid};
		}
		
		return Query.executeDML(sql, params);
	}
	
	@Override
	public String toString() {
		return "User [uuid=" + uuid + ", username=" + username + ", info=" + info + ", work=" + work + ", follow="
				+ follow + ", fans=" + fans + ", videoWork=" + videoWork + ", articleWork=" + articleWork
				+ ", compilation=" + compilation + "]";
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String uuid, String username, String info, int work, int follow, int fans, int videoWork,
			int articleWork, int compilation) {
		super();
		this.uuid = uuid;
		this.username = username;
		this.info = info;
		this.work = work;
		this.follow = follow;
		this.fans = fans;
		this.videoWork = videoWork;
		this.articleWork = articleWork;
		this.compilation = compilation;
	}
	
	public boolean isRelation() {
		return relation;
	}
	public void setRelation(boolean relation) {
		this.relation = relation;
	}
	public boolean isDone() {
		return done;
	}
	public void setDone(boolean done) {
		this.done = done;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getWork() {
		return work;
	}
	public void setWork(int work) {
		this.work = work;
	}
	public int getFollow() {
		return follow;
	}
	public void setFollow(int follow) {
		this.follow = follow;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public int getVideoWork() {
		return videoWork;
	}
	public void setVideoWork(int videoWork) {
		this.videoWork = videoWork;
	}
	public int getArticleWork() {
		return articleWork;
	}
	public void setArticleWork(int articleWork) {
		this.articleWork = articleWork;
	}
	public int getCompilation() {
		return compilation;
	}
	public void setCompilation(int compilation) {
		this.compilation = compilation;
	}
	

}
