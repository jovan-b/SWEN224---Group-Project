package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * A 1x1 treasure worth points that can be found on floors and in containers.
 * 
 * @author Sarah Dobie 300315033
 *
 */
public class SmallTreasure implements Item {

	private Image image;
	private Image scaledImage;
	private String description;
	private int points;
	
	/**
	 * Constructor for class KeyCard.
	 */
	public SmallTreasure(String imageFile, int points, String description){
		this.description = description;
		this.points = points;
		loadImages(imageFile);
	}

	/**
	 * Parse and store all required images.
	 */
	private void loadImages(String fileName) {
		try{
			image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+fileName));
			scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading image file: "+e.getMessage());
		}
	}
	
	@Override
	public void use(Player p, Controller ctrl) {}

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

	public int getPoints() {
		return points;
	}
	
}
