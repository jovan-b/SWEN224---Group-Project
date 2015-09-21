package gameObjects.weapons.projectiles;

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
	public abstract void update();
	
	/**
	 * Creates a new instance of this projectile which heads 
	 * in a given direction
	 * 
	 * @param p the player who fired the projectile
	 * @param theta the direction the projectile is heading
	 * 
	 * @return a new projectile object
	 */
	public abstract Projectile newInstance(Player p, double theta);
	
	public static int xDiff(double theta, int speed){
		return (int)(Math.cos(theta)*speed);
	}
	
	public static int yDiff(double theta, int speed){
		return (int)(Math.sin(theta)*speed);
	}
}
