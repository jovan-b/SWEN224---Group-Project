package characters;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import gameObjects.Compass;
import gameObjects.Drawable;
import gameObjects.Room;

/**
 * An interface representing a playable character
 * Players base speed and size are defined
 * Implementations should change them by using multiplication constants e.g.
 * 		FIRERATE * firerateMulti
 * 
 * @author Jah Seng Lee
 *
 */
public interface Player extends Drawable {
	
	public static final int FIRERATE = 4;	//projectiles per second
	public static final int BASE_SPEED = 2;	//pixels per frame
	public static final int BASE_HEIGHT = 50;
	public static final int BASE_WIDTH = 30;
	
	public void move(String dir);
	public void shoot(int x, int y);
	
	/**
	 * Adds the amount onto the player's health bar.
	 * Negative values cause damage.
	 * 
	 * @param amt the amount to change health by
	 */
	public void modifyHealth(int amt);
	
	public int getX();
	public int getY();
	public int getViewDirection();
	public Image getImage();
	public void rotateViewLeft();
	public void rotateViewRight();
	public void setCompass(Compass compass);
	public void setRow(int row);
	public int getRow();
	public Room getCurrentRoom();
	public void setCurrentRoom(Room r);
	public Image[][] getImages();
	public void setScaledImages(Image[][] newImages);
	public Rectangle getBoundingBox();
	
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

	    double angle1 = Math.atan2(point1Y - 0, point1X - 0);
	    double angle2 = Math.atan2(point2Y - 0, point2X - 0);

	    return angle1 - angle2; 
	}
	abstract void update();
}
