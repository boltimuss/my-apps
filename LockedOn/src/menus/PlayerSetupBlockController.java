package menus;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

public class PlayerSetupBlockController {

	@FXML Label playerNumberLbl;
	@FXML TextArea aircraftList;
	@FXML ImageView aircraftCardImage;
	@FXML TextArea SkillsList;
	@FXML TextArea skillDescription;
	
	public void setPlayerName(String playerName)
	{
		playerNumberLbl.setText(playerName);
	}
	
}
