package zmg.bean;

public class DBConfiguration {

	private String driver;
	private String url;
	private String user;
	private String password;
	private String usingDB;
	private int poolMaxSize;
	private int poolMixSize;
	
	
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsingDB() {
		return usingDB;
	}
	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}
	public int getPoolMaxSize() {
		return poolMaxSize;
	}
	public void setPoolMaxSize(int poolMaxSize) {
		this.poolMaxSize = poolMaxSize;
	}
	public int getPoolMixSize() {
		return poolMixSize;
	}
	public void setPoolMixSize(int poolMixSize) {
		this.poolMixSize = poolMixSize;
	}
	
}
