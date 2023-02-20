package messagebus;

public interface Subscriber {

	public void onMessageReceived(String topic, Object message);
}
