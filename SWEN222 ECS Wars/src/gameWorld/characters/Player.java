package gameWorld.characters;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.gameEvents.Event;
import gameWorld.gameEvents.GameClock;
import gameWorld.gameEvents.RespawnEvent;
import gameWorld.gameObjects.Door;
import gameWorld.gameObjects.Floor;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.Wall;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.weapons.PaintballGun;
import gameWorld.gameObjects.weapons.ScatterGun;
import gameWorld.gameObjects.weapons.Weapon;
import gui.Compass;
import gui.GUICanvas;

/**
 * An interface representing a playable character
 * Players base speed and size are defined
 * Implementations should change them by using multiplication constants e.g.
 * 		FIRERATE * firerateMulti
 * 
 * @author Jah Seng Lee
 * @author Chris Read
 * @author Sarah Dobie 300315033
 *
 */
public abstract class Player {
	public static final int RESPAWN_TIME = 3000; //3 seconds
	public static final int BASE_SPEED = 2;	//pixels per frame
	public static final int BASE_HEIGHT = 50;
	public static final int BASE_WIDTH = 30;
	public final int HEALTH_MAX = 200;

	public static final int INVENTORY_SIZE = 3;

	/**
	 * Enum representing the type of player.
	 */
	public enum Type{
		DavePlayer,
		PondyPlayer,
		NonPlayer
	}

	//fields describing state of player
	protected Weapon currentWeapon;
	protected Item[] inventory = new Item[INVENTORY_SIZE];
	protected int health = HEALTH_MAX;
	protected int points = 0;


	//position describing the centre of a player object
	protected int posX;
	protected int posY;
	protected int viewDirection;
	protected int hitBox = 10;

	private int tempX;
	private int tempY;

	protected Compass compass;
	protected int lastDirMoved;
	protected int animState; // the current animation frame
	protected int animModifier; // flicks between 1 and -1 to loop animation
	protected int animCounter; // counts each frame the player has moved
	protected Room currentRoom;
	protected int currentRow; // view dependant row - for drawing correctly

	protected GUICanvas canvas;


	// Player sprite images
	protected Image[][] sprites;
	protected Image[][] scaledSprites;

	//player's speed is this constant * Player.SPEED
	protected int speedModifier = 0;
	protected int speed = speedModifier + Player.BASE_SPEED;

	/**
	 * Constructor for class Player.
	 * @param room The room the player starts in
	 * @param posX The centre x position of this player
	 * @param posY The centre y position of this player
	 */
	public Player(Room room, int posX, int posY){
		this.currentRoom = room;
		this.posX = posX;
		this.posY = posY;
		this.viewDirection = 0;
		this.lastDirMoved = 2;
		this.animState = 0;
		this.animModifier = 1;
		this.animCounter = 0;
		this.currentWeapon = new ScatterGun();
	}

	/**
	 * Draws the player to the canvas
	 */
	public void draw(Graphics g, GUICanvas c) {
		//Blank for now
	}

	/**
	 * Updates player 1 tick, and propagates update throughout
	 * room
	 */
	public void update() {
		currentRoom.update();

		//TODO: add collision detection with other players
		//TODO: check if dead and drop items/lose points/etc.
	}

	/**
	 * Shoots the player's current weapon
	 * @param x
	 * @param y
	 */
	public void shoot(int x, int y) {
		double theta = Player.angleBetweenPlayerAndMouse(canvas.getWidth()/2, canvas.getHeight()/2,
				x, y);

		//Correct theta based on view direction
		theta += Math.toRadians(90)*viewDirection;
		currentRoom.addProjectile(currentWeapon.fire(this, theta));
	}

	/**
	 * Update player's position by "speed" amount
	 * Direction is specified by dir (up, down, left, right)
	 */
	public void move(String dir) {
		animate();
		// Convert movement direction to global coordinates
		int moveDir = convertFromViewDir(moved(dir));
		movePlayer(moveDir);
	}

	/**
	 * Moves the player relative to the global coordinates
	 * @param dir Global direction to move
	 */
	private void movePlayer(int dir) {
		tempX = posX;
		tempY = posY;
		lastDirMoved = dir;
		switch(dir){
		case 1: tempX += speed;
		break;
		case 3: tempX -= speed;
		break;
		case 2: tempY += speed;
		break;
		default: tempY -= speed;
		break;
		}
		if (canMove(tempX, tempY)){
			posX = tempX;
			posY = tempY;
		}
	}

	/**
	 * Modifies the player's health.
	 * Negative values cause damage
	 * 
	 * @param amt the amount to change by
	 */
	public void modifyHealth(int amt){
		health += amt;
		if(health > HEALTH_MAX){
			health = HEALTH_MAX;
		} else if(health < 0){
			health = 0;
		}
	}

	// TODO replace this
	/**
	 * Update the player's animation state.
	 */
	private void animate() {
		animCounter++;
		if (animCounter > 10){
			animState += animModifier;
			animCounter = 0;
			if (animState <= 0 || animState >= 2){
				animModifier *= -1;
			}
		}
	}

	/**
	 * Sets the lastDirMoved variable according to the button pressed
	 * @param dir The directional button pressed
	 * @return The direction moved
	 */
	private int moved(String dir) {
		switch(dir){
		case "up": return 0;
		case "right": return 1;
		case "down": return 2;
		case "left": return 3;
		default: return 0;
		}
		//System.out.println("Position: " + this.posX + " " + this.posY);
	}

	/**
	 * Returns whether the player can move to a position
	 * @param x The x position to move to
	 * @param y The y position to move to
	 * @return true iff the player can move to (x,y)
	 */
	public boolean canMove(int x, int y){
		boolean inDoor = true;
		// Checks top right corner
		Item item = currentRoom.itemAt(x+hitBox, y+hitBox);
		if (!item.canWalk()){return false;
		} else {
			if (!(item instanceof Door)){inDoor = false;}
		}
		// Checks bottom right corner
		item = currentRoom.itemAt(x+hitBox, y-hitBox);
		if (!item.canWalk()){return false;
		} else {
			if (!(item instanceof Door)){inDoor = false;}
		}
		// Checks bottom left corner
		item = currentRoom.itemAt(x-hitBox, y-hitBox);
		if (!item.canWalk()){return false;
		} else {
			if (!(item instanceof Door)){inDoor = false;}
		}
		// Checks top left corner
		item = currentRoom.itemAt(x-hitBox, y+hitBox);
		if (!item.canWalk()){return false;
		} else {
			if (!(item instanceof Door)){inDoor = false;}
		}
		if (inDoor){
			((Door)item).walkThrough(this);
		}
		return true;
	}

	/**
	 * Utility method to convert a direction relative to the view direction
	 * back to the direction of the global coordinates
	 * @param toConvert the input value to convert
	 * @return the converted direction value
	 */
	private int convertFromViewDir(int toConvert) {
		int temp = toConvert;
		for (int i = 1; i <= viewDirection; i++){
			temp++;
			if (temp > 3){
				temp = 0;
			}
		}
		return temp;
	}

	/**
	 * Utility method to convert a global direction
	 * to a direction relative to the view
	 * @param toConvert the input value to convert
	 * @param viewDirection2 
	 * @return the converted direction value
	 */
	private int convertToViewDir(int toConvert, int viewDir) {
		int temp = toConvert;
		for (int i = 1; i <= viewDir; i++){
			temp--;
			if (temp < 0){
				temp = 3;
			}
		}
		return temp;
	}


	/**
	 * Rotates the player's view clockwise
	 */
	public void rotateViewRight() {
		viewDirection--;
		if (viewDirection < 0){
			viewDirection = 3;
		}
		compass.rotate(90);
	}

	/**
	 * Rotates the player's view anticlockwise
	 */
	public void rotateViewLeft() {
		viewDirection++;
		if (viewDirection > 3){
			viewDirection = 0;
		}
		compass.rotate(-90);
	}

	/**
	 * Gives the angle between the player and the mouse
	 * 
	 * @param point1X The x position of the player
	 * @param point1Y The y position of the player
	 * @param point2X The x position of the mouse
	 * @param point2Y The y position of the mouse
	 * @return The angle (in radians) between the player and the mouse.
	 */
	public static double angleBetweenPlayerAndMouse(double point1X, double point1Y, 
			double point2X, double point2Y) {

		double dy = point2Y-point1Y;
		double dx = point2X-point1X;
		double theta = Math.atan2(dy,dx);

		return theta;
	}

	/**
	 * Moves an item from the world into the player's inventory.
	 * @param item The item to pick up
	 * @return true iff the pick up was successful
	 */
	public boolean pickUp(Item item) {
		int index = 0;
		// look for empty space in inventory
		while(index < INVENTORY_SIZE){
			if (inventory[index] == null){
				inventory[index] = item;
				return true;
			}
			index++;
		}
		// no space in inventory
		return false;
	}

	/**
	 * Gets the inventory of this player.
	 * @return The array of items the player has in their inventory.
	 */
	public Item[] getInventory(){
		return inventory;
	}

	/**
	 * Gets the item at the given position in the player's inventory.
	 * @param index The position of the item
	 * @return The item at inventory[index]
	 */
	public Item inventoryItemAt(int index){
		if (index < 0 || INVENTORY_SIZE <= index){
			return new Wall();
		}
		Item item = inventory[index];
		if (item == null){
			return new Wall();
		}
		return item;
	}

	/**
	 * Determines whether this player's inventory contains the given item.
	 * @param item The item to search for
	 * @return true iff the player's inventory contains item
	 */
	public Item inventoryContains(Item item){
		for (Item i : inventory){
			if (i != null && i.getDescription().equals(item.getDescription())){
				return i;
			}
		}
		return null;
	}

	/**
	 * 'Drops' an item from the player's inventory into either a selected
	 * container, or onto the floor tile the player is on.
	 * @param index The inventory index of the item to drop
	 * @param container The current container selected (or null if none is selected).
	 */
	public void dropItem(int index, Container container) {
		// check index is valid
		if (index < 0 || INVENTORY_SIZE <= index){
			return;
		}
		// try to add to an open container
		if (container != null) {
			if  (container.remainingCapacity() > 0){
				// check the item is successfully added to container
				if(container.addItem(inventory[index])){
					inventory[index] = null;
				}
			}
			return;
		}
		// try to add to the floor tile the player is on
		Item square = currentRoom.itemAt((int)posX, (int)posY);
		if (square instanceof Floor){
			if (((Floor) square).getItem() == null){
				((Floor) square).setItem(inventory[index]);
				inventory[index] = null;
			}
		}
	}
	
	/**
	 * Give points to the player.
	 * @param points The points to give
	 */
	public void givePoints(int points){
		this.points += points;
	}
	
	/**
	 * Take points from the player.
	 * @param points The points to take
	 */
	public void removePoints(int points){
		this.points -= points;
	}

	/**
	 * Gets the Type of player this is.
	 * @return The Type of player
	 */
	public abstract Type getType();

	//State booleans
	
	/**
	 * Determine whether the player is dead.
	 * @return true iff the player is dead
	 */
	public boolean isDead(){
		return health <= 0;
	}

	//Getters
	public int getX() {return posX;}

	public int getY() {return posY;}

	public Room getCurrentRoom() {return currentRoom;}

	public int getRow() {return currentRow;}

	public int getHealth(){return health;}

	public int getGlobalDir() {
		return lastDirMoved;
	}


	public Image[][] getImages() {return sprites;}

	public int getViewDirection() {return viewDirection;}

	/**
	 * Gets the appropriate image for the given room direction
	 * @param viewDir The view direction
	 * @return The sprite image appropriate to player and view direction.
	 */
	public Image getImage(int viewDir) {	
		int spriteDir = convertToViewDir(lastDirMoved, viewDir);
		return scaledSprites[spriteDir][animState];
	}

	/**
	 * Get the direction the player is facing.
	 * @return The direction the player is facing
	 */
	public int getFacing(){
		return convertToViewDir(lastDirMoved, viewDirection);
	}

	/**
	 * Gets this player's bounding box.
	 * @return The minimal rectangle that encloses the whole player.
	 */
	public Rectangle getBoundingBox(){
		return new Rectangle((int)posX-hitBox, (int)posY-hitBox, hitBox*2, hitBox*2);
	}

	//Setters
	public void setRow(int row) {currentRow = row;}

	public void setHealth(int health){this.health = health;}

	/**
	 * Change the player the room is in.
	 * @param newRoom The room to put the player in
	 * @param newX The new x position of the player
	 * @param newY The new y position of the player
	 */
	public void setCurrentRoom(Room newRoom, int newX, int newY) {
		currentRoom.removePlayer(this);
		newRoom.addPlayer(this);
		this.currentRoom = newRoom;

		this.posX = newX;
		this.posY = newY;

		//Prevent the pesky move updates from overwriting the pos change
		this.tempX = newX;
		this.tempY = newY;	
	}

	//TODO: Maybe move all view direction code up to one of the views rather
	//than the player object?
	
	/**
	 * Change the view direction of the player.
	 * @param dir The new view direction
	 */
	public void setViewDirection(int dir){
		if (dir < 0 || dir > 3){return;}
		viewDirection = dir;
	}

	/**
	 * Change the direction the player is facing.
	 * @param dir The new direction to face
	 */
	public void setFacing(int dir){
		if (dir < 0 || dir > 3){ return; }
		lastDirMoved = dir;
	}

	/**
	 * Sets the player's position.
	 * @param x The new x position
	 * @param y The new y position
	 */
	public void setXY(int x, int y){
		posX = x;
		posY = y;
		animate();
	}

	public void setCanvas(GUICanvas canvas){this.canvas = canvas;}

	public void setCompass(Compass c){this.compass = c;}

	public void setScaledImages(Image[][] newImages) {
		scaledSprites = newImages;
	}

	public void setSpeedModifier(int modifier){
		speedModifier = modifier;
		speed = Player.BASE_SPEED+speedModifier;
	}

}
