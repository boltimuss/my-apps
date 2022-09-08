package data;

import java.util.HashMap;

import Missile.Missile;
import action.ActionCard;
import aircraft.card.AircraftCard;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

public class GameData {

	@Getter @Setter private HashMap<String, AircraftCard> aircraftCards = new HashMap<String, AircraftCard>();
	@Getter @Setter private HashMap<String, Missile> missiles = new HashMap<String, Missile>(); 
	@Getter @Setter private HashMap<String, ActionCard> actionCards = new HashMap<String, ActionCard>(); 
	@Getter @Setter private ImageView countermeasure;
}
