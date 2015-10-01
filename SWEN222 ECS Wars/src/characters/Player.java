package characters;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.GUICanvas;
import gameEvents.Event;
import gameEvents.GameClock;
import gameEvents.RespawnEvent;
import gameObjects.Compass;
import gameObjects.Door;
import gameObjects.Floor;
import gameObjects.Item;
import gameObjects.Room;
import gameObjects.Wall;
import gameObjects.weapons.PaintballGun;
import gameObjects.weapons.ScatterGun;
import gameObjects.weapons.Weapon;

/**
 * An interface representing a playable character
 * Players base speed and size are defined
 * Implementations should change them by using multiplication constants e.g.
 * 		FIRERATE * firerateMulti
 * 
 * @author Jah Seng Lee
 * @author Chris Read
 *
 */
public abstract class Player {
	public static final int RESPAWN_TIME = 3000; //3 seconds
	public static final int BASE_SPEED = 2;	//pixels per frame
	public static final int BASE_HEIGHT = 50;
	public static final int BASE_WIDTH = 30;
	public static final int HEALTH_MAX = 200;
	
	
	public static final int INVENTORY_SIZE = 3;
	
	public enum Type{
		DavePlayer,
		PondyPlayer,
		NonPlayer
	}
	
	//fields describing state of player
	protected Weapon currentWeapon;
	protected Item[] inventory = new Item[INVENTORY_SIZE];
	protected int health = 200;
	

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
	
	public void setCanvas(GUICanvas canvas){
		this.canvas = canvas;
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

	// Sets the lastDirMoved variable according to the button pressed
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
	 * Returns whether the player can move to a square
	 * @param x
	 * @param y
	 * @return
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

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room newRoom, int newX, int newY) {
		currentRoom.removePlayer(this);
		newRoom.addPlayer(this);
		this.currentRoom = newRoom;

		this.posX = newX;
		this.posY = newY;
		
		//Prevent the pesky move updates from overwriting the pos change
		this.tempX = newX;
		this.tempY = newY;
		
		// Move player out of doorway - to prevent being stuck in the door
//		int dir = convertFromViewDir(lastDirMoved);
		switch(lastDirMoved){
		case 1:
			this.posX += 24;
			this.tempX += 24;
			break;
		case 2:
			this.posY += 24;
			this.tempY += 24;
			break;
		case 3:
			this.posX -= 24;
			this.tempX -= 24;
			break;
		default:
			this.posY -= 24;
			this.tempY -= 24;
			break;
		}
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

	public int getX() {
		return (int)posX;
	}

	public int getY() {
		return (int)posY;
	}

	public int getViewDirection() {
		return viewDirection;
	}
	
	public void setViewDirection(int dir){
		if (dir < 0 || dir > 3){return;}
		viewDirection = dir;
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
	
	public void setCompass(Compass c){
		this.compass = c;
	}

//	public Image getImage() {	
//		int spriteDir = convertToViewDir(lastDirMoved, viewDirection);
//		return scaledSprites[spriteDir][animState];
//	}
	
	public Image getImage(int viewDir) {	
		int spriteDir = convertToViewDir(lastDirMoved, viewDir);
		return scaledSprites[spriteDir][animState];
	}
	
	public void setRow(int row) {
		currentRow = row;
	}

	public int getRow() {
		return currentRow;
	}

	public Image[][] getImages() {
		return sprites;
	}

	public void setScaledImages(Image[][] newImages) {
		scaledSprites = newImages;
	}
	
	public Rectangle getBoundingBox(){
		return new Rectangle((int)posX-hitBox, (int)posY-hitBox, hitBox*2, hitBox*2);
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setFacing(int dir){
		if (dir < 0 || dir > 3){ return; }
		lastDirMoved = dir;
	}
	
	public int getFacing(){
		return convertToViewDir(lastDirMoved, viewDirection);
	}
	
	public void setSpeedModifier(int modifier){
		speedModifier = modifier;
		speed = Player.BASE_SPEED+speedModifier;
	}
	
	/**
	 * Gives the angle between the player and the mouse
	 * 
	 * @param point1X
	 * @param point1Y
	 * @param point2X
	 * @param point2Y
	 * @return
	 */
	public static double angleBetweenPlayerAndMouse(double point1X, double point1Y, 
	        double point2X, double point2Y) {

//	    double angle1 = Math.atan2(point1Y - 0, point1X - 0);
//	    double angle2 = Math.atan2(point2Y - 0, point2X - 0);
//
//	    return angle1 - angle2; 
		
		double dy = point2Y-point1Y;
		double dx = point2X-point1X;
		double theta = Math.atan2(dy,dx);

		return theta;
	}

	public boolean pickUp(Item item) {
		int index = 0;
		while(index < INVENTORY_SIZE){
			if (inventory[index] == null){
				inventory[index] = item;
				return true;
			}
			index++;
		}
		return false;
	}
	
	public Item[] getInventory(){
		return inventory;
	}
	
	public Item inventoryItem(int index){
		if (index < 0 || INVENTORY_SIZE <= index){
			return new Wall();
		}
		Item item = inventory[index];
		if (item == null){
			return new Wall();
		}
		return item;
	}
	
	public boolean inventoryContains(Item item){
		for (Item i : inventory){
			if (i != null && i.getDescription().equals(item.getDescription())){
				return true;
			}
		}
		return false;
	}

	public void dropItem(int index) {
		if (index < 0 || INVENTORY_SIZE <= index){
			return;
		}
		Item square = currentRoom.itemAt((int)posX, (int)posY);
		if (square instanceof Floor){
			if (((Floor) square).getItem() == null){
				((Floor) square).setItem(inventory[index]);
				inventory[index] = null;
			}
		}
	}
	
	public abstract Type getType();
}
