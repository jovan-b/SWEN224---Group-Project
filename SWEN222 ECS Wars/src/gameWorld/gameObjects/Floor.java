package gameWorld.gameObjects;

import java.awt.Graphics;
import java.awt.Image;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gui.GUICanvas;


/**
 * A blank item used for empty squares which player can walk over.
 * 
 * @author Chris Read 300254724
 * @author Sarah Dobie 300315033
 *
 */
public class Floor implements Item, ItemSpawner {

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
	public void use(Player p, Controller ctrl) {
		if (item != null){
			if (p.pickUp(item)){
				setItem(null);
			}
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

	@Override
	public boolean addSpawnItem(Item item) {
		if(this.item == null){
			this.item = item;
			return true;
		}
		return false;
		
	}

	@Override
	public int remainingCapacity() {
		if(item == null){
			return 1;
		}
		return 0;
	}

	@Override
	public Type getType() {
		return null;
	}

}
