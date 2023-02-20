package map;

import java.util.HashMap;

/**
 * Contains the hexes that comprise the map
 * 
 * @author Lance
 *
 */
public class MapModel {

	private HashMap<Integer, MapHex> hexes = new HashMap<Integer, MapHex>();
	
	public MapModel()
	{
	}
	
	public void addHex(MapHex hex)
	{
		Integer hash = (hex.getColumn() * 300) + hex.getRow();
		hexes.put(hash, hex);
	}
	
	public MapHex getHexByNum(int num)
	{
		for (Integer key : hexes.keySet())
		{
			if (hexes.get(key).getHexNum() == num) return hexes.get(key);
		}
		
		return null;
	}
	
	public MapHex getMapHexAt(int column, int row)
	{
		Integer hash = (column * 300) + row;
		return hexes.get(hash);
	}
	
	public double getHexWidth()
	{
		return getMapHexAt(0, 0).getHexWidth();
	}
	
	public double getHexHeight()
	{
		return getMapHexAt(0, 0).getHexHeight();
	}
	
	public double getCompleteHexWidth()
	{
		return getMapHexAt(0, 0).getHexCompleteWidth();
	}
	
	public void setHexOffsetX(double offsetX)
	{
		for (Integer key : hexes.keySet())
		{
			hexes.get(key).setXOffset(offsetX);
		}
	}
	
	public MapHex getHexAtPoint(double x, double y)
	{
		for (Integer key : hexes.keySet())
		{
			MapHex hex = hexes.get(key);
			if (isPointWithinHex(hex, x, y)) return hex;
		}
		
		return null;
	}
	
	public boolean isPointWithinHex(MapHex hex, double x, double y)
	{
		
		/* calculate the slopes */
		double slope = (hex.getHexPoint(300).getY() - hex.getHexPoint(0).getY()) /
				       (hex.getHexPoint(0).getX() - hex.getHexPoint(300).getX());

		/* determine if y is above or below the 0-1, and 3-4 hex sides */
		if ((y < hex.getHexPoint(0).getY()) || (y > hex.getHexPoint(180).getY())) return false;
		
		/* determine if x is out side of the hex sides */
		if ((x < hex.getHexPoint(300).getX()) || (x > hex.getHexPoint(120).getX())) return false;
		
		/* determine if x is inside the box */
		if ((x >= hex.getHexPoint(0).getX()) && (x <= hex.getHexPoint(60).getX())) return true;
		
		/* check the top-left corner */
		if (x < hex.getHexPoint(0).getX() && y < hex.getHexPoint(300).getY())
		{
			double slopeInterceptX = y / slope;
			return (x > slopeInterceptX);
		}
		
		/* check the bottom-left corner */
		if (x < hex.getHexPoint(0).getX() && y >= hex.getHexPoint(300).getY())
		{
			double slopeInterceptX = hex.getHexPoint(240).getX() - ((hex.getHexPoint(240).getY() - y) / slope);
			return (x > slopeInterceptX);
		}
		
		/* check the top-right corner */
		if (x > hex.getHexPoint(60).getX() && y < hex.getHexPoint(300).getY())
		{
			double slopeInterceptX = hex.getHexPoint(120).getX() - ((hex.getHexPoint(120).getY() - y) / slope);
			return (x < slopeInterceptX);
		}
		
		/* check the bottom-right corner */
		if (x > hex.getHexPoint(60).getX() && y >= hex.getHexPoint(300).getY())
		{
			double slopeInterceptX = hex.getHexPoint(180).getX() + ((hex.getHexPoint(180).getY() - y) / slope);
			return (x < slopeInterceptX);
		}
		
		return false;
		
	}
}
