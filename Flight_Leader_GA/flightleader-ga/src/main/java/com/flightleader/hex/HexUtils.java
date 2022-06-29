package com.flightleader.hex;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.HexagonOrientation;
import org.hexworks.mixite.core.api.HexagonalGrid;
import org.hexworks.mixite.core.api.HexagonalGridBuilder;
import org.hexworks.mixite.core.api.HexagonalGridCalculator;
import org.hexworks.mixite.core.api.HexagonalGridLayout;
import org.hexworks.mixite.core.api.Point;
import org.hexworks.mixite.core.api.contract.SatelliteData;

import javafx.geometry.Dimension2D;

/**
 * Class used for hex calculations
 * 
 * @author lance.additon
 *
 */
public class HexUtils {

	private static final int GRID_HEIGHT = 300;
	private static final int ACTUAL_GRID_HEIGHT = 35;
	private static final int GRID_WIDTH = 300;
	private static final int ACTUAL_GRID_WIDTH = 59;
	private static final HexagonalGridLayout GRID_LAYOUT = HexagonalGridLayout.RECTANGULAR;
	private static final HexagonOrientation ORIENTATION = HexagonOrientation.FLAT_TOP;
	private static final double RADIUS = 30;
	private HexagonalGrid<HexData> grid;
	private HexagonalGridCalculator<HexData> calculator;
	private static HexUtils instance;
	
	private HexUtils()
	{
		HexagonalGridBuilder<HexData> builder = new HexagonalGridBuilder<HexData>()
		        .setGridHeight(GRID_HEIGHT)
		        .setGridWidth(GRID_WIDTH)
		        .setGridLayout(GRID_LAYOUT)
		        .setOrientation(ORIENTATION)
		        .setRadius(RADIUS);

		grid = builder.build();
		calculator = builder.buildCalculatorFor(grid);
		initData();
	}
	
	private void initData()
	{
		for (int col = 0; col < ACTUAL_GRID_WIDTH; col++)
		{
			for (int row = 0; row < ACTUAL_GRID_HEIGHT; row++)
			{
				Hexagon<HexData> hex = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(
						col, row-(col/2))).get();
				String name = "" + (char) (65 + (col % 26));
				
				for (int i = 0; i < (col / 26); i++)
				{
					name += (char) (65 + (col % 26));
				}
				
				name += (row + 1);
				
				HexData hd = new HexData();
				hd.setName(name);
				hex.setSatelliteData(hd);
			}
		}
		
	}
	
	public static HexUtils getInstanceOf()
	{
		if (instance == null) 
		{
			instance = new HexUtils();
		}
		
		return instance;
	}
	
	public Hexagon<HexData> getHexagonByName(String name)
	{
		for (Hexagon<HexData> hex:grid.getHexagons())
		{
			if (hex.getSatelliteData().get().getName().equals(name)) return hex;
		}
		
		return null;
	}
	
	public Hexagon<HexData> getHexagonByCoordinate(int col, int row)
	{
		int x = col;
		int y = row - (col/2);
		return grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(x, y)).get();
	}
	
	public int getDistance(Point src, Point dest)
	{
		int srcX = (int) (src.getCoordinateX());
		int srcY = (int) (src.getCoordinateY());
		int destX = (int) (dest.getCoordinateX());
		int destY = (int) (dest.getCoordinateY());
		
		Hexagon<HexData> srcHex = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(srcX, srcY)).get();
		Hexagon<HexData> destHex = grid.getByCubeCoordinate(CubeCoordinate.fromCoordinates(destX, destY)).get();
		return instance.calculator.calculateDistanceBetween(srcHex, destHex);
	}
	
	public double getHexSize()
	{
		return RADIUS;
	}
	
	public HexagonalGrid<HexData> getHexGrid()
	{
		return grid;
	}
}
