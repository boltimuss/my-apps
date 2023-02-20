package map;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import ship.Ship;

/**
 * Represents a hex on the map view
 * 
 * @author Lance
 *
 */
public class MapHex {

	private int column = -1;
	private int row = -1;
	private int hexNum;
	private Point2D[] hexPoints = new Point2D[6];
	private Ship ship;
	private Color outline = Color.WHITE;
	private double lineWidth = 1.0;
	
	public MapHex()
	{
	}
	
	public double getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setOutline(Color outline)
	{
		this.outline = outline;
	}
	
	public Color getOutline()
	{
		return outline;
	}
	
	public void setShip(Ship ship)
	{
		this.ship = ship;
	}
	
	public Ship getShip()
	{
		return ship;
	}
	
	public int getHexNum() {
		return hexNum;
	}
	
	public void setHexNum(int num) {
		hexNum = num;
	}
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public Point2D getHexPoint(int degree)
	{
		return hexPoints[degree/60];
	}
	
	public void setHexPoint(int degree, Point2D pt)
	{
		hexPoints[degree/60] = pt;
	}
	
	public double getHexWidth()
	{
		return hexPoints[3].getX() - hexPoints[5].getX();
	}
	
	public double getHexCompleteWidth()
	{
		return hexPoints[2].getX() - hexPoints[5].getX();
	}
	
	public double getHexHeight()
	{
		return hexPoints[3].getY() - hexPoints[0].getY();
	}
	
	public void setXOffset(double xOffset)
	{
		for (int i = 0; i < 6; i++)
		{
			hexPoints[i] = new Point2D(hexPoints[i].getX() + xOffset, hexPoints[i].getY());
		}
	}
	
}
