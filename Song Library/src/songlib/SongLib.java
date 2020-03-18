package songlib;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * @author      Sunehar Sandhu
 * @author		Nisha Bhat
 * @version     1.0
 * @since       2020-02-21
 */
 
public class SongLib extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
	 * start method to run application 
	 * 
	 * @param primaryStage main gui window
	 */
    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	        
        FXMLLoader loader = new FXMLLoader(); 
		loader.setLocation(getClass().getResource("GUI.fxml"));
		Pane root = (Pane)loader.load();
		
		GUIController GUIController = loader.getController();
		GUIController.start(primaryStage); // error
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Song Library");
		primaryStage.setScene(scene);
		primaryStage.show();
       
    }
}