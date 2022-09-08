package application;
	
import java.io.IOException;
import java.util.ArrayList;

import org.dozer.DozerBeanMapper;

import Missile.Missile;
import Missile.Missile.MissileListData;
import action.ActionCard;
import aircraft.Aircraft;
import aircraft.card.AircraftCard;
import aircraft.display.Altitude;
import data.ActionCardData;
import data.AircraftData;
import data.AircraftData.AircraftListData;
import data.GameData;
import data.JsonUtilities;
import gamestate.GameState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import messagebus.MessageBus;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml")); 
			Parent root = loader.load();
			
			MainWindowController controller = loader.getController();
			controller.loadGraphics();
			
			GameData gameData = new GameData();
			loadData(gameData);
			loadActionCardData(gameData);
			loadActionCardData(gameData);
			
			GameState gameState = new GameState();
			
			Scene scene = new Scene(root,1024,824);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			Aircraft a1 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
			Aircraft a2 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
			Aircraft a3 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
			a1.setAltitude(Altitude.LOW);
			a2.setAltitude(Altitude.MEDIUM);
			a3.setAltitude(Altitude.HIGH);
			MessageBus.getInstanceOf().sendMessage("loadMfd1", a1);
			MessageBus.getInstanceOf().sendMessage("loadMfd2", a2);
			MessageBus.getInstanceOf().sendMessage("loadMfd3", a3);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadData(GameData gameData) throws IOException 
	{
		loadMissileData(gameData);
		loadAircraftData(gameData);
	}
	
	private void loadActionCardData(GameData gameData) throws IOException
	{
		ActionCardData actionCardData = new JsonUtilities<ActionCardData>().loadFromJson("/resources/actionCardData.json", ActionCardData.class);
		for (ActionCard actionCard:actionCardData.getActionCards())
		{
			gameData.getActionCards().put(actionCard.getTitle(), actionCard);
		}
	}
	
	
	private void loadMissileData(GameData gameData) throws IOException
	{
		DozerBeanMapper mapper = new DozerBeanMapper();
		MissileListData missileData = new JsonUtilities<MissileListData>().loadFromJson("/resources/missileData.json", MissileListData.class);
		for (Missile missile:missileData.getMissiles())
		{
			gameData.getMissiles().put(missile.getName(), mapper.map(missile, Missile.class));
		}
	}
	
	private void loadAircraftData(GameData gameData) throws IOException
	{
		DozerBeanMapper mapper = new DozerBeanMapper();
		ArrayList<String> urls = new ArrayList<String>();
		urls.add("resources/dozerMappingAircraftData.xml");
		mapper.setMappingFiles(urls);
		AircraftListData aircraftData = new JsonUtilities<AircraftListData>().loadFromJson("/resources/aircraftData.json", AircraftListData.class);
		for (AircraftData aircraft:aircraftData.getAircraft())
		{
			gameData.getAircraftCards().put(aircraft.getName(), mapper.map(aircraft, AircraftCard.class));
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
