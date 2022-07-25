package com.flightleader.engine;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.aircraft.Aircraft.ACCELERATION;
import com.flightleader.aircraft.Aircraft.AFTERBURNER;
import com.flightleader.engine.messages.GameSetupMessage;
import com.flightleader.messagebus.MessageBus;

public class EngineTests {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Test
	public void setupTest() {
		
		Engine e = new Engine();
		
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
		for (String s:l)
		{
			System.out.println(s);
		}
	}

}
