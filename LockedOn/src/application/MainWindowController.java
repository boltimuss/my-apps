package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import menus.StartNewGameController;
import messagebus.MessageBus;
import messagebus.Subscriber;

public class MainWindowController implements Subscriber {

	@FXML
	ImageView mainWindowGraphic;
	
	@FXML
	Pane mainPane;
	
	@FXML
	Button mfd1Button;
	
	@FXML
	Button mfd2Button;
	
	@FXML
	Button mfd3Button;
	
	public void toggleMfd1()
	{
		MessageBus.getInstanceOf().sendMessage("mfd1VisibleToggle", null);
	}
	
	public void toggleMfd2()
	{
		MessageBus.getInstanceOf().sendMessage("mfd2VisibleToggle", null);
	}
	
	public void toggleMfd3()
	{
		MessageBus.getInstanceOf().sendMessage("mfd3VisibleToggle", null);
	}
	
	public void newGame() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/menus/StartNewGame.fxml")); 
		Parent newGameView = loader.load();
		
		Scene scene = new Scene(newGameView,850,650);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		Stage newGameStage = new Stage();
		newGameStage.setResizable(false);
		newGameStage.setScene(scene);
		newGameStage.setTitle("Start New Game");
		newGameStage.initModality(Modality.APPLICATION_MODAL); 
		
		StartNewGameController controller = loader.getController();
		controller.init(newGameStage);
		newGameStage.showAndWait();
	}
	
	public void loadGame()
	{
		
	}
	
	public void saveGame()
	{
		
	}
	
	public void newScenario()
	{
		
	}
	
	public void loadScenario()
	{
		
	}
	
	public void viewAircraftCards()
	{
		
	}
	public void viewOrdanance()
	{
		
	}
	public void about()
	{
		
	}
	
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
