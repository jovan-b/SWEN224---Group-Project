package gameWorld.gameObjects.weapons.projectiles;

import java.awt.Image;
import java.awt.Rectangle;

import gameWorld.characters.Player;

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
	 * Creates a new instance of this projectile which heads 
	 * in a given direction
	 * 
	 * @param p the player who fired the projectile
	 * @param theta the direction the projectile is heading
	 * 
	 * @return a new projectile object
	 */
	public Projectile newInstance(Player p, double theta);
	
	public static double xDiff(double theta, double speed){
		return Math.cos(theta)*speed;
	}

	public static double yDiff(double theta, double speed){
		return Math.sin(theta)*speed;
	}

	public boolean isActive();

	/**
	 * getters for the x and y pos of the projectile - for drawing
	 * @return
	 */
	public int getX();

	public int getY();

	/**
	 * Returns the bounding box for this projectile
	 * @return
	 */
	public Rectangle getBoundingBox();
	
	/**
	 * Returns the player that fired this projectile
	 * @return
	 */
	public Player getPlayer();
	
	public int getRow();

	public Image getImage(int scale);

	public int getSize();

	public void setActive(boolean active);

	/**
	 * Sets the speed multiplier for this projectile
	 * @param multi
	 */
	public void setSpeedMultiplier(double multi);

	public void setRow(int x);
}
