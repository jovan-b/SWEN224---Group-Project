package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Item.Type;

/**
 * A torch item that allows the player to see in dark environments
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class Torch implements Item {
	
	private Image torchImage;
	private Image torchImageOn;
	private Image scaledTorchImage;
	private Image scaledTorchImageOn;
	private String description;
	
	private boolean isOn;

	/**
	 * Constructor for class Torch.
	 */
	public Torch() {
		description = "Torch: May it be a light for you in dark places.";
		loadImages();
		isOn = false;
	}

	/**
	 * Loads all images required for this object.
	 */
	private void loadImages() {
		try{
		torchImage = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"Torch.png"));
		torchImageOn = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"TorchOn.png"));
		scaledTorchImage = torchImage;
		scaledTorchImageOn = torchImageOn;
		} catch(IOException e){
			System.out.println("Error loading Torch file: "+e.getMessage());
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		isOn = !isOn;
	}

	@Override
	public Image getImage(int viewDirection) {
		if (viewDirection == 0){
			return torchImage;
		}
		return torchImageOn;
	}

	@Override
	public boolean canWalk() {
		return true;
	}

	@Override
	public int yOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		if (viewDirection == 0){
			this.scaledTorchImage = scaledImage;
		}
		this.scaledTorchImageOn = scaledImage;
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		if (isOn){
			return scaledTorchImageOn;
		}
		return scaledTorchImage;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public boolean isOn() {
		return isOn;
	}
	
	@Override
	public Type getType() {
		return Type.Torch;
	}

}
