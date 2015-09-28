package gameObjects;

import java.awt.Graphics;
import java.awt.Image;

import characters.Player;
import main.GUICanvas;


/**
 * A blank item used for empty squares - player can walk over
 * @author Chris Read
 *
 */
public class Floor implements Item {

	private Item item;
	
	/**
	 * Constructs a floor with no items on it.
	 */
	public Floor() {}

	/**
	 * Constructs a floor with the given item on it.
	 * @param item The item to place on the floor.
	 */
	public Floor(Item item) {
		this.item = item;
	}

	@Override
	public void use(Player p) {
		if (item != null){
			item.use(p);
		}
	}

	@Override
	public Image getImage(int viewDirection) {
		if(item != null){
			return item.getImage(viewDirection);
		}
		return null;
	}

	@Override
	public boolean canWalk() {
		return true;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int yOffset(int viewDirection) {
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		if(item != null){
			item.setScaledImage(viewDirection, scaledImage);
		}
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		if(item != null){
			return item.getScaledImage(viewDirection);
		}
		return null;
	}
	
	/**
	 * Returns the item on this floor.
	 * @return The Item on this floor, or null if there is none
	 */
	public Item getItem() {
		return item;
	}
	
	/**
	 * Sets the item on this floor.
	 * @param item The Item to place on the floor, or null for no item.
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public String getDescription() {
		if(item != null){
			return item.getDescription();
		}
		return null;
	}

}
