package com.flightleader.main;

import java.io.IOException;

import org.hexworks.mixite.core.api.CubeCoordinate;

import com.flightleader.controller.MainWindowController;
import com.flightleader.hex.HexUtils;
import com.flightleader.messagebus.MessageBus;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
    	
    	stage.widthProperty().addListener((obs, oldVal, newVal) -> {
    	     MessageBus.getInstanceOf().broadcastMessage("windowSizeChanged", new Dimension2D((double) newVal, stage.getHeight()));
    	});

    	stage.heightProperty().addListener((obs, oldVal, newVal) -> {
    		MessageBus.getInstanceOf().broadcastMessage("windowSizeChanged", new Dimension2D(stage.getWidth(), (double) newVal));
    	});
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainWindow.fxml"));
    	Parent root = loader.load();
    	MainWindowController controller = loader.getController();
        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	
            @Override
            public void handle(WindowEvent e) {
            	MessageBus.getInstanceOf().broadcastMessage("saveBeforeExit", null);
             Platform.exit();
             System.exit(0);
            }
          });
        
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}