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

}
