package com.flightleader.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.Point;
import java.lang.reflect.Type;
import java.time.LocalDate;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.aircraft.Aircraft.ACCELERATION;
import com.flightleader.aircraft.Aircraft.AFTERBURNER;
import com.flightleader.aircraft.Aircraft.CANOPY_TYPE;
import com.flightleader.aircraft.Aircraft.INTERNAL_GUNS;
import com.flightleader.aircraft.Aircraft.PRIMARY_USE;
import com.flightleader.aircraft.Aircraft.SIZE;
import com.flightleader.aircraft.Aircraft.SUPERSONIC;
import com.flightleader.aircraft.Aircraft.TURNTYPE;
import com.flightleader.aircraft.AircraftInfoController;
import com.flightleader.aircraft.AircraftListController;
import com.flightleader.hex.HexData;
import com.flightleader.hex.HexUtils;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class MainWindowController implements Subscriber {

	private static final int ACTUAL_GRID_HEIGHT = 35;
	private static final int ACTUAL_GRID_WIDTH = 59;
	private double zoom = 1.0;
	private Point2D dragStart;
	private Point2D canvasDrag;
	private Point2D canvasTranslation;
	private HashMap<String, Aircraft> aircraftList = new HashMap<String, Aircraft>();
	
	@FXML
	private Canvas canvas;
	
    @FXML
    public void initialize() {
       
    	MessageBus.getInstanceOf().addSubscriber("windowSizeChanged", this);
    	MessageBus.getInstanceOf().addSubscriber("aircraftSaved", this);
    	MessageBus.getInstanceOf().addSubscriber("editAircraft", this);
    	MessageBus.getInstanceOf().addSubscriber("deleteAircraft", this);
    	MessageBus.getInstanceOf().addSubscriber("saveBeforeExit", this);
    	
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
        
        loadAircraftList();
//        Aircraft demo = new Aircraft();
//        demo.entryDate = LocalDate.now().toString();
//        demo.acceleration = ACCELERATION.HIGH;
//        demo.afterburner = AFTERBURNER.NO;
//        demo.aircraftName = "F-14/A";
//        demo.canopyType = CANOPY_TYPE.BUBBLE_CANOPY;
//        demo.countermeasure = 9;
//        demo.crewSize = 2;
//        demo.internalGuns = INTERNAL_GUNS.MACHINE_GUN;
//        demo.maxAltitude = 15;
//        demo.maxSpeed = 20;
//        demo.missileRails = 8;
//        demo.notes = "qw dqwd qwqwd qwqwd wd";
//        demo.primaryUse = PRIMARY_USE.INTERCEPTOR;
//        demo.radar = 12;
//        demo.size = SIZE.LARGE;
//        demo.supersonic = SUPERSONIC.NO;
//        demo.turnType = TURNTYPE.B;
//        demo.setCrewQualityAValue(11);
//        demo.setCrewQualityBValue(22);
//        demo.setCrewQualityCValue(33);
//        demo.setCrewQualityDValue(44);
//        demo.setCrewQualityEValue(55);
//        demo.setCrewQualityFValue(66);
//        aircraftList.put(demo.getAircraftName(), demo);
        
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
		        
		        gc.setFont(Font.font("monospaced", 11.0*(HexUtils.getInstanceOf().getHexSize()/32)));
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
    		
    		drawCanvas();
    		event.consume();
      }
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
    
    private void displayAircraftInfoView(Aircraft aircraft)
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/aircraftInfo.fxml"));
    	Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	AircraftInfoController controller = loader.getController();
        Scene scene = new Scene(root, 640, 740);
        Stage stage = new Stage();
        controller.setStage(stage);
        if (aircraft != null)
        {
        	controller.loadAircraft(aircraft);
        }
        stage.setScene(scene);
        stage.show();
    }
    
    public void addAircraft(ActionEvent event)
    {
    	displayAircraftInfoView(null);
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
				
			case "aircraftSaved":
				
				Aircraft aircraft = (Aircraft)message;
				aircraftList.put(aircraft.getAircraftName(), aircraft);
				saveAircraftList();
				break;
				
			case "editAircraft":
				
				aircraft = new Aircraft((Aircraft)message);
				displayAircraftInfoView(aircraft);
				break;
				
			case "deleteAircraft":
				
				aircraft = ((Aircraft)message);
				aircraftList.remove(aircraft.getAircraftName());
				break;
				
			case "saveBeforeExit":
				
				saveAircraftList();
				break;
		}
		
		return null;
	}

	public void listAircraft()
	{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/aircraftList.fxml"));
    	Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	AircraftListController controller = loader.getController();
    	controller.setAircraftMap(aircraftList);
        Scene scene = new Scene(root, 1612, 300);
        scene.getStylesheets().add(getClass().getResource("/aircraftListCss.css").toExternalForm());
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
	}
	
	private void loadAircraftList()
	{
		String userDir = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");
		String fileName = "flightLeader_config.json";
		Gson gson = new Gson();
		
		File f = new File(userDir+sep+fileName);
		if (!f.exists()) return;
		
		try (FileReader reader = new FileReader(userDir+sep+fileName)) {
			Type type = new TypeToken<HashMap<String, Aircraft>>(){}.getType();
			aircraftList = gson.fromJson(reader, type); 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void saveAircraftList() {
		
		String userDir = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");
		String fileName = "flightLeader_config.json";
		Gson gson = new Gson();
		
		try (FileWriter writer = new FileWriter(userDir+sep+fileName)) {
            gson.toJson(aircraftList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void newScenario()
	{
		System.out.println("new Scenario");
	}
}
