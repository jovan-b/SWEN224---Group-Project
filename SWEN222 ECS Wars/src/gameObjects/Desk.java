package gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import characters.Player;
import main.Controller;
import main.GUICanvas;

/**
 * A decorative 2x1 desk object which cannot be walked on.
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class Desk implements Item {
	
	private Image imageHz;
	private Image imageVt;
	private Image scaledImageHz;
	private Image scaledImageVt;
	private boolean horizontal; // true if the desk is horizontal when looking North
	
	/**
	 * Constructor for class Desk.
	 * @param horizontal True if the desk is horizontal when view is North
	 */
	public Desk(boolean horizontal) {
		this.horizontal = horizontal;
		loadImages();
	}

	/**
	 * Parses and stores all required images.
	 */
	private void loadImages() {
		try {
			imageHz = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"DeskHor.png"));
			scaledImageHz = imageHz;
			imageVt = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"DeskVer.png"));
			scaledImageVt = imageVt;
		} catch (IOException e) {
			System.out.println("Failed to read Desk image file: " + e.getMessage());
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		return;
	}

	@Override
	public Image getImage(int viewDirection) {
		if(this.horizontal){
			// the desk is horizontal when facing north/south
			switch(viewDirection){
			case 0: case 2: return imageHz; // north/south
			case 1: case 3: return imageVt; // east/west
			}
		} else {
			// the desk is vertical when facing north/south
			switch(viewDirection){
			case 0: case 2: return imageVt; // north/south
			case 1: case 3: return imageHz; // east/west
			}
		}
		return imageHz;
	}

	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public int xOffset(int viewDirection) {
		if(this.horizontal){
			// the desk is horizontal when facing north/south
			switch(viewDirection){
			case 2: return 1; // SOUTH
			default: return 0;
			}
		} else {
			// the desk is vertical when facing north/south
			switch(viewDirection){
			case 3: return 1; // WEST
			default: return 0;
			}
		}
	}

	@Override
	public int yOffset(int viewDirection) {
		if(this.horizontal){
			// the desk is horizontal when facing north/south
			switch(viewDirection){
			case 1: return 2; // EAST
			default: return 1;
			}
		} else {
			// the desk is vertical when facing north/south
			switch(viewDirection){
			case 2: return 2; // SOUTH
			default: return 1;
			}
		}
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		if(this.horizontal){
			// the desk is horizontal when facing north/south
			switch(viewDirection){
			case 0: case 2: this.scaledImageHz = scaledImage; return; // north/south
			case 1: case 3: this.scaledImageVt = scaledImage; return; // east/west
			}
		} else {
			// the desk is vertical when facing north/south
			switch(viewDirection){
			case 0: case 2: this.scaledImageVt = scaledImage; return; // north/south
			case 1: case 3: this.scaledImageHz = scaledImage; return; // east/west
			}
		}
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		if(this.horizontal){
			// the desk is horizontal when facing north/south
			switch(viewDirection){
			case 0: case 2: return scaledImageHz; // north/south
			case 1: case 3: return scaledImageVt; // east/west
			}
		} else {
			// the desk is vertical when facing north/south
			switch(viewDirection){
			case 0: case 2: return scaledImageVt; // north/south
			case 1: case 3: return scaledImageHz; // east/west
			}
		}
		return scaledImageHz;
	}

	@Override
	public String getDescription() {
		return null;
	}
	
	
}
