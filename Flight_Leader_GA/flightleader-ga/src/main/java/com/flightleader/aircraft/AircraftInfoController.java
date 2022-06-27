package com.flightleader.aircraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.flightleader.aircraft.Aircraft.ACCELERATION;
import com.flightleader.aircraft.Aircraft.SUPERSONIC;
import com.flightleader.messagebus.MessageBus;
import com.google.gson.Gson;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
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
	private boolean canSaveCB, canSaveTF;
	
	@FXML
	Button saveBTN;
	
	@FXML
	TextArea notesTA;
	
	@FXML
	Canvas tokenView;
	
	@FXML TextField aircraftNameTF, crewQualityAValue, crewQualityBValue, crewQualityCValue, crewQualityDValue, crewQualityEValue, 
	crewQualityFValue;
	
	@FXML
	DatePicker entryDateDP;
	
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
		
		addTextFieldValidationListener(aircraftNameTF, false);
		addTextFieldValidationListener(crewQualityAValue, true);
		addTextFieldValidationListener(crewQualityBValue, true);
		addTextFieldValidationListener(crewQualityCValue, true);
		addTextFieldValidationListener(crewQualityDValue, true);
		addTextFieldValidationListener(crewQualityEValue, true);
		addTextFieldValidationListener(crewQualityFValue, true);
		
		saveBTN.setDisable(true);
		repaintToken(); 
    }
	
	public void loadAircraft(Aircraft aircraft)
	{
		aircraftNameTF.setText(aircraft.getAircraftName());
		DateTimeFormatter customDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(aircraft.getEntryDate(), customDateTimeFormatter);
		entryDateDP.setValue(localDate);
		maxSpeedCB.getSelectionModel().select(aircraft.getMaxSpeed()-1);
		maxAltitudeCB.getSelectionModel().select(aircraft.getMaxAltitude()-1);
		missileRailsCB.getSelectionModel().select(aircraft.getMissileRails());
		internalGunCB.getSelectionModel().select(aircraft.getInternalGuns().ordinal());
		turnTypeCB.getSelectionModel().select(aircraft.getTurnType().ordinal());
		sizeCB.getSelectionModel().select(aircraft.getSize().ordinal());
		accelerationCB.getSelectionModel().select(aircraft.getAcceleration().ordinal());
		afterburnerCB.getSelectionModel().select(aircraft.getAfterburner().ordinal());
		supersonicCB.getSelectionModel().select(aircraft.getSupersonic().ordinal());
		radarCB.getSelectionModel().select(aircraft.getRadar());
		counterMeasureCB.getSelectionModel().select(aircraft.getCountermeasure());
		crewSizeCB.getSelectionModel().select(aircraft.getCrewSize());
		canopyTypeCB.getSelectionModel().select(aircraft.getCanopyType().ordinal());
		primaryUseCB.getSelectionModel().select(aircraft.getPrimaryUse().ordinal());
		notesTA.setText(aircraft.getNotes());
		crewQualityAValue.setText(""+aircraft.getCrewQualityAValue());
		crewQualityBValue.setText(""+aircraft.getCrewQualityBValue());
		crewQualityCValue.setText(""+aircraft.getCrewQualityCValue());
		crewQualityDValue.setText(""+aircraft.getCrewQualityDValue());
		crewQualityEValue.setText(""+aircraft.getCrewQualityEValue());
		crewQualityFValue.setText(""+aircraft.getCrewQualityFValue());
		if (aircraft.tokenImage != null && !aircraft.tokenImage.isEmpty())
		{
			try {
				tokenImage = new ImageView(new Image(new FileInputStream(aircraft.getTokenImage())));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		this.aircraft = aircraft;
		checkComboBoxesForSaving();
		checkTextFieldsForSaving();
		
		repaintToken(); 
	}
	
	public void onEntryDate()
	{
		aircraft.setEntryDate(entryDateDP.getValue().toString());
		checkTextFieldsForSaving();
	}
	
	private void addTextFieldValidationListener(TextField tf, boolean numericOnly)
	{
		tf.textProperty().addListener((observable, oldValue, newValue) -> {
			
			if (!newValue.matches("\\d*") && numericOnly) {
	            tf.setText(newValue.replaceAll("[^\\d]", ""));
	        }
			
			checkTextFieldsForSaving();
			
		});
	}
	
	private void checkTextFieldsForSaving()
	{
		canSaveTF = true;
		
		canSaveTF &= !aircraftNameTF.getText().isEmpty() && !aircraftNameTF.getText().isBlank();
		canSaveTF &= !crewQualityAValue.getText().isEmpty() && !crewQualityAValue.getText().isBlank();
		canSaveTF &= !crewQualityBValue.getText().isEmpty() && !crewQualityBValue.getText().isBlank();
		canSaveTF &= !crewQualityCValue.getText().isEmpty() && !crewQualityCValue.getText().isBlank();
		canSaveTF &= !crewQualityDValue.getText().isEmpty() && !crewQualityDValue.getText().isBlank();
		canSaveTF &= !crewQualityEValue.getText().isEmpty() && !crewQualityEValue.getText().isBlank();
		canSaveTF &= !crewQualityFValue.getText().isEmpty() && !crewQualityFValue.getText().isBlank();
		canSaveTF &= (entryDateDP.getValue() != null);
		
		saveBTN.setDisable(!(canSaveTF && canSaveCB));
	}
	
	private void checkComboBoxesForSaving()
	{
		canSaveCB = true;
		canSaveCB &= turnTypeCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= accelerationCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= supersonicCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= missileRailsCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= internalGunCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= radarCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= counterMeasureCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= crewSizeCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= canopyTypeCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= maxSpeedCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= maxAltitudeCB.getSelectionModel().getSelectedIndex() > -1;
		canSaveCB &= primaryUseCB.getSelectionModel().getSelectedIndex() > -1;
		
		saveBTN.setDisable(!(canSaveTF && canSaveCB));
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
			case "maxSpeedCB":
				aircraft.maxSpeed = Integer.valueOf(maxSpeedCB.getSelectionModel().getSelectedItem());
				break;
			case "maxAltitudeCB":
				aircraft.maxAltitude = Integer.valueOf(maxAltitudeCB.getSelectionModel().getSelectedItem());
				break;
			default:
					break;
		}
		
		checkComboBoxesForSaving();
		
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
			
	        sizeCB.getSelectionModel().select(1);
	        afterburnerCB.getSelectionModel().select(1);
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
		gc.drawImage(tokenImage.getImage(), 25, 18, 50, 65);
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
        	gc.fillText(""+(aircraft.missileRails-10), 18, 88);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(19, 91, 24, 91);
        }
        else if (aircraft.missileRails >= 0)
        {
        	gc.fillText(""+aircraft.missileRails, 18, 88);
        }
        
        if (aircraft.internalGuns == null) return;
        
        switch (aircraft.internalGuns)
        {
	        case NONE:
	        	break;
	        
	        case CANNON:
	        	gc.fillText("C", 25, 88);
	        	break;
	        	
	        case MACHINE_GUN:
	        	gc.fillText("M", 25, 88);
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
        	gc.strokeLine(66,33, 70, 33);
        }
        else if (aircraft.radar >= 0)
        {
        	gc.fillText(""+aircraft.radar, 63, 30);
        }
        
        if (aircraft.countermeasure > 9)
        {
        	gc.fillText(""+(aircraft.countermeasure-10), 70, 30);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(73,33, 77, 33);
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
				aircraft.tokenImage = selectedFile.getAbsolutePath();
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
	
	public void onSave(ActionEvent event)
	{
		aircraft.notes = notesTA.getText();
		aircraft.setCrewQualityAValue(Integer.valueOf(crewQualityAValue.getText()));
		aircraft.setCrewQualityBValue(Integer.valueOf(crewQualityBValue.getText()));
		aircraft.setCrewQualityCValue(Integer.valueOf(crewQualityCValue.getText()));
		aircraft.setCrewQualityDValue(Integer.valueOf(crewQualityDValue.getText()));
		aircraft.setCrewQualityEValue(Integer.valueOf(crewQualityEValue.getText()));
		aircraft.setCrewQualityFValue(Integer.valueOf(crewQualityFValue.getText()));
		MessageBus.getInstanceOf().broadcastMessage("aircraftSaved", aircraft);
		stage.close();
	}

	public void onKeyTyped(KeyEvent event)
	{
		
	}
}
