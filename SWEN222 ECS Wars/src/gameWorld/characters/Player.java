package gameWorld.characters;

import java.awt.Image;
import java.awt.Rectangle;

import gameWorld.PointValues;
import gameWorld.Room;
import gameWorld.gameObjects.Door;
import gameWorld.gameObjects.Floor;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.Wall;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.weapons.PaintballGun;
import gameWorld.gameObjects.weapons.Weapon;
import gameWorld.gameObjects.weapons.projectiles.Projectile;
import gui.GUICanvas;

/**
 * An abstract class representing a playable character
 * 
 * @author Jah Seng Lee 300279468
 * @author Chris Read 300254724
 * @author Sarah Dobie 300315033
 * @author Jovan Bogoievski 300305140
 *
 */
public abstract class Player {
	public static final int RESPAWN_TIME = 3000; //3 seconds
	public static final int BASE_SPEED = 2;	//pixels per frame
	public static final int BASE_HEIGHT = 50;
	public static final int BASE_WIDTH = 30;
	public static final  int HEALTH_MAX = 200;

	public static final int INVENTORY_SIZE = 3;

	/**
	 * Enum representing the type of player.
	 */
	public enum PlayerType{
		DavePlayer,
		PondyPlayer,
		MarcoPlayer,
		StreaderPlayer,
		NonPlayer
	}

	//fields describing state of player
	protected Weapon currentWeapon;
	protected Item[] inventory = new Item[INVENTORY_SIZE];
	protected int maxHealth = HEALTH_MAX;

	protected int health = HEALTH_MAX;
	protected int points = 500;


	//position describing the centre of a player object
	protected int posX;
	protected int posY;
	protected int hitBox = 10;

	protected int tempX;
	protected int tempY;

	protected int lastDirMoved;
	protected int animState; // the current animation frame
	protected int animModifier; // flicks between 1 and -1 to loop animation
	protected int animCounter; // counts each frame the player has moved
	protected Room currentRoom;
	protected int currentRow; // view dependant row - for drawing correctly
	
	protected boolean disconnected;

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
		this.lastDirMoved = 2;
		this.animState = 0;
		this.animModifier = 1;
		this.animCounter = 0;
		this.currentWeapon = new PaintballGun();
		this.disconnected = false;
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
		double theta = getTheta(x, y);
		currentRoom.addProjectile(currentWeapon.fire(this, theta));
	}
	
	/**
	 * Shoots from the players direction
	 * @param theta
	 */
	public void shoot(double theta){
		currentRoom.addProjectile(currentWeapon.fire(this, theta));
	}
	
	/**
	 * Calculates the direction to shoot from
	 * @param x
	 * @param y
	 * @return
	 */
	public double getTheta(int x, int y){
		Double theta = Player.angleBetweenPlayerAndMouse(canvas.getWidth()/2, canvas.getHeight()/2,
				x, y);
		theta += Math.toRadians(90)*canvas.getViewDirection();
		return theta;
	}

	/**
	 * Update player's position by "speed" amount
	 * Direction is specified by dir (up, down, left, right)
	 */
	public void move(int dir) {
		if (isDead()){return;}
		animate();
		// Convert movement direction to global coordinates
		movePlayer(dir);
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
	 * @param p The projectile the player was shot with, or null
	 * if they were not shot.
	 */
	public void modifyHealth(int amt, Projectile p){
		health += amt;
		if(health > HEALTH_MAX){
			health = HEALTH_MAX;
		} else if(health <= 0){
			health = 0;
			// give points to the player that killed me
			if(p != null && p.getPlayer() != this){
				p.getPlayer().givePoints(PointValues.KILL_PLAYER);
			}
			// take points from this player for dying
			removePoints(PointValues.DEATH);
		}
	}

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
		
		//Get the bounding box at the new position
		Rectangle box = this.getBoundingBox();
		box.translate(x-posX, y-posY);
		
		for (Player p : currentRoom.getAllCharacters()){
			if (p == this){continue;}
			//Check if the player intersects with another player
			if (box.intersects(p.getBoundingBox())){
				//If this player is already intersecting (such as spawning inside them)
				//let them move
				if (this.getBoundingBox().intersects(p.getBoundingBox())){
					return true;
				}
				
				return false;
			}
		}
		
		return true;
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
		Container container = null;
		// look for empty space in inventory
		while(index < INVENTORY_SIZE){
			if (inventory[index] == null){
				inventory[index] = item;
				return true;
			} else if (	container == null && inventory[index] instanceof Container){
				//Look for empty containers in the inventory
				Container c = (Container)inventory[index];
				if (c.remainingCapacity() > 0){
					container = c;
				}
			}
			index++;
		}
		
		if (container != null){
			return container.addItem(item);
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
	 * Removes ('consumes') an item from the player's inventory.
	 * @param item The item to remove
	 * @return true iff the item was successfully removed
	 */
	public boolean removeItem(Item item){
		if(item == null){return true;} // ignore null - will have no effect
		// search for item
		for(int i=0; i<inventory.length; i++){
			if(inventory[i] == item){
				// found item, remove it
				inventory[i] = null;
				return true;
			}
		}
		// didn't find item in inventory
		return false;
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
	public abstract PlayerType getType();

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
	
	public int getMaxHealth() {return maxHealth;}

	public int getPoints() {return points;}
	
	public void setPoints(int points) {this.points = points;}

	public int getGlobalDir() {
		return lastDirMoved;
	}
	
	public abstract String getName();


	public Image[][] getImages() {return sprites;}

	/**
	 * Gets the appropriate image for the given room direction
	 * @param viewDir The view direction
	 * @return The sprite image appropriate to player and view direction.
	 */
	public Image getImage(int viewDir) {	
		int spriteDir = GUICanvas.convertToViewDir(lastDirMoved, viewDir);
		return scaledSprites[spriteDir][animState];
	}

	/**
	 * Get the direction the player is facing.
	 * @return The direction the player is facing
	 */
	public int getFacing(){
		return GUICanvas.convertToViewDir(lastDirMoved, canvas.getViewDirection());
	}

	/**
	 * Gets this player's bounding box.
	 * @return The minimal rectangle that encloses the whole player.
	 */
	public Rectangle getBoundingBox(){
		return new Rectangle(posX-hitBox, posY-hitBox, hitBox*2, hitBox*2);
	}

	//Setters
	/**
	 * Sets the row variable for the current room relative to the view direction 
	 * @param row the row variable to set
	 */
	public void setRow(int row) {currentRow = row;}

	public void setHealth(int health){this.health = health;}

	/**
	 * Change the player the room is in.
	 * @param newRoom The room to put the player in
	 * @param newX The new x position of the player
	 * @param newY The new y position of the player
	 */
	public void setCurrentRoom(Room newRoom, int newX, int newY) {
		if (currentRoom != null){
			currentRoom.removePlayer(this);
		}
		newRoom.addPlayer(this);
		this.currentRoom = newRoom;

		this.posX = newX;
		this.posY = newY;

		//Prevent the pesky move updates from overwriting the pos change
		this.tempX = newX;
		this.tempY = newY;	
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

	/**
	 * replace player sprites with images scaled to the current view direction
	 * @param newImages
	 */
	public void setScaledImages(Image[][] newImages) {
		scaledSprites = newImages;
	}

	public void setSpeedModifier(int modifier){
		speedModifier = modifier;
		speed = Player.BASE_SPEED+speedModifier;
	}
	
	public Weapon getWeapon(){
		return currentWeapon;
	}

	/**
	 * Replace the player's current weapon with the one on the given
	 * floor.
	 * @param weapon The weapon to pick up
	 * @param floor The floor the weapon is on
	 */
	public void pickUpWeapon(Weapon weapon, Floor floor) {
		// drop current weapon
		floor.setItem(currentWeapon);
		// set my weapon to the new one
		this.currentWeapon = weapon;
	}
	
	public void setCurrentWeapon(Weapon currentWeapon) {
		this.currentWeapon = currentWeapon;
	}
	
	/**
	 * Sets a players position and room
	 * Used in multiplayer to send the players state
	 * @param x
	 * @param y
	 * @param direction
	 * @param room
	 */
	public void setPosition(int x, int y, int direction, Room room){
		//If the players room has changed, update them to their new room
		if(room != null && room != currentRoom){
			setCurrentRoom(room, x, y);
		//else just update their position
		} else{
			posX = x;
			posY = y;
		}
		setFacing(direction);
		animate();
	}

	/**
	 * Returns if a player is disconnected
	 * @return true if disconnected, false if still in game.
	 */
	public boolean isDisconnected(){
		return disconnected;
	}

	/**
	 * Sets the players status as disconnected.
	 */
	public void disconnect(){
		disconnected = true;
		//Sets them out of the games bounds, will change this later
		setPosition(99999999, 99999999, 1, currentRoom);
	}

}
