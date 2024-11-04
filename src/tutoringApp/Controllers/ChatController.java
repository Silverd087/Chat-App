package tutoringApp.Controllers;

 import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import tutoringApp.Models.User;
import tutoringApp.Models.UserDao;
import tutoringApp.Models.MessageLog;
import tutoringApp.Models.WClient;
import tutoringApp.Models.WClientListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

 public class ChatController implements Initializable, WClientListener {

 @FXML
 private TextField messageBox;
 @FXML
 private ListView chatPane;
 @FXML
 private Button sendButton;

 WClient client;
 @FXML
 private ListView contactList;

 @FXML
 private TextField searchBar;
 
 @FXML
 private Label recipient;
 
private Stage stage;
private Scene scene;
private Parent root;
private String username; 

 

public void setUsername(String username) {
	this.username = username;
}

@Override
 public void initialize(URL url, ResourceBundle rb) {
	 Platform.runLater(() -> {
		 client = new WClient();
		 client.start(this);
		 
		 addContacts();
		 recipient.setText(username);

		 contactList.setOnMouseClicked(event -> {
		     if (event.getClickCount() == 1) {
		         // Get the selected username
		         String selectedUsername = (String) contactList.getSelectionModel().getSelectedItem();
		         
		         recipient.setText(selectedUsername);

		         System.out.println(selectedUsername);
		         System.out.println(username);
		         
		         // Load the conversation with the selected username
		         loadConversation(selectedUsername);
		         
		         contactList.getItems().clear();
		         addContacts();

		     }
		 });
		 }); 
 }

 public void addToChat() {
 String message = messageBox.getText();
 client.sendMessage(message);
 chatPane.getItems().add(message);
 messageBox.clear();
 }

 @Override
 public void onMessageRecieved(String message) {
 Platform.runLater(() -> {
 chatPane.getItems().add(message);
 });
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
    public void switchToCalendartScene(ActionEvent event) {
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
    
    public void switchToSettingsScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tutoringApp/Views/Profile.fxml"));
            root = loader.load();

	    	ProfileController profileController = loader.getController();
	    	profileController.setUsername(username);
	    	profileController.loadUserProfile(username);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
	 public void searchUser() {
	     UserDao userDao = new UserDao(); // Initialize the UserDao instance
	     String username = searchBar.getText();
	     if(!username.equals("")) {
	         User user = userDao.getUserByUsername(username);
	         if(user == null) {
	        	 Alert alert = new Alert(Alert.AlertType.WARNING);
	        	 alert.setContentText("User not found");
	        	 alert.show(); 
	         }
	         else {
	        	 contactList.getItems().clear();
	        	 contactList.getItems().add(username);
	         }
	     }
	     else {
	    	 contactList.getItems().clear();
	    	 addContacts();
	     }
	     }

	 public void addContacts() {
		    UserDao userDao = new UserDao(); // Initialize the UserDao instance

		     List<String> usernames;
		     usernames = userDao.getAllUsernames(username);



		     contactList.getItems().addAll(usernames);
	 }
	 public void loadConversation(String selectedUsername) {
		    File conversationLogFile = createConversationLogFile(selectedUsername, username);

		    chatPane.getItems().clear();
		    contactList.setItems(FXCollections.observableArrayList(selectedUsername));

		    if (conversationLogFile.exists() && conversationLogFile.length() != 0) {
		        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(conversationLogFile))) {
		            List<MessageLog> conversationLines = (List<MessageLog>) ois.readObject();
		            Platform.runLater(() -> {
		                conversationLines.forEach(messageLog -> chatPane.getItems().add(messageLog.getContent()));
		            });
		        } catch (IOException | ClassNotFoundException e) {
		            e.printStackTrace();
		            chatPane.getItems().add("Error loading conversation with " + selectedUsername + ".");
		        }
		    } else {
		        chatPane.getItems().add("Conversation with " + selectedUsername + " not found.");
		    }
		}

		public static synchronized File createConversationLogFile(String client1, String client2) {
		    String directoryName = "conversationLogs";
		    if (!new File(directoryName).exists()) {
		        new File(directoryName).mkdir();
		    }

		    String fileName = generateConversationFileName(client1, client2);
		    File conversationLogFile = new File(directoryName, fileName);

		    if (!conversationLogFile.exists()) {
		        try {
		            conversationLogFile.createNewFile();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    return conversationLogFile;
		}

		public static synchronized String generateConversationFileName(String client1, String client2) {
		    return client1.compareTo(client2) < 0 ? (client1 + "_" + client2 + "_conversation.log") : (client2 + "_" + client1 + "_conversation.log");
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