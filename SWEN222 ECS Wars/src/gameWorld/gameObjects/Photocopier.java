package gameWorld.gameObjects;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gui.GUICanvas;

/**
 * A photocopier item which can be interacted with.
 * 2x1 image, 1x1 grid space.
 * 
 * @author Sarah Dobie 300315033
 *
 */
public class Photocopier implements Item {

	// original images
	private Image imageNorth;
	private Image imageSouth;
	private Image imageWest;
	private Image imageEast;
	// scaled images
	private Image scaledImageNorth;
	private Image scaledImageSouth;
	private Image scaledImageWest;
	private Image scaledImageEast;
	
	private int[] xoffset; // x offset for the index view direction
	private char dir; // the dir of the item when looking North
	private String description;

	/**
	 * Constructor for class Photocopier
	 * @param dir The view of the photocopier when facing North.
	 * 'F' = front
	 * 'B' = back
	 * 'L' = left
	 * 'R' = right
	 */
	public Photocopier(char dir) {
		this.dir = dir;
		loadImages(dir);
		scaledImageNorth = imageNorth;
		scaledImageSouth = imageSouth;
		scaledImageWest = imageWest;
		scaledImageEast = imageEast;
		setupOffset(dir);
		this.description = "A photocopier. What does it do?";
	}

	/**
	 * Determine the x offset for the given start direction.
	 * @param dir The base direction of the photocopier
	 */
	private void setupOffset(char dir) {
		xoffset = new int[4];
		xoffset[0] = 0;
		xoffset[1] = 0;
		xoffset[2] = 0;
		xoffset[3] = 0;
		switch(dir){
		case 'L' : // EAST
			xoffset[1] = 1;
			break;
		case 'B' : // SOUTH
			xoffset[2] = 1;
			break;
		case 'R' : // WEST
			xoffset[3] = 1;
			break;
		default : // NORTH
			xoffset[0] = 1;
			break;
		}
	}

	/**
	 * Loads all required images for this item.
	 * @param dir The base direction of the photocopier
	 */
	private void loadImages(char dir) {
		// initialise vars
		Image f = null;
		Image b = null;
		Image l = null;
		Image r = null;
		// read image files
		try {
			f = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"CopierF.png"));
			b = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"CopierB.png"));
			l = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"CopierL.png"));
			r = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"CopierR.png"));
		} catch (IOException e) {
			System.out.println("Failed to read Photocopier image file: " + e.getMessage());
		}
		// set directional images
		switch(dir){
		case 'B' : imageNorth = b;
				   imageSouth = f;
				   imageEast = r;
				   imageWest = l;
				   break;
		case 'L' : imageNorth = l;
		   		   imageSouth = r;
		   		   imageEast = f;
		   		   imageWest = b;
		   		   break;
		case 'R' : imageNorth = r;
				   imageSouth = l;
				   imageEast = b;
				   imageWest = f;
				   break;
		default :  imageNorth = f;
		   		   imageSouth = b;
		   		   imageEast = l;
		   		   imageWest = r;
		   		   break;
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		return;
	}

	@Override
	public Image getImage(int viewDirection) {
		switch(viewDirection){
		case 1 : return imageEast; // EAST
		case 2 : return imageSouth; // SOUTH
		case 3 : return imageWest; // WEST
		default : return imageNorth; // NORTH
		}
	}

	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public int xOffset(int viewDirection) {
		return xoffset[viewDirection];
	}

	@Override
	public int yOffset(int viewDirection) {
		return 1;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		switch(viewDirection){
		case 1 : scaledImageEast = scaledImage; return; // EAST
		case 2 : scaledImageSouth = scaledImage; return; // SOUTH
		case 3 : scaledImageWest = scaledImage; return; // WEST
		default : scaledImageNorth = scaledImage; return; // NORTH
		}
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		switch(viewDirection){
		case 1 : return scaledImageEast; // EAST
		case 2 : return scaledImageSouth; // SOUTH
		case 3 : return scaledImageWest; // WEST
		default : return scaledImageNorth; // NORTH
		}
	}

	@Override
	public String getDescription() {
		return description;
	}


}


