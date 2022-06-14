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
//	private LinkedList<LinkedList<Double>> yAxisValues = new LinkedList<LinkedList<Double>>();
	private LinkedList<Tick> yAxisValues = new LinkedList<Tick>();
	private boolean valuesInit = false;
	
	/**
	 * The location in the graph where the axis is located
	 */
	private Point2D graphLocation;
	
	/**
	 * the height of the axis
	 */
	private double height;
	
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
	 * the amount by which the spacing between ticks gradually grows/shrinks
	 */
	private double nonLinearDeltaTick;
	
	/**
	 * the starting amount for non linear increments between ticks
	 */
	private double startingNonLinearDeltaTick;
	
	/**
	 * the starting amount for linear increments between ticks
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

	public double[] getDivisions() {
		return divisions;
	}

	public Axis setDivisions(double[] divisions) {
		this.divisions = divisions;
		
		yAxisValues = new LinkedList<>();
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
		if (!valuesInit) calculateValues();
		
		height = Math.abs(yAxisValues.get(0).getyValue() - yAxisValues.getLast().getyValue());
		
		/* draw main axis spine */
		gc.setFill(Color.BLACK);
		gc.setLineWidth(2.0);
		gc.moveTo(graphLocation.getX(), graphLocation.getY());
		gc.lineTo(graphLocation.getX(), graphLocation.getY() + height);
		gc.stroke();
		
		/* draw each tick */
		for (Tick tick:yAxisValues)
		{
			drawTick(gc, tick);
		}
	}
	
	private void calculateValues()
	{
		valuesInit = true;
		seedInitialValues();
		int div = 1;
		double currentValue = 0;
		double nextValue = 0;
		double currentYValue = 0;
		double nextYValue = 0;
		double newYValue = 0;
		double newValue = 0;
		LinkedList<Tick> newList = new LinkedList<Tick>();
		int addedElements = 1;
		
		while (div < divisions.length)
		{
			
			for (Tick tick:yAxisValues)
			{
				newList.add(new Tick(tick));
			}
			
			for (int step = 0; step < yAxisValues.size() - 1; step++)
			{
				currentValue = yAxisValues.get(step).getValue();
				nextValue = yAxisValues.get(step+1).getValue();
				currentYValue = yAxisValues.get(step).getyValue();
				nextYValue = yAxisValues.get(step+1).getyValue();
				
				if (!ascending)
				{
					newValue = currentValue - (Math.abs(currentValue - nextValue) / 2.0);
				}
				
				else
				{
					newValue = (Math.abs(currentValue - nextValue) / 2.0) + currentValue;
				}
				newYValue = (Math.abs(currentYValue - nextYValue) / 2.0) + currentYValue;
				
				newList.add(step+addedElements, new Tick(div,newYValue,newValue));
				addedElements++;
			}
			
			for (Tick tick:newList)
			{
				yAxisValues.add(new Tick(tick));
			}
			
			div++;
		}
	}
	
	private void seedInitialValues()
	{
		double value = startingValue;
		double deltaValue = 0;
		double yValue = graphLocation.getY();
		double deltaTick = startingNonLinearDeltaTick;
		
		yAxisValues.add(new Tick(0,yValue,value+deltaValue));
		value += (!ascending) ? -divisions[0] : divisions[0];
		yValue += deltaTick;
		deltaTick -= nonLinearDeltaTick;
		
		for (int i = 1; i <= ((Math.abs(startingValue - endingValue)) / divisions[0]); i++)
		{
			yAxisValues.add(new Tick(0,yValue,value+deltaValue));
			value += (!ascending) ? -divisions[0] : divisions[0];
			yValue += deltaTick;
			deltaTick -= nonLinearDeltaTick;
		}
	}
	
	private void drawTick(GraphicsContext gc, Tick tick)
	{
		
		/* setup values */
		int tickWidth = 8;
		double leftSideTick = graphLocation.getX() - (tickWidth - (tick.getDivision() * 3));
		double rightSideTick = graphLocation.getX() + (tickWidth - (tick.getDivision()  * 3));
		
		/* draw tick */
		gc.setFill(Color.BLACK);
		gc.setLineWidth(1.0);
		
		switch (side)
		{
			case LEFT:
				gc.strokeLine(leftSideTick, tick.getyValue(), graphLocation.getX(), tick.getyValue());
				break;
			case RIGHT:
				gc.strokeLine(graphLocation.getX(), tick.getyValue(), rightSideTick, tick.getyValue());
				break;
			case BOTH:
				gc.strokeLine(leftSideTick, tick.getyValue(), rightSideTick, tick.getyValue());
				break;
		}
		
		gc.stroke();
		
		/* draw text if needed */
		if (tick.getDivision() == 0)
		{
			gc.setFont(Font.font(fontSize));
			gc.fillText(""+tick.getValue(), leftSideTick - 34, tick.getyValue() + 4);
		}
		
	}
}
