package gameWorld.gameObjects.weapons;

import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import gameWorld.characters.Player;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.weapons.projectiles.Projectile;

/**
 * An abstract weapon superclass
 * 
 * @author Carl
 *
 */
public abstract class Weapon implements Item {
	protected double fireRate = 0;
	protected Projectile projectile;
	protected Image image;
	protected Image scaledImage;
	protected String name;
	
	protected boolean canFire;
	
	public enum WeaponType{
		PaintballGun,
		ScatterGun,
		LTSAGun,
		Pistol
	}
	
	public Weapon(double fireRate, Projectile projectile){
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
		if (!canFire){return null;}
		
		Projectile proj = projectile.newInstance(p, theta);

		canFire = false;
		startFireDelay((long)(1000/fireRate));
		
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
	
	@Override
	public Item.Type getType(){
		return Item.Type.Weapon;
	}
	
	public abstract WeaponType getWeaponType();

	/**
	 * returns the weapon's name - for displaying as part of the HUD
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Weapon){
			return ((Weapon)o).getWeaponType() == this.getWeaponType();
		}
		return false;
	}
	
}
