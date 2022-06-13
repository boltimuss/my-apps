package application;
	
import com.birdsofprey.nomograph.Axis;
import com.birdsofprey.nomograph.ColorRange;
import com.birdsofprey.nomograph.Nomograph;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	private Canvas canvas = new Canvas(400, 400);
	private Nomograph nomo1;
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			initNomo1();
			
			BorderPane root = new BorderPane();
			root.getChildren().add(canvas);
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			nomo1.draw(canvas.getGraphicsContext2D());
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initNomo1()
	{
		nomo1 = new Nomograph();
		nomo1.setBoxed(false);
		
		Axis altitude = new Axis();
		altitude.setAscending(false).
			setGraphLocation(new Point2D(50,50)).
			setHeight(280).
			setPlotPrintIncrement(10).
			setStartingValue(320).
			setEndingValue(0).
			addTextColorRanges(new ColorRange(Color.BLACK, 0.0, 320.0)).
			setDivisions(new double[] {10.0, 5.0}).
			setLinear(false).
			setNonLinearDeltaTick(10.0).
			setStartingNonLinearDeltaTick(0.1);
		
		nomo1.addAxis(altitude);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
