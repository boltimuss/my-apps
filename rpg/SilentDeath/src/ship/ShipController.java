package ship;

import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShipController {

	public static enum TORPEDO_TYPE
	{
		MK10,
		MK20,
		MK30,
		MK40,
		MK50
	}
	
    @FXML
    private Label shipName;
    
    @FXML
    private ImageView shipImage;
    
    @FXML
    private Label bpv;
    
    @FXML
    private Label tpv;
    
    @FXML
    private Label mk10torpNum;
    
    @FXML
    private Label mk20torpNum;
    
    @FXML
    private Label mk30torpNum;
    
    @FXML
    private Label mk40torpNum;
    
    @FXML
    private Label mk50torpNum;
    
    private HashMap<TORPEDO_TYPE, Label> torpedoLabels = new HashMap<TORPEDO_TYPE, Label>();
    
    private boolean init;
    
    public ShipController()
    {
    }

    public int getTorpedoStores(TORPEDO_TYPE torpType)
    {
    	if (!init) init();
    	
    	return Integer.valueOf(torpedoLabels.get(torpType).getText());
    }
    
    public void setTorpedoStores(TORPEDO_TYPE torpType, int numberOfTorpedoes)
    {
    	if (!init) init();
    	torpedoLabels.get(torpType).setText("" + numberOfTorpedoes);
    }
    
    public void setBpv(int newBpv)
    {
    	bpv.setText("" + newBpv);
    }
    
    public void setTpv(int newTpv)
    {
    	tpv.setText("" + newTpv);
    }
    
    public int getBpv()
    {
    	return Integer.valueOf(bpv.getText());
    }
    
    public int getTpv()
    {
    	return Integer.valueOf(tpv.getText());
    }
    
	public Image getShipImage() {
		return shipImage.getImage();
	}

	public void setShipImage(Image shipImage) {
		this.shipImage.setImage(shipImage);
	}

	public String getShipName() {
		return shipName.getText();
	}

	public void setShipName(String shipName) {
		this.shipName.setText(shipName);
	}
    
	private void init()
	{
		torpedoLabels.put(TORPEDO_TYPE.MK10, mk10torpNum);
    	torpedoLabels.put(TORPEDO_TYPE.MK20, mk20torpNum);
    	torpedoLabels.put(TORPEDO_TYPE.MK30, mk30torpNum);
    	torpedoLabels.put(TORPEDO_TYPE.MK40, mk40torpNum);
    	torpedoLabels.put(TORPEDO_TYPE.MK50, mk50torpNum);
    	
    	init= true;
	}
}
