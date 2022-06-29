package com.flightleader.controller;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public abstract class ViewController {

	protected Stage stage;
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	public void onCancel(ActionEvent event)
	{
		stage.close();
	}
}
