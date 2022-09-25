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
import data.AircraftData.MissileLoad;
import data.GameData;
import data.JsonUtilities;
import gamestate.GameState;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import messagebus.MessageBus;
import messagebus.Subscriber;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

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
			
			mfd1 = new Mfd(1, mainWindow);
			mfd2 = new Mfd(2, mainWindow);
			mfd3 = new Mfd(3, mainWindow);
			initMfd(mfd1, 1);
			initMfd(mfd2, 2);
			initMfd(mfd3, 3);
			
			mainWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					MessageBus.getInstanceOf().sendMessage("close", null);				
				}});
			
			mainWindow.xProperty().addListener((observable, oldValue, newValue) -> {
				
			    mfd1.setPosition((double) newValue, mainWindow.getY());
			    mfd2.setPosition((double) newValue, mainWindow.getY());
			    mfd3.setPosition((double) newValue, mainWindow.getY());
			});
			
			mainWindow.yProperty().addListener((observable, oldValue, newValue) -> {
				
			    mfd1.setPosition(mainWindow.getX(), (double) newValue);
			    mfd2.setPosition(mainWindow.getX(), (double) newValue);
			    mfd3.setPosition(mainWindow.getX(), (double) newValue);
			});
			
			mainWindow.setTitle("Locked-on (Down In Flames)");
			mainWindow.show();
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
//			Aircraft a1 = new Aircraft(gameData.getAircraftCards().get("F-18"));
//			Aircraft a2 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
//			Aircraft a3 = new Aircraft(gameData.getAircraftCards().get("F-15C Eagle"));
//			a1.setMissile1(gameData.getMissiles().get("AA-8B-1"));
//			a1.setMissile2(gameData.getMissiles().get("AA-8B-1"));
//			a1.setMissile3(gameData.getMissiles().get("AA-8B-1"));
//			a1.setMissile4(gameData.getMissiles().get("AA-8B-1"));
//			a2.setMissile1(gameData.getMissiles().get("AA-8B-1"));
//			a2.setMissile2(gameData.getMissiles().get("AA-8B-1"));
//			a2.setMissile3(gameData.getMissiles().get("AA-8B-1"));
//			a2.setMissile4(gameData.getMissiles().get("AA-8B-1"));
//			a3.setMissile1(gameData.getMissiles().get("AA-8B-1"));
//			a3.setMissile2(gameData.getMissiles().get("AA-8B-1"));
//			a3.setMissile3(gameData.getMissiles().get("AA-8B-1"));
//			a3.setMissile4(gameData.getMissiles().get("AA-8B-1"));
//			a1.setAltitude(Altitude.HIGH);
//			a2.setAltitude(Altitude.MEDIUM);
//			a3.setAltitude(Altitude.LOW);
//			a1.setAfterburner1(Afterburner.ON);
//			a1.setAfterburner2(Afterburner.ON);
//			a2.setAfterburner1(Afterburner.OFF);
//			a2.setAfterburner2(Afterburner.OFF);
//			a3.setAfterburner1(Afterburner.VACANT);
//			a3.setAfterburner2(Afterburner.OFF);
//			a1.setRange(Range.GUNS);
//			a2.setRange(Range.HEAT_SEEKING);
//			a3.setRange(Range.ACTIVE_HOMING);
//			MessageBus.getInstanceOf().sendMessage("loadMfd1", a1);
//			MessageBus.getInstanceOf().sendMessage("loadMfd2", a2);
//			MessageBus.getInstanceOf().sendMessage("loadMfd3", a3);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initMfd(Mfd mfd, int mfdNum)
	{
		int width = 500;
		int height = 535;
		
		mfd.setWidth(width);
		mfd.setMaxWidth(width);
		mfd.setMinWidth(width);
		mfd.setHeight(height);
		mfd.setMaxHeight(height);
		mfd.setMinHeight(height);
		mfd.hide();
		
		switch (mfdNum)
		{
			case 1:
				mfd.setX(mainWindow.getX());
				mfd.setY(mainWindow.getY());
				break;
			case 2 :
				mfd.setX(mainWindow.getX() + mainWindow.getWidth() + 10);
				mfd.setY(mainWindow.getY());
				break;
			case 3:
				mfd.setX(mainWindow.getX() + mainWindow.getWidth() + 34);
				mfd.setY(mainWindow.getY());
				break;
		}

		mfd.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				event.consume();
			}});
		
	}
	
	private void loadData(GameData gameData) throws IOException 
	{
//		loadMissileData(gameData);
//		loadAircraftData(gameData);
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
			
			gameData.getAircraftCards().get(aircraft.getName()).setMissileLoad(new MissileLoad[aircraft.getMissileLoad().length]);
			for (int i = 0; i<aircraft.getMissileLoad().length; i++) 
			{
				gameData.getAircraftCards().get(aircraft.getName()).getMissileLoad()[i] = aircraft.getMissileLoad()[i];
			}
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
			mfd2.close();
			mfd3.close();
		}
		
		return null;
	}
}
