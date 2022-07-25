package com.flightleader.messagebus;

import java.util.HashMap;
import java.util.LinkedList;

public class MessageBus {

	private static MessageBus instance;
	private HashMap<String, LinkedList<Subscriber>> subscribers = new HashMap<String, LinkedList<Subscriber>>();
	
	private MessageBus() {}
	
	public static MessageBus getInstanceOf()
	{
		if (instance == null)
		{
			instance = new MessageBus();
		}
		
		return instance;
	}
	
	public void addSubscriber(String topic, Subscriber subscriber)
	{
		if (!subscribers.containsKey(topic))
		{
			subscribers.put(topic, new LinkedList<Subscriber>());
		}
		
		subscribers.get(topic).add(subscriber);
	}
	
	public void broadcastMessage(String topic, Object message)
	{
		if (subscribers.containsKey(topic))
		{
			for (Subscriber sub:subscribers.get(topic))
			{
				sub.onMessageReceived(topic, message);
			}
		}
	}
	
	public Object sendMessage(String topic, Object message)
	{
		if (subscribers.containsKey(topic))
		{
			return subscribers.get(topic).getFirst().onMessageReceived(topic, message);
		}
		
		return null;
	}
}
