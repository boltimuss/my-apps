package com.flightleader.aircraft;

import com.flightleader.messagebus.MessageBus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Aircraft {

	public enum TURNTYPE
	{
		A("A"), B("B"), D("D"), E("E"), F("F");
		
		String name;
		private TURNTYPE(String newName) {
			name = newName;
		}
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
	
}
