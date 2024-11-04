package tutoringApp.Models;

public class Tutor {
    private int id;
    private String username;
    private String password;
    private String[] courses;
	
    
    
    public Tutor(int id, String username, String password, String[] courses) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.courses = courses;
	}
    public Tutor() {}
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public String[] getCourses() {
		return courses;
	}
	public void setCourses(String[] courses) {
		this.courses = courses;
	}

    
}