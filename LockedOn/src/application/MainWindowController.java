package application;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import messagebus.Subscriber;

public class MainWindowController implements Subscriber {

	@FXML
	ImageView mainWindowGraphic;
	
	@FXML
	Pane mainPane;
	
	public void loadGraphics()
	{
		Image image = new Image(getClass().getResourceAsStream("/resources/map.png"));
		mainWindowGraphic.setImage(image);
	}
	
	@Override
	public Object onMessageReceived(String topic, Object message) {
		
		return null;
	}
}
