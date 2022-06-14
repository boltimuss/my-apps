package application;
	
import com.birdsofprey.nomograph.Axis;
import com.birdsofprey.nomograph.Axis.SIDE;
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
	
	private Canvas canvas = new Canvas(500, 800);
	private Nomograph nomo1;
	
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			initNomo1();
			
			BorderPane root = new BorderPane();
			root.getChildren().add(canvas);
			Scene scene = new Scene(root,500, 800);
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
			setStartingValue(320).
			setEndingValue(0).
			addTextColorRanges(new ColorRange(Color.BLACK, 0.0, 320.0)).
			setDivisions(new double[] {10.0, 5.0, 2.5}).
			setLinear(false).
//			setNonLinearDeltaTick(0.46875 / 1.5).
			setNonLinearDeltaTick(0.454545 / 1.5).
			setStartingNonLinearDeltaTick(35.0 / 1.50).
			setFontSize(9.5).
			setSide(SIDE.LEFT);
		
//		Axis KEAS = new Axis();
//		altitude.setAscending(true).
//			setGraphLocation(new Point2D(150,50)).
//			setHeight(320).
//			setPlotPrintIncrement(10).
//			setStartingValue(320).
//			setEndingValue(0).
//			addTextColorRanges(new ColorRange(Color.BLACK, 0.0, 320.0)).
//			setDivisions(new double[] {10.0, 5.0}).
//			setLinear(false).
//			setNonLinearDeltaTick(.452).
//			setStartingNonLinearDeltaTick(13.0).
//			setFontSize(9.5).
//			setSide(SIDE.LEFT);
		
		nomo1.addAxis(altitude);
//		nomo1.addAxis(KEAS);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
