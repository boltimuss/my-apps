package com.flightleader.engine;

import java.util.LinkedList;
import java.util.List;

public class FlightLog {

	private LinkedList<String> logs = new LinkedList<String>();
	
	public FlightLog()
	{
		
	}
	
	public void logEntry(String logEntry)
	{
		logs.add(logEntry);
	}
	
	public List<String> getLogCopy()
	{
		return new LinkedList<String>(logs);
	}
}
