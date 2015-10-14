package gameWorld.gameObjects;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * A 1x1 object that consumes treasure items to give the player points
 * @author Chris Read 300254724
 *
 */
public class VanishingCabinet implements Item {
	
	// base images
	private Image imageNorth;
	private Image imageSouth;
	private Image imageWest;
	private Image imageEast;
	// scaled images
	private Image scaledImageNorth;
	private Image scaledImageSouth;
	private Image scaledImageWest;
	private Image scaledImageEast;
	
	private String description = "A Vanishing Cabinet, it seems to like certain items...";

	/**
	 * Constructor for class VanishingCabinet.
	 * @param dir The direction (F, B, L, R) that the cabinet
	 * is facing when the view direction is North.
	 */
	public VanishingCabinet(char dir) {
		loadImages(dir);
		scaledImageNorth = imageNorth;
		scaledImageSouth = imageSouth;
		scaledImageWest = imageWest;
		scaledImageEast = imageEast;
	}
	
	/**
	 * Loads all images required for this object.
	 * @param dir The base direction of this item
	 */
	private void loadImages(char dir) {
		// initialise vars
		Image f = null;
		Image b = null;
		Image l = null;
		Image r = null;
		// read image files
		try {
			f = ImageIO.read(VanishingCabinet.class.getResource("/Items/CabinetBlueF.png"));
			b = ImageIO.read(VanishingCabinet.class.getResource("/Items/CabinetBlueB.png"));
			l = ImageIO.read(VanishingCabinet.class.getResource("/Items/CabinetBlueL.png"));
			r = ImageIO.read(VanishingCabinet.class.getResource("/Items/CabinetBlueR.png"));
		} catch (IOException e) {
			System.out.println("Failed to read Photocopier image file: " + e.getMessage());
		}
		// set directional images
		assignImages(dir, f, b, l, r);
	}

	/**
	 * Assigns images to the appropriate directional fields, depending on
	 * the base direction of the item.
	 * @param dir The base direction (F, B, L, R) of the item
	 * @param f Image showing front of item
	 * @param b Image showing back of item
	 * @param l Image showing left side of item
	 * @param r Image showing right side of item
	 */
	private void assignImages(char dir, Image f, Image b, Image l, Image r) {
		switch(dir){
		case 'B' : imageNorth = b; // BACK
				   imageSouth = f;
				   imageEast = r;
				   imageWest = l;
				   break;
		case 'L' : imageNorth = l; // LEFT
		   		   imageSouth = r;
		   		   imageEast = b;
		   		   imageWest = f;
		   		   break;
		case 'R' : imageNorth = r; // RIGHT
				   imageSouth = l;
				   imageEast = f;
				   imageWest = b;
				   break;
		default :  imageNorth = f; // FRONT
		   		   imageSouth = b;
		   		   imageEast = l;
		   		   imageWest = r;
		   		   break;
		}
	}

	@Override
	/**
	 * Consumes a SmallTreasure in the player's inventory and gives the player 
	 * its value in points
	 */
	public void use(Player p, Controller ctrl) {
		Item[] inventory = p.getInventory();
		for (int i = 0; i < inventory.length; i++){
			Item item = inventory[i];
			if (item != null && item instanceof SmallTreasure){
				SmallTreasure treasure = (SmallTreasure) item;
				p.givePoints(treasure.getPoints());
				p.removeItem(treasure);
				ctrl.reSpawnItem(new SmallTreasure());
				return;
			}
		}
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
	public int yOffset(int viewDirection) {
		return 2;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
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

	@Override
	public Type getType() {
		return null;
	}

}
