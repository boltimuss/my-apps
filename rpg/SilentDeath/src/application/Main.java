package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import map.MapView;
import map.MapView.SelectionMode;
import ship.Ship;
import ship.ShipView;
import ship.ShipController.TORPEDO_TYPE;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		String s = "ThunderBird";
		Ship ship = new Ship(s);
		ship.setBpv(25);
		ship.setTpv(50);
		ship.setTorpedoStores(TORPEDO_TYPE.MK10, 4);
		ship.setTorpedoStores(TORPEDO_TYPE.MK20, 1);
//		
//		ShipView shipView = new ShipView(s);
//		shipView.displayShip(ship);
		
//		ship.setFacing(0);
		MapView mv = new MapView(1900,1050, 22, 22);
		mv.addShipToHex(ship, 7, 12);
		mv.setSelectMode(SelectionMode.SELECT_SRC_HEX);
		mv.displayMap();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
