package com.flightleader.controller;

import java.util.List;

import org.hexworks.mixite.core.api.CubeCoordinate;
import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.HexagonalGrid;
import org.hexworks.mixite.core.api.Point;

import com.flightleader.hex.HexData;
import com.flightleader.hex.HexUtils;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;

public class MainWindowController implements Subscriber {

	private static final int ACTUAL_GRID_HEIGHT = 35;
	private static final int ACTUAL_GRID_WIDTH = 59;
	private double zoom = 1.0;
	private Point2D dragStart;
	private Point2D canvasDrag;
	private Point2D canvasTranslation;
	private Point2D zoomOrigin;
	private Point2D oldZoomOrigin = new Point2D(0,0);
	private boolean zoomChange = false;
	private double oldZoom = 1.0;
	private double dx = 0;
	private double dy = 0;
	
	@FXML
	Canvas canvas;
	
    @FXML
    public void initialize() {
       
    	MessageBus.getInstanceOf().addSubscriber("windowSizeChanged", this);
    	zoomOrigin = new Point2D(0,0);
    	canvasDrag = new Point2D(0,0);
    	canvasTranslation = new Point2D(0,0);
    	
        canvas.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
            	
            	event.consume();
            	double delta = 1.2;
            	zoomChange = true;
            	oldZoomOrigin = new Point2D(zoomOrigin.getX(), zoomOrigin.getY());
            	oldZoom = zoom;
            	zoomOrigin = new Point2D(event.getX(), event.getY());
            	
                if (event.getDeltaY() < 0)
                	zoom /= delta;
                else
                	zoom *= delta;
            	
            	zoom = Math.max(0.5, zoom);
            	zoom = Math.min(44.0, zoom);

            	drawCanvas();
    	        
            }});
        
    	drawCanvas();
    }
    
    private void drawCanvas()
    {
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.setTransform(new Affine());
    	gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    	gc.translate(canvasTranslation.getX()+ canvasDrag.getX(), canvasTranslation.getY()+canvasDrag.getY());
    	
//    	gc.translate(zoomOrigin.getX(), zoomOrigin.getY());
    	
    	Affine a = gc.getTransform();
    	Affine a2 = gc.getTransform();
    	a.appendScale(zoom, zoom, zoomOrigin);
    	a2.appendScale(zoom	, zoom, oldZoomOrigin);
    	
		if (zoomChange)
		{
			zoomChange = false;
			System.out.println("----------------------------------------------------------------");
			System.out.println("a: "+a);
			System.out.println("zoomOrigin: "+zoomOrigin);
			System.out.println("----------------------------------------------------------------");
			System.out.println("a2: "+a2);
			System.out.println("oldZoomOrigin: "+oldZoomOrigin);
//			gc.translate(a.getTx()-a2.getTx(), a.getTy()-a2.getTy());
			gc.setTransform(a2);
			
		}
		else {
			gc.setTransform(a);
		}
	
		
//    	gc.translate(zoomOrigin.getX(), zoomOrigin.getY());
//		gc.scale(zoom, zoom);
//    	gc.translate(-zoomOrigin.getX(), -zoomOrigin.getY());
	
		drawHexes(gc);
    }
    
    private void drawHexes(GraphicsContext gc) 
    {
    	for (int col = 0; col < ACTUAL_GRID_WIDTH; col++)
    	{
    		for (int row = 0; row < ACTUAL_GRID_HEIGHT ; row++)
    		{
    			Hexagon<HexData> hex = HexUtils.getInstanceOf().getHexagonByCoordinate(
    					col, row);
    			List<Point> points = hex.getPoints();
    			
		        gc.setStroke(Color.BLACK);
		        gc.setLineWidth(2);
		        gc.strokePolygon(
		        		new double[]{
			        		points.get(0).getCoordinateX(),
			        		points.get(1).getCoordinateX(),
			        		points.get(2).getCoordinateX(),
			        		points.get(3).getCoordinateX(),
			        		points.get(4).getCoordinateX(),
			        		points.get(5).getCoordinateX()},
		        		new double[]{
			        		points.get(0).getCoordinateY(),
			        		points.get(1).getCoordinateY(),
			        		points.get(2).getCoordinateY(),
			        		points.get(3).getCoordinateY(),
			        		points.get(4).getCoordinateY(),
			        		points.get(5).getCoordinateY()}, 
		        		6);
		        
		        gc.setFont(Font.font("monospaced", 11.0));
		        gc.setFill(Color.BLACK);
		        gc.fillText(hex.getSatelliteData().get().getName(), points.get(2).getCoordinateX()-1, points.get(2).getCoordinateY()-4);
    		}
    	}
    }
    
    public void onMouseClicked(MouseEvent event)
    {
    	if (event.getButton() == MouseButton.SECONDARY)
    	{
    	}
    	
    	event.consume();
    }
    
    public void onMouseDragged(MouseEvent event)
    {
    	if (dragStart == null)
    	{
    		dragStart = new Point2D(event.getX(), event.getY());
    	}
    	
    	clampTranslation(event.getX(), event.getY());
    	drawCanvas();
    }
    
    private void clampTranslation(double x, double y)
    {
    	if (dragStart == null) return;
    	
    	double newX = x - dragStart.getX();
    	double newY = y - dragStart.getY();

//    	if (newX + canvasTranslation.getX() > canvas.getWidth() -75)
//    	{
//    		newX = canvas.getWidth() - canvasTranslation.getX() - 75;
//    	}
//    	else if (newX + canvasTranslation.getX() < ACTUAL_GRID_WIDTH * -44.25)
//    	{
//    		newX = (ACTUAL_GRID_WIDTH * -44.25) - canvasTranslation.getX();
//    	}
//    	
//    	if (newY + canvasTranslation.getY() > canvas.getHeight() -118)
//    	{
//    		newY = canvas.getHeight() - canvasTranslation.getY() - 118;
//    	}
//    	else if (newY + canvasTranslation.getY() < ACTUAL_GRID_HEIGHT * -51.18)
//    	{
//    		newY =  (ACTUAL_GRID_HEIGHT * -51.18) - canvasTranslation.getY();
//    	}
    	
    	canvasDrag = new Point2D(newX, newY);
    }
    
    public void onMouseReleased(MouseEvent event)
    {
    	
    	if (event.getButton() == MouseButton.SECONDARY)
    	{
    		return;
    	}
    	
    	dragStart = null;
    	canvasTranslation = canvasTranslation.add(canvasDrag);
    }

	@Override
	public Object onMessageReceived(String topic, Object message) {
		
		switch (topic)
		{
			case "windowSizeChanged":
				Dimension2D d = (Dimension2D)message;
				
				if (!Double.isNaN(d.getHeight()) && !Double.isNaN(d.getWidth()))
				{
					canvas.setWidth(d.getWidth());
					canvas.setHeight(d.getHeight());
					clampTranslation(0,0);
					drawCanvas();
				}
				break;
		}
		
		return null;
	}
}
