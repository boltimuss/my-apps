package menus;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartNewGameController {

	private Stage stage;
	@FXML private ScrollPane playerStatsSP;
	@FXML private ComboBox<String> startingRangeCB;
	@FXML VBox playerStatBlocks;
	
	public void init(Stage stage) throws IOException
	{
		this.stage = stage;
		
		startingRangeCB.getItems().addAll("Guns", "Heat Seeking", "Radar Homing", "Active Homing");
		initPlayers();
	}
	
	private void initPlayers() throws IOException
	{
		playerStatBlocks.getChildren().clear();
		for (int i = 0; i < 2; i++)
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/menus/PlayerSetupBlock.fxml")); 
			VBox statBlock = loader.load();
			
			PlayerSetupBlockController controller = loader.getController();
			controller.setPlayerName((i == 0) ? "CPU" : "Human Player");
			controller.init();
			playerStatBlocks.getChildren().add(statBlock);
		}
	}
	
	public void onCancel()
	{
		stage.close();
	}
	
	public void startGame()
	{
		
	}
}
