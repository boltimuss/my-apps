package com.flightleader.hex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hexworks.mixite.core.api.contract.SatelliteData;

import com.flightleader.aircraft.Aircraft;

public class HexData implements SatelliteData {

	private double movementCost = 1;
	private boolean opaque;
	private boolean passable;
	private HashMap<String, Object> data = new HashMap<>();
	private String name;
	private LinkedList<Aircraft> aircraft = new LinkedList<>();
	
	@Override
	public double getMovementCost() {
		return movementCost;
	}

	@Override
	public boolean getOpaque() {
		return opaque;
	}

	@Override
	public boolean getPassable() {
		return passable;
	}

	@Override
	public void setMovementCost(double arg0) {
		movementCost = arg0;
	}

	@Override
	public void setOpaque(boolean arg0) {
		opaque = arg0;
	}

	@Override
	public void setPassable(boolean arg0) {
		passable = arg0;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Aircraft> getAircraft() {
		return aircraft;
	}

	public void setAircraft(List<Aircraft> aircraft) {
		this.aircraft = (LinkedList<Aircraft>) aircraft;
	}

}
