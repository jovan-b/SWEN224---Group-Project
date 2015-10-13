package gameWorld.gameObjects.weapons;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.weapons.projectiles.LtsaBullet;
import gameWorld.gameObjects.weapons.projectiles.PaintBall;
import gameWorld.gameObjects.weapons.projectiles.Projectile;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LtsaGun extends Weapon {
	public static final double BULLET_SPREAD = 10;
	private String description;
	
	public LtsaGun(){
		super(4, new LtsaBullet());
		this.description = "The LTSA Gun. Effectively ineffective.";
		this.name = "LTSA Gun";
		loadImages();
	}
	
	private void loadImages(){
		try{
			super.image = ImageIO.read(LtsaGun.class.getResource("/Items/gun3.png"));
			super.scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading weapon image file: "+e.getMessage());
		}
	}
	
	@Override
	public Projectile fire(Player p, double theta){
		double spread = Math.toRadians((Math.random()*BULLET_SPREAD)-BULLET_SPREAD/2);
		if (this.canFire && projectile instanceof LtsaBullet){
			((LtsaBullet)projectile).nextState();
		}
		return super.fire(p, theta+spread);
	}

	@Override
	public void use(Player p, Controller ctrl) {}

	@Override
	public Image getImage(int viewDirection) {
		return super.image;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int yOffset(int viewDirection) {
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		super.scaledImage = scaledImage;
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return super.scaledImage;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public WeaponType getWeaponType() {
		return WeaponType.LTSAGun;
	}

}
