package characters;

import gameObjects.Compass;
import gameObjects.Room;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

public class TestPlayer implements Player {

	//position describing the centre of a player object
	private int posX;
	private int posY;
	private int viewDirection;
	
	private Compass compass;
	
	private Room currentRoom;

	//player's speed is this constant * Player.SPEED
	private int speedMulti = 1;
	private int speed = speedMulti * Player.SPEED;

	public TestPlayer(Room room, int posX, int posY){
		this.currentRoom = room;
		this.posX = posX;
		this.posY = posY;
		this.viewDirection = 0; //FIXME hardcoded for testing
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
	public void rotateViewLeft() {
		viewDirection--;
		if (viewDirection < 0){
			viewDirection = 3;
		}
		compass.rotate(90);
	}

	@Override
	public void rotateViewRight() {
		viewDirection++;
		if (viewDirection > 3){
			viewDirection = 0;
		}
		compass.rotate(-90);
	}
	
	public void setCompass(Compass c){
		this.compass = c;
	}

}
