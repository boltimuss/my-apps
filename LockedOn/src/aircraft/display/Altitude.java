package aircraft.display;

public enum Altitude {

	LOW("LOW"),
	MEDIUM("MEDIUM"),
	HIGH("HIGH");
	
	private Altitude(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
}

