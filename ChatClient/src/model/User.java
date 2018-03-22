package model;

public class User {

	private int id;
	private String password;
	private String username;
	private String sex;
	private int age;

	public User(int id, String username, String sex, int age) {
		this.id = id;
		this.username = username;
		this.sex = sex;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getSex() {
		return sex;
	}

	public int getAge() {
		return age;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
