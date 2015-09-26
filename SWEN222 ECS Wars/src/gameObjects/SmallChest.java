package gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import characters.Player;

/**
 * A 1x1 container which can store 5 items.
 * @author Sarah Dobie
 *
 */
public class SmallChest extends Container {
	private static final int CAPACITY = 5;

	private Image imageNorth;
	private Image imageSouth;
	private Image imageWest;
	private Image imageEast;
	private Image scaledImageNorth;
	private Image scaledImageSouth;
	private Image scaledImageWest;
	private Image scaledImageEast;
	
	public SmallChest(char dir) {
		super(CAPACITY);
		System.out.println("new small chest");
		loadImages(dir);
		scaledImageNorth = imageNorth;
		scaledImageSouth = imageSouth;
		scaledImageWest = imageWest;
		scaledImageEast = imageEast;
	}
	
	private void loadImages(char dir) {
		// initialise vars
		Image f = null;
		Image b = null;
		Image l = null;
		Image r = null;
		// read image files
		try {
			f = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"SmallChestF.png"));
			b = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"SmallChestB.png"));
			l = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"SmallChestL.png"));
			r = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"SmallChestR.png"));
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
	public void use(Player p) {
		// TODO display items, let player pick some up
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
		return 1;
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
	
}
