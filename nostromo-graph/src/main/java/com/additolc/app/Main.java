package com.additolc.app;
	
import com.additolc.graphs.Step3Chart;
import com.additolc.state.AircraftState;
import com.additolc.state.GameState;
import com.additolc.graphs.Step4Chart;
import com.additolc.graphs.Step12Chart;
import com.additolc.graphs.Step13Chart;
import com.additolc.graphs.Step1Chart;
import com.additolc.graphs.Step2Chart;

import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

public class Main extends Application {
	
	@Override
	public void start(@SuppressWarnings("exports") Stage keasStage) {
		
		try {
			
			GameState gs = GameState.getInstanceOf();
			gs.getAircraftState().put("test", new AircraftState());
			gs.setCurrentAircraft("test");
			
			Stage step1ChartStage = new Stage();
			Stage step2ChartStage = new Stage();
			Stage step3ChartStage = new Stage();
			Stage step4ChartStage = new Stage();
			Stage step12ChartStage = new Stage();
			Stage step13ChartStage = new Stage();
			
			Step1Chart step1Chart = new Step1Chart(new Dimension2D(570, 690));
			Step2Chart step2Chart = new Step2Chart(new Dimension2D(550, 690));
			Step3Chart step3Chart = new Step3Chart(new Dimension2D(1030, 1030));
			Step4Chart step4Chart = new Step4Chart(new Dimension2D(1030, 1030));
			Step12Chart step12chart = new Step12Chart(new Dimension2D(1030, 1030));
			Step13Chart step13chart = new Step13Chart(new Dimension2D(1030, 1030));
			
			ScrollPane sp1 = new ScrollPane();
			GridPane gp1 = new GridPane();
			gp1.add(step1Chart, 0, 0);
			sp1.setContent(gp1);
			ScrollPane sp2 = new ScrollPane();
			GridPane gp2 = new GridPane();
			gp2.add(step2Chart, 0, 0);
			sp2.setContent(gp2);
			ScrollPane sp3 = new ScrollPane();
			GridPane gp3 = new GridPane();
			gp3.add(step3Chart, 0, 0);
			sp3.setContent(gp3);
			ScrollPane sp4 = new ScrollPane();
			GridPane gp4 = new GridPane();
			gp4.add(step4Chart, 0, 0);
			sp4.setContent(gp4);
			ScrollPane sp12 = new ScrollPane();
			GridPane gp12 = new GridPane();
			gp12.add(step12chart, 0, 0);
			sp12.setContent(gp12);
			ScrollPane sp13 = new ScrollPane();
			GridPane gp13 = new GridPane();
			gp13.add(step13chart, 0, 0);
			sp13.setContent(gp13);
			
			Scene scene1 = new Scene(sp1,580, 700);
			Scene scene2 = new Scene(sp2,570, 700);
			Scene scene3 = new Scene(sp3,1040, 1040);
			Scene scene4 = new Scene(sp4,1040, 1040);
			Scene scene12 = new Scene(sp12,1040, 1040);
			Scene scene13 = new Scene(sp13,1040, 1040);
			scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene3.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene4.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene12.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			scene13.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			step1ChartStage.setScene(scene1);
			step1ChartStage.setTitle("step1 chart");
			step1ChartStage.show();
			step2ChartStage.setScene(scene2);
			step2ChartStage.setTitle("step2 chart");
			step2ChartStage.show();
			step3ChartStage.setScene(scene3);
			step3ChartStage.setTitle("step3 chart");
			step3ChartStage.show();
			step4ChartStage.setScene(scene4);
			step4ChartStage.setTitle("step4 chart");
			step4ChartStage.show();
			step12ChartStage.setScene(scene12);
			step12ChartStage.setTitle("step12 chart");
			step12ChartStage.show();
			step13ChartStage.setScene(scene13);
			step13ChartStage.setTitle("step13 chart");
			step13ChartStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
