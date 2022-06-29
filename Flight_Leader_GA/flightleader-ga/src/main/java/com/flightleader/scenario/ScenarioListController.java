package com.flightleader.scenario;

import java.util.HashMap;
import java.util.Map;

import com.flightleader.aircraft.Aircraft;
import com.flightleader.controller.ViewController;
import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ScenarioListController extends ViewController implements Subscriber {

	private HashMap<String, Scenario> scenarioList;
	
	@FXML
	TableView<Scenario> scenarioListView;
	
	@FXML
    public void initialize() 
	{
		scenarioListView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("title"));
		scenarioListView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
		scenarioListView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("enemyMissionProfile"));
		scenarioListView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("playerMissionProfile"));
		scenarioListView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("enemyOpposingForces"));
		scenarioListView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("playerOpposingForces"));
		scenarioListView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("rulesOfEngagement"));
		scenarioListView.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("options"));
		scenarioListView.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("editBTN"));
		scenarioListView.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("deleteBTN"));
		
		MessageBus.getInstanceOf().addSubscriber("editScenario", this);
		MessageBus.getInstanceOf().addSubscriber("deleteScenario", this);
	}

	public void setScenarioMap(Map<String, Scenario> list)
	{
		this.scenarioList = (HashMap<String, Scenario>) list;
		
		for (String scenarioName:list.keySet())
		{
			scenarioListView.getItems().add(list.get(scenarioName));
		}
	}
	
	@Override
	public Object onMessageReceived(String topic, Object message) {
		
		switch (topic)
		{
			case "editScenario":
				stage.close();
				break;
				
			case "deleteScenario":
				
				Scenario scenarioDelete = (Scenario)message;
				scenarioListView.getItems().remove(scenarioDelete);
				break;
			
		}
		
		return null;
	}
}
