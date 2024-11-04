package tutoringApp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

public class ProfileController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;

    @FXML
    private Label nameLabel;
    
    @FXML
    private Label lastNameLabel;

    @FXML
    private Label emailLabel;
    
    @FXML
    private Label courseCodesLabel;
    
    private String username;
    

	public void setUsername(String username) {
		this.username = username;
	}

	public void loadUserProfile(String name) {
        // Database connection parameters
        final String DB_URL = "jdbc:mysql://localhost:3306/user-authentication-demo";
        final String DB_USER = "root";
        final String DB_PASSWORD = "admin";


        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Prepare SQL statement to retrieve user profile information
            String sql = "SELECT * FROM tutors WHERE username=?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);

            // Execute query
            resultSet = statement.executeQuery();

            // Check if result set contains data
            if (resultSet.next()) {
                // Extract user profile information from result set
                String retrievedName = resultSet.getString("firstname");
                String retrievedLastName = resultSet.getString("lastname");
                String retrievedEmail = resultSet.getString("email");


                // Search for courses by student name
                String courses = searchCoursesByStudentName(name);

                // Display user profile information in the GUI
                nameLabel.setText(retrievedName);
                lastNameLabel.setText(retrievedLastName);
                emailLabel.setText(retrievedEmail);
                courseCodesLabel.setText(courses);
            } else {
                // If profile is not found, display an error message
                nameLabel.setText("User not found");
                courseCodesLabel.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close database resources
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to search for courses by student name
    private String searchCoursesByStudentName(String studentName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Establish database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user-authentication-demo", "root", "admin");

            // Prepare SQL statement to select courses by student name
            String sql = "SELECT courses FROM tutors WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentName);

            // Execute query
            rs = pstmt.executeQuery();

            // Concatenate courses into a single string
            StringBuilder courses = new StringBuilder();
            while (rs.next()) {
                String course = rs.getString("courses");
                if (course != null && !course.isEmpty()) {
                    courses.append(course).append("/ ");
                }
            }

            // Remove trailing comma and space
            if (courses.length() > 0) {
                courses.delete(courses.length() - 2, courses.length());
            }

            return courses.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            // Close database resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void switchToCalendarScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Calendar.fxml"));
            root = loader.load();

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            CalendarController controller = loader.getController();
            controller.setUsername(username);
            controller.setUsernameLabel(username);
            controller.addFullCalendarView(new FullCalendarView(YearMonth.now(),controller,username));
    		stage.setTitle("Calendar");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void switchToChatScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Chat.fxml"));
            root = loader.load();

	    	ChatController chatController = loader.getController();
	    	chatController.setUsername(username);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Chat");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
	
	@FXML
	public void handleLogoutButtonAction(ActionEvent event) {

		    try {
		        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Login.fxml"));
		        root = loader.load();

		        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		        scene = new Scene(root);
		        stage.setScene(scene);
		        stage.setTitle("Login");
		        stage.show();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
}