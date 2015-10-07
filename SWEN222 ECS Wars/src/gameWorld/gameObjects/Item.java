package gameWorld.gameObjects;

import java.awt.Image;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * Represents all in-game items eg. desks, weapons, books etc
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public interface Item {
	
	public enum Type{
		KeyCard,
		SmallTreasure,
		Torch,
		Pouch,
		MedicineBottle,
		PillBottle,
		Map,
		Cabinet,
		//TODO: Add weapon after implementation
		
	}
	
	/**
	 * Allows the player to interact with this item if possible.
	 * @param p The player using this item.
	 */
	public void use(Player p, Controller ctrl);

	/**
	 * Returns the image representing this item.
	 * @param viewDirection The direction this is being viewed from
	 * @return The appropriate image representing this item
	 */
	public Image getImage(int viewDirection);

	/**
	 * Whether or not this item can be walked over by players.
	 * @return True if this can be walked on, false otherwise.
	 */
	public boolean canWalk();
	
	/**
	 * Determines how many rows higher than the item's position that
	 * the top-left of its image will be.
	 * @param viewDirection The direction the item is being viewed in.
	 * @return The y offset (in rows) of this item's top-left corner.
	 */
	public int yOffset(int viewDirection);

	/**
	 * Determines how many columns to the left than the item's position that
	 * the top-left of its image will be.
	 * @param viewDirection The direction the item is being viewed in.
	 * @return The x offset (in columns) of this item's top-left corner.
	 */
	public int xOffset(int viewDirection);

	/**
	 * Sets the scaled image of this item to the given image.
	 * @param viewDirection The direction the item is being viewed in
	 * @param scaledImage The image to set the scaled image as.
	 */
	public void setScaledImage(int viewDirection, Image scaledImage);
	
	/**
	 * Returns the appropriately scaled copy of this item's image.
	 * @param viewDirection The direction the item is being viewed from
	 * @return An appropriately scaled copy of this item's image.
	 */
	public Image getScaledImage(int viewDirection);
	
	/**
	 * If this object can be picked up or used, return a brief description
	 * of it.
	 * @return A brief description of this object, or null if it is not interactive.
	 */
	public String getDescription();
	
	/**
	 * Returns type of items which can be picked up in rooms
	 * If cannot be picked up, return null
	 * 
	 * @return
	 */
	public abstract Type getType();
}
