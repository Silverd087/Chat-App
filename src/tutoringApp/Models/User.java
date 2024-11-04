package tutoringApp.Models;


public class User {
    private String username;
    private String password;
    private static int id;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static int getId() {
		return id;
	}
	public static void setId(int id) {
		User.id = id;
	}
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	


}