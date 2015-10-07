package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * A map class to display the layout of the rooms.
 * @author Chris Read 300254724
 *
 */
public class Map implements Item {
	
	private Image mapImage;
	private Image scaledMapImage;
	private boolean mapOpen;
	private String description;

	/**
	 * Creates a new Map object and loads relevant images
	 */
	public Map() {
		mapOpen = false;
		description = "A map displaying the room layout";
		try {
			mapImage = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"MapIcon.png"));
		} catch (IOException e) {
			System.out.println("Error reading Map image: " + e.getMessage());
		}
		scaledMapImage = mapImage;
	}

	/**
	 * Toggles the mapOpen boolean
	 *  - this flag is for displaying the map overlay on the screen 
	 */
	@Override
	public void use(Player p, Controller ctrl) {
		mapOpen = !mapOpen;
	}

	@Override
	public Image getImage(int viewDirection) {
		return mapImage;
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
		scaledMapImage = scaledImage;

	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return scaledMapImage;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns if the map overlay should be drawn on the screen
	 * @return True if the map is open, false if it isn't
	 */
	public boolean isOpen(){
		return mapOpen;
	}

}
