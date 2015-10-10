package gameWorld.gameObjects.containers;

import java.util.ArrayList;
import java.util.List;

import gameWorld.characters.Player;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.ItemSpawner;
import gameWorld.gameObjects.Wall;

/**
 * Represents an Item which can contain several other Items.
 * 
 * @author Sarah Dobie 300315033
 *
 */
public abstract class Container implements ItemSpawner {
	protected List<Item> contents;
	protected int capacity;

	/**
	 * Constructor for class container.
	 * @param capacity The max number of items this container can hold.
	 */
	public Container(int capacity){
		contents = new ArrayList<Item>();
		this.capacity = capacity;
	}
	
	/**
	 * Returns a shallow clone of the contents of this container.
	 * @return A shallow clone of this container's contents
	 */
	public List<Item> getContents() {
		return new ArrayList<Item>(contents);
	}
	
	/**
	 * Adds an item to the contents of this container.
	 * @param item The item to add
	 * @return True if the item was successfully added, false otherwise.
	 */
	public boolean addItem(Item item){
		if(contents.size() < capacity && item != this){
			contents.add(item);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes all items from this container.
	 */
	public void empty(){
		contents.clear();
	}
	
	public Item getItem(int index){
		if (0 <= index && index < contents.size()){
			return contents.get(index);
		}
		return new Wall();
	}
	
	public void pickUpItem(int index, Player player){
		if (index < 0 || contents.size() <= index){
			return;
		}
		Item item = contents.get(index);
		if (player.pickUp(item)){
			contents.remove(index);
		}
	}
	
	public int remainingCapacity(){
		return capacity - contents.size();
	}
	
}
