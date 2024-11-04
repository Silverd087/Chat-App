package tutoringApp.Controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.text.DateFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tutoringApp.Models.CalendarEvent;
import tutoringApp.Models.CalendarEventDao;

public class CalendarController{
	
    @FXML
	public Pane calendarPane;
    @FXML ListView<CalendarEvent> events;
    
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String username;
	
	@FXML 
	private Label usernameLabel;
    @FXML
    private Pane calendarView;

    private FullCalendarView fullCalendarView;


    public void setFullCalendarView(FullCalendarView fullCalendarView) {
        this.fullCalendarView = fullCalendarView;
    }
    public void addFullCalendarView(FullCalendarView fullCalendarView) {
        calendarView.getChildren().add(fullCalendarView.getView());
    }
   	
    
    public Label getUsernameLabel() {
		return usernameLabel;
	}

	public void setUsernameLabel(Label usernameLabel) {
		this.usernameLabel = usernameLabel;
	}
	public void setUsernameLabelText(String username) {
		this.usernameLabel.setText(username);;
	}

	private void populateEventsListView(List<CalendarEvent> events) {
        this.events.getItems().clear();
        this.events.getItems().addAll(events);
    }
    public void displayEventsForDateAndName(LocalDate date,String username) {
            // Get the events for the selected date
        	CalendarEventDao calendarEventDao = new CalendarEventDao();
            List<CalendarEvent> events = calendarEventDao.findByDateAndName(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), username);
            // Display the events in the ListView
            this.events.getItems().clear();
            this.events.getItems().addAll(events);
        }
    public boolean checkForCalendarEvent(LocalDate date, String username) {
        // Get the list of CalendarEvents for the given date and username
    	CalendarEventDao calendarEventDao = new CalendarEventDao();
        List<CalendarEvent> events = calendarEventDao.findByDateAndName(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), username);
        // Check if there are any events for the given date and username
        if (events.isEmpty()) {
            return false; // No events found
        } else {
            return true; // One or more events found
        }
    }
    
	
	public void setUsernameLabel(String usernameLabel) {
		this.usernameLabel.setText(usernameLabel);;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void switchToChatScene(ActionEvent event) {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Chat.fxml"));
            root = loader.load();

	    	ChatController chatController = loader.getController();
	    	chatController.setUsername(username);			
	    	stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Chat");
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
    public void switchToSettingsScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Profile.fxml"));
            root = loader.load();

	    	ProfileController profileController = loader.getController();
	    	profileController.setUsername(usernameLabel.getText());
	    	profileController.loadUserProfile(usernameLabel.getText());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public void switchToTutorFormScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/TutorForm.fxml"));
            root = loader.load();

	    	TutorFormController TutorFormController = loader.getController();
	    	TutorFormController.setUsername(usernameLabel.getText());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Tutor Form");
            stage.show();
        } catch (IOException e) {
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
