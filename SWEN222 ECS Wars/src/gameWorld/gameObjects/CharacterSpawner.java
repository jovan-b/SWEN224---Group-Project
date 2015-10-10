package gameWorld.gameObjects;

import gameWorld.Room;

/**
 * Represents an item that can spawn a player onto it.
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class CharacterSpawner {
	
	private int x;
	private int y;
	private Room room;
	
	/**
	 * Constructor for class CharacterSpawner.
	 * @param col The column to spawn player at
	 * @param row The row to spawn player at
	 */
	public CharacterSpawner(int col, int row, Room room){
		this.x = col*24+12;
		this.y = row*24+12;
		this.room = room;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Room getRoom() {
		return room;
	}
	
}
