package com.birdsofprey.nomograph;

public class Tick {

	private int division;
	private double yValue;
	private double value;
	
	public Tick(int division, double yValue, double value) {
		super();
		this.division = division;
		this.yValue = yValue;
		this.value = value;
	}
	
	public Tick(Tick tick)
	{
		this.division = tick.division;
		this.yValue = tick.yValue;
		this.value = tick.value;
	}
	
	public int getDivision() {
		return division;
	}
	public void setDivision(int division) {
		this.division = division;
	}
	public double getyValue() {
		return yValue;
	}
	public void setyValue(double yValue) {
		this.yValue = yValue;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	public String toString()
	{
		return "Division: " + division + ", yValue: " + yValue + ", Value: " + value;
	}
	
}
