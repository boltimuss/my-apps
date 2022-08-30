package application;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainWindowController {

	@FXML
	ImageView mainWindowGraphic;
	
	public void loadGraphic()
	{
		Image image = new Image(getClass().getResourceAsStream("/resources/mainwindow.png"));
		mainWindowGraphic.setImage(image);
	}
}
