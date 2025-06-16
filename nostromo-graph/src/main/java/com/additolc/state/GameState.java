package com.additolc.state;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class GameState {

	public static GameState instance;
	
	@Getter
	private HashMap<String, AircraftState> aircraftState = new HashMap<>();
	
	@Getter @Setter
	private String currentAircraft;
	
	public static GameState getInstanceOf()
	{
		if (instance == null) 
		{
			instance = new GameState();
		}
		
		return instance;
	}

}
