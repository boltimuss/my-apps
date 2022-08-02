package com.flightleader.engine.actions;

import java.util.LinkedList;
import java.util.List;

import org.hexworks.mixite.core.api.Hexagon;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.engine.State;
import com.flightleader.hex.HexData;
import com.flightleader.hex.HexUtils;

public class DetermineInitiativeAction implements Action {

	public State performAction(State state, Object message) {
		
		LinkedList<Aircraft> playerAircraft = state.getPlayerAircraft();
		LinkedList<Aircraft> enemyAircraft = state.getEnemyAircraft();
		
		for (Aircraft a:playerAircraft)
		{
			a.setInitiative(calculateInitiative(a, state));
		}
		
		for (Aircraft a:enemyAircraft)
		{
			a.setInitiative(calculateInitiative(a, state));
		}
		
		return state;
	}

	private int calculateInitiative(Aircraft a, State state)
	{
		int initiative = a.getCurrentSpeed() + a.getCurrentAltitude();
		if (isAircraftInFormation(a, state)) initiative++;
		initiative += (a.getTallies().size() > 0) ? 1 : 0;
		initiative += (a.getLocks().size() > 0) ? 1 : 0;
		
		return initiative;
	}
	
	boolean isRouteFormation(List<Aircraft> aircraft, Aircraft srcAircraft)
	{
		for (Aircraft destinationAircraft:aircraft)
		{
			if (destinationAircraft.getId() == srcAircraft.getId()) continue;
			Hexagon<HexData> h1 = HexUtils.getInstanceOf().getHexagonByName(srcAircraft.getHexLocation());
			Hexagon<HexData> h2 = HexUtils.getInstanceOf().getHexagonByName(destinationAircraft.getHexLocation());
			
			boolean distanceCheck = HexUtils.getInstanceOf().getDistance(h1.getCubeCoordinate(), h2.getCubeCoordinate()) == 1;
			boolean sameDirection = srcAircraft.getFacing() == destinationAircraft.getFacing();
			boolean sameAltitude = srcAircraft.getCurrentAltitude() == destinationAircraft.getCurrentAltitude();
			
			if (distanceCheck && sameDirection && sameAltitude)
			{
				boolean trailingRight = (h2.getGridX() > h1.getGridX()) && (Math.abs(h2.getGridY()) > Math.abs(h1.getGridY()));
				boolean trailingLeft = (h2.getGridX() < h1.getGridX()) && (Math.abs(h1.getGridY()) == Math.abs(h2.getGridY()));
				boolean leadingRight = (h2.getGridX() > h1.getGridX()) && (Math.abs(h1.getGridY()) == Math.abs(h2.getGridY()));
				boolean leadingLeft = (h2.getGridX() < h1.getGridX()) && (Math.abs(h2.getGridY()) < Math.abs(h1.getGridY()));
				
				return trailingLeft || trailingRight || leadingRight || leadingLeft;
			}
		}
		
		return false;
	}
	
	boolean isTrailFormation(List<Aircraft> aircraft, Aircraft srcAircraft)
	{
		for (Aircraft destinationAircraft:aircraft)
		{
			if (destinationAircraft.getId() == srcAircraft.getId()) continue;
			Hexagon<HexData> h1 = HexUtils.getInstanceOf().getHexagonByName(srcAircraft.getHexLocation());
			Hexagon<HexData> h2 = HexUtils.getInstanceOf().getHexagonByName(destinationAircraft.getHexLocation());
			int distance = HexUtils.getInstanceOf().getDistance(h1.getCubeCoordinate(), h2.getCubeCoordinate());
			if (distance >=1 && distance <= 2)
			{
				boolean trailingOne = (h2.getGridX() == h1.getGridX()) && (Math.abs(h2.getGridY() - h1.getGridY()) == 1);
				boolean trailingTwo = (h2.getGridX() == h1.getGridX()) && (Math.abs(h2.getGridY() - h1.getGridY()) == 2);
				
				return trailingOne || trailingTwo;
			}
		}
		
		return false;
	}
	
	boolean isEchelonFormation(List<Aircraft> aircraft, Aircraft srcAircraft)
	{
		return false;
	}
	
	boolean isFingertipFormation(List<Aircraft> aircraft, Aircraft srcAircraft)
	{
		return false;
	}
	
	boolean isLineAbreastFormation(List<Aircraft> aircraft, Aircraft srcAircraft)
	{
		return false;
	}
	
	private boolean isAircraftInFormation(Aircraft a, State state)
	{
		LinkedList<Aircraft> aircraft;
		if (a.isPlayerControlled())
		{
			aircraft = state.getPlayerAircraft();
		}
		else
		{
			aircraft = state.getEnemyAircraft();
		}
		
		if (isRouteFormation(aircraft, a)) return true;
		if (isTrailFormation(aircraft, a)) return true;
		
		/* check for echelon formation */
		// get all aircraft within 2, or 4 hexes
		// if aircraft is one col to the left/right and forward 3, or
		// the aircraft is 3 col to the left/right and forward 4, then in formation
		
		/* check for Line Abreast formation */
		// get all aircraft within 2-3 hexes
		// if the aircraft is 2 col to the left/right (for range of two), 
		// or 3 col to the left and one forward then in formation
		
		return false;
	}
}
