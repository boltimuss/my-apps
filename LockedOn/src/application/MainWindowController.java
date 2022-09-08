package application;

import aircraft.Aircraft;
import aircraft.display.AircraftDisplayView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import messagebus.MessageBus;
import messagebus.Subscriber;

public class MainWindowController implements Subscriber {

	@FXML
	ImageView mainWindowGraphic;
	
	@FXML
	ImageView mfdLeft;
	
	@FXML
	ImageView mfdMiddle;
	
	@FXML
	ImageView mfdRight;
	
	@FXML
	Pane mainPane;
	
	AircraftDisplayView[] mfd = new AircraftDisplayView[3];
	
	@FXML
	private void initialize()
	{
		MessageBus.getInstanceOf().addSubscriber("loadMfd1", this);
		MessageBus.getInstanceOf().addSubscriber("loadMfd2", this);
		MessageBus.getInstanceOf().addSubscriber("loadMfd3", this);
	}
	
	public void loadGraphics()
	{
		Image image = new Image(getClass().getResourceAsStream("/resources/mainwindow.png"));
		mainWindowGraphic.setImage(image);
		mfd[0] = new AircraftDisplayView(0, mainPane);
		mfd[1] = new AircraftDisplayView(1, mainPane);
		mfd[2] = new AircraftDisplayView(2, mainPane);
	}
	
	@Override
	public Object onMessageReceived(String topic, Object message) {
		
		switch (topic)
		{
			case "loadMfd1":
				mfd[0].loadMfd((Aircraft) message);
				break;
			case "loadMfd2":
				mfd[1].loadMfd((Aircraft) message);
				break;
			case "loadMfd3":
				mfd[2].loadMfd((Aircraft) message);
				break;
		}
		
		return null;
	}
}
