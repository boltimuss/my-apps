package aircraft.display;

import aircraft.Afterburner;
import aircraft.Aircraft;
import data.GameData;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messagebus.MessageBus;
import messagebus.Subscriber;

public class Mfd extends Stage implements Subscriber {

	private final int WIDTH = (int)(240 * 2.25);
	private final int HEIGHT = (int)(220 * 2.5);
	private final int CARD_WIDTH = (int) (200 * 1.1);
	private final int CARD_HEIGHT = (int) (282 * 1.1);
	private Pane pane;
	private ImageView[] countermeasureView = new ImageView[3];
	private ImageView altitudeView;
	private ImageView rangeView;
	private ImageView afterburnerOnView[] = new ImageView[2];
	private ImageView afterburnerOffView[] = new ImageView[2];
	private ImageView aircraftDisplayView;
	private ImageView cardView;
	private int id;
	private int countermeasures;
	private Image countermeasureImage;
	private Image aircraftDisplayImage ;
	private Image altitudeImage;
	private Image afterburnerOnImage;
	private Image afterburnerOffImage;
	private Image rangeImage;
	private Image cardImage;
	
	public Mfd(int id)
	{
		this.id = id;
		init();
	}
	
	public void loadMfd(Aircraft aircraft)
	{
		setCountermeasures(aircraft.getCmNumber());
		setAltitude(aircraft.getAltitude());
		setRange(aircraft.getRange());
		setAfterburners(aircraft);
		setCard(aircraft);
	}
	
	private void init()
	{
		loadImages();
		initStage();
		
		MessageBus.getInstanceOf().addSubscriber("loadMfd" + id, this);
	}
	
	private void setCard(Aircraft aircraft)
	{
		cardImage = aircraft.getAircraftCard().getCard();
		cardView = new ImageView();
		cardView.setFitHeight(CARD_HEIGHT);
		cardView.setFitWidth(CARD_WIDTH);
		cardView.setImage(cardImage);
		cardView.setLayoutX(0);
		cardView.setLayoutY(0);
		pane.getChildren().add(cardView);
	}
	
	private void initStage() {
		
		pane = new Pane();
		pane.setPrefSize(WIDTH, HEIGHT);
		pane.getChildren().add(aircraftDisplayView);
		Scene scene = new Scene(pane,902,925);
		setScene(scene);
	}
	
	private void loadImages()
	{
		countermeasureImage = new Image(getClass().getResourceAsStream("/resources/tokens/CounterMeasures.png"));
		aircraftDisplayImage = new Image(getClass().getResourceAsStream("/resources/aircraftDisplay.png"));
		altitudeImage = new Image(getClass().getResourceAsStream("/resources/tokens/Altitude.png"));
		rangeImage = new Image(getClass().getResourceAsStream("/resources/tokens/Range.png"));
		afterburnerOnImage = new Image(getClass().getResourceAsStream("/resources/tokens/Afterburner_on.png"));
		afterburnerOffImage = new Image(getClass().getResourceAsStream("/resources/tokens/Afterburner_off.png"));
		
		aircraftDisplayView = new ImageView();
		aircraftDisplayView.setFitHeight(HEIGHT);
		aircraftDisplayView.setFitWidth(WIDTH);
		aircraftDisplayView.setImage(aircraftDisplayImage);
	}
	
	private void setAfterburners(Aircraft aircraft)
	{
		
		Afterburner[] ab = new Afterburner[] {aircraft.getAfterburner1(), aircraft.getAfterburner2()};
		for (int i = 0; i < 2; i++) {
			
			if (afterburnerOnView[i] == null) {
				afterburnerOnView[i] = new ImageView();
				afterburnerOnView[i].setImage(afterburnerOnImage);
				afterburnerOnView[i].setFitHeight(24);
				afterburnerOnView[i].setFitWidth(24);
				afterburnerOnView[i].setLayoutX(94 + (i * 33));
				afterburnerOnView[i].setLayoutY(181);
			}
			pane.getChildren().add(afterburnerOnView[i]);
			
			if (afterburnerOffView[i] == null) {
				afterburnerOffView[i] = new ImageView();
				afterburnerOffView[i].setImage(afterburnerOffImage);
				afterburnerOffView[i].setFitHeight(24);
				afterburnerOffView[i].setFitWidth(24);
				afterburnerOffView[i].setLayoutX(94 + (i * 33));
				afterburnerOffView[i].setLayoutY(181);
			}
			pane.getChildren().add(afterburnerOffView[i]);
			
			switch (ab[i]) {
				case VACANT:
					afterburnerOffView[i].setVisible(false);
					afterburnerOnView[i].setVisible(false);
					break;
				case OFF:
					afterburnerOffView[i].setVisible(true);
					afterburnerOnView[i].setVisible(false);
					break;
				case ON:
					afterburnerOffView[i].setVisible(false);
					afterburnerOnView[i].setVisible(true);
					break;
			}
			
		}
	}
	
	private void setRange(Range range)
	{
		if (rangeView == null) {
			rangeView = new ImageView();
			rangeView.setImage(rangeImage);
			rangeView.setFitHeight(24);
			rangeView.setFitWidth(24);
			rangeView.setLayoutX(188);
			rangeView.setLayoutY(109 + (range.ordinal() * 26));
		}
		pane.getChildren().add(rangeView);
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

	@Override
	public Object onMessageReceived(String topic, Object Message) {
		
		if (topic.equals("loadMfd" + id)) {
			setAfterburners((Aircraft)Message);
		}
		
		return null;
	}
	
}
