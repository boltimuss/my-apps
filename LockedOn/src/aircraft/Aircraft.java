package aircraft;

import Missile.Missile;
import aircraft.card.AircraftCard;
import aircraft.display.Altitude;
import aircraft.display.Range;
import lombok.Getter;
import lombok.Setter;

public class Aircraft {

	@Getter @Setter private AircraftCard aircraftCard;
	@Getter @Setter private int cmNumber;
	@Getter @Setter private Missile missile1;
	@Getter @Setter private Missile missile2;
	@Getter @Setter private Missile missile3;
	@Getter @Setter private Missile missile4;
	@Getter @Setter private Altitude altitude;
	@Getter @Setter private Range range;
	@Getter @Setter private Afterburner afterburner1;
	@Getter @Setter private Afterburner afterburner2;
	
	public Aircraft(AircraftCard aircraftCard)
	{
		this.aircraftCard = aircraftCard;
		this.cmNumber = Integer.valueOf(aircraftCard.getCmRating());
	}
}
