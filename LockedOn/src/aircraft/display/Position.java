package aircraft.display;

public enum Position {

	ADVANTAGED("ADVANTAGED"),
	DISADVANTAGED("DISADVANTAGED"),
	NEUTRAL("NEUTRAL"),
	TAILING("TAILING"),
	TAILED("TAILED");
	
	private Position(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
}
