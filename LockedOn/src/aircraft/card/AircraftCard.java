package aircraft.card;

import lombok.Getter;
import lombok.Setter;

public class AircraftCard {

	@Getter @Setter private String image;
	@Getter @Setter private String Country;
	@Getter @Setter private String serviceYear;
	@Getter @Setter private String speedRating;
	@Getter @Setter private String gunRating;
	@Getter @Setter private String cmRating;
	@Getter @Setter private String preTurnThrustRating;
	@Getter @Setter private String postTurnThrustRating;
	@Getter @Setter private String preTurnABThrustRating;
	@Getter @Setter private String postTurnABThrustRating;
	@Getter @Setter private String performanceRating;
	@Getter @Setter private String victoryPointReductionNoMissiles;
	@Getter @Setter private String victoryPointReductionMissiles;
	@Getter @Setter private String victoryPointNoEnemyMissiles;
	@Getter @Setter private MissileLoad[] missileLoad;
}
