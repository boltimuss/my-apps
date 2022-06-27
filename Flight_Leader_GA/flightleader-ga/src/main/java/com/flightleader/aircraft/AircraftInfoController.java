package com.flightleader.aircraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.flightleader.aircraft.Aircraft.ACCELERATION;
import com.flightleader.aircraft.Aircraft.SUPERSONIC;
import com.flightleader.hex.HexUtils;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AircraftInfoController {

	private Stage stage;
	private ImageView tokenImage;
	private Aircraft aircraft;
	private boolean antiTextLoop;
	
	@FXML
	Canvas tokenView;
	
	@FXML TextField aircraftNameTF;
	
	@FXML
	ComboBox<String> turnTypeCB, sizeCB, accelerationCB, afterburnerCB, supersonicCB, missileRailsCB, internalGunCB,
								radarCB, counterMeasureCB, crewSizeCB, canopyTypeCB, maxSpeedCB, maxAltitudeCB, primaryUseCB;
	
	public void setStage(@SuppressWarnings("exports") Stage stage)
	{
		this.stage = stage;
	}
	
	@FXML
    public void initialize() {
       
		loadAircraftJsonProperties();
		aircraft = new Aircraft();
		tokenImage = new ImageView(new Image(getClass().getResourceAsStream("/bogey.png")));
		aircraftNameTF.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue.length() >= 7 && !antiTextLoop) {
		    	antiTextLoop = true;
		    	String limitedText = newValue.substring(0, 7);
		    	Platform.runLater(()->{
		    		aircraftNameTF.setText(limitedText);
		    		aircraft.aircraftName = limitedText;
		    		repaintToken(); 
		    	});
		    	return;
		    }
		    else if (antiTextLoop)
		    {
		    	antiTextLoop = false;
		    }
		    aircraft.aircraftName = newValue;
		    repaintToken(); 
		});
		repaintToken(); 
    }
	
	@SuppressWarnings("unchecked")
	public void validateTokenSelected(@SuppressWarnings("exports") ActionEvent event)
	{
		updateAircraftData((ComboBox<String>)event.getSource());
	}
	
	private void updateAircraftData(ComboBox<String> cb) {
		
		if (cb.getSelectionModel().getSelectedIndex() == -1) return;
		
		switch(cb.getId()) 
		{
			case "turnTypeCB":
				aircraft.turnType = Aircraft.TURNTYPE.values()[turnTypeCB.getSelectionModel().getSelectedIndex()];
				break;
			case "sizeCB":
				aircraft.size = Aircraft.SIZE.values()[sizeCB.getSelectionModel().getSelectedIndex()];
				break;
			case "accelerationCB":
				aircraft.acceleration = Aircraft.ACCELERATION.values()[accelerationCB.getSelectionModel().getSelectedIndex()];
				break;
			case "afterburnerCB":
				aircraft.afterburner = Aircraft.AFTERBURNER.values()[afterburnerCB.getSelectionModel().getSelectedIndex()];
				break;
			case "supersonicCB":
				aircraft.supersonic = Aircraft.SUPERSONIC.values()[supersonicCB.getSelectionModel().getSelectedIndex()];
				break;
			case "missileRailsCB":
				aircraft.missileRails = Integer.valueOf(missileRailsCB.getSelectionModel().getSelectedItem());
				break;
			case "internalGunCB":
				aircraft.internalGuns = Aircraft.INTERNAL_GUNS.values()[internalGunCB.getSelectionModel().getSelectedIndex()];
				break;
			case "radarCB":
				aircraft.radar = Integer.valueOf(radarCB.getSelectionModel().getSelectedItem());
				break;
			case "counterMeasureCB":
				aircraft.countermeasure = Integer.valueOf(counterMeasureCB.getSelectionModel().getSelectedItem());
				break;
			case "crewSizeCB":
				aircraft.crewSize = Integer.valueOf(crewSizeCB.getSelectionModel().getSelectedItem());
				break;
			case "canopyTypeCB":
				aircraft.canopyType = Aircraft.CANOPY_TYPE.values()[canopyTypeCB.getSelectionModel().getSelectedIndex()];
				break;
			case "primaryUseCB":
				aircraft.primaryUse = Aircraft.PRIMARY_USE.values()[primaryUseCB.getSelectionModel().getSelectedIndex()];
				break;
		}
		
		repaintToken();
	}
	
	private void loadAircraftJsonProperties()
	{
		try {
			
			Gson gson = new Gson();
			ClassLoader classLoader = getClass().getClassLoader();
	        InputStream inputStream = classLoader.getResourceAsStream("aircraftProperties.json");
	        Reader reader = new InputStreamReader(inputStream);
	        AircraftProperties aircraftProps = gson.fromJson(reader, AircraftProperties.class);
			
	        turnTypeCB.getItems().addAll(aircraftProps.turnType);
	        sizeCB.getItems().addAll(aircraftProps.size);
	        accelerationCB.getItems().addAll(aircraftProps.acceleration);
	        afterburnerCB.getItems().addAll(aircraftProps.afterburner);
	        supersonicCB.getItems().addAll(aircraftProps.supersonic);
	        missileRailsCB.getItems().addAll(aircraftProps.missileRails);
	        internalGunCB.getItems().addAll(aircraftProps.internalGun);
	        radarCB.getItems().addAll(aircraftProps.radar);
	        counterMeasureCB.getItems().addAll(aircraftProps.countermeasure);
	        crewSizeCB.getItems().addAll(aircraftProps.crewSize);
	        canopyTypeCB.getItems().addAll(aircraftProps.canopyType);
	        maxSpeedCB.getItems().addAll(aircraftProps.maxSpeed);
	        maxAltitudeCB.getItems().addAll(aircraftProps.maxAltitude);
	        primaryUseCB.getItems().addAll(aircraftProps.primaryUse);
			
	        sizeCB.getSelectionModel().clearAndSelect(1);
	        afterburnerCB.getSelectionModel().clearAndSelect(1);
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void repaintToken()
	{
		GraphicsContext gc = tokenView.getGraphicsContext2D();
		gc.setTransform(new Affine());
		gc.clearRect(0, 0, tokenView.getWidth(), tokenView.getHeight());
		gc.setFill(Color.rgb(156, 206, 250));
		gc.fillRect(0, 0, tokenView.getWidth(), tokenView.getHeight());
		gc.drawImage(tokenImage.getImage(), 25, 25, 50, 50);
		drawRadarCountermeasuresCanopy(gc);
		drawCrewSize(gc);
		drawAircraftNameAndIdent(gc);
		drawWeapons(gc);
		drawTurnType(gc);
		gc.setFill(Color.BLACK);
		gc.setLineWidth(1.5);
		gc.strokeRect(0, 0, 100, 100);
	}
	
	private void drawWeapons(GraphicsContext gc)
	{
		gc.setFont(Font.font("monospaced", 12.0));
        gc.setFill(Color.BLACK);
        if (aircraft.missileRails > 9)
        {
        	gc.fillText(""+(aircraft.missileRails-10), 22, 88);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(23, 91, 28, 91);
        }
        else if (aircraft.missileRails >= 0)
        {
        	gc.fillText(""+aircraft.missileRails, 22, 88);
        }
        
        if (aircraft.internalGuns == null) return;
        
        switch (aircraft.internalGuns)
        {
	        case NONE:
	        	break;
	        
	        case CANNON:
	        	gc.fillText("C", 29, 88);
	        	break;
	        	
	        case MACHINE_GUN:
	        	gc.fillText("M", 29, 88);
	        	break;
        }
	}
	
	private void drawAircraftNameAndIdent(GraphicsContext gc)
	{
		gc.setFont(Font.font("monospaced", 12.0));
        gc.setFill(Color.BLACK);
        Affine a = gc.getTransform();
        gc.rotate(-90);
        gc.translate(-156, 22);
        gc.fillText(aircraft.aircraftName, 78, 64);
        gc.setTransform(a);
        
        if (aircraft.id > 0) gc.fillText(""+aircraft.id, 68, 88);
	}
	
	private void drawTurnType(GraphicsContext gc)
	{
		gc.setFont(Font.font("monospaced", 12.0));
        gc.setFill(Color.BLACK);
        
		if (aircraft.size == Aircraft.SIZE.SMALL && aircraft.turnType != null)
		{
			gc.fillText(aircraft.turnType.toString().toLowerCase(), 22, 32);
		}
		else if (aircraft.turnType != null)
		{
			gc.fillText(aircraft.turnType.toString(), 22, 32);
		}
		
		if (aircraft.afterburner == Aircraft.AFTERBURNER.NO && aircraft.acceleration != null)
		{
			gc.fillText((aircraft.acceleration == ACCELERATION.NORMAL) ? "n" : "h", 29, 32);
		}
		else if (aircraft.acceleration != null)
		{
			gc.fillText((aircraft.acceleration == ACCELERATION.NORMAL) ? "N" : "H", 29, 32);
		}
		
		if (aircraft.supersonic != null && aircraft.supersonic == SUPERSONIC.NO)
		{
			gc.setLineWidth(2.0);
        	gc.strokeLine(23,34, 34, 34);
		}
	}
	
	private void drawRadarCountermeasuresCanopy(GraphicsContext gc)
	{
		if (aircraft.canopyType == Aircraft.CANOPY_TYPE.BUBBLE_CANOPY)
		{
			gc.setFill(Color.WHITE);
			gc.fillOval(62, 17, 19, 15);
		}
		gc.setFont(Font.font("monospaced", 16.0));
        gc.setFill(Color.BLACK);
        
        if (aircraft.radar > 9)
        {
        	gc.fillText(""+(aircraft.radar-10), 63, 30);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(62,34, 66, 34);
        }
        else if (aircraft.radar >= 0)
        {
        	gc.fillText(""+aircraft.radar, 63, 30);
        }
        
        if (aircraft.countermeasure > 9)
        {
        	gc.fillText(""+(aircraft.countermeasure-10), 70, 30);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(69,34, 73, 34);
        }
        else if (aircraft.countermeasure >= 0)
        {
        	gc.fillText(""+aircraft.countermeasure, 70, 30);
        }
	}
	
	private void drawCrewSize(GraphicsContext gc)
	{
		if (aircraft.crewSize < 1) return;
		
		for (int i = 1; i <= aircraft.crewSize; i++)
		{
			gc.fillOval(56, 32-(i*6), 5, 5);
		}
	}
	
	public void selectTokenImage(MouseEvent event)
	{
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Select Aircraft Token Silhouette");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Image Files", "*.png"));
		 File selectedFile = fileChooser.showOpenDialog(stage);
		 if (selectedFile != null) {
			 try {
				tokenImage = new ImageView(new Image(new FileInputStream(selectedFile.getAbsolutePath())));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			 repaintToken();
		 }
	}
	
	public void onCancel(ActionEvent event)
	{
		stage.close();
	}
	
	public void onKeyTyped(KeyEvent event)
	{
		
	}
}
