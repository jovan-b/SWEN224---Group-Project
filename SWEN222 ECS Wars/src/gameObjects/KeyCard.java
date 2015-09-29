package gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import characters.Player;

/**
 * A key card that allows the player to open certain in-game doors.
 * @author Sarah Dobie, Chris Read
 *
 */
public class KeyCard implements Item {

	private Image image;
	private Image scaledImage;
	private String description;
	
	/**
	 * Constructor for class KeyCard.
	 */
	public KeyCard(){
		description = "Key Card: Allows access to locked rooms.";
		try{
		image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"IDCard.png"));
		scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading KeyCard file: "+e.getMessage());
		}
	}
	
	@Override
	public void use(Player p) {
		System.out.println("Player used the keycard!!");
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
