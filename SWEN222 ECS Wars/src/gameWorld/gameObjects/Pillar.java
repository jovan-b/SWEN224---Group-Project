package gameWorld.gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Item.Type;
import gui.GUICanvas;

/**
 * A decorative pillar item which takes up 1x1 grid space that can't
 * be walked on, and has a 1x3 image.
 * 
 * @author Chris Read 300254724
 * @author Sarah Dobie 300315033
 *
 */
public class Pillar implements Item {

	private Image image;
	private Image scaledImage;
	private int yOffset = 3;
	
	/**
	 * Constructor for class Pillar.
	 */
	public Pillar() {
		loadImages();
	}

	/**
	 * Loads all images required by this object.
	 */
	private void loadImages() {
		try {
			image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"Pillar.png"));
			scaledImage = image;
		} catch (IOException e) {
			System.out.println("Failed to read Pillar image file: " + e.getMessage());
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		return;
	}

	@Override
	public Image getImage(int viewDirection) {
		return image;
	}

	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int yOffset(int viewDirection) {
		return yOffset;
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
		return null;
	}
	
	@Override
	public Type getType() {
		return null;
	}

}
