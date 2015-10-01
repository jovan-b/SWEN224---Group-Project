package gameObjects;

import java.awt.Graphics;
import java.awt.Image;

import characters.Player;
import main.Controller;
import main.GUICanvas;

/**
 * A door which connects two rooms, which a player can walk through
 * if it is unlocked, either by default, or with a keycard.
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class Door implements Item {
	
	private Room room1; // room on one side of door
	private Room room2; // room on other side of door
	private String id; // this door's unique 2-digit id
	private int room1Col; // the column pos in room1
	private int room1Row; // the row pos in room1
	private int room2Col; // the column pos in room2
	private int room2Row; // the row pos in room2
	private boolean unlocked; // true if the door is unlocked by default
	private boolean tempUnlocked; // true if the door has been unlocked by a keycard
								  // or is unlocked by default
	
	/**
	 * Constructor for class door.
	 * @param parseCode The parsed id for this door
	 * @param room1 The first room this door is connected to
	 * @param room1Col The col pos of room1
	 * @param room1Row The row pos of room1
	 */
	public Door(String parseCode, Room room1, int room1Col, int room1Row){
		this.id = parseCode;
		determineLocked();
		this.room1 = room1;
		this.room1Col = room1Col;
		this.room1Row = room1Row;
//		System.out.println("New Door: " + parseCode + " " + room1.getName());
	}

	/**
	 * Determines whether or not the door is locked, and updates the fields
	 * appropriately.
	 * A door is locked if its 2-digit id is >= 50; otherwise it is unlocked.
	 */
	private void determineLocked() {
		int intCode = Integer.parseInt(id);
		if(intCode >= 50){
			unlocked = false;
		} else {
			unlocked = true;
		}
		tempUnlocked = unlocked;
	}
	
	/**
	 * Sets the second room this door is connected to.
	 * @param room2 The second room this door is connected to
	 * @param room2Col The col pos of room2
	 * @param room2Row The row pos of room2
	 */
	public void addRoom2(Room room2, int room2Col, int room2Row){
		this.room2 = room2;
		this.room2Col = room2Col;
		this.room2Row = room2Row;
//		System.out.println("Connecting to Door: " + id + " " + room2.getName());
	}
	
	@Override
	public void use(Player p, Controller ctrl) {
		if(p.inventoryContains(new KeyCard())){
			tempUnlocked = true;
			System.out.println("Unlocked the door"); // TODO implement properly
		}
		
	}

	/**
	 * Transports a player through this door and into the other room.
	 * @param p The player to transport
	 */
	public void walkThrough(Player p) {
		int x;
		int y;
		// check which room the player is currently in
		if(room1 == p.getCurrentRoom()){
			// player is being moved to room2
			x = p.getX()-(room1Col*24);
			y = p.getY()-(room1Row*24);
			p.setCurrentRoom(room2, (room2Col*24)+x, (room2Row*24)+y);
		} else if(room2 == p.getCurrentRoom()){
			// player is being moved to room1
			x = p.getX()-(room2Col*24);
			y = p.getY()-(room2Row*24);
			p.setCurrentRoom(room1, (room1Col*24)+x, (room1Row*24)+y);
		}
		// reset temporary unlocked status if the door was unlocked with a keycard
		if(!unlocked){
			tempUnlocked = false;
		}
	}

	@Override
	public Image getImage(int viewDirection) {
		return null;
	}

	@Override
	public boolean canWalk() {
		return tempUnlocked;
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
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return null;
	}

	/**
	 * Returns the first room this door is connected to.
	 * @return The first room this door connects to
	 */
	public Room getRoom1() {
		return room1;
	}

	/**
	 * Returns the second room this door is connected to.
	 * @return The second room this door connects to
	 */
	public Room getRoom2() {
		return room2;
	}

	/**
	 * Returns the unique 2-digit id of this door.
	 * @return A 2-digit id number for this door
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the column of this door in room1
	 * @return The column of this door in room1
	 */
	public int getRoom1Col() {
		return room1Col;
	}

	/**
	 * Get the row of this door in room1
	 * @return The row of this door in room1
	 */
	public int getRoom1Row() {
		return room1Row;
	}

	/**
	 * Get the column of this door in room2
	 * @return The column of this door in room2
	 */
	public int getRoom2Col() {
		return room2Col;
	}

	/**
	 * Get the row of this door in room2
	 * @return The row of this door in room2
	 */
	public int getRoom2Row() {
		return room2Row;
	}

	/**
	 * Returns true if the door is unlocked by default.
	 * @return true if and only if the door is unlocked by default
	 */
	public boolean isUnlocked() {
		return unlocked;
	}

	@Override
	public String getDescription() {
		return null;
	}
	
}
