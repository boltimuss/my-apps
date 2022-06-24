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
	private Point2D topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner;
	private boolean hardstopLeft, hardstopTop, hardstop,Right, hardstopBottom;
	private Point2D oldCanvasDrag;
	
	@FXML
	Canvas canvas;
	
    @FXML
    public void initialize() {
       
    	MessageBus.getInstanceOf().addSubscriber("windowSizeChanged", this);
    	canvasDrag = new Point2D(0,0);
    	canvasTranslation = new Point2D(0,0);
    	
        canvas.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
            	
            	event.consume();
            	
                if (event.getDeltaY() < 0)
                {
                	zoom -= .25;
                }
                else
                {
                	zoom += .25;
                }
            	
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
    	
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	
    	gc.translate(((width - (width*zoom))/2), ((height - (height*zoom))/2));
		gc.scale(zoom, zoom);
		gc.translate(canvasTranslation.getX()+ canvasDrag.getX(), canvasTranslation.getY()+canvasDrag.getY());
		
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
    	if (event.getButton() == MouseButton.SECONDARY) 
    	{

    		canvasDrag = new Point2D((event.getSceneX() - dragStart.getX()) * (1 / zoom),
    				(event.getSceneY() - dragStart.getY()) * (1 / zoom));
    		
    		checkBounds(event);
    		drawCanvas();
    		event.consume();
      }
    }
    
    private void checkBounds(MouseEvent event)
    {
    	
    	double width = canvas.getWidth();
    	double height = canvas.getHeight();
    	
    	Affine a = new Affine();
    	a.appendTranslation(((width - (width*zoom))/2), ((height - (height*zoom))/2));
		a.appendScale(zoom, zoom);
		a.appendTranslation(canvasTranslation.getX()+ canvasDrag.getX(), canvasTranslation.getY()+canvasDrag.getY());
		
		topLeftCorner = a.transform(new Point2D(0,0));
		topRightCorner = a.transform(new Point2D(canvas.getWidth(),0));
		bottomLeftCorner = a.transform(new Point2D(0,canvas.getHeight()));
		bottomRightCorner = a.transform(new Point2D(canvas.getWidth(),canvas.getHeight()));
		
		if (topLeftCorner.getX() >= 0) 
		{
			canvasDrag = new Point2D(canvasDrag.getX()-topLeftCorner.getX(), canvasDrag.getY());
			hardstopLeft = true;
		}
		
		System.out.println("canvasDrag"+canvasDrag);
		System.out.println("topLeftCorner"+topLeftCorner);
    }
    
    public void onMousePressed(MouseEvent event)
    {
    	if (event.getButton() == MouseButton.SECONDARY) 
    	{
    		dragStart = new Point2D(event.getSceneX(), event.getSceneY());
	    }
    }
    
    public void onMouseReleased(MouseEvent event)
    {
    	
    	if (event.getButton() == MouseButton.SECONDARY)
    	{
        	canvasTranslation = canvasTranslation.add(canvasDrag);
        	canvasDrag = new Point2D(0,0);
        	event.consume();
    	}
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
					drawCanvas();
				}
				break;
		}
		
		return null;
	}
}
