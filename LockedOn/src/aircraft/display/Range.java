package aircraft.display;

public enum Range {
	
	GUNS("GUNS"),
	HEAT_SEEKING("HEAT_SEEKING"),
	RADAR_HOMING("RADAR_HOMING"),
	ACTIVE_HOMING("ACTIVE_HOMING");
	
	private Range(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
}
