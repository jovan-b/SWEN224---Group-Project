package gameWorld.gameObjects;

/**
 * An item which can hold other items (eg. containers, floors)
 * and will have items spawned within them when a new game is created.
 * @author Sarah Dobie 300315033
 *
 */
public interface ItemSpawner extends Item {
	
	/**
	 * Adds an item to this item's inventory.
	 * REQUIRES The item has a remaining capacity of at
	 * least 1 for this method to be successful.
	 * @param item The item to add.
	 * @return true iff the item was added successfully.
	 */
	public boolean addSpawnItem(Item item);
	
	/**
	 * Gets the number of spaces remaining in this item.
	 * @return The number of item spaces remaining
	 */
	public int remainingCapacity();
	
}
