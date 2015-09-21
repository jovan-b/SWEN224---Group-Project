package gameObjects.weapons;

import characters.Player;
import gameObjects.Item;
import gameObjects.weapons.projectiles.Projectile;

/**
 * An abstract weapon superclass
 * 
 * @author Carl
 *
 */
public abstract class Weapon implements Item {
	private int fireRate = 0;
	private Projectile projectile;
	
	public Weapon(int fireRate, Projectile projectile){
		this.fireRate = fireRate;
		this.projectile = projectile;
	}
	
	/**
	 * Spawns a new projectile at location of Player p
	 * Projectile should move in direction from player described by angle theta
	 * 
	 * @param p
	 * @param theta
	 */
	public Projectile fire(Player p, double theta) {
		return projectile.newInstance(p, theta);
	}

	@Override
	public boolean canWalk() {
		return true;
	}
	
}
