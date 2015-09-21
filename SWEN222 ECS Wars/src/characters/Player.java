package characters;

import java.awt.Image;
import java.awt.Point;

import gameObjects.Compass;
import gameObjects.Drawable;

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
	
	abstract void move(String dir);
	abstract void shoot(int x, int y);
	
	abstract int getX();
	abstract int getY();
	abstract int getViewDirection();
	abstract Image getImage();
	abstract void rotateViewLeft();
	abstract void rotateViewRight();
	abstract void setCompass(Compass compass);
	abstract void setRow(int row);
	abstract int getRow();
	abstract Image[][] getImages();
	abstract void setScaledImages(Image[][] newImages);
	
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
