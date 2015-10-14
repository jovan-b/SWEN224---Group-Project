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
	 * Moves an item at the specified index into the player's 
	 * inventory if they have room. Removes the item from
	 * the Container
	 * @param index The index of the item to pick up
	 * @param player The Player picking up the item
	 */
	public void pickUpItem(int index, Player player){
		if (index < 0 || contents.size() <= index){
			return;
		}
		Item item = contents.get(index);
		if (player.pickUp(item)){
			contents.remove(index);
		}
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
	 * Returns a shallow clone of the contents of this container.
	 * @return A shallow clone of this container's contents
	 */
	public List<Item> getContents() {
		return new ArrayList<Item>(contents);
	}

	/**
	 * returns the item at the specified index in the Container
	 * or a blank wall if outside the bounds
	 * @param index The index of the item to return
	 * @return the item at index
	 */
	public Item getItem(int index){
		if (0 <= index && index < contents.size()){
			return contents.get(index);
		}
		return new Wall();
	}
	
	/**
	 * returns the number of empty slots in the Container
	 * or 0 if it is full
	 */
	public int remainingCapacity(){
		return capacity - contents.size();
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof Container)){return false;}
		if(this.getType() == ((Container)o).getType()){
			for(int i = 0; i < this.getContents().size(); i++){
				if(((Container)o).getContents().get(i) == null){
					//item is not in o, return false
					return false;
				}
				if(!this.getContents().get(i).equals(((Container)o).getContents().get(i))){
					//items are not equal
					return false;
				}	
			}
		}
		
		return true;
	}
	
}
