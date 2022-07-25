package com.flightleader.engine.actions;

import java.util.LinkedList;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.engine.State;

public class DetermineInitiativeAction implements Action {

	public State performAction(State state, Object message) {
		
		LinkedList<Aircraft> playerAircraft = state.getPlayerAircraft();
		LinkedList<Aircraft> enemyAircraft = state.getEnemyAircraft();
		
		for (Aircraft a:playerAircraft)
		{
			a.setInitiative(calculateInitiative(a));
		}
		
		for (Aircraft a:enemyAircraft)
		{
			a.setInitiative(calculateInitiative(a));
		}
		
		return null;
	}

	private int calculateInitiative(Aircraft a)
	{
		return 0;
	}
	
	private boolean isAircraftInFormation(Aircraft a)
	{
		return false;
	}
}
