package gameObjects.weapons;

import java.awt.Graphics;
import java.awt.Image;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import characters.Player;
import main.GUICanvas;
import gameObjects.Item;
import gameObjects.weapons.projectiles.Projectile;

public class Weapon implements Item {
	//TODO: Player as parameter, or player as field?
	private int fireRate = 0;
	private Class<Projectile> projectile;
	
	public Weapon(int fireRate, Class<Projectile> projectile){
		this.fireRate = fireRate;
		this.projectile = projectile;
	}
	
	/**
	 * Fires the gun in a specific direction
	 * 
	 * @param p the player using the weapon
	 * @param dir direction the gun is facing
	 */
	public void fire(Player p, String dir){
		//TODO: Something with fire rate
		try {
			//FIXME: There may be nothing wrong here, but this could potentially break horribly
			Constructor<Projectile> c = projectile.getConstructor(int.class, int.class, String.class);
			Projectile shot = c.newInstance(p.getX(), p.getY(), dir);
			
			
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Spawns a new projectile at location of Player p
	 * Projectile should move in direction from player described by angle theta
	 * 
	 * @param p
	 * @param theta
	 */
	public Projectile fire(Player p, double theta) {
		return new BasicProjectile(p.getX(), p.getY(), theta);
	}


	@Override
	public void draw(Graphics g, GUICanvas c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void use() {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canWalk() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * A basic projectile which this weapon can fire
	 * Should travel in a linear fashion in theta angle last x, y position
	 * 
	 * @author Jah Seng Lee
	 *
	 */
	private class BasicProjectile implements Projectile{
		
		private int x;
		private int y;
		private double theta;
		
		public BasicProjectile(int x, int y, double theta){
			this.x = x;
			this.y = y;
			this.theta = theta;
		}

		@Override
		public void update() {
			//TODO: update projectile's x, y by it's speed
			//needs to be in relation to angle theta
			//update until wall it hit
		}
		
	}
}
