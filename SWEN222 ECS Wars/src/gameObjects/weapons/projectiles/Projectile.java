package gameObjects.weapons.projectiles;

import java.awt.Rectangle;

import characters.Player;

/**
 * A projectile object, which moves through the map in a particular direction
 * 
 * @author Carl Anderson
 *
 */
public interface Projectile {
	
	public static final int BASE_SPEED = 5;

	/**
	 * Causes this projectile to update it's position
	 */
	public void update();
	
	/**
	 * getters for the x and y pos of the projectile - for drawing
	 * @return
	 */
	public int getX();
	public int getY();
	
	public void setRow(int x);
	public int getRow();
	
	/**
	 * Creates a new instance of this projectile which heads 
	 * in a given direction
	 * 
	 * @param p the player who fired the projectile
	 * @param theta the direction the projectile is heading
	 * 
	 * @return a new projectile object
	 */
	public Projectile newInstance(Player p, double theta);
	
	/**
	 * Returns the bounding box for this projectile
	 * @return
	 */
	public Rectangle getBoundingBox();
	
	public static int xDiff(double theta, int speed){
		return (int)(Math.cos(theta)*speed);
	}
	
	public static int yDiff(double theta, int speed){
		return (int)(Math.sin(theta)*speed);
	}
}
