package com.additolc.messaging;

public interface Subscriber {

	public Object onMessageReceived(String topic, Object Message);
}
