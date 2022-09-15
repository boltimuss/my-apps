package aircraft.card;

import javafx.scene.image.Image;
import lombok.Getter;
import lombok.Setter;

public class AircraftCard {

	
	@Getter @Setter transient private Image card;
	@Getter @Setter private String electronicWarfare;
	@Getter @Setter private String name;
	@Getter @Setter private String image;
	@Getter @Setter private String country;
	@Getter @Setter private String serviceYear;
	@Getter @Setter private String speedRating;
	@Getter @Setter private String gunRating;
	@Getter @Setter private String cmRating;
	@Getter @Setter private String preTurnThrustRating;
	@Getter @Setter private String postTurnThrustRating;
	@Getter @Setter private String preTurnABThrustRating;
	@Getter @Setter private String postTurnABThrustRating;
	@Getter @Setter private String performanceRating;
	@Getter @Setter private String victoryPointNoMissiles;
	@Getter @Setter private String victoryPointMissiles;
	@Getter @Setter private String victoryPointReductionNoEnemyMissiles;
	@Getter @Setter private MissileLoad[] missileLoad;
}
