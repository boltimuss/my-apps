package com.flightleader.engine;

import java.util.HashMap;

import com.flightleader.engine.actions.Action;
import com.flightleader.engine.rules.Rule;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;

public class Engine implements Subscriber {

	private HashMap<String, Action> actions = new HashMap<>();
	private HashMap<String, Rule> rules = new HashMap<>();
	private State currentState;
	
	public Engine()
	{
		initSubscriptions();
		initRules();
		initActions();
	}
	
	public Object onMessageReceived(String topic, Object message) {
		
		if (topic.startsWith("engine.actions.")) 
		{
			currentState = actions.get(topic).performAction(currentState, message);
			return currentState;
		}
		else if (topic.startsWith("engine.rules."))
		{
			return rules.get(topic).isValid(currentState, message);
		}
		
		return null;
	}
	
	private void initSubscriptions()
	{
		MessageBus.getInstanceOf().addSubscriber("engine.actions.setup", this);
		MessageBus.getInstanceOf().addSubscriber("engine.actions.determineInitiative", this);
	}
	
	private void initActions() 
	{
		
	}

	private void initRules() 
	{
	}
}
