package gameObjects.containers;

import gameObjects.Item;

/**
 * A representation of a container of Item objects
 * Anything that can store Item objects should extend this
 * 
 * @author Jah Seng Lee
 *
 */
public abstract class Container implements Item{
	
	protected Item[] items;
	
	
	public Container(int size) {
		items = new Item[size];
	}

	/**
	 * Returns true if items contains the specified item
	 * Otherwise, returns false
	 * 
	 * @param item
	 * @return
	 */
	public boolean contains(Item item){
		for(int i = 0; i < items.length; i++){
			if(items[i].equals(item)){
				return true;
			}
		}
		//item not found in array
		return false;
	}
	
	/**
	 * Gets the item at the specified index
	 * Returns null if item at index is null
	 * Throws  error if index is out of bounds
	 * 
	 * @param i
	 * @return
	 */
	public Item getItem(int i){
		return items[i];
	}
	
	/**
	 * Adds item to the container
	 * If the container is full, return false (item not added)
	 * Otherwise, return true
	 * 
	 * @param item item to be added to container
	 * @return
	 */
	public boolean addItem(Item item){
		
		for(int i = 0; i < items.length; i++){
			if(items[i] == null){
				items[i] = item;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes and returns the specified item in the array
	 * If item is not present, returns null
	 * 
	 * @param item
	 * @return item if present, otherwise null
	 */
	public Item removeItem(Item item){
		for(int i = 0; i < items.length; i++){
			if(items[i].equals(item)){
				Item returnItem = items[i];
				items[i] = null;
				return returnItem;
			}
		}
		//item not found in array
		return null;
	}
	
	/**
	 * Removes and returns item at index i
	 * If item is null, return null
	 * 
	 * @param i
	 * @return items[i]
	 */
	public Item removeItem(int i){
		Item returnItem = items[i];
		items[i] = null;
		return returnItem;
	}
	
	
}
