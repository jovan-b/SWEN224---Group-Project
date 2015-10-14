package gameWorld.gameObjects.weapons;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.weapons.projectiles.Projectile;
import gameWorld.gameObjects.weapons.projectiles.RubberBullet;

public class ScatterGun extends Weapon{
	public static final double FIRE_RATE = 1.5;
	public static final int MAX_SHOTS = 5;
	public static final int MIN_SHOTS = 2;
	public static final double MAX_SPREAD = Math.toRadians(30);
	
	private String description;

	public ScatterGun() {
		super(FIRE_RATE, new RubberBullet());
		this.description = "A Scatter Gun. Watch out for ricochets.";
		this.name = "Scatter Gun";
		loadImages();
	}
	
	private void loadImages(){
		try{
			super.image = ImageIO.read(ScatterGun.class.getResource("/Items/gun2.png"));
			super.scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading weapon image file: "+e.getMessage());
		}
	}
	
	@Override
	public Projectile fire(Player p, double theta){
		if (canFire){
			int max = (int)(Math.random()*(MAX_SHOTS-MIN_SHOTS))+MIN_SHOTS;
			for (int i = 0; i < max; i++){
				double t = theta+(Math.random()*MAX_SPREAD - MAX_SPREAD/2);
				p.getCurrentRoom().addProjectile(projectile.newInstance(p, t));
			}
		}
		
		return super.fire(p, theta+(Math.random()*MAX_SPREAD - MAX_SPREAD/2));
	}

	@Override
	public void use(Player p, Controller ctrl) {}

	@Override
	public int yOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public Image getImage(int viewDirection) {
		return super.image;
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
		return WeaponType.ScatterGun;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
		super.scaledImage = scaledImage;
	}

}
