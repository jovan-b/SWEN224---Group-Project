package characters;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.GUICanvas;
import gameObjects.Compass;
import gameObjects.Drawable;
import gameObjects.Item;
import gameObjects.Room;
import gameObjects.weapons.PaintballGun;
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
public abstract class Player implements Drawable {
	
	public static final int FIRERATE = 4;	//projectiles per second
	public static final int BASE_SPEED = 2;	//pixels per frame
	public static final int BASE_HEIGHT = 50;
	public static final int BASE_WIDTH = 30;
	
	
	protected final int INVENTORY_SIZE = 10;
	
	//fields describing state of player
	protected Weapon currentWeapon;
	protected Item[] inventory = new Item[INVENTORY_SIZE];
	protected int health = 50;
	protected int counter = 0;	//used to keep track of when player can fire
								//is 0 when player can fire
	

	//position describing the centre of a player object
	protected int posX;
	protected int posY;
	protected int viewDirection;
	protected int hitBox = 11;
	
	protected Compass compass;
	protected int lastDirMoved;
	protected int animState; // the current animation frame
	protected int animModifier; // flicks between 1 and -1 to loop animation
	protected int animCounter; // counts each frame the player has moved
	protected Room currentRoom;
	protected int currentRow; // view dependant row - for drawing correctly
	
	protected GUICanvas canvas;


	// Player sprite images
	Image[][] sprites;
	Image[][] scaledSprites;

	//player's speed is this constant * Player.SPEED
	protected int speedMulti = 1;
	protected int speed = speedMulti * Player.BASE_SPEED;
	
	public Player(Room room, int posX, int posY){
		this.currentRoom = room;
		this.posX = posX;
		this.posY = posY;
		this.viewDirection = 0;
		this.lastDirMoved = 2;
		this.animState = 0;
		this.animModifier = 1;
		this.animCounter = 0;
		this.currentWeapon = new PaintballGun();
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
		updateCounter();
		//TODO: add collision detection with other players
		//TODO: check if dead and drop items/lose points/etc.
	}

	private void updateCounter() {
		if(counter != 0){
			++counter;
		}
		if(getFirerate() <= counter){
			counter = 0;
		}
	}

	abstract int getFirerate();

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
		++counter;
		currentRoom.addProjectile(currentWeapon.fire(this, theta));
	}

	/**
	 * Update player's position by "speed" amount
	 * Direction is specified by dir (up, down, left, right)
	 */
	public void move(String dir) {
		moved(dir);
		animate();
		switch(viewDirection){
		case 1:
			moveEast(dir);
			return;
		case 2:
			moveSouth(dir);
			return;
		case 3:
			moveWest(dir);
			return;
		default :
			moveNorth(dir);
			return;
		}
	}
	
	/**
	 * Modifies the player's health.
	 * Negative values cause damage
	 * 
	 * @param amt the amount to change by
	 */
	public void modifyHealth(int amt){
		//TODO: Check that health hasn't gone above a maximum
		health += amt;
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
	private void moved(String dir) {
		switch(dir){
		case "up": lastDirMoved = 0;
			break;
		case "right": lastDirMoved = 1;
			break;
		case "down": lastDirMoved = 2;
			break;
		case "left": lastDirMoved = 3;
			break;
		}
	}

	private void moveNorth(String dir) {
		int tempX = posX;
		int tempY = posY;
		switch(dir){
		case "right": tempX += speed;
			break;
		case "left": tempX -= speed;
			break;
		case "down": tempY += speed;
			break;
		case "up": tempY -= speed;
			break;
		}
		if (canMove(tempX, tempY)){
			posX = tempX;
			posY = tempY;
		}
	}
	
	private void moveEast(String dir) {
		int tempX = posX;
		int tempY = posY;
		switch(dir){
		case "right": tempY += speed;
			break;
		case "left": tempY -= speed;
			break;
		case "down": tempX -= speed;
			break;
		case "up": tempX += speed;
			break;
		}
		if (canMove(tempX, tempY)){
			posX = tempX;
			posY = tempY;
		}
	}
	
	private void moveSouth(String dir) {
		int tempX = posX;
		int tempY = posY;
		switch(dir){
		case "right": tempX -= speed;
			break;
		case "left": tempX += speed;
			break;
		case "down": tempY -= speed;
			break;
		case "up": tempY += speed;
			break;
		}
		if (canMove(tempX, tempY)){
			posX = tempX;
			posY = tempY;
		}
	}
	
	private void moveWest(String dir) {
		int tempX = posX;
		int tempY = posY;
		switch(dir){
		case "right": tempY -= speed;
			break;
		case "left": tempY += speed;
			break;
		case "down": tempX += speed;
			break;
		case "up": tempX -= speed;
			break;
		}
		if (canMove(tempX, tempY)){
			posX = tempX;
			posY = tempY;
		}
	}
	
	/**
	 * Returns whether the player can move to a square
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean canMove(int x, int y){
		if (!currentRoom.itemAt(x+hitBox, y+hitBox).canWalk() ||
				!currentRoom.itemAt(x+hitBox, y-hitBox).canWalk() ||
				!currentRoom.itemAt(x-hitBox, y-hitBox).canWalk() ||
				!currentRoom.itemAt(x-hitBox, y+hitBox).canWalk()){
			return false;
		}
		return true;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public int getX() {
		return posX;
	}

	public int getY() {
		return posY;
	}

	public int getViewDirection() {
		return viewDirection;
	}

	/**
	 * Rotates the player's view clockwise
	 */
	public void rotateViewRight() {
		viewDirection--;
		lastDirMoved++;
		if (viewDirection < 0){
			viewDirection = 3;
		}
		if (lastDirMoved > 3){
			lastDirMoved = 0;
		}
		compass.rotate(90);
	}

	/**
	 * Rotates the player's view anticlockwise
	 */
	public void rotateViewLeft() {
		viewDirection++;
		lastDirMoved--;
		if (viewDirection > 3){
			viewDirection = 0;
		}
		if (lastDirMoved < 0){
			lastDirMoved = 3;
		}
		compass.rotate(-90);
	}
	
	public void setCompass(Compass c){
		this.compass = c;
	}

	public Image getImage() {		
		return scaledSprites[lastDirMoved][animState];
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
		return new Rectangle(posX-hitBox, posY-hitBox, hitBox*2, hitBox*2);
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

	public boolean canShoot() {
		if(counter  == 0){
			return true;
		}
		return false;
	}
}
