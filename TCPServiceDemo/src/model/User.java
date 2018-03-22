package model;

public class User {
	private String username;
	private String password;

	public User(String message) {
		String[] mes = message.split("#");
		username = mes[0];
		password = mes[1];
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
