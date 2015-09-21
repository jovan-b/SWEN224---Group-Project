package characters;

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
}
