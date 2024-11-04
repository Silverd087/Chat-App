package tutoringApp.Controllers;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tutoringApp.Models.User;
import tutoringApp.Models.UserDao;
import tutoringApp.Models.UserNotFoundException;

public class SceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String username;
	
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;

	// Initialize user authenticated state
		
	public void switchToLoginScene(ActionEvent event) {
		 try {
			root = FXMLLoader.load(getClass().getResource("/tutoringApp/Views/Login.fxml"));
			stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Login");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void switchToSignUpScene(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("/tutoringApp/Views/SignUp.fxml"));
			stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Sign Up");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void switchToDashboardScene(ActionEvent event, String username) {
	    try {
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Dashboard.fxml"));
	    	Parent root = loader.load(); // This line must come before getting the controller
	    	DashboardController dashboardController = loader.getController();
	    	dashboardController.setUsername(username);

	        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.setTitle("Dashboard");
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
    public void loginButtonClicked(ActionEvent event) throws SQLException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        
		// Create a new UserDao instance
		UserDao userDao = new UserDao();

		User user;
		try {
			user = userDao.authenticateUser(username, password);
			if (user != null) {
				// User is authenticated, so you can show the main application screen or perform other actions
				
				switchToDashboardScene(event,username);

			}
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			// User is not authenticated, so you can show an error message
			System.out.println(e);
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("The account doesn't exist.");
			alert.show();		
			
		}


    }
    public void SignUpButtonClicked(ActionEvent event) {
        // Get the username and password entered by the user
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserDao userDao = new UserDao();

        // Attempt to get the user with the provided username
        User user = userDao.getUserByUsername(username);

        if (user == null) {
            // The user does not exist, attempt to register the new user
            try {
				userDao.addUser(new User(username, password));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // Reset the username and password fields
            usernameField.clear();
            passwordField.clear();

            // Display an alert to the user indicating they have been registered
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "User has been registered");
            alert.showAndWait();

            // Switch to the login scene
            switchToLoginScene(event);

        } else {
            // The user already exists, display an alert to the user
            Alert alert = new Alert(Alert.AlertType.ERROR, "User already exists");
            alert.showAndWait();

            // Reset the password field
            passwordField.clear();
            usernameField.clear();
        }
    }

    
}
