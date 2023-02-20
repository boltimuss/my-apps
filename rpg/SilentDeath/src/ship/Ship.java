package ship;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import messagebus.MessageBus;
import ship.ShipController.TORPEDO_TYPE;

public class Ship {

	private Image shipImage;
	private int facing = 0;
	private String shipName;
	private int tpv;
	private int bpv;
    private HashMap<TORPEDO_TYPE, Integer> torpedoStores = new HashMap<TORPEDO_TYPE, Integer>();
	
	public Ship(String shipName)
	{
		this.shipName = shipName;
		URL url = this.getClass().getResource("/resources/ships/" + shipName + ".png");
		try {
			shipImage = new Image(url.toURI().toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		for (TORPEDO_TYPE t:TORPEDO_TYPE.values())
		{
			torpedoStores.put(t, 0);
		}
	}
	
	public HashMap<TORPEDO_TYPE, Integer> getTorpedoStores()
	{
		return torpedoStores;
	}
	
    public int getTorpedoStores(TORPEDO_TYPE torpType)
    {
    	return Integer.valueOf(torpedoStores.get(torpType));
    }
    
    public void setTorpedoStores(TORPEDO_TYPE torpType, int numberOfTorpedoes)
    {
    	torpedoStores.put(torpType, numberOfTorpedoes);
    }
	
	public int getTpv() {
		return tpv;
	}

	public void setTpv(int tpv) {
		this.tpv = tpv;
	}

	public int getBpv() {
		return bpv;
	}

	public void setBpv(int bpv) {
		this.bpv = bpv;
	}

	public Image getShipImage() {
		return shipImage;
	}

	public void setShipImage(Image shipImage) {
		this.shipImage = shipImage;
		MessageBus.sendMessage("ShipViewChange." + shipName, this);
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
		MessageBus.sendMessage("ShipViewChange." + shipName, this);
	}

	public int getFacing()
	{
		return facing;
	}
	
	public void setFacing(int facing)
	{
		this.facing = facing;
		MessageBus.sendMessage("ShipViewChange." + shipName, this);
	}
	
	public Image getImage()
	{
		return shipImage;
	}
	
	public void incFacing()
	{
		facing++;
		if (facing > 5) facing = 0;
	}
	
	public void decFacing()
	{
		facing--;
		if (facing < 0) facing = 5;
	}
}
