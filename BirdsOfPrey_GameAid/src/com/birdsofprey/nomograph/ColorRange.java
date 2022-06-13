package com.birdsofprey.nomograph;

import javafx.scene.paint.Color;

public class ColorRange {

	private Color color;
	private double minValue;
	private double maxValue;
	
	public ColorRange(Color newColor, double newMinValue, double newMaxValue)
	{
		this.color = newColor;
		this.minValue = newMinValue;
		this.maxValue = newMaxValue; 
	}
	
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
}
