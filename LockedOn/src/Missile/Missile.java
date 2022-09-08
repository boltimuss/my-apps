package Missile;

import lombok.Getter;
import lombok.Setter;

public class Missile {

	@Getter @Setter private int numberOfMissiles;
	@Getter @Setter private String image;
	@Getter @Setter private String name;
	@Getter @Setter private String serviceYear;
	@Getter @Setter private String pointValue;
	@Getter @Setter private String missileType;
	@Getter @Setter private String neutralValue;
	@Getter @Setter private String advantagedValue;
	@Getter @Setter private String tailingValue;
	
	public class MissileListData {
		
		@Getter @Setter private Missile[] missiles;
	}
}
