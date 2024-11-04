package tutoringApp.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.mysql.cj.conf.IntegerProperty;

import tutoringApp.Controllers.CalendarController;
import tutoringApp.Models.CalendarEvent;
import tutoringApp.Models.CalendarEventDao;
import tutoringApp.Models.TutorDao;
import tutoringApp.Models.TutoringSession;
import tutoringApp.Models.TutoringSessionDao;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DashboardController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
    @FXML
    private Label usernameLabel; // Label to display the username
    
    @FXML
    private TableView<TutoringSession> tutoringSessionTable;
    @FXML
    private TableColumn<TutoringSession,Integer> idCol;
    @FXML
    private TableColumn<TutoringSession,String> nameCol;
    @FXML
    private TableColumn<TutoringSession,String> courseCol;
    @FXML
    private TableColumn<TutoringSession,String> chapterCol;
    @FXML
    private TableColumn<TutoringSession,String> timeCol;
    @FXML
    private TableColumn<TutoringSession,String> dateCol;
    @FXML
    private TableColumn<TutoringSession,String> statusCol;

    ObservableList<TutoringSession> tutoringSessionList = FXCollections.observableArrayList();
        
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	addAllTutoringSessionsToTableView();
    	}
    
    // Method to set the username on the label
    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void handleAddSessionButtonAction() {
        TutorDao tutorDao = new TutorDao();
        if (tutorDao.checkIfUserIsTutor(usernameLabel.getText())) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/TutoringSessionForm.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage();
                stage.initOwner(this.stage);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setScene(new Scene(parent));
                TutoringSessionFormController controller = loader.<TutoringSessionFormController>getController();
                controller.setUsername(usernameLabel.getText());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Permission Denied");
            alert.setHeaderText("You do not have permission to access this feature.");
            alert.setContentText("Your account does not have tutor privileges.");
            alert.showAndWait();
        }
    }

    public void handleEditButtonAction() {
        TutoringSession selectedSession = tutoringSessionTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No tutoring session selected");
            alert.setContentText("Please select a tutoring session to edit.");
            alert.showAndWait();
            return;
        }
        if (!selectedSession.getTutorName().equals(usernameLabel.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Permission denied");
            alert.setContentText("You do not have permission to edit this tutoring session.");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/TutoringSessionForm.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.initOwner(this.stage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(parent));
            TutoringSessionFormController controller = loader.<TutoringSessionFormController>getController();
            controller.setTutoringSession(selectedSession);
            controller.setUsername(usernameLabel.getText());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void handleDeleteButtonAction(ActionEvent event) {
        TutoringSession selectedSession = tutoringSessionTable.getSelectionModel().getSelectedItem();
        if (selectedSession == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No tutoring session selected");
            alert.setContentText("Please select a tutoring session to delete.");
            alert.showAndWait();
            return;
        }

        if (!selectedSession.getTutorName().equals(usernameLabel.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Permission denied");
            alert.setContentText("You do not have permission to delete this tutoring session.");
            alert.showAndWait();
            return;
        }

        TutoringSessionDao tutoringSessionDao = new TutoringSessionDao();
        CalendarEventDao calendarEventDao = new CalendarEventDao();

        // Delete the corresponding CalendarEvent
        CalendarEvent calendarEvent = calendarEventDao.getCalendarEventByTutoringSession(selectedSession,usernameLabel.getText());
        System.out.println(calendarEvent);
        if (calendarEvent != null) {
            if (calendarEventDao.deleteCalendarEvent(calendarEvent)) {
                System.out.println("CalendarEvent deleted successfully");
            } else {
                System.out.println("Failed to delete CalendarEvent");
            }
        }

        // Delete the TutoringSession
        if (tutoringSessionDao.deleteTutoringSession(selectedSession)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Tutoring session deleted");
            alert.setContentText("The tutoring session has been successfully deleted.");
            alert.showAndWait();
            addAllTutoringSessionsToTableView();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to delete tutoring session");
            alert.setContentText("The tutoring session could not be deleted at this time.");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void switchToChatScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Chat.fxml"));
            root = loader.load();

	    	ChatController chatController = loader.getController();
	    	chatController.setUsername(usernameLabel.getText());

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Chat");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
            controller.setUsername(usernameLabel.getText());
            controller.setUsernameLabel(usernameLabel.getText());
            controller.addFullCalendarView(new FullCalendarView(YearMonth.now(),controller,usernameLabel.getText()));
    		stage.setTitle("Calendar");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void switchToProfileScene(ActionEvent event) {
        try {
    	    String selectedUser = tutoringSessionTable.getSelectionModel().getSelectedItem().getTutorName();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Profile.fxml"));
            root = loader.load();

	    	ProfileController profileController = loader.getController();
	    	profileController.setUsername(usernameLabel.getText());
	    	profileController.loadUserProfile(selectedUser);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
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
    
	public void addAllTutoringSessionsToTableView() {
	    Platform.runLater(() -> {
	        TutoringSessionDao tutoringSessionDao = new TutoringSessionDao();
	        List<TutoringSession> tutoringSessions = tutoringSessionDao.getAllTutoringSessions();

	        tutoringSessionTable.getItems().clear();
	        tutoringSessionTable.getItems().addAll(tutoringSessions);

	        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
	        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTutorName()));
	        courseCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse()));
	        chapterCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChapter()));
	        timeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime().toString()));
	        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
	        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
	    });
	}
	public void handleRefreshButtonAction(ActionEvent event) {
		tutoringSessionTable.getItems().clear();
		addAllTutoringSessionsToTableView();
	}
	@FXML
	private void bookTutoringSession(ActionEvent event) {
	    // Get the selected tutoring session from the table view
	    TutoringSession selectedSession = tutoringSessionTable.getSelectionModel().getSelectedItem();
	    if (selectedSession == null) {
	        // Handle the case where no session is selected
	        return;
	    }

	    // Get the current user's username
	    String currentUser = usernameLabel.getText();

	    // Check if the selected session is already booked
	    if (selectedSession.getStatus().equals("booked")) {
	        // Show an error message
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Cannot Book Session");
	        alert.setContentText("The selected tutoring session is already booked.");
	        alert.showAndWait();
	        return;
	    }

	    // Check if the current user is the tutor for the selected session
	    if (currentUser.equals(selectedSession.getTutorName())) {
	        // Show an error message
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Cannot Book Session");
	        alert.setContentText("You cannot book a tutoring session with yourself.");
	        alert.showAndWait();
	        return;
	    }

	    // Create a new calendar event
	    CalendarEvent eventToAdd = new CalendarEvent();
	    eventToAdd.setTuteeName(currentUser);
	    eventToAdd.setTutorName(selectedSession.getTutorName());
	    eventToAdd.setCourse(selectedSession.getCourse());
	    eventToAdd.setDate(selectedSession.getDate());
	    eventToAdd.setTime(selectedSession.getTime());

	    // Add the calendar event to the database
	    CalendarEventDao eventDao = new CalendarEventDao();
	    eventDao.save(eventToAdd);

	    // Update the tutoring session's status in the database
	    TutoringSessionDao sessionDao = new TutoringSessionDao();
	    selectedSession.setStatus("booked");
	    sessionDao.updateTutoringSession(selectedSession);

	    // Show a success message
	    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
	    successAlert.setTitle("Session Booked");
	    successAlert.setHeaderText("Success!");
	    successAlert.setContentText("You have successfully booked a tutoring session for " + selectedSession.getDate() + " at " + selectedSession.getTime() + " with " + selectedSession.getTutorName() + ".");
	    successAlert.showAndWait();
	}
	
	@FXML
	public void handleLogoutButtonAction(ActionEvent event) {
	    // Clear the username label
	    usernameLabel.setText("");

	    // Clear the tutoring session table
	    tutoringSessionTable.getItems().clear();

	    // Switch to the login scene
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Login.fxml"));
	        root = loader.load();

	        stage = (Stage) ((Node) tutoringSessionTable.getScene().getRoot()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.setTitle("Login");
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
