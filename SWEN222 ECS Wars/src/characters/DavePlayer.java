package characters;

import gameObjects.Compass;
import gameObjects.Item;
import gameObjects.Room;
import gameObjects.weapons.Weapon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

/**
 * Represents the Dave playable character
 * @author Chris Read
 *
 */
public class DavePlayer implements Player {
	
	private final int INVENTORY_SIZE = 10;
	
	//fields describing state of player
	private Weapon currentWeapon;
	private Item[] inventory = new Item[INVENTORY_SIZE];
	private int health = 50;
	

	//position describing the centre of a player object
	private int posX;
	private int posY;
	private int viewDirection;
	private int hitBox = 11;
	
	private Compass compass;
	private int lastDirMoved;
	private int animState; // the current animation frame
	private int animModifier; // flicks between 1 and -1 to loop animation
	private int animCounter; // counts each frame the player has moved
	private Room currentRoom;
	private int currentRow; // view dependant row - for drawing correctly


	// Player sprite images
	Image[][] sprites;
	Image[][] scaledSprites;

	//player's speed is this constant * Player.SPEED
	private int speedMulti = 1;
	private int speed = speedMulti * Player.BASE_SPEED;

	public DavePlayer(Room room, int posX, int posY){
		this.currentRoom = room;
		this.posX = posX;
		this.posY = posY;
		this.viewDirection = 0;
		this.lastDirMoved = 2;
		this.animState = 0;
		this.animModifier = 1;
		this.animCounter = 0;
		
		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++){
				for (int ani = 0; ani < 3; ani++){
					sprites[dir][ani] = ImageIO.read(new File("Resources"+File.separator+"Players"+File.separator+"Dave"+dir+ani+".png"));
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
		scaledSprites = sprites;
	}

	@Override
	public void draw(Graphics g, GUICanvas c) {
		//Blank for now
	}
	
	@Override
	public void update() {
		currentRoom.update();
		//TODO: add collision detection with other players
		//TODO: check if dead and drop items/lose points/etc.
	}

	
	/**
	 * 
	 */
	@Override
	public void shoot(int x, int y) {
		double theta = Player.angleBetweenPlayerAndMouse(this.posX, this.posY,
				x, y);
		
		currentWeapon.fire(this, theta);
	}

	/**
	 * Update player's position by "speed" amount
	 * Direction is specified by dir (up, down, left, right)
	 */
	@Override
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

	@Override
	public int getX() {
		return posX;
	}

	@Override
	public int getY() {
		return posY;
	}

	@Override
	public int getViewDirection() {
		return viewDirection;
	}

	@Override
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

	@Override
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

	@Override
	public Image getImage() {		
		return scaledSprites[lastDirMoved][animState];
	}

	@Override
	public void setRow(int row) {
		currentRow = row;
	}

	@Override
	public int getRow() {
		return currentRow;
	}

	@Override
	public Image[][] getImages() {
		return sprites;
	}

	@Override
	public void setScaledImages(Image[][] newImages) {
		scaledSprites = newImages;
	}

}
