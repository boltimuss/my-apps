package messagebus;

public interface Subscriber {

	public Object onMessageReceived(String topic, Object Message);
}
