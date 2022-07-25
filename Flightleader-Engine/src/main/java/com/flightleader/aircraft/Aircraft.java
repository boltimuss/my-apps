package com.flightleader.aircraft;

import lombok.Getter;
import lombok.Setter;

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
		
	}
	
	@Getter @Setter public String entryDate;
	@Getter @Setter public int id = -1;
	@Getter @Setter public String aircraftName;
	@Getter @Setter public int currentAltitude;
	@Getter @Setter public int currentSpeed;
	@Getter @Setter public boolean afterburnersOn;
	@Getter @Setter public TURNTYPE turnType;
	@Getter @Setter public SIZE size = SIZE.SMALL;
	@Getter @Setter public ACCELERATION acceleration;
	@Getter @Setter public AFTERBURNER afterburner = AFTERBURNER.NO;
	@Getter @Setter public SUPERSONIC supersonic;
	@Getter @Setter public int missileRails = -1;
	@Getter @Setter public INTERNAL_GUNS internalGuns;
	@Getter @Setter public int radar = -1;
	@Getter @Setter public int countermeasure = -1;
	@Getter @Setter public int crewSize = -1;
	@Getter @Setter public CANOPY_TYPE canopyType;
	@Getter @Setter public int maxSpeed;
	@Getter @Setter public int maxAltitude;
	@Getter @Setter public PRIMARY_USE primaryUse;
	@Getter @Setter public int crewQualityAValue, crewQualityBValue, crewQualityCValue, crewQualityDValue, crewQualityEValue, crewQualityFValue;
	@Getter @Setter public CREW_QUALITY crewQuality;
	@Getter @Setter public FACING facing;
	@Getter @Setter public String hexLocation;
	@Getter @Setter public boolean improvedGunnery;
	@Getter @Setter public THROTTLE throttle = THROTTLE.IDLE;
	
}
