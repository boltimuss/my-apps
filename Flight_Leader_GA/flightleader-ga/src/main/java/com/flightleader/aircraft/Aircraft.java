package com.flightleader.aircraft;

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
	
	public Aircraft() {}
	
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
}
