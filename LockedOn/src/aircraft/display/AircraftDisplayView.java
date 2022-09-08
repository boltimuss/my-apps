package aircraft.display;

import aircraft.Aircraft;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AircraftDisplayView {

	private final int HEIGHT = 220;
	private final int WIDTH = 240;
	private Pane pane;
	private ImageView[] countermeasureView = new ImageView[3];
	private ImageView altitudeView;
	private ImageView aircraftDisplayView;
	private int id;
	private Point2D displayCoord;
	private int countermeasures;
	private Pane mainView;
	private Image countermeasureImage;
	private Image aircraftDisplayImage ;
	private Image altitudeImage;
	
	public AircraftDisplayView(int id, Pane mainView)
	{
		this.mainView = mainView;
		this.id = id;
		displayCoord = new Point2D(50 + (id * 342), 535);
		init(mainView);
	}
	
	public void loadMfd(Aircraft aircraft)
	{
		setCountermeasures(aircraft.getCmNumber());
		setAltitude(aircraft.getAltitude());
	}
	
	private void init(Pane mainView)
	{
		loadImages();
		initPane(mainView);
	}
	
	private void initPane(Pane mainView) {
		
		pane = new Pane();
		pane.setPrefSize(WIDTH, HEIGHT);
		pane.setLayoutX(displayCoord.getX());
		pane.setLayoutY(displayCoord.getY());
		pane.getChildren().add(aircraftDisplayView);
		mainView.getChildren().add(pane);
	}
	
	private void loadImages()
	{
		countermeasureImage = new Image(getClass().getResourceAsStream("/resources/tokens/CounterMeasures.png"));
		aircraftDisplayImage = new Image(getClass().getResourceAsStream("/resources/aircraftDisplay.png"));
		altitudeImage = new Image(getClass().getResourceAsStream("/resources/tokens/Altitude.png"));
		
		aircraftDisplayView = new ImageView();
		aircraftDisplayView.setFitHeight(HEIGHT);
		aircraftDisplayView.setFitWidth(WIDTH);
		aircraftDisplayView.setImage(aircraftDisplayImage);
	}
	
	private void setAltitude(Altitude altitude)
	{
		if (altitudeView == null) {
			altitudeView = new ImageView();
			altitudeView.setImage(altitudeImage);
			altitudeView.setFitHeight(28);
			altitudeView.setFitWidth(28);
			altitudeView.setLayoutX(35);
			altitudeView.setLayoutY(183 - (altitude.ordinal() * 38));
		}
		pane.getChildren().add(altitudeView);
	}
	
	private void setCountermeasures(int num)
	{
		countermeasures = num;
		
		for (int i = 0; i < num; i++)
		{
			if (countermeasureView[i] == null) {
				countermeasureView[i] = new ImageView();
				countermeasureView[i].setImage(countermeasureImage);
				countermeasureView[i].setFitHeight(24);
				countermeasureView[i].setFitWidth(24);
				countermeasureView[i].setLayoutX(77+(33 * i));
				countermeasureView[i].setLayoutY(2);
			}
			pane.getChildren().add(countermeasureView[i]);
		}
	}
	
}
