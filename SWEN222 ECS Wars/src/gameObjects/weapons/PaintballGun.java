package gameObjects.weapons;

import gameObjects.weapons.projectiles.Projectile;
import gameObjects.weapons.projectiles.RubberBullet;

import java.awt.Image;

import characters.Player;

public class PaintballGun extends Weapon {
	public static final double BULLET_SPREAD = 10;
	
	public PaintballGun(){
		super(4, new RubberBullet());
	}
	
	@Override
	public Projectile fire(Player p, double theta){
		double spread = Math.toRadians((Math.random()*BULLET_SPREAD)-BULLET_SPREAD/2);
		return super.fire(p, theta+spread);
	}

	@Override
	public void use(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(int viewDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int xOffset(int viewDirection) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int yOffset(int viewDirection) {
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
