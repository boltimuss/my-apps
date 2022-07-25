package com.flightleader.engine;

import java.util.HashMap;

import com.flightleader.engine.actions.Action;
import com.flightleader.engine.actions.GameSetupAction;
import com.flightleader.engine.rules.Rule;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;

public class Engine implements Subscriber {

	private HashMap<String, Action> actions = new HashMap<String, Action>();
	private HashMap<String, Rule> rules = new HashMap<String, Rule>();
	private State currentState = new State();
	private FlightLog flightLogs = new FlightLog();
	
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
		else if (topic.startsWith("engine.update.logs"))
		{
			flightLogs.logEntry((String) message);
		}
		else if (topic.startsWith("engine.request.logs"))
		{
			return flightLogs.getLogCopy();
		}
		return null;
	}
	
	private void initSubscriptions()
	{
		MessageBus.getInstanceOf().addSubscriber("engine.request.logs", this);
		MessageBus.getInstanceOf().addSubscriber("engine.update.logs", this);
		MessageBus.getInstanceOf().addSubscriber("engine.actions.getLogs", this);
		MessageBus.getInstanceOf().addSubscriber("engine.actions.setup", this);
		MessageBus.getInstanceOf().addSubscriber("engine.actions.determineInitiative", this);
	}
	
	private void initActions() 
	{
		actions.put("engine.actions.setup", new GameSetupAction());
	}

	private void initRules() 
	{
	}
}
