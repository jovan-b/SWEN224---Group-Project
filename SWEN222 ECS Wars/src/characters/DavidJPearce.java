package characters;

import gameObjects.Item;
import gameObjects.weapons.Weapon;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import main.GUICanvas;

public class DavidJPearce implements Player{
	
	private final int INVENTORY_SIZE = 10;
	
	private Weapon currentWeapon;
	private Item[] inventory = new Item[INVENTORY_SIZE];
	private Image currentImage; //TODO: initialise with image of DJP
	
	//in order to change movement speed, speedMulti should be changed
	private int speedMulti = 1;
	private int speed = speedMulti * Player.BASE_SPEED;

	//stores position of character in xy
	//position is relative to center of character sprite
	private int posY;
	private int posX;

	public DavidJPearce(int posX, int posY){
		this.posX = posX;
		this.posY = posY;
	}
	
	@Override
	public void draw(Graphics g, GUICanvas c) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	@Override
	public void shoot(int x, int y) {
		double theta = angleBetweenPlayerAndMouse(this.posX, this.posY,
				x, y);
		
		currentWeapon.fire(this, theta);
	}
	
	public static double angleBetweenPlayerAndMouse(double point1X, double point1Y, 
	        double point2X, double point2Y) {

	    double angle1 = Math.atan2(point1Y - 0, point1X - 0);
	    double angle2 = Math.atan2(point2Y - 0, point2X - 0);

	    return Math.toDegrees(angle1 - angle2); 
	}

	/**
	 * Update player's position by "speed" amount
	 * Direction is specified by dir (up, down, left, right)
	 */
	@Override
	public void move(String dir) {
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
		// TODO Auto-generated method stub
		return 0;
	}
}
