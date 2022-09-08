package action;

import lombok.Getter;
import lombok.Setter;

public class ActionCard {

	@Getter @Setter private String title;
	@Getter @Setter private String image;
	@Getter @Setter private String positioning;
	@Getter @Setter private String range;
	@Getter @Setter private String[] effects;
	@Getter @Setter private String[] reactsTo;
	@Getter @Setter private String exclusions;
}
