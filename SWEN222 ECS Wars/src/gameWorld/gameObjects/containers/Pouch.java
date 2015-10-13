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
 * A small container which may be picked up.
 * @author Sarah Dobie 300315033
 *
 */
public class Pouch extends Container implements ItemSpawner {
	
	private Image image;
	private Image scaledImage;
	private String description;

	/**
	 * Constructor for class Pouch.
	 */
	public Pouch() {
		super(4);
		this.description = "A small pouch which holds "+super.capacity+" items.";
		loadImages();
	}

	/**
	 * Loads all required images.
	 */
	private void loadImages() {
		try{
			image = ImageIO.read(Pouch.class.getResource("/Items/Pouch.png"));
			scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading KeyCard file: "+e.getMessage());
		}
	}

	/**
	 * Sets the currentContainer to this, so the player can move
	 * items between inventories
	 */
	@Override
	public void use(Player p, Controller ctrl) {
		ctrl.getGUI().getCanvas().setCurrentContainer(this);
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

	@Override
	public boolean addSpawnItem(Item item) {
		if(item != this){
			return super.addItem(item);
		}
		return false;
	}

	@Override
	public Type getType() {
		return Type.Pouch;
	}

}
