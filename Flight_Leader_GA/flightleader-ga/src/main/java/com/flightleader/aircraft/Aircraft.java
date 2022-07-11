package com.flightleader.aircraft;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.flightleader.aircraft.Aircraft.ACCELERATION;
import com.flightleader.aircraft.Aircraft.SUPERSONIC;
import com.flightleader.messagebus.MessageBus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;

public class Aircraft {

	public enum CREW_QUALITY 
	{
		EXPERIENCED("Experienced"), AVERAGE("Average"), INEXPERIENCED("Inexperienced");
		
		String name;
		private CREW_QUALITY(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum TURNTYPE
	{
		A("A"), B("B"), D("D"), E("E"), F("F");
		
		String name;
		private TURNTYPE(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum SIZE 
	{
		LARGE("Large"), SMALL("Small");
		
		String name;
		private SIZE(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum ACCELERATION
	{
		HIGH("High"), NORMAL("Normal");
		
		String name;
		private ACCELERATION(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum AFTERBURNER
	{
		YES("Yes"), NO("No");
		
		String name;
		private AFTERBURNER(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum SUPERSONIC
	{
		YES("Yes"), NO("No");
		
		String name;
		private SUPERSONIC(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum INTERNAL_GUNS
	{
		NONE("NONE"), CANNON("Cannon"), MACHINE_GUN("Machine Gun");
		
		String name;
		private INTERNAL_GUNS(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum CANOPY_TYPE
	{
		BUBBLE_CANOPY("Bubble Canopy"), REAR_BLOCKING("Rear Blocking");
		
		String name;
		private CANOPY_TYPE(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum PRIMARY_USE
	{
		AIR_SUPERIORITY("Air Superiority"),
		ELECTRONIC_WARFARE("Electronic Warfare"), 
		FIGHTER_BOMBER("Fighter-Bomber"), 
		FIGHTER_TRAINER("Fighter-Trainer"), 
		INTERCEPTOR("Interceptor"),
		MULTI_ROLE("MultiRole"), 
		RECON("Reconnaissance");
		
		String name;
		private PRIMARY_USE(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum FACING
	{
		NORTH("North"),
		NORTH_EAST("Northeast"), 
		SOUTH_EAST("Southeast"), 
		SOUTH("South"), 
		SOUTH_WEST("Southwest"),
		NORTH_WEST("Northwest");
		
		String name;
		private FACING(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public enum THROTTLE
	{
		IDLE("Idle"),
		MIL("Military"), 
		AFTER_BURNER("AfterBurner");
		
		String name;
		private THROTTLE(String newName) {
			name = newName;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
	public Aircraft() {
		
		editBTN = new Button("edit");
		Aircraft a = this;
		editBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("editAircraft", a);
			}
		});
		
		deleteBTN = new Button("delete");
		deleteBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("deleteAircraft", a);
			}
		});
	}
	
	public Aircraft(Aircraft airC) {
		
		editBTN 					= new Button("edit");
		deleteBTN 				= new Button("delete");
		tokenImage			= airC.tokenImage;
		entryDate 				= airC.entryDate;
		tokenImage 			= airC .tokenImage;
		id 							= airC.id;
		aircraftName 			= airC.aircraftName;
		currentAltitude 		= airC.currentAltitude;
		currentSpeed 			= airC.currentSpeed;
		afterburnersOn 		= airC.afterburnersOn;
		turnType 				= airC.turnType;
		size 						= airC.size;
		acceleration 			= airC.acceleration;
		afterburner 			= airC.afterburner;
		supersonic 				= airC.supersonic;
		missileRails 			= airC.missileRails;
		internalGuns 			= airC.internalGuns;
		radar 					= airC.radar;
		countermeasure 		= airC.countermeasure;
		crewSize 				= airC.crewSize;
		canopyType 			= airC.canopyType;
		maxSpeed 				= airC.maxSpeed;
		maxAltitude 			= airC.maxAltitude;
		primaryUse 			= airC.primaryUse;
		notes 					= airC.notes;
		crewQualityAValue 	= airC.crewQualityAValue;
		crewQualityBValue 	= airC.crewQualityBValue;
		crewQualityCValue 	= airC.crewQualityCValue;
		crewQualityDValue 	= airC.crewQualityDValue;
		crewQualityEValue 	= airC.crewQualityEValue;
		crewQualityFValue 	= airC.crewQualityFValue;
		crewQuality			= airC.crewQuality;
		hexLocation			= airC.hexLocation;
		improvedGunnery	= airC.improvedGunnery;
		
		Aircraft a = this;
		editBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("editAircraft", a);
			}
		});
		
		deleteBTN = new Button("delete");
		deleteBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("deleteAircraft", a);
			}
		});
	}
	
	public transient Button editBTN;
	public transient Button deleteBTN;
	public transient Image tokenImageIcon;
	public String entryDate;
	public String tokenImage;
	public int id = -1;
	public String aircraftName;
	public int currentAltitude;
	public int currentSpeed;
	public boolean afterburnersOn;
	public TURNTYPE turnType;
	public SIZE size = SIZE.SMALL;
	public ACCELERATION acceleration;
	public AFTERBURNER afterburner = AFTERBURNER.NO;
	public SUPERSONIC supersonic;
	public int missileRails = -1;
	public INTERNAL_GUNS internalGuns;
	public int radar = -1;
	public int countermeasure = -1;
	public int crewSize = -1;
	public CANOPY_TYPE canopyType;
	public int maxSpeed;
	public int maxAltitude;
	public PRIMARY_USE primaryUse;
	public String notes;
	public int crewQualityAValue, crewQualityBValue, crewQualityCValue, crewQualityDValue, crewQualityEValue, 
					crewQualityFValue;
	public CREW_QUALITY crewQuality;
	public FACING facing;
	public String hexLocation;
	public boolean improvedGunnery;
	public THROTTLE throttle = THROTTLE.IDLE;
	
	public String getTokenImage() {
		return tokenImage;
	}
	public void setTokenImage(String tokenImage) {
		this.tokenImage = tokenImage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAircraftName() {
		return aircraftName;
	}
	public void setAircraftName(String aircraftName) {
		this.aircraftName = aircraftName;
	}
	public int getCurrentAltitude() {
		return currentAltitude;
	}
	public void setCurrentAltitude(int currentAltitude) {
		this.currentAltitude = currentAltitude;
	}
	public int getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(int currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	public boolean isAfterburnersOn() {
		return afterburnersOn;
	}
	public void setAfterburnersOn(boolean afterburnersOn) {
		this.afterburnersOn = afterburnersOn;
	}
	public TURNTYPE getTurnType() {
		return turnType;
	}
	public void setTurnType(TURNTYPE turnType) {
		this.turnType = turnType;
	}
	public SIZE getSize() {
		return size;
	}
	public void setSize(SIZE size) {
		this.size = size;
	}
	public ACCELERATION getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(ACCELERATION acceleration) {
		this.acceleration = acceleration;
	}
	public AFTERBURNER getAfterburner() {
		return afterburner;
	}
	public void setAfterburner(AFTERBURNER afterburner) {
		this.afterburner = afterburner;
	}
	public SUPERSONIC getSupersonic() {
		return supersonic;
	}
	public void setSupersonic(SUPERSONIC supersonic) {
		this.supersonic = supersonic;
	}
	public int getMissileRails() {
		return missileRails;
	}
	public void setMissileRails(int missileRails) {
		this.missileRails = missileRails;
	}
	public INTERNAL_GUNS getInternalGuns() {
		return internalGuns;
	}
	public void setInternalGuns(INTERNAL_GUNS internalGuns) {
		this.internalGuns = internalGuns;
	}
	public int getRadar() {
		return radar;
	}
	public void setRadar(int radar) {
		this.radar = radar;
	}
	public int getCountermeasure() {
		return countermeasure;
	}
	public void setCountermeasure(int countermeasure) {
		this.countermeasure = countermeasure;
	}
	public int getCrewSize() {
		return crewSize;
	}
	public void setCrewSize(int crewSize) {
		this.crewSize = crewSize;
	}
	public CANOPY_TYPE getCanopyType() {
		return canopyType;
	}
	public void setCanopyType(CANOPY_TYPE canopyType) {
		this.canopyType = canopyType;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getMaxAltitude() {
		return maxAltitude;
	}
	public void setMaxAltitude(int maxAltitude) {
		this.maxAltitude = maxAltitude;
	}
	public PRIMARY_USE getPrimaryUse() {
		return primaryUse;
	}
	public void setPrimaryUse(PRIMARY_USE primaryUse) {
		this.primaryUse = primaryUse;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public Button getEditBTN() {
		return editBTN;
	}
	public void setEditBTN(Button editBTN) {
		this.editBTN = editBTN;
	}
	public Button getDeleteBTN() {
		return deleteBTN;
	}
	public void setDeleteBTN(Button deleteBTN) {
		this.deleteBTN = deleteBTN;
	}
	public int getCrewQualityAValue() {
		return crewQualityAValue;
	}
	public void setCrewQualityAValue(int crewQualityAValue) {
		this.crewQualityAValue = crewQualityAValue;
	}
	public int getCrewQualityBValue() {
		return crewQualityBValue;
	}
	public void setCrewQualityBValue(int crewQualityBValue) {
		this.crewQualityBValue = crewQualityBValue;
	}
	public int getCrewQualityCValue() {
		return crewQualityCValue;
	}
	public void setCrewQualityCValue(int crewQualityCValue) {
		this.crewQualityCValue = crewQualityCValue;
	}
	public int getCrewQualityDValue() {
		return crewQualityDValue;
	}
	public void setCrewQualityDValue(int crewQualityDValue) {
		this.crewQualityDValue = crewQualityDValue;
	}
	public int getCrewQualityEValue() {
		return crewQualityEValue;
	}
	public void setCrewQualityEValue(int crewQualityEValue) {
		this.crewQualityEValue = crewQualityEValue;
	}
	public int getCrewQualityFValue() {
		return crewQualityFValue;
	}
	public void setCrewQualityFValue(int crewQualityFValue) {
		this.crewQualityFValue = crewQualityFValue;
	}
	public CREW_QUALITY getCrewQuality() {
		return crewQuality;
	}
	public void setCrewQuality(CREW_QUALITY crewQuality) {
		this.crewQuality = crewQuality;
	}

	public FACING getFacing() {
		return facing;
	}

	public void setFacing(FACING facing) {
		this.facing = facing;
	}

	public String getHexLocation() {
		return hexLocation;
	}

	public void setHexLocation(String hexLocation) {
		this.hexLocation = hexLocation;
	}
	
	public boolean isImprovedGunnery() {
		return improvedGunnery;
	}

	public void setImprovedGunnery(boolean improvedGunnery) {
		this.improvedGunnery = improvedGunnery;
	}

	public void draw(GraphicsContext gc, 
							   double width, 
							   double height, 
							   double imgX, 
							   double imgY,
							   Color backgroundColor)
	{
		gc.clearRect(0, 0, width, height);
		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, width, height);
		
		if (tokenImage != null && !tokenImage.isEmpty())
		{
			try {
				tokenImageIcon = new Image(new FileInputStream(getTokenImage()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		else
		{
			tokenImageIcon = new Image(getClass().getResourceAsStream("/bogey.png"));
		}
		
		gc.drawImage(tokenImageIcon, 25+imgX, 18+imgY, 50, 65);
		drawRadarCountermeasuresCanopy(gc, imgX, imgY);
		drawCrewSize(gc, imgX, imgY);
		drawAircraftNameAndIdent(gc, imgX, imgY);
		drawWeapons(gc, imgX, imgY);
		drawTurnType(gc, imgX, imgY);
		gc.setFill(Color.BLACK);
		gc.setLineWidth(1.5);
		gc.strokeRect(0, 0, 100, 100);
	}
	
	private void drawWeapons(GraphicsContext gc, double imgX, double imgY)
	{
		
		if (improvedGunnery)
		{
			gc.setFill(Color.WHITE);
			gc.fillOval(16+imgX, 76+imgY, 19, 16);
		}
		
		gc.setFont(Font.font("monospaced", 12.0));
        gc.setFill(Color.BLACK);
        
        if (missileRails > 9)
        {
        	gc.fillText(""+(missileRails-10), 18+imgX, 88+imgY);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(19+imgX, 91+imgY, 24+imgX, 91+imgY);
        }
        else if (missileRails >= 0)
        {
        	gc.fillText(""+missileRails, 18+imgX, 88+imgY);
        }
        
        if (internalGuns == null) return;
        
        switch (internalGuns)
        {
	        case NONE:
	        	break;
	        
	        case CANNON:
	        	gc.fillText("C", 25+imgX, 88+imgY);
	        	break;
	        	
	        case MACHINE_GUN:
	        	gc.fillText("M", 25+imgX, 88+imgY);
	        	break;
        }
	}
	
	private void drawRadarCountermeasuresCanopy(GraphicsContext gc, double imgX, double imgY)
	{
		if (canopyType == Aircraft.CANOPY_TYPE.BUBBLE_CANOPY)
		{
			gc.setFill(Color.WHITE);
			gc.fillOval(62+imgX, 17+imgY, 19, 15);
		}
		gc.setFont(Font.font("monospaced", 16.0));
        gc.setFill(Color.BLACK);
        
        if (radar > 9)
        {
        	gc.fillText(""+(radar-10), 63+imgX, 30+imgY);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(66+imgX,33+imgY, 70+imgX, 33+imgY);
        }
        else if (radar >= 0)
        {
        	gc.fillText(""+radar, 63+imgX, 30+imgY);
        }
        
        if (countermeasure > 9)
        {
        	gc.fillText(""+(countermeasure-10), 70+imgX, 30+imgY);
        	gc.setLineWidth(2.0);
        	gc.strokeLine(73+imgX,33+imgY, 77+imgX, 33+imgY);
        }
        else if (countermeasure >= 0)
        {
        	gc.fillText(""+countermeasure, 70+imgX, 30+imgY);
        }
	}
	
	private void drawCrewSize(GraphicsContext gc, double imgX, double imgY)
	{
		if (crewSize < 1) return;
		
		for (int i = 1; i <= crewSize; i++)
		{
			gc.fillOval(56+imgX, (32-(i*6))+imgY, 5, 5);
		}
	}
	
	private void drawAircraftNameAndIdent(GraphicsContext gc, double imgX, double imgY)
	{
		gc.setFont(Font.font("monospaced", 12.0));
        gc.setFill(Color.BLACK);
        Affine a = gc.getTransform();
        gc.rotate(-90);
        gc.translate(-156+imgX, 22+imgY);
        gc.fillText(aircraftName, 78+imgX, 64+imgY);
        gc.setTransform(a);
        
        if (id > 0) gc.fillText(""+id, 68+imgX, 88+imgY);
	}
	
	private void drawTurnType(GraphicsContext gc, double imgX, double imgY)
	{
		gc.setFont(Font.font("monospaced", 12.0));
        gc.setFill(Color.BLACK);
        
		if (size == Aircraft.SIZE.SMALL && turnType != null)
		{
			gc.fillText(turnType.toString().toLowerCase(), 22+imgX, 32+imgY);
		}
		else if (turnType != null)
		{
			gc.fillText(turnType.toString(), 22+imgX, 32+imgY);
		}
		
		if (afterburner == Aircraft.AFTERBURNER.NO && acceleration != null)
		{
			gc.fillText((acceleration == ACCELERATION.NORMAL) ? "n" : "h", 29+imgX, 32+imgY);
		}
		else if (acceleration != null)
		{
			gc.fillText((acceleration == ACCELERATION.NORMAL) ? "N" : "H", 29+imgX, 32+imgY);
		}
		
		if (supersonic != null && supersonic == SUPERSONIC.NO)
		{
			gc.setLineWidth(2.0);
        	gc.strokeLine(23+imgX,34+imgY, 34+imgX, 34+imgY);
		}
	}
}
