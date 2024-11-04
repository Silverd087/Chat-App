package tutoringApp.Models;

public class UserNotFoundException extends Exception{
	public UserNotFoundException(){
		super("user not found");
	}
}
