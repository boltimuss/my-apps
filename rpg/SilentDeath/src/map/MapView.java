package map;

import java.util.LinkedList;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import ship.Ship;

/**
 * Displays the map where the ships are moved
 * 
 * @author Lance
 *
 */
public class MapView {

	private int width;
	private int height;
	private int sideLength = 50;
	private int numCols;
	private int numRows;
	private double zoomFactor = 1.0;
	private double mouseTranslationXTotal = 0;
	private double mouseTranslationYTotal = 0;
	private double mouseTranslationX = 0;
	private double mouseTranslationY = 0;
	private double dragStartX = -9999999;
	private double dragStartY = -9999999;
	private MapModel mapModel;
	private Stage stage;
	private Canvas mapCanvas;
	private double maxZoomFactor;
	private double minZoomFactor;
	private double hexOffsetX;
	private double deltaZoom;
	private double mapWidth;
	private double mapHeight;
	private SelectionMode selectMode;
	private SelectionMode previousSelectMode;
	private double mouseX;
	private double mouseY;
	private int selectedSrcHex = -1;
	private int selectedDestHex = -1;
	
	public enum SelectionMode 
	{
		NONE,
		SELECT_SRC_DEST_HEX,
		SELECT_SRC_HEX,
		SELECT_FOR_ROTATION
	}
	
	public MapView()
	{
		this.width 	= 100;
		this.height = 100;
		mapCanvas = new Canvas(width, height);
		this.mapModel = new MapModel();
	}
	
	public MapView(int width, int height, int cols, int rows)
	{
		this.width 	= width; 
		this.height = height;
		this.mapModel = new MapModel();
		mapCanvas = new Canvas(width, height);
		numCols = cols;
		numRows = rows;
		initHexMap();
	}
	
	private void initHexMap()
	{
		double topOfRow = 0;
		double[] nextRowStart = new double[] {0,0,0};
		
		for (int row = 0; row < numRows; row++)
		{
			nextRowStart = new double[]{0,0,nextRowStart[2]};
			topOfRow = nextRowStart[2];
			
			for (int col = 0; col < numCols; col++)
			{
				MapHex hex = new MapHex();
				hex.setColumn(col);
				hex.setRow(row);
				hex.setHexNum(1000 + (numCols * row) + col);
				
				if ((col % 2) == 0)
				{
					nextRowStart = initHex(hex, nextRowStart[0], topOfRow);
				}
				else
				{
					double[] temp = initHex(hex, nextRowStart[0], nextRowStart[1]);
					nextRowStart[0] = temp[0];
					nextRowStart[1] = temp[1];
				}
				
				mapModel.addHex(hex);
			}
		}
	}
	
	private double[] initHex(MapHex hex, double x, double y)
	{
		double[] nextRowCoord = new double[] {0, 0, 0};
		
		for ( int deg = 0; deg < 360; deg+= 60) {
			
			hex.setHexPoint(deg, new Point2D(x, y));
			double destX = Math.cos(deg * (Math.PI / 180) ) * sideLength;
			double destY = Math.sin(deg * (Math.PI / 180) ) * sideLength;
	        x = x + destX;
	        y = y + destY;
	        
	        if (deg == 60)
	        {
	        	nextRowCoord[0] = x;
	        	nextRowCoord[1] = y;
	        	
	        }
	        else if (deg == 180)
	        {
	        	nextRowCoord[2] = y;
	        }
		}
		
		return nextRowCoord;
	}
	
	public void displayMap()
	{
		try {
			
			mapCanvas.setWidth(width);
			mapCanvas.setHeight(height);
			maxZoomFactor = 2;
			hexOffsetX = mapModel.getCompleteHexWidth() - mapModel.getHexWidth();
			mapModel.setHexOffsetX(hexOffsetX);
			mapWidth = ((numCols * mapModel.getHexWidth()) + mapModel.getCompleteHexWidth() - mapModel.getHexWidth());
			mapHeight = ((numRows * mapModel.getHexHeight()) + (mapModel.getHexHeight() * .75));
			minZoomFactor = width / (numCols * mapModel.getCompleteHexWidth());
			minZoomFactor = Math.min(width / mapWidth, height / mapHeight);
			zoomFactor = minZoomFactor;
			mouseTranslationXTotal = ((width - mapWidth) / 2);
			mouseTranslationYTotal = ((height - mapHeight) / 2);
			
			stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Combat Map");
			stage.getIcons().add(new Image(getClass().getResourceAsStream("../resources/hex-outline.png")));
			ScrollPane sp = new ScrollPane(mapCanvas);
			sp.setStyle("-fx-background: rgb(0,0,0); -fx-background-color: rgb(0,0,0)");
			sp.setVbarPolicy(ScrollBarPolicy.NEVER);
			sp.setHbarPolicy(ScrollBarPolicy.NEVER);
			sp.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
			    @Override
			    public void handle(ScrollEvent event) {
			    	
			    	if (selectMode == SelectionMode.SELECT_FOR_ROTATION)
			    	{
			    		if (event.getDeltaY() > 0)
				        {
				        	mapModel.getHexByNum(selectedSrcHex).getShip().decFacing();
				        } 
				        else 
				        {
				        	mapModel.getHexByNum(selectedSrcHex).getShip().incFacing();
				        }
			    	}
			    	
			    	else 
			    	{
				        if (event.getDeltaY() > 0)
				        {
				        	deltaZoom = .25;
				        } 
				        else 
				        {
				        	deltaZoom = -.25;
				        }
				        
				        zoomFactor += deltaZoom;
				        zoomFactor = Math.max(minZoomFactor, zoomFactor);
				        zoomFactor = Math.min(maxZoomFactor, zoomFactor);
				        event.consume();

				        checkBoundaries();
			    	}
			        
			        drawHexMap();
			    }
			});
			
			mapCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent me) {
			      if (me.getButton() == MouseButton.SECONDARY) 
			      {
			    	  dragStartX = me.getSceneX();
			    	  dragStartY = me.getSceneY();
			      }
			    }
			  });
			mapCanvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent me) {
			      if (me.getButton() == MouseButton.SECONDARY) 
			      {
			    	  mapCanvas.setCursor(Cursor.CLOSED_HAND);
		    		  mouseTranslationX = (me.getSceneX() - dragStartX) * (1 / zoomFactor);
		    		  mouseTranslationY = (me.getSceneY() - dragStartY) * (1 / zoomFactor);
		    		  
		    		  checkBoundaries();
				      drawHexMap();
			      }
			    }
			  });
			mapCanvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent me) {
			    	mapCanvas.setCursor(Cursor.DEFAULT);
			    	mouseTranslationXTotal += mouseTranslationX;
			    	mouseTranslationYTotal += mouseTranslationY;
			    	mouseTranslationX = 0;
			    	mouseTranslationY = 0;
			    	drawHexMap();
			    }
			  });
			mapCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
			    @Override
			    public void handle(MouseEvent me) {
			    	
					if (selectMode == SelectionMode.NONE)
					{
						return;
					}
					
					else if ((selectMode == SelectionMode.SELECT_SRC_DEST_HEX || selectMode == SelectionMode.SELECT_SRC_HEX) && me.getButton() == MouseButton.PRIMARY) 
					{
						selectHex(me);
					}
					
					else if (me.getButton() == MouseButton.MIDDLE) 
					{
						rotateShip(me);
					}
					
			    }
			  });
			
			Scene scene = new Scene(sp, width, height, Color.BLACK);
			scene.getStylesheets().add(getClass().getResource("mapView.css").toExternalForm());
			drawHexMap();
			stage.setScene(scene);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void rotateShip(MouseEvent me)
	{
    	mouseX = me.getSceneX();
    	mouseY = me.getSceneY();
    	mouseX-= ((width - (width*zoomFactor))/2);
    	mouseY-= ((height - (height*zoomFactor))/2);
    	mouseX/= zoomFactor;
    	mouseY/= zoomFactor;
    	mouseX-= mouseTranslationXTotal + mouseTranslationX;
    	mouseY-= mouseTranslationYTotal + mouseTranslationY;
    	
    	MapHex hex = mapModel.getHexAtPoint(mouseX, mouseY);
    	
    	if (hex != null) {
        	
    		Ship ship = hex.getShip();
    		
    		if (ship != null && hex.getHexNum() != selectedSrcHex)
    		{
	    		hex.setOutline(Color.rgb(0, 0, 255));
	    		hex.setLineWidth(4.0);
	    		selectedSrcHex = hex.getHexNum();
	        	previousSelectMode = selectMode;
	        	selectMode = SelectionMode.SELECT_FOR_ROTATION;
    		}
    		else
    		{
    			hex.setOutline(Color.WHITE);
	    		hex.setLineWidth(1.0);
	    		selectedSrcHex = -1;
	        	selectMode = previousSelectMode;
    		}
    	}
    	
    	drawHexMap();
	}
	
	private void selectHex(MouseEvent me)
	{
    	
    	mouseX = me.getSceneX();
    	mouseY = me.getSceneY();
    	mouseX-= ((width - (width*zoomFactor))/2);
    	mouseY-= ((height - (height*zoomFactor))/2);
    	mouseX/= zoomFactor;
    	mouseY/= zoomFactor;
    	mouseX-= mouseTranslationXTotal + mouseTranslationX;
    	mouseY-= mouseTranslationYTotal + mouseTranslationY;
    	
    	MapHex hex = mapModel.getHexAtPoint(mouseX, mouseY);
    	previousSelectMode = selectMode;
    	
    	if (hex != null) {
    	
    		/* if selectedSrcHex == -1, or if select mode is src only then select this hex as source */
    		if (selectedSrcHex == -1 || (selectMode == SelectionMode.SELECT_SRC_HEX && hex.getHexNum() != selectedSrcHex))
	    	{
	    		hex.setOutline(Color.rgb(0, 255, 0));
	    		hex.setLineWidth(4.0);
	    		selectedSrcHex = hex.getHexNum();
	    	}
    			
    		/* if hexNum == selectedSrcHex, then deselect the src and dest hex */
    		else if (hex.getHexNum() == selectedSrcHex)
    		{
    			hex.setOutline(Color.WHITE);
	    		hex.setLineWidth(1.0);
	    		selectedSrcHex = -1;
	    		selectedDestHex = -1;
    		}
    		
    		/* if hexnum != selectedSrcHex and selectedDestHex == -1 the select this hex as dest */
    		else if (selectMode == SelectionMode.SELECT_SRC_DEST_HEX && hex.getHexNum() != selectedSrcHex && selectedDestHex == -1)
    		{
    			hex.setOutline(Color.RED);
	    		hex.setLineWidth(4.0);
	    		selectedDestHex = hex.getHexNum();
    		}
    		
    		/* if hexnum == selectedDestHex, then deselect the the dest hex */
    		else if (selectMode == SelectionMode.SELECT_SRC_DEST_HEX && hex.getHexNum() == selectedDestHex)
    		{
    			hex.setOutline(Color.WHITE);
	    		hex.setLineWidth(1.0);
	    		selectedDestHex = -1;
    		}
	    	
    	}
    	
    	drawHexMap();
	}
	
	private void checkBoundaries()
	{
		double xMax = (((((((width - mapWidth) / 2))-(((width / zoomFactor) - mapWidth) / 2)))+(width/zoomFactor))-mapModel.getCompleteHexWidth());
		double xMin = (((((((width - mapWidth) / 2))-(((width / zoomFactor) - mapWidth) / 2))))-mapWidth+mapModel.getCompleteHexWidth());
		double yMin = (((((((height - mapHeight) / 2))-(((height / zoomFactor) - mapHeight) / 2))))-mapHeight+(mapModel.getHexHeight()*1.25));
		double yMax = (((((((height - mapHeight) / 2))-(((height / zoomFactor) - mapHeight) / 2)))+(height/zoomFactor))-mapModel.getHexHeight());
		  
		if ((mouseTranslationX + mouseTranslationXTotal) > xMax)
		{
			mouseTranslationX = xMax - mouseTranslationXTotal;
		}
		if ((mouseTranslationX + mouseTranslationXTotal) < xMin)
		{
			mouseTranslationX = xMin - mouseTranslationXTotal;
		}
		if ((mouseTranslationY + mouseTranslationYTotal) > yMax)
		{
			mouseTranslationY = yMax - mouseTranslationYTotal;
		}
		if ((mouseTranslationY + mouseTranslationYTotal) < yMin)
		{
			mouseTranslationY = yMin - mouseTranslationYTotal;
		}
	}
	
	private void drawHexMap()
	{
		GraphicsContext gc = mapCanvas.getGraphicsContext2D();
		
		gc.setTransform(new Affine());
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		gc.translate(((width - (width*zoomFactor))/2), ((height - (height*zoomFactor))/2));
		gc.scale(zoomFactor, zoomFactor); 
		gc.translate(mouseTranslationXTotal + mouseTranslationX, mouseTranslationYTotal + mouseTranslationY);
		
		for (int row = 0; row < numRows; row++)
		{
			for (int col = 0; col < numCols; col++)
			{
				gc.beginPath();
				drawHex(gc, mapModel.getMapHexAt(col, row));
				gc.stroke();
				gc.closePath();
			}
		}
		
		/* redraw selected hexes */
		gc.beginPath();
		MapHex hex = mapModel.getHexByNum(selectedSrcHex);
		if (hex != null) drawHex(gc, mapModel.getHexByNum(selectedSrcHex));
		gc.stroke();
		gc.closePath();
	} 
	
	private void drawHex(GraphicsContext gc, MapHex hex)
	{
		/* Draw the hex */
		if (hex.getHexNum() != selectedSrcHex && hex.getHexNum() != selectedDestHex)
		{
			hex.setLineWidth(1.0);
			hex.setOutline(Color.WHITE);
		}
		gc.setLineWidth(hex.getLineWidth());
		gc.setStroke(hex.getOutline());
		
		for ( int deg = 0; deg < 360; deg+= 60) {
			
			double x1;
			double y1;
			double x2;
			double y2;
					
			if (deg < 300) 
			{
				x1 = hex.getHexPoint(deg).getX();
				y1 = hex.getHexPoint(deg).getY();
				x2 = hex.getHexPoint(deg+60).getX();
				y2 = hex.getHexPoint(deg+60).getY();
			}
			else 
			{
				x1 = hex.getHexPoint(deg).getX();
				y1 = hex.getHexPoint(deg).getY();
				x2 = hex.getHexPoint(0).getX();
				y2 = hex.getHexPoint(0).getY();
			}
			
			gc.moveTo(x1, y1);
	        gc.lineTo(x2, y2);
		}
		
		/* place the hex number */
		gc.setFill(Color.WHITE);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(""+hex.getHexNum(), hex.getHexPoint(0).getX() + ((hex.getHexPoint(60).getX() - hex.getHexPoint(0).getX())/2), hex.getHexPoint(0).getY() + 13);
		
		/* draw any ships that are attached to the hex */
		Ship ship = hex.getShip();
		if (ship != null)
		{
			Affine transform = gc.getTransform();
			gc.translate(hex.getHexPoint(300).getX() + (hex.getHexCompleteWidth()/2), hex.getHexPoint(0).getY() + (hex.getHexWidth()*.5) + 6);
			gc.rotate(ship.getFacing() * 60);
			gc.drawImage(hex.getShip().getImage(), -hex.getHexWidth()*.75*.5, -hex.getHexHeight()*.75*.5, hex.getHexWidth()*.75, hex.getHexHeight()*.75);
			gc.setTransform(transform);
		}
		
		/* draw any missiles that are attached to the hex */
		
	}
	
	public void setSelectMode(SelectionMode mode)
	{
		selectMode = mode;
	}
	
	public void addShipToHex(Ship ship, int hexCol, int hexRow)
	{
		mapModel.getMapHexAt(hexCol, hexRow).setShip(ship);
	}
}
