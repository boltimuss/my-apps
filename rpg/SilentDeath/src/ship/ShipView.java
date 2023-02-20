package ship;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import messagebus.MessageBus;
import messagebus.Subscriber;
import ship.ShipController.TORPEDO_TYPE;

/**
 * Class for displaying the ship and it's systems
 * 
 * @author Lance
 *
 */
public class ShipView implements Subscriber {

	private Stage stage;
	private ShipController shipController;
	private String shipName;
	
	public ShipView(String shipName)
	{
		this.shipName = shipName;
		MessageBus.subscribeToTopic("ShipViewChange." + shipName, this);
	}
	
	public void displayShip(Ship ship)
	{
		stage = new Stage();
		stage.setResizable(false);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("../resources/hex-outline.png")));
		Parent root = null;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ShipView.fxml")); 
			root = loader.load();
			shipController = (ShipController) loader.getController();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MessageBus.sendMessage("ShipViewChange." + ship.getShipName(), ship);
		
        Scene scene = new Scene(root, 1464, 820);
        stage.setScene(scene);
        stage.setTitle(shipName);
        stage.show();
	}

	@Override
	public void onMessageReceived(String topic, Object message) {
		
		Ship ship = (Ship)message;
		shipController.setShipName(ship.getShipName());
		shipController.setShipImage(ship.getShipImage());
		shipController.setBpv(ship.getBpv());
		shipController.setTpv(ship.getTpv());
		
		for (TORPEDO_TYPE t:ship.getTorpedoStores().keySet())
		{
			shipController.setTorpedoStores(t, ship.getTorpedoStores(t));
		}
		
	}
}
