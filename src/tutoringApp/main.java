package tutoringApp;

 import javafx.application.Application;
import javafx.fxml.FXMLLoader;
 import javafx.scene.Parent;
 import javafx.scene.Scene;
 import javafx.stage.Stage;

 public class main extends Application {

 @Override
 public void start(Stage stage) throws Exception {

 Parent root = FXMLLoader.load(getClass().getResource("/tutoringApp/Views/Login.fxml"));
 Scene scene = new Scene(root, 600, 400);
 stage.setScene(scene);
 stage.setTitle("Login");
 stage.setResizable(false);
 stage.show();

 }

 public static void main(String[] args){
 launch(args) ;
 }

 }