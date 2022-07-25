package com.flightleader.engine;

import java.util.LinkedList;

import com.flightleader.aircraft.Aircraft;

import lombok.Getter;
import lombok.Setter;

public class State {

	@Getter @Setter private LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
	@Getter @Setter private LinkedList<Aircraft> enemyAircraft = new LinkedList<Aircraft>();
	@Getter @Setter private int turn;
	
}
