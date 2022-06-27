package com.flightleader.aircraft;

import java.util.HashMap;
import java.util.Map;

import com.flightleader.messagebus.MessageBus;
import com.flightleader.messagebus.Subscriber;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AircraftListController implements Subscriber {

	private HashMap<String, Aircraft> aircraftList;
	private Stage stage;
	
	@FXML
	private TableView<Aircraft> aircraftListView;
	
	public void setAircraftMap(Map<String, Aircraft> list)
	{
		this.aircraftList = (HashMap<String, Aircraft>) list;
		
		for (String aircraft:list.keySet())
		{
			aircraftListView.getItems().add(list.get(aircraft));
		}
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	@FXML
    public void initialize() 
	{
		aircraftListView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("aircraftName"));
		aircraftListView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("entryDate"));
		aircraftListView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("maxSpeed"));
		aircraftListView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("maxAltitude"));
		aircraftListView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("missileRails"));
		aircraftListView.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("turnType"));
		aircraftListView.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("size"));
		aircraftListView.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("acceleration"));
		aircraftListView.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("afterburner"));
		aircraftListView.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("supersonic"));
		aircraftListView.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("internalGuns"));
		aircraftListView.getColumns().get(11).setCellValueFactory(new PropertyValueFactory<>("radar"));
		aircraftListView.getColumns().get(12).setCellValueFactory(new PropertyValueFactory<>("countermeasure"));
		aircraftListView.getColumns().get(13).setCellValueFactory(new PropertyValueFactory<>("crewSize"));
		aircraftListView.getColumns().get(14).setCellValueFactory(new PropertyValueFactory<>("canopyType"));
		aircraftListView.getColumns().get(15).setCellValueFactory(new PropertyValueFactory<>("primaryUse"));
		aircraftListView.getColumns().get(16).setCellValueFactory(new PropertyValueFactory<>("notes"));
		aircraftListView.getColumns().get(17).setCellValueFactory(new PropertyValueFactory<>("crewQualityAValue"));
		aircraftListView.getColumns().get(18).setCellValueFactory(new PropertyValueFactory<>("crewQualityBValue"));
		aircraftListView.getColumns().get(19).setCellValueFactory(new PropertyValueFactory<>("crewQualityCValue"));
		aircraftListView.getColumns().get(20).setCellValueFactory(new PropertyValueFactory<>("crewQualityDValue"));
		aircraftListView.getColumns().get(21).setCellValueFactory(new PropertyValueFactory<>("crewQualityEValue"));
		aircraftListView.getColumns().get(22).setCellValueFactory(new PropertyValueFactory<>("crewQualityFValue"));
		aircraftListView.getColumns().get(23).setCellValueFactory(new PropertyValueFactory<>("editBTN"));
		aircraftListView.getColumns().get(24).setCellValueFactory(new PropertyValueFactory<>("deleteBTN"));
		
		MessageBus.getInstanceOf().addSubscriber("editAircraft", this);
		MessageBus.getInstanceOf().addSubscriber("deleteAircraft", this);
	}

	@Override
	public Object onMessageReceived(String topic, Object message) {
		
		switch (topic)
		{
			case "editAircraft":
				stage.close();
				break;
				
			case "deleteAircraft":
				
				Aircraft aircraftToDelete = (Aircraft)message;
				aircraftListView.getItems().remove(aircraftToDelete);
				break;
			
		}
		
		return null;
	}
	
}
