package tutoringApp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class TutorFormController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private String username;
	
    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField email;
    
    @FXML
    private TextField course;

    @FXML
    private Button btnSubmit;


	public void setUsername(String username) {
		this.username = username;
	}

	@FXML
    public void submitForm(ActionEvent event) {
        String firstName = sanitizeInput(txtFirstName.getText());
        String lastName = sanitizeInput(txtLastName.getText());
        String courseText = sanitizeInput(course.getText());
        String emailText = sanitizeInput(email.getText());

        // Check if any of the required fields are empty
        if (firstName.isEmpty() || lastName.isEmpty() || courseText.isEmpty()||emailText.isEmpty()) {
            // Display a pop-up warning
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        // Call a method to handle form submission, inserting data into the database
        insertStudentIntoDatabase(firstName, lastName,courseText,emailText);
        switchToDashboardScene(event);
        
    }

    private String sanitizeInput(String input) {
        // Convert input to lowercase
        return input.toLowerCase().trim(); // Trim leading and trailing whitespace
    }

    private void insertStudentIntoDatabase(String firstName, String lastName,String course,String email) {
        final String DB_URL = "jdbc:mysql://localhost:3306/user-authentication-demo";
        final String DB_USER = "root";
        final String DB_PASSWORD = "admin";
        String sql = "INSERT INTO tutors (firstname, lastname, courses,email,username) VALUES (?, ?, ?,?,?)";

        try {
            // Explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, course);
                statement.setString(4, email);
                statement.setString(5, username);
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    // Data inserted successfully, show a popup with a green tick icon
                    showSuccessAlert("Data inserted successfully");
                } else {
                    showAlert("Error", "Failed to insert data into the database.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while inserting data into the database.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Customize the alert icon
        
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/tutoringApp/Ressources/images/tick.jpg")));

        alert.showAndWait();
    }
	public void switchToDashboardScene(ActionEvent event) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
