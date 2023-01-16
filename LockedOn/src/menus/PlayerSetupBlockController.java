package menus;

import data.GameData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import messagebus.MessageBus;

public class PlayerSetupBlockController {

	@FXML Label playerNumberLbl;
	@FXML VBox aircraftList;
	@FXML ImageView aircraftCardImage;
	@FXML VBox SkillsList;
	@FXML TextArea skillDescription;
	@FXML Button addAircraft, addSkill;
	
	public void setPlayerName(String playerName)
	{
		playerNumberLbl.setText(playerName);
	}
	
	public void init()
	{
		addAircraft();
	}
	
	public void addAircraft()
	{
		GameData gameData = (GameData)MessageBus.getInstanceOf().sendMessage("getGameData", null);
		ComboBox<String> cb = new ComboBox<>();
		cb.setMaxHeight(24);
		cb.setMaxWidth(180);
		cb.getItems().add("<DELETE>");
		cb.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				@SuppressWarnings("unchecked")
				ComboBox<String> src = (ComboBox<String>) event.getSource();
				if (src.getSelectionModel().getSelectedItem().equals("<DELETE>"))
				{
					aircraftList.getChildren().remove(src);
				}
				else
				{
					Image image = new Image("/resources/aircraftcards/"+gameData.getAircraftCards().get(src.getSelectionModel().getSelectedItem()).getImage()+".png");
					aircraftCardImage.setImage(image);
				}
				
			}});
		
		for (String aircraftKey:gameData.getAircraftCards().keySet())
		{
			cb.getItems().add(gameData.getAircraftCards().get(aircraftKey).getName());
		}
		aircraftList.getChildren().add(cb);
	}
	
	public void addSkill()
	{
		
	}
}
