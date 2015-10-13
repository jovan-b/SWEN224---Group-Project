package gameWorld.gameObjects.containers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.ItemSpawner;

/**
 * A 1x1 container which can store 4 items.
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class Cabinet extends Container implements ItemSpawner {
	private static final int CAPACITY = 4;

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
	
	private String description = "A cabinet, may contain items.";
	
	/**
	 * Constructor for class Cabinet.
	 * @param dir The direction (F, B, L, R) that the cabinet
	 * is facing when the view direction is North.
	 */
	public Cabinet(char dir) {
		super(CAPACITY);
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
			f = ImageIO.read(Cabinet.class.getResource("/Items/CabinetF.png"));
			b = ImageIO.read(Cabinet.class.getResource("/Items/CabinetB.png"));
			l = ImageIO.read(Cabinet.class.getResource("/Items/CabinetL.png"));
			r = ImageIO.read(Cabinet.class.getResource("/Items/CabinetR.png"));
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
	public void use(Player p, Controller ctrl) {
		ctrl.getGUI().getCanvas().setCurrentContainer(this);
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
	public boolean addSpawnItem(Item item) {
		return super.addItem(item);
	}

	@Override
	public int remainingCapacity() {
		return super.capacity - contents.size();
	}

	@Override
	public Type getType() {
		return Type.Cabinet;
	}
	
}
