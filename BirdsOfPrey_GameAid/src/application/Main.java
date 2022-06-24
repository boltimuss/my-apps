package application;
	
import com.birdsofprey.nomograph.Axis;
import com.birdsofprey.nomograph.Axis.SIDE;
import com.birdsofprey.nomograph.ColorRange;
import com.birdsofprey.nomograph.Nomograph;
import com.birdsofprey.nomograph.data.NomographData;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	private Canvas canvas = new Canvas(500, 900);
	private Nomograph nomo1;
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			initNomo1();
			
			BorderPane root = new BorderPane();
			root.getChildren().add(canvas);
			Scene scene = new Scene(root,500, 900);
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
			setGraphLocation(new Point2D(50,100)).
			setStartingValue(320).
			setEndingValue(0).
			addTextColorRanges(new ColorRange(Color.BLACK, 0.0, 320.0)).
			setDivisions(new double[] {10.0, 5.0}).
			setDataPoints(NomographData.altitude).
			setZoom(8.0).
			setFontSize(9.5).
			setSide(SIDE.LEFT);
		
		Axis KEAS_left = new Axis();
		KEAS_left.setAscending(true).
			setGraphLocation(new Point2D(150,-33)).
			setStartingValue(40).
			setEndingValue(1000).
			addTextColorRanges(new ColorRange(Color.BLACK, 60.0, 775.0)).
			addTextColorRanges(new ColorRange(Color.ORANGE, 840.0, 1000.0)).
			setDivisions(new double[] {40.0, 20.0, 10.0}).
			setDataPoints(NomographData.KEAS).
			setZoom(8.0).
			setFontSize(9.5).
			setSide(SIDE.LEFT);
		
//		Axis KEAS_left = new Axis();
//		KEAS_left.setAscending(true).
//			setGraphLocation(new Point2D(150,77)).
//			setStartingValue(40).
//			setEndingValue(1000).
//			addTextColorRanges(new ColorRange(Color.BLACK, 60.0, 775.0)).
//			addTextColorRanges(new ColorRange(Color.ORANGE, 840.0, 1000.0)).
//			setDivisions(new double[] {40.0, 20.0, 10.0}).
//			setLinear(false).
//			setNonLinearDeltaTick(1.25).
//			setParabolicExponent(4).
//			setStartingNonLinearDeltaTick(54).
//			setFontSize(9.5).
//			setSide(SIDE.LEFT);
		
		nomo1.addAxis(altitude);
		nomo1.addAxis(KEAS_left);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
