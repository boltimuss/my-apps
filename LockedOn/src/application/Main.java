package application;
	
import java.io.IOException;
import java.util.ArrayList;

import org.dozer.DozerBeanMapper;

import Missile.Missile;
import Missile.Missile.MissileListData;
import action.ActionCard;
import aircraft.Afterburner;
import aircraft.Aircraft;
import aircraft.card.AircraftCard;
import aircraft.display.Mfd;
import aircraft.display.Altitude;
import aircraft.display.Range;
import data.ActionCardData;
import data.AircraftData;
import data.AircraftData.AircraftListData;
import data.GameData;
import data.JsonUtilities;
import gamestate.GameState;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import messagebus.MessageBus;
import messagebus.Subscriber;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application implements Subscriber {
	
	private Stage mainWindow;
	private Mfd mfd1, mfd2, mfd3;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			mainWindow = primaryStage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindow.fxml")); 
			Parent root = loader.load();
			
			MainWindowController controller = loader.getController();
			controller.loadGraphics();
			
			GameData gameData = new GameData();
			loadData(gameData);
			loadActionCardData(gameData);
			
			GameState gameState = new GameState();
			
			Scene scene = new Scene(root,902,925);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			mainWindow.setResizable(false);
			mainWindow.setScene(scene);
			MessageBus.getInstanceOf().addSubscriber("close", this);
			mainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					MessageBus.getInstanceOf().sendMessage("close", null);				
				}});
			mainWindow.show();
			
			mfd1 = new Mfd(1);
			initMfd(mfd1, 1);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			Aircraft a1 = new Aircraft(gameData.getAircraftCards().get("F-18"));
			Aircraft a2 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
			Aircraft a3 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
			a1.setAltitude(Altitude.LOW);
			a2.setAltitude(Altitude.MEDIUM);
			a3.setAltitude(Altitude.HIGH);
			a1.setAfterburner1(Afterburner.ON);
			a1.setAfterburner2(Afterburner.ON);
			a2.setAfterburner1(Afterburner.OFF);
			a2.setAfterburner2(Afterburner.OFF);
			a3.setAfterburner1(Afterburner.VACANT);
			a3.setAfterburner2(Afterburner.OFF);
			a1.setRange(Range.GUNS);
			a2.setRange(Range.HEAT_SEEKING);
			a3.setRange(Range.ACTIVE_HOMING);
			MessageBus.getInstanceOf().sendMessage("loadMfd1", a1);
//			MessageBus.getInstanceOf().sendMessage("loadMfd2", a2);
//			MessageBus.getInstanceOf().sendMessage("loadMfd3", a3);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initMfd(Mfd mfd, int mfdNum)
	{
		mfd.setResizable(false);
		mfd.setWidth(800);
		mfd.setMaxWidth(800);
		mfd.setMinWidth(800);
		mfd.setHeight(800);
		mfd.setMaxHeight(800);
		mfd.setMinHeight(800);
		
		switch (mfdNum)
		{
			case 1:
				mfd.setX(mainWindow.getX() + mainWindow.getWidth() - 14);
				mfd.setY(mainWindow.getY());
				break;
			case 2 :
				break;
			case 3:
				break;
		}

		mfd.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				MessageBus.getInstanceOf().sendMessage("close", null);				
			}});
		
		mfd.show();
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
			
			AircraftCard a = gameData.getAircraftCards().get(aircraft.getName());
			gameData.getAircraftCards().get(aircraft.getName()).setCard(
					new Image(getClass().getResourceAsStream("/resources/aircraftcards/" + a.getImage() + ".png")));
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public Object onMessageReceived(String topic, Object Message) {
		
		if (topic.equals("close"))
		{
			mainWindow.close();
			mfd1.close();
//			mfd2.close();
//			mfd3.close();
		}
		
		return null;
	}
}
