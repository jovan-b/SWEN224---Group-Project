package gameObjects.weapons;

import java.util.Timer;
import java.util.TimerTask;

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
	
	boolean canFire;
	
	public Weapon(int fireRate, Projectile projectile){
		this.fireRate = fireRate;
		this.projectile = projectile;
		canFire = true;
	}
	
	/**
	 * Spawns a new projectile at location of Player p
	 * Projectile should move in direction from player described by angle theta
	 * 
	 * @param p
	 * @param theta
	 */
	public Projectile fire(Player p, double theta) {
		Projectile proj = projectile.newInstance(p, theta);
		
		if (!canFire){ //If we aren't allowed to shoot, fire a dud
			proj.setActive(false);
		} else {
			canFire = false;
			startFireDelay(1000/fireRate);
		}
		
		return proj;
	}

	@Override
	public boolean canWalk() {
		return true;
	}
	
	protected void startFireDelay(long delay){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				canFire = true;
			}
			
		}, delay);
	}
	
}
