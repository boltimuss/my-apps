package com.flightleader.engine.actions;

import java.util.LinkedList;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.engine.State;
import com.flightleader.engine.messages.GameSetupMessage;
import com.flightleader.messagebus.MessageBus;

public class GameSetupAction implements Action {

	public State performAction(State state, Object message) {
		
		GameSetupMessage msg = (GameSetupMessage) message;
		
		LinkedList<Aircraft> playerAircraft = state.getPlayerAircraft();
		playerAircraft.addAll(msg.getPlayerAircraft());
		state.setPlayerAircraft(playerAircraft);
		
		LinkedList<Aircraft> enemyAircraft = state.getEnemyAircraft();
		enemyAircraft.addAll(msg.getEnemyAircraft());
		state.setEnemyAircraft(enemyAircraft);
		
		createLogEntry(state);
		
		return state;
	}

	private void createLogEntry(State state)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("===================================================================\n");
		sb.append("Setting up the game with the following player aircraft:\n");
		sb.append("\n");
		for (Aircraft aircraft:state.getPlayerAircraft())
		{
			sb.append(aircraft.toString()+"\n");
			sb.append("\n");
		}
		sb.append("Setting up the game with the following enemy aircraft:\n");
		sb.append("\n");
		for (Aircraft aircraft:state.getEnemyAircraft())
		{
			sb.append(aircraft.toString()+"\n");
			sb.append("\n");
		}
		
		MessageBus.getInstanceOf().sendMessage("engine.update.logs", sb.toString());
	}
	
}
