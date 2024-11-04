package tutoringApp.Controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import tutoringApp.Models.TutoringSession;
import tutoringApp.Models.TutoringSessionDao;

public class TutoringSessionFormController {
    
    @FXML
    private TextField courseField;
    @FXML
    private TextField chapterField;
    @FXML
    private TextField timeField;
    @FXML
    private DatePicker datePicker;
    
    private String username;
	private TutoringSession tutoringSession;
    
    
	public void setUsername(String username) {
		this.username = username;
	}

	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
	    String course = courseField.getText();
	    String chapter = chapterField.getText();
	    String time = timeField.getText();
	    LocalDate date = datePicker.getValue();
	    String dateString = new String();
	    if(date != null) {
		     dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	    }

	    if (course == null || course.isEmpty() || chapter == null || chapter.isEmpty() || time == null || time.isEmpty() || date == null) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Invalid input");
	        alert.setContentText("Please fill in all the fields.");
	        alert.showAndWait();
	        return;
	    }

	    if (tutoringSession == null) {
	        // Add the tutoring session to the database
	        TutoringSessionDao tutoringSessionDao = new TutoringSessionDao();
	        tutoringSessionDao.addTutoringSession(username, course, chapter, time, date);
	    } else {
	        // Update the existing tutoring session in the database
	        tutoringSession.setCourse(course);
	        tutoringSession.setChapter(chapter);
	        tutoringSession.setTime(time);
	        tutoringSession.setDate(dateString);
	        TutoringSessionDao tutoringSessionDao = new TutoringSessionDao();
	        tutoringSessionDao.updateTutoringSession(tutoringSession);
	    }

	    // Clear the form fields
	    courseField.clear();
	    chapterField.clear();
	    timeField.clear();
	    datePicker.getEditor().clear(); // Clear the DatePicker editor

	}
	public void setTutoringSession(TutoringSession tutoringSession) {
	    this.tutoringSession = tutoringSession;
	    courseField.setText(tutoringSession.getCourse());
	    chapterField.setText(tutoringSession.getChapter());
	    timeField.setText(tutoringSession.getTime());
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate date = LocalDate.parse(tutoringSession.getDate(), formatter);
	    datePicker.setValue(date);	}
}
