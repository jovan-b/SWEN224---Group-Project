package gameWorld.gameObjects.weapons;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.weapons.Weapon.WeaponType;
import gameWorld.gameObjects.weapons.projectiles.PistolBullet;
import gameWorld.gameObjects.weapons.projectiles.Projectile;

public class Pistol extends Weapon {
	public static final double BULLET_SPREAD = 5;
	private String description;
	
	public Pistol(){
		super(1, new PistolBullet());
		this.description = "A very big Pistol. Made of Retributium.";
		this.name = "Pistol";
		loadImages();
	}
	
	private void loadImages(){
		try{
			super.image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"gun0.png"));
			super.scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading weapon image file: "+e.getMessage());
		}
	}
	
	@Override
	public Projectile fire(Player p, double theta){
		double spread = Math.toRadians((Math.random()*BULLET_SPREAD)-BULLET_SPREAD/2);
		return super.fire(p, theta+spread);
	}

	@Override
	public void use(Player p, Controller ctrl) {}

	@Override
	public Image getImage(int viewDirection) {
		return super.image;
	}

	@Override
	public int yOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int xOffset(int viewDirection) {
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
		return WeaponType.Pistol;
	}

}
