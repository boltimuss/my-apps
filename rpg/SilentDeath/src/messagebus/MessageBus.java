package messagebus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.application.Platform;

public class MessageBus {

	private static HashMap<String, LinkedList<Subscriber>> subscribers = new HashMap<String, LinkedList<Subscriber>>();
	
	public static void sendMessage(String topic, Object message)
	{
		Iterator<Subscriber> it = subscribers.get(topic).iterator();
		while (it.hasNext())
		{
			Subscriber s = it.next();
			
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					s.onMessageReceived(topic, message); 
				}});
		}
	}
	
	public static void subscribeToTopic(String topic, Subscriber subscriber)
	{
		if (!subscribers.containsKey(topic))
		{
			LinkedList<Subscriber> l = new LinkedList<Subscriber>();
			subscribers.put(topic, l);
		}
		
		subscribers.get(topic).add(subscriber);
	}
}
