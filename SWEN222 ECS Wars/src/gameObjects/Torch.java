package gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import characters.Player;

/**
 * A torch item that allows the player to see in dark environments
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class Torch implements Item {
	
	private Image image;
	private Image scaledImage;
	private String description;

	/**
	 * Constructor for class Torch.
	 */
	public Torch() {
		description = "Torch: May it be a light for you in dark places.";
		loadImages();
	}

	/**
	 * Loads all images required for this object.
	 */
	private void loadImages() {
		try{
		image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"Torch.png"));
		scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading Torch file: "+e.getMessage());
		}
	}

	@Override
	public void use(Player p) {
		System.out.println("Player used the torch!!");
	}

	@Override
	public Image getImage(int viewDirection) {
		return image;
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
		this.scaledImage = scaledImage;
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return scaledImage;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
