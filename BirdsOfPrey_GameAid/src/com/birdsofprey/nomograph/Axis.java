package com.birdsofprey.nomograph;

import java.util.LinkedList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Axis {

	static public enum SIDE {
		LEFT,
		RIGHT,
		BOTH
	}
	
	private double fontSize;
	private SIDE side;
	private LinkedList<LinkedList<Double>> yAxisValues = new LinkedList<LinkedList<Double>>();
	
	/**
	 * The location in the graph where the axis is located
	 */
	private Point2D graphLocation;
	
	/**
	 * the height of the axis
	 */
	private int height;
	
	/**
	 * how often a value is printed on the graph
	 */
	private double plotPrintIncrement;
	
	/**
	 * each value represents the delta-value for each tick. Index 0 is for the printed
	 * major tick, then the next smaller size is index 1, etc.
	 */
	private double[] divisions;
	
	/**
	 * the colors for the printed value for a given value
	 */
	private LinkedList<ColorRange> textColorRanges = new LinkedList<ColorRange>();

	/**
	 * if true the starting value starts at the bottom and works its way up
	 */
	private boolean ascending;
	
	/**
	 * the starting value to start the axis with
	 */
	private double startingValue;
	
	/**
	 * the value to stop drawing the axis at
	 */
	private double endingValue;
	
	/**
	 * if true, then the distance between each tick remains the same as the values change
	 */
	private boolean isLinear;
	
	/**
	 * the amount by which the spacing between ticks gradually grows
	 */
	private double nonLinearDeltaTick;
	
	/**
	 * the starting amount for non linear increments between ticks
	 */
	private double startingNonLinearDeltaTick;
	
	/**
	 * the starting amount for non linear increments between ticks
	 */
	private double startingLinearDeltaTick;
	
	/**
	 * the amount by which the spacing between ticks gradually grows
	 */
	private double linearDeltaTick;
	
	public Point2D getGraphLocation() {
		return graphLocation;
	}

	public Axis setGraphLocation(Point2D graphLocation) {
		this.graphLocation = graphLocation;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public Axis setHeight(int height) {
		this.height = height;
		return this;
	}

	public double getPlotPrintIncrement() {
		return plotPrintIncrement;
	}

	public Axis setPlotPrintIncrement(double plotPrintIncrement) {
		this.plotPrintIncrement = plotPrintIncrement;
		return this;
	}

	public double[] getDivisions() {
		return divisions;
	}

	public Axis setDivisions(double[] divisions) {
		this.divisions = divisions;
		
		yAxisValues = new LinkedList<>();
		for (int i = 0; i < divisions.length; i++)
		{
			yAxisValues.add(new LinkedList<>());
		}
		return this;
	}

	public Axis addTextColorRanges(ColorRange textColorRange) {
		this.textColorRanges.add(textColorRange);
		return this;
	}

	public boolean isAscending() {
		return ascending;
	}

	public Axis setAscending(boolean ascending) {
		this.ascending = ascending;
		return this;
	}

	public double getStartingValue() {
		return startingValue;
	}

	public Axis setStartingValue(double startingValue) {
		this.startingValue = startingValue;
		return this;
	}

	public boolean isLinear() {
		return isLinear;
	}

	public Axis setLinear(boolean isLinear) {
		this.isLinear = isLinear;
		return this;
	}

	public double getNonLinearDeltaTick() {
		return nonLinearDeltaTick;
	}

	public Axis setNonLinearDeltaTick(double nonLinearDeltaTick) {
		this.nonLinearDeltaTick = nonLinearDeltaTick;
		return this;
	}

	public Axis setStartingNonLinearDeltaTick(double startingNonLinearDeltaTick) {
		this.startingNonLinearDeltaTick = startingNonLinearDeltaTick;
		return this;
	}

	public Axis setStartingLinearDeltaTick(double startingLinearDeltaTick) {
		this.startingLinearDeltaTick = startingLinearDeltaTick;
		return this;
	}

	public Axis setLinearDeltaTick(double linearDeltaTick) {
		this.linearDeltaTick = linearDeltaTick;
		return this;
	}
	
	public Axis setEndingValue(double endingValue) {
		this.endingValue = endingValue;
		return this;
	}
	
	public Axis setFontSize(double fontSize) {
		this.fontSize = fontSize;
		return this;
	}
	
	public Axis setSide(SIDE side) {
		this.side = side;
		return this;
	}
	
	public void draw(GraphicsContext gc)
	{
		/* draw main axis spine */
		gc.setFill(Color.BLACK);
		gc.setLineWidth(2.0);
		gc.moveTo(graphLocation.getX(), graphLocation.getY());
		gc.lineTo(graphLocation.getX(), graphLocation.getY() + height);
		gc.stroke();
		
		/* draw each tick */
		double yLoc = graphLocation.getY();
		double value = startingValue;
		int tickIndex = 0;
		double deltaSpacing = startingNonLinearDeltaTick;
		boolean firstTick = true;
		double stopEndingValue = endingValue;
		int step = 0;
		
		if (!ascending)
		{
			while (tickIndex < divisions.length) 
			{
				while (value >= stopEndingValue) 
				{
					
					
					if (tickIndex == 0)
					{
						yAxisValues.get(tickIndex).add(value);
					}
					else
					{
						
					}
					
					drawTick(gc, tickIndex, yLoc, value);
					step++;
					value -= divisions[0];
					
					if (isLinear)
					{
						yLoc += linearDeltaTick;
					}
					else
					{
						if (firstTick)
						{
							yLoc += (deltaSpacing);
							firstTick = false;
						}
						else
						{
							yLoc += (deltaSpacing);
							deltaSpacing -= Math.pow(nonLinearDeltaTick, 2);
						}
					}
				}
				
				tickIndex++;
				if (tickIndex < divisions.length) 
				{
					yLoc = graphLocation.getY();
					value = startingValue;
					deltaSpacing = startingNonLinearDeltaTick;
					firstTick = true;
					stopEndingValue = endingValue;
					step = 0;
				}
			}
		}
		
	}
	
	private void drawTick(GraphicsContext gc, int index, double yValue, double value)
	{
		
		/* setup values */
		int tickWidth = 8;
		double leftSideTick = graphLocation.getX() - (tickWidth - (index * 3));
		double rightSideTick = graphLocation.getX() + (tickWidth - (index * 3));
		
		/* draw tick */
		gc.setFill(Color.BLACK);
		gc.setLineWidth(2.0 - (index * .5));
		
		switch (side)
		{
			case LEFT:
				gc.moveTo(leftSideTick, yValue);
				gc.lineTo(graphLocation.getX(), yValue);
				break;
			case RIGHT:
				gc.moveTo(graphLocation.getX(), yValue);
				gc.lineTo(rightSideTick, yValue);
				break;
			case BOTH:
				gc.moveTo(leftSideTick, yValue);
				gc.lineTo(rightSideTick, yValue);
				break;
		}
		
		gc.stroke();
		
		/* draw text if needed */
		if (index == 0)
		{
			gc.setFont(Font.font(fontSize));
			gc.fillText(""+value, leftSideTick - 34, yValue + 4);
		}
		
	}
}
