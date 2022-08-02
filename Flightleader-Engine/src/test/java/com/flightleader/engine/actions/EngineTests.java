package com.flightleader.engine.actions;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.aircraft.Aircraft.ACCELERATION;
import com.flightleader.aircraft.Aircraft.AFTERBURNER;
import com.flightleader.aircraft.Aircraft.FACING;
import com.flightleader.engine.State;
import com.flightleader.engine.actions.DetermineInitiativeAction;
import com.flightleader.engine.messages.GameSetupMessage;
import com.flightleader.messagebus.MessageBus;

public class EngineTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setupTest() {
		
		GameSetupMessage msg = new GameSetupMessage();
		Aircraft a1 = new Aircraft();
		Aircraft a2 = new Aircraft();
		
		a1.acceleration = ACCELERATION.HIGH;
		a1.afterburner = AFTERBURNER.YES;
		a1.afterburnersOn = false;
		a1.aircraftName = "F-22";
		a1.setId(23);
		
		a2.acceleration = ACCELERATION.HIGH;
		a2.afterburner = AFTERBURNER.YES;
		a2.afterburnersOn = true;
		a2.aircraftName = "F-16";
		a2.setId(23);
		
		LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
		playerAircraft.add(a1);
		playerAircraft.add(a2);
		msg.setPlayerAircraft(playerAircraft);
		
		MessageBus.getInstanceOf().sendMessage("engine.actions.setup", msg);
		List<String> l = (List<String>) MessageBus.getInstanceOf().sendMessage("engine.request.logs", null);
	}

	@Test
	public void isRouteFormationLeadingLeftTest() {
		
		Aircraft a1 = new Aircraft();
		Aircraft a2 = new Aircraft();
		
		a1.acceleration = ACCELERATION.HIGH;
		a1.afterburner = AFTERBURNER.YES;
		a1.afterburnersOn = false;
		a1.aircraftName = "F-22";
		a1.setHexLocation("C4");
		a1.setId(23);
		a1.setPlayerControlled(true);
		a1.setCurrentAltitude(4);
		a1.setFacing(FACING.NORTH);
		
		a2.acceleration = ACCELERATION.HIGH;
		a2.afterburner = AFTERBURNER.YES;
		a2.afterburnersOn = true;
		a2.aircraftName = "F-16";
		a2.setHexLocation("D4");
		a2.setId(24);
		a2.setPlayerControlled(true);
		a2.setCurrentAltitude(4);
		a2.setFacing(FACING.NORTH);
		
		LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
		playerAircraft.add(a1);
		playerAircraft.add(a2);
		
		DetermineInitiativeAction dta = new DetermineInitiativeAction();
		assertTrue(dta.isRouteFormation(playerAircraft, a1));
	}
	
	@Test
	public void isRouteFormationLeadingRightTest() {
		
		Aircraft a1 = new Aircraft();
		Aircraft a2 = new Aircraft();
		
		a1.acceleration = ACCELERATION.HIGH;
		a1.afterburner = AFTERBURNER.YES;
		a1.afterburnersOn = false;
		a1.aircraftName = "F-22";
		a1.setHexLocation("E4");
		a1.setId(23);
		a1.setPlayerControlled(true);
		a1.setCurrentAltitude(4);
		a1.setFacing(FACING.NORTH);
		
		a2.acceleration = ACCELERATION.HIGH;
		a2.afterburner = AFTERBURNER.YES;
		a2.afterburnersOn = true;
		a2.aircraftName = "F-16";
		a2.setHexLocation("D4");
		a2.setId(24);
		a2.setPlayerControlled(true);
		a2.setCurrentAltitude(4);
		a2.setFacing(FACING.NORTH);
		
		LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
		playerAircraft.add(a1);
		playerAircraft.add(a2);
		
		DetermineInitiativeAction dta = new DetermineInitiativeAction();
		assertTrue(dta.isRouteFormation(playerAircraft, a1));
	}
	
	@Test
	public void isRouteFormationTrailingRightTest() {
		
		Aircraft a1 = new Aircraft();
		Aircraft a2 = new Aircraft();
		
		a1.acceleration = ACCELERATION.HIGH;
		a1.afterburner = AFTERBURNER.YES;
		a1.afterburnersOn = false;
		a1.aircraftName = "F-22";
		a1.setHexLocation("D4");
		a1.setId(23);
		a1.setPlayerControlled(true);
		a1.setCurrentAltitude(4);
		a1.setFacing(FACING.NORTH);
		
		a2.acceleration = ACCELERATION.HIGH;
		a2.afterburner = AFTERBURNER.YES;
		a2.afterburnersOn = true;
		a2.aircraftName = "F-16";
		a2.setHexLocation("C4");
		a2.setId(24);
		a2.setPlayerControlled(true);
		a2.setCurrentAltitude(4);
		a2.setFacing(FACING.NORTH);
		
		LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
		playerAircraft.add(a1);
		playerAircraft.add(a2);
		
		DetermineInitiativeAction dta = new DetermineInitiativeAction();
		assertTrue(dta.isRouteFormation(playerAircraft, a1));
	}
	
	@Test
	public void isRouteFormationTrailingLeftTest() {
		
		Aircraft a1 = new Aircraft();
		Aircraft a2 = new Aircraft();
		
		a1.acceleration = ACCELERATION.HIGH;
		a1.afterburner = AFTERBURNER.YES;
		a1.afterburnersOn = false;
		a1.aircraftName = "F-22";
		a1.setHexLocation("B4");
		a1.setId(23);
		a1.setPlayerControlled(true);
		a1.setCurrentAltitude(4);
		a1.setFacing(FACING.NORTH);
		
		a2.acceleration = ACCELERATION.HIGH;
		a2.afterburner = AFTERBURNER.YES;
		a2.afterburnersOn = true;
		a2.aircraftName = "F-16";
		a2.setHexLocation("C4");
		a2.setId(24);
		a2.setPlayerControlled(true);
		a2.setCurrentAltitude(4);
		a2.setFacing(FACING.NORTH);
		
		LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
		playerAircraft.add(a1);
		playerAircraft.add(a2);
		
		DetermineInitiativeAction dta = new DetermineInitiativeAction();
		assertTrue(dta.isRouteFormation(playerAircraft, a1));
	}
	
	@Test
	public void isRouteFormationOtherChecksTest() {
		
		Aircraft a1 = new Aircraft();
		Aircraft a2 = new Aircraft();
		
		a1.acceleration = ACCELERATION.HIGH;
		a1.afterburner = AFTERBURNER.YES;
		a1.afterburnersOn = false;
		a1.aircraftName = "F-22";
		a1.setHexLocation("B4");
		a1.setId(23);
		a1.setPlayerControlled(true);
		a1.setCurrentAltitude(4);
		a1.setFacing(FACING.NORTH);
		
		a2.acceleration = ACCELERATION.HIGH;
		a2.afterburner = AFTERBURNER.YES;
		a2.afterburnersOn = true;
		a2.aircraftName = "F-16";
		a2.setHexLocation("C4");
		a2.setId(24);
		a2.setPlayerControlled(true);
		a2.setCurrentAltitude(4);
		a2.setFacing(FACING.NORTH);
		
		LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
		playerAircraft.add(a1);
		playerAircraft.add(a2);
		
		DetermineInitiativeAction dta = new DetermineInitiativeAction();
		assertTrue(dta.isRouteFormation(playerAircraft, a1));
		
		// distance > 1
		playerAircraft.get(0).setHexLocation("F6");
		assertFalse(dta.isRouteFormation(playerAircraft, a1));
		
		// facing other direction
		playerAircraft.get(0).setHexLocation("B4");
		playerAircraft.get(0).setFacing(FACING.NORTH_EAST);
		assertFalse(dta.isRouteFormation(playerAircraft, a1));
		
		// different altitudes
		playerAircraft.get(0).setHexLocation("B4");
		playerAircraft.get(0).setFacing(FACING.NORTH);
		playerAircraft.get(0).setCurrentAltitude(5);
		assertFalse(dta.isRouteFormation(playerAircraft, a1));
	}
	
	@Test
	public void isFingertipFormationTest() {
		
	}
}
