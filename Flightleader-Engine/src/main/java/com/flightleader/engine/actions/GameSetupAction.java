package com.flightleader.engine.actions;

import java.util.LinkedList;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.engine.State;
import com.flightleader.engine.messages.GameSetupMessage;

public class GameSetupAction implements Action {

	public State performAction(State state, Object message) {
		
		GameSetupMessage msg = (GameSetupMessage) message;
		
		LinkedList<Aircraft> playerAircraft = state.getPlayerAircraft();
		playerAircraft.addAll(msg.getPlayerAircraft());
		state.setPlayerAircraft(playerAircraft);
		
		LinkedList<Aircraft> enemyAircraft = state.getEnemyAircraft();
		enemyAircraft.addAll(msg.getEnemyAircraft());
		state.setEnemyAircraft(enemyAircraft);
		
		return state;
	}

}
