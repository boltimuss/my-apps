package aircraft.display;

import Missile.Missile;
import aircraft.card.AircraftCard;
import lombok.Getter;
import lombok.Setter;

public class AircraftDisplay {

	@Getter @Setter private AircraftCard aircraftCard;
	@Getter @Setter private Missile[] missiles;
	@Getter @Setter private int counterMeasures;
	@Getter @Setter private int altitude;
	@Getter @Setter private int range;
	@Getter @Setter private int[] afterburners;
	@Getter @Setter private Position position;
}
