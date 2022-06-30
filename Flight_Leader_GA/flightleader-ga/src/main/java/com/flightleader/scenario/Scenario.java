package com.flightleader.scenario;

import java.util.LinkedList;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.messagebus.MessageBus;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class Scenario {

	public enum MISSION_PROFILE
	{
		PENETRATION("Penetration"), SWEEP("Sweep"), CAP("CAP");
		
		String name;
		private MISSION_PROFILE(String newName) {
			name = newName;
		}
		public String toString() {
			return name;
		}
	}
	
	private String title;
	private MISSION_PROFILE enemyMissionProfile;
	private MISSION_PROFILE playerMissionProfile;
	private String description;
	private String enemyOpposingForces;
	private String playerOpposingForces;
	private String terrainAltitude;
	private String boundaries;
	private String rulesOfEngagement;
	private String options;
	private String enemyVictoryPoints;
	private String playerVictoryPoints;
	private LinkedList<Aircraft> enemyAircraft = new LinkedList<Aircraft>();
	private LinkedList<Aircraft> playerAircraft = new LinkedList<Aircraft>();
	private boolean enemyController;
	private boolean playerController;
	public transient Button editBTN;
	public transient Button deleteBTN;
	
	public Scenario() 
	{
		editBTN = new Button("edit");
		Scenario s = this;
		editBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("editScenario", s);
			}
		});
		
		deleteBTN = new Button("delete");
		deleteBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("deleteScenario", s);
			}
		});
	}
	
	public Scenario(Scenario newScenario)
	{
		title = newScenario.title;
		enemyMissionProfile = newScenario.enemyMissionProfile;
		playerMissionProfile = newScenario.playerMissionProfile;
		description = newScenario.description;
		enemyOpposingForces = newScenario.enemyOpposingForces;
		playerOpposingForces = newScenario.playerOpposingForces;
		terrainAltitude = newScenario.terrainAltitude;
		boundaries = newScenario.boundaries;
		rulesOfEngagement = newScenario.rulesOfEngagement;
		options = newScenario.options;
		enemyVictoryPoints = newScenario.enemyVictoryPoints;
		playerVictoryPoints = newScenario.playerVictoryPoints;
		enemyAircraft = newScenario.enemyAircraft;
		playerAircraft = newScenario.playerAircraft;
		enemyController = newScenario.enemyController;
		playerController = newScenario.playerController;
		
		Scenario s = this;
		editBTN = new Button("edit");
		editBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("editScenario", s);
			}
		});
		
		deleteBTN = new Button("delete");
		deleteBTN.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				MessageBus.getInstanceOf().broadcastMessage("deleteScenario", s);
			}
		});
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public MISSION_PROFILE getEnemyMissionProfile() {
		return enemyMissionProfile;
	}
	public void setEnemyMissionProfile(MISSION_PROFILE enemyMissionProfile) {
		this.enemyMissionProfile = enemyMissionProfile;
	}
	public MISSION_PROFILE getPlayerMissionProfile() {
		return playerMissionProfile;
	}
	public void setPlayerMissionProfile(MISSION_PROFILE playerMissionProfile) {
		this.playerMissionProfile = playerMissionProfile;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEnemyOpposingForces() {
		return enemyOpposingForces;
	}
	public void setEnemyOpposingForces(String enemyOpposingForces) {
		this.enemyOpposingForces = enemyOpposingForces;
	}
	public String getPlayerOpposingForces() {
		return playerOpposingForces;
	}
	public void setPlayerOpposingForces(String playerOpposingForces) {
		this.playerOpposingForces = playerOpposingForces;
	}
	public String getTerrainAltitude() {
		return terrainAltitude;
	}
	public void setTerrainAltitude(String terrainAltitude) {
		this.terrainAltitude = terrainAltitude;
	}
	public String getBoundaries() {
		return boundaries;
	}
	public void setBoundaries(String boundaries) {
		this.boundaries = boundaries;
	}
	public String getRulesOfEngagement() {
		return rulesOfEngagement;
	}
	public void setRulesOfEngagement(String rulesOfEngagement) {
		this.rulesOfEngagement = rulesOfEngagement;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getEnemyVictoryPoints() {
		return enemyVictoryPoints;
	}
	public void setEnemyVictoryPoints(String enemyVictoryPoints) {
		this.enemyVictoryPoints = enemyVictoryPoints;
	}
	public String getPlayerVictoryPoints() {
		return playerVictoryPoints;
	}
	public void setPlayerVictoryPoints(String playerVictoryPoints) {
		this.playerVictoryPoints = playerVictoryPoints;
	}
	public LinkedList<Aircraft> getEnemyAircraft() {
		return enemyAircraft;
	}
	public void setEnemyAircraft(LinkedList<Aircraft> enemyAircraft) {
		this.enemyAircraft = enemyAircraft;
	}
	public LinkedList<Aircraft> getPlayerAircraft() {
		return playerAircraft;
	}
	public void setPlayerAircraft(LinkedList<Aircraft> playerAircraft) {
		this.playerAircraft = playerAircraft;
	}
	public boolean isEnemyController() {
		return enemyController;
	}
	public void setEnemyController(boolean enemyController) {
		this.enemyController = enemyController;
	}
	public boolean isPlayerController() {
		return playerController;
	}
	public void setPlayerController(boolean playerController) {
		this.playerController = playerController;
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
}
