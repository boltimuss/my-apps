package com.flightleader.scenario;

import java.util.HashMap;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.controller.ViewController;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.scenario.Scenario.MISSION_PROFILE;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ScenarioViewController extends ViewController {

	@FXML
	ComboBox<String> enemyMissionCB, playerMissionCB, chosenAircraftCB;
	
	@FXML
	TextField titleTF, terrainAltitudeTF, boundariesTF;
	
	@FXML
	Button placeAircraftBTN;
	
	@FXML
	CheckBox enemyControllerPresent, playerControllerPresent;
	
	@FXML
	TextArea descriptionTA, opposingForcesEnemyTA, opposingForcesPlayerTA, roeTA, optionsTA,
				 enemyVictoryPointsTA, playerVictoryPointsTA;
	
	private Scenario scenario;
	
	@FXML
	public void initialize() 
	{
		for (MISSION_PROFILE mp:MISSION_PROFILE.values())
		{
			enemyMissionCB.getItems().add(mp.toString());
			playerMissionCB.getItems().add(mp.toString());
		}
		
		@SuppressWarnings("unchecked")
		HashMap<String, Aircraft> aircraftList = (HashMap<String, Aircraft>) MessageBus.getInstanceOf().sendMessage("getAircraftList", null);
		for (String s:aircraftList.keySet())
		{
			chosenAircraftCB.getItems().add(s);
		}
		
		scenario = new Scenario();
	}
	
	public Scenario getScenario()
	{
		return scenario;
	}
	
	public void loadScenario(Scenario scenario)
	{
		titleTF.setText(scenario.getTitle());
		enemyMissionCB.getSelectionModel().select(scenario.getEnemyMissionProfile().ordinal());
		playerMissionCB.getSelectionModel().select(scenario.getPlayerMissionProfile().ordinal());
		enemyControllerPresent.setSelected(scenario.isEnemyController());
		playerControllerPresent.setSelected(scenario.isPlayerController());
		descriptionTA.setText(scenario.getDescription());
		opposingForcesEnemyTA.setText(scenario.getEnemyOpposingForces());
		opposingForcesPlayerTA.setText(scenario.getPlayerOpposingForces());
		terrainAltitudeTF.setText(scenario.getTerrainAltitude());
		boundariesTF.setText(scenario.getBoundaries());
		roeTA.setText(scenario.getRulesOfEngagement());
		optionsTA.setText(scenario.getOptions());
		enemyVictoryPointsTA.setText(scenario.getEnemyVictoryPoints());
		playerVictoryPointsTA.setText(scenario.getPlayerVictoryPoints());
		
	}
	
	@Override
	public void onCancel(ActionEvent event)
	{
		MessageBus.getInstanceOf().broadcastMessage("endScenarioMode", null);
		stage.close();
	}
	
	public void selectAircraft()
	{
		MessageBus.getInstanceOf().broadcastMessage("chosenAircraft", chosenAircraftCB.getSelectionModel().getSelectedItem());
		placeAircraftBTN.setDisable(false);
	}
	
	public void placeAircraft()
	{
		MessageBus.getInstanceOf().broadcastMessage("placeAircraft", chosenAircraftCB.getValue());
	}
	
	public void onSave()
	{
		scenario.setTitle(titleTF.getText());
		scenario.setEnemyMissionProfile(MISSION_PROFILE.values()[enemyMissionCB.getSelectionModel().getSelectedIndex()]);
		scenario.setPlayerMissionProfile(MISSION_PROFILE.values()[playerMissionCB.getSelectionModel().getSelectedIndex()]);
		scenario.setEnemyController(enemyControllerPresent.isSelected());
		scenario.setPlayerController(playerControllerPresent.isSelected());
		scenario.setDescription(descriptionTA.getText());
		scenario.setEnemyOpposingForces(opposingForcesEnemyTA.getText());
		scenario.setPlayerOpposingForces(opposingForcesPlayerTA.getText());
		scenario.setTerrainAltitude(terrainAltitudeTF.getText());
		scenario.setBoundaries(boundariesTF.getText());
		scenario.setRulesOfEngagement(roeTA.getText());
		scenario.setOptions(optionsTA.getText());
		scenario.setEnemyVictoryPoints(enemyVictoryPointsTA.getText());
		scenario.setPlayerVictoryPoints(playerVictoryPointsTA.getText());
		MessageBus.getInstanceOf().broadcastMessage("scenarioSaved", scenario);
		stage.close();
	}
}
