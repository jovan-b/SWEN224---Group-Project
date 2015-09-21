package gameObjects.weapons;

import java.awt.Graphics;
import java.awt.Image;

import characters.Player;
import main.GUICanvas;
import gameObjects.Item;
import gameObjects.weapons.projectiles.Projectile;

public class Weapon implements Item {
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
	public void draw(Graphics g, GUICanvas c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void use() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canWalk() {
		return true;
	}

	@Override
	public Image getImage(int viewDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int yOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
