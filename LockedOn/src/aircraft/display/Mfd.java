package aircraft.display;


import Missile.Missile;
import aircraft.Afterburner;
import aircraft.Aircraft;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import messagebus.MessageBus;
import messagebus.Subscriber;

public class Mfd extends Stage implements Subscriber {

	private final int WIDTH = (int)(490);
	private final int HEIGHT = (int)(500);
	private final int CARD_WIDTH = (int) (166);
	private final int CARD_HEIGHT = (int) (212);
	private Pane pane;
	private ImageView[] countermeasureView = new ImageView[3];
	private ImageView altitudeView;
	private ImageView rangeView;
	private ImageView afterburnerOnView[] = new ImageView[2];
	private ImageView afterburnerOffView[] = new ImageView[2];
	private ImageView aircraftDisplayView;
	private ImageView cardView;
	private ImageView missile1 = new ImageView();
	private ImageView missile2 = new ImageView();
	private ImageView missile3 = new ImageView();
	private ImageView missile4 = new ImageView();
	private int id;
	private int countermeasures;
	private Image missile1Image;
	private Image missile2Image;
	private Image missile3Image;
	private Image missile4Image;
	private Image countermeasureImage;
	private Image aircraftDisplayImage ;
	private Image altitudeImage;
	private Image afterburnerOnImage;
	private Image afterburnerOffImage;
	private Image rangeImage;
	private Image cardImage;
	private Image lockedImage;
	private Image unlockedImage;
	private ImageView anchorView;
	private boolean anchored = false;
	private Stage mainWindow;
	private double lockedX;
	private double lockedY;
	private boolean isVisible;
	
	public Mfd(int id, Stage stage)
	{
		this.id = id;
		mainWindow = stage;
		init();
	}
	
	private void loadMfd(Aircraft aircraft)
	{
		setCountermeasures(aircraft.getCmNumber());
		setAltitude(aircraft.getAltitude());
		setRange(aircraft.getRange());
		setAfterburners(aircraft);
		setCard(aircraft);
		setMissiles(aircraft);
	}
	
	private void setMissiles(Aircraft aircraft) {
		
		if (aircraft.getMissile1() != null) missile1Image = new Image(getClass().getResourceAsStream("/resources/missiletokens/" + aircraft.getMissile1().getImage() + ".png"));
		if (aircraft.getMissile2() != null) missile2Image = new Image(getClass().getResourceAsStream("/resources/missiletokens/" + aircraft.getMissile2().getImage() + ".png"));
		if (aircraft.getMissile3() != null) missile3Image = new Image(getClass().getResourceAsStream("/resources/missiletokens/" + aircraft.getMissile3().getImage() + ".png"));
		if (aircraft.getMissile4() != null) missile4Image = new Image(getClass().getResourceAsStream("/resources/missiletokens/" + aircraft.getMissile4().getImage() + ".png"));
		
		loadMissile(aircraft.getMissile1(), missile1, missile1Image, 1);
		loadMissile(aircraft.getMissile2(), missile2, missile2Image, 2);
		loadMissile(aircraft.getMissile3(), missile3, missile3Image, 3);
		loadMissile(aircraft.getMissile4(), missile4, missile4Image, 4);
	}

	private void loadMissile(Missile m, ImageView imageView, Image image, int id)
	{
		if (image == null) return;
		
		imageView.setImage(image);
		imageView.setFitHeight(72);
		imageView.setFitWidth(39);
		
		switch (id)
		{
			case 1:
				imageView.setLayoutX(74);
				imageView.setLayoutY(174);
				break;
			case 2:
				imageView.setLayoutX(101);
				imageView.setLayoutY(85);
				break;
			case 3:
				imageView.setLayoutX(354);
				imageView.setLayoutY(87);
				break;
			case 4:
				imageView.setLayoutX(380);
				imageView.setLayoutY(174);
				break;
		}
		
		pane.getChildren().add(imageView);
	}
	
	public void setPosition(double xPosition, double yPosition)
	{
		if (anchored)
		{
			setX(lockedX + (mainWindow.getX() + mainWindow.getWidth() - 14));
			setY(lockedY + mainWindow.getY());
		}
	}
	
	
	public void setAnchored(boolean isAnchored)
	{
		anchored = isAnchored;
		if (anchored)
		{
			lockedX = getX() - (mainWindow.getX() + mainWindow.getWidth() - 14);
			lockedY = getY() - mainWindow.getY();
		}
	}
	
	private void init()
	{
		initStage();
		loadImages();
		
		MessageBus.getInstanceOf().addSubscriber("loadMfd" + id, this);
		MessageBus.getInstanceOf().addSubscriber("mfd" + id + "VisibleToggle", this);
	}
	
	private void setCard(Aircraft aircraft)
	{
		cardImage = aircraft.getAircraftCard().getCard();
		cardView = new ImageView();
		cardView.setFitHeight(CARD_HEIGHT);
		cardView.setFitWidth(CARD_WIDTH);
		cardView.setImage(cardImage);
		cardView.setLayoutX(164);
		cardView.setLayoutY(154);
		pane.getChildren().add(cardView);
	}
	
	private void initStage() {
		
		initStyle(StageStyle.UTILITY);
		setResizable(false);
		setTitle("MFD " + id);
		pane = new Pane();
		pane.setPrefSize(WIDTH, HEIGHT);
		Scene scene = new Scene(pane,800,800);
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
		pane.getChildren().add(aircraftDisplayView);
		
		int anchorWidth = 20;
		int anchorHeight = 30;
		anchorView = new ImageView();
		lockedImage = new Image(getClass().getResourceAsStream("/resources/locked.png"));
		unlockedImage = new Image(getClass().getResourceAsStream("/resources/unlocked.png"));
		anchorView.setImage(unlockedImage);
		anchorView.setFitWidth(anchorWidth);
		anchorView.setFitHeight(anchorHeight);
		anchorView.setStyle("-fx-border-color: #000000; ");
		anchorView.setLayoutX(4);
		anchorView.setLayoutY(4);
		anchorView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setAnchored(!anchored);
				anchorView.setImage((anchored) ? lockedImage : unlockedImage);
			}});
		pane.getChildren().add(anchorView);
	}
	
	private void setAfterburners(Aircraft aircraft)
	{
		
		Afterburner[] ab = new Afterburner[] {aircraft.getAfterburner1(), aircraft.getAfterburner2()};
		int width = 31;
		int height = 31;
		
		for (int i = 0; i < 2; i++) {
			
			if (afterburnerOnView[i] == null) {
				afterburnerOnView[i] = new ImageView();
				afterburnerOnView[i].setImage(afterburnerOnImage);
				afterburnerOnView[i].setFitHeight(height);
				afterburnerOnView[i].setFitWidth(width);
				afterburnerOnView[i].setLayoutX(204 + (i * 55));
				afterburnerOnView[i].setLayoutY(391);
			}
			pane.getChildren().add(afterburnerOnView[i]);
			
			if (afterburnerOffView[i] == null) {
				afterburnerOffView[i] = new ImageView();
				afterburnerOffView[i].setImage(afterburnerOffImage);
				afterburnerOffView[i].setFitHeight(height);
				afterburnerOffView[i].setFitWidth(width);
				afterburnerOffView[i].setLayoutX(204 + (i * 55));
				afterburnerOffView[i].setLayoutY(391);
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
			rangeView.setFitHeight(32);
			rangeView.setFitWidth(32);
			rangeView.setLayoutX(357);
			rangeView.setLayoutY(264 + (range.ordinal() * 47));
		}
		pane.getChildren().add(rangeView);
	}
	
	private void setAltitude(Altitude altitude)
	{
		if (altitudeView == null) {
			altitudeView = new ImageView();
			altitudeView.setImage(altitudeImage);
			altitudeView.setFitHeight(32);
			altitudeView.setFitWidth(32);
			altitudeView.setLayoutX(112);
			altitudeView.setLayoutY(271 + (altitude.ordinal() * 63));
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
				countermeasureView[i].setFitHeight(36);
				countermeasureView[i].setFitWidth(41);
				countermeasureView[i].setLayoutX(175+(52 * i));
				countermeasureView[i].setLayoutY(84);
			}
			pane.getChildren().add(countermeasureView[i]);
		}
	}

	@Override
	public Object onMessageReceived(String topic, Object Message) {
		
		if (topic.equals("loadMfd" + id)) {
			loadMfd((Aircraft)Message);
		}
		
		else if (topic.equals("mfd" + id + "VisibleToggle"))
		{
			isVisible = !isVisible;
			
			if (isVisible)
			{
				show();
			}
			else 
			{
				hide();
			}
		}
		
		return null;
	}
	
}
