package gameObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Item which can contain several other Items.
 * @author Sarah Dobie
 *
 */
public abstract class Container implements Item {
	protected List<Item> contents;
	private int capacity;

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
		if(contents.size() < capacity){
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
	
}
