package com.flightleader.engine.messages;

import java.util.LinkedList;

import com.flightleader.aircraft.Aircraft;

import lombok.Getter;
import lombok.Setter;

public class GameSetupMessage {

	@Getter @Setter private LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
	@Getter @Setter private LinkedList<Aircraft> enemyAircraft = new LinkedList<Aircraft>();
}
