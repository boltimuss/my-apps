package com.flightleader.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.hexworks.mixite.core.api.Hexagon;
import org.hexworks.mixite.core.api.Point;
import java.lang.reflect.Type;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.aircraft.AircraftInfoController;
import com.flightleader.aircraft.AircraftListController;
import com.flightleader.hex.HexData;
import com.flightleader.hex.HexUtils;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;
import com.flightleader.scenario.Scenario;
import com.flightleader.scenario.ScenarioListController;
import com.flightleader.scenario.ScenarioViewController;
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
import javafx.scene.image.Image;
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
	private HashMap<String, Aircraft> aircraftList = new HashMap<>();
	private HashMap<String, Scenario> scenarios = new HashMap<>();
	private Aircraft chosenAircraft;
	private boolean scenarioPlacingAircraft;
	private String placingAircraftHexName = "";
	
	@FXML
	private Canvas canvas;
	
    @FXML
    public void initialize() {
       
    	MessageBus.getInstanceOf().addSubscriber("windowSizeChanged", this);
    	MessageBus.getInstanceOf().addSubscriber("aircraftSaved", this);
    	MessageBus.getInstanceOf().addSubscriber("editAircraft", this);
    	MessageBus.getInstanceOf().addSubscriber("deleteAircraft", this);
    	MessageBus.getInstanceOf().addSubscriber("editScenario", this);
    	MessageBus.getInstanceOf().addSubscriber("deleteScenario", this);
    	MessageBus.getInstanceOf().addSubscriber("saveBeforeExit", this);
    	MessageBus.getInstanceOf().addSubscriber("scenarioSaved", this);
    	MessageBus.getInstanceOf().addSubscriber("getAircraftList", this);
    	MessageBus.getInstanceOf().addSubscriber("placeAircraft", this);
    	MessageBus.getInstanceOf().addSubscriber("stopPlaceAircraft", this);
    	MessageBus.getInstanceOf().addSubscriber("chosenAircraft", this);
    	
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
        loadScenarioList();
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
		        
		        if (scenarioPlacingAircraft)
		        {
		        	gc.setFill(Color.YELLOW);
		        	gc.fillPolygon(
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
		        }
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
		        
		        List<Aircraft> hexAircraftList = hex.getSatelliteData().get().getAircraft();
		        if (!hexAircraftList.isEmpty())
		        {
		        	Image tokenImage = null;
					try {
						if (hexAircraftList.get(0).getTokenImage() == null)
						{
							tokenImage = new Image(getClass().getResourceAsStream("/bogey.png"));							
						}
						else
						{
							tokenImage = new Image(new FileInputStream(hexAircraftList.get(0).getTokenImage()));
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
		        	gc.drawImage(tokenImage, col, row, 30, 30);
		        }
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
    
    public void onMouseMoved(MouseEvent event)
    {
    	if (event.getButton() != MouseButton.SECONDARY && 
    		event.getButton() != MouseButton.PRIMARY &&
    		scenarioPlacingAircraft) 
    	{
        	double width = canvas.getWidth();
        	double height = canvas.getHeight();
    		double mouseX = event.getSceneX();
    		double mouseY = event.getSceneY();
    		mouseX-= ((width - (width*zoom))/2);
        	mouseY-= ((height - (height*zoom))/2);
        	mouseX/= zoom;
        	mouseY/= zoom;
        	mouseX-= canvasTranslation.getX()+ canvasDrag.getX();
        	mouseY-= canvasTranslation.getY()+canvasDrag.getY();
        	mouseY-=24;
        	
        	if (!HexUtils.getInstanceOf().getHexGrid().getByPixelCoordinate(mouseX, mouseY).isPresent()) return;
        	
        	Hexagon<HexData> hex = HexUtils.getInstanceOf().getHexGrid().getByPixelCoordinate(mouseX, mouseY).get();
        	List<Aircraft> hexAircraftList = hex.getSatelliteData().get().getAircraft();
        	String hexName = hex.getSatelliteData().get().getName();
        	System.out.println(hexName + " -> ("+mouseX+","+mouseY+")");
        	
        	
        	
//        	if (placingAircraftHexName.isEmpty() || !hexName.equals(placingAircraftHexName)) {
//        		
//        		if (!placingAircraftHexName.isEmpty())
//        		{
//        			HexUtils.getInstanceOf().getHexagonByName(placingAircraftHexName).getSatelliteData().get().getAircraft().clear();
//        		}
//        		placingAircraftHexName = hex.getSatelliteData().get().getName();
//        		hexAircraftList.add(chosenAircraft);
//            	hex.getSatelliteData().get().setAircraft(hexAircraftList);
//            	drawCanvas();
//        	}


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
    	AircraftInfoController controller = openNewView("/aircraftInfo.fxml", null, new Dimension2D(640, 740));
    	controller.loadAircraft(aircraft);
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
				
			case "editScenario":
				
				Scenario scenario = new Scenario((Scenario)message);
				displayScenarioInfoView(scenario);
				break;
				
			case "deleteScenario":
				
				scenario = ((Scenario)message);
				scenarios.remove(scenario.getTitle());
				break;
				
			case "saveBeforeExit":
				
				saveAircraftList();
				saveScenarioList();
				break;
				
			case "scenarioSaved":
				
				scenario = (Scenario)message;
				scenarios.put(scenario.getTitle(), scenario);
				saveScenarioList();
				break;
				
			case "getAircraftList":
				return aircraftList;
				
			case "chosenAircraft":
				chosenAircraft = aircraftList.get(message);
				break;
				
			case "placeAircraft":
				scenarioPlacingAircraft = true;
				drawCanvas();
				break;
				
			case "stopPlaceAircraft":
				scenarioPlacingAircraft = false;
				drawCanvas();
				break;
		}
		
		return null;
	}

	private void displayScenarioInfoView(Scenario scenario) {
		ScenarioViewController controller = openNewView("/scenarioView.fxml", null, new Dimension2D(616, 400));
    	controller.loadScenario(scenario);
	}

	public void listAircraft()
	{
		AircraftListController controller = openNewView("/aircraftList.fxml", "/aircraftListCss.css", new Dimension2D(1612, 300));
    	controller.setAircraftMap(aircraftList);
	}
	
	public void listScenarios()
	{
		ScenarioListController controller = openNewView("/scenarioList.fxml", null, new Dimension2D(1450, 400));
		controller.setScenarioMap(scenarios);
	}
	
	private void loadAircraftList()
	{
		String userDir = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");
		String fileName = "flightLeader_aircraft.json";
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
		String fileName = "flightLeader_aircraft.json";
		Gson gson = new Gson();
		
		try (FileWriter writer = new FileWriter(userDir+sep+fileName)) {
            gson.toJson(aircraftList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void saveScenarioList() {
		
		String userDir = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");
		String fileName = "flightLeader_scenarios.json";
		Gson gson = new Gson();
		
		try (FileWriter writer = new FileWriter(userDir+sep+fileName)) {
            gson.toJson(scenarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void loadScenarioList() {
		
		String userDir = System.getProperty("user.home");
		String sep = System.getProperty("file.separator");
		String fileName = "flightLeader_scenarios.json";
		Gson gson = new Gson();
		
		File f = new File(userDir+sep+fileName);
		if (!f.exists()) return;
		
		try (FileReader reader = new FileReader(userDir+sep+fileName)) {
			Type type = new TypeToken<HashMap<String, Scenario>>(){}.getType();
			scenarios = gson.fromJson(reader, type); 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private <T extends ViewController> T openNewView(String fxmlFile, String cssFile, Dimension2D sceneSize)
	{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
    	Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	T controller = loader.getController();
        Scene scene = new Scene(root, sceneSize.getWidth(), sceneSize.getHeight());
        
        if (cssFile != null) scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
        Stage stage = new Stage();
        controller.setStage(stage);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        
        return controller;
	}
	
	public void newScenario()
	{
		ScenarioViewController controller = openNewView("/scenarioView.fxml", null, new Dimension2D(616, 400));
	}
}
