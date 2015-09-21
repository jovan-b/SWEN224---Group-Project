package characters;

import gameObjects.Compass;
import gameObjects.Room;

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

public class DavePlayer implements Player {

	//position describing the centre of a player object
	private int posX;
	private int posY;
	private int viewDirection;
	
	private Compass compass;
	private int lastDirMoved;
	private int animState; // the current animation frame
	private int animModifier; // flicks between 1 and -1 to loop animation
	private int animCounter; // counts each frame the player has moved
	private Room currentRoom;
	private int currentRow; // view dependant row - for drawing correctly
	
	// Player sprite images
	Image[][] sprites;

	//player's speed is this constant * Player.SPEED
	private int speedMulti = 1;
	private int speed = speedMulti * Player.SPEED;

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
	}

	@Override
	public void draw(Graphics g, GUICanvas c) {
		//Blank for now
	}

	@Override
	public void shoot() {
		//blank for now
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
		switch(dir){
		case "right": posX += speed;
			break;
		case "left": posX -= speed;
			break;
		case "down": posY += speed;
			break;
		case "up": posY -= speed;
			break;
		}
	}
	
	private void moveEast(String dir) {
		switch(dir){
		case "right": posY += speed;
			break;
		case "left": posY -= speed;
			break;
		case "down": posX -= speed;
			break;
		case "up": posX += speed;
			break;
		}
	}
	
	private void moveSouth(String dir) {
		switch(dir){
		case "right": posX -= speed;
			break;
		case "left": posX += speed;
			break;
		case "down": posY -= speed;
			break;
		case "up": posY += speed;
			break;
		}
	}
	
	private void moveWest(String dir) {
		switch(dir){
		case "right": posY -= speed;
			break;
		case "left": posY += speed;
			break;
		case "down": posX += speed;
			break;
		case "up": posX -= speed;
			break;
		}
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
		return sprites[lastDirMoved][animState];
	}

	@Override
	public void setRow(int row) {
		currentRow = row;
	}

	@Override
	public int getRow() {
		return currentRow;
	}

}
