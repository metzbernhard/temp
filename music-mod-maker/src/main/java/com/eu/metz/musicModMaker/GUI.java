package com.eu.metz.musicModMaker;



import org.apache.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class GUI extends Application {
    
    private static Logger logger = Logger.getLogger(GUI.class);
	
	@Override
	public void start(Stage stage) throws Exception {
		
    	Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        
        Scene scene = new Scene(root);        
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        stage.getIcons().add(new Image(GUI.class.getResourceAsStream("icon.png")));
        stage.setTitle("Music Mod Maker");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
		
	}

	public static void main(String[] args) {
	    
	    logger.debug("Application Launch");
		launch(args);

	}

}
