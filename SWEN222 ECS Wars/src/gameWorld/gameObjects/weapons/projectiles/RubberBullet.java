package gameWorld.gameObjects.weapons.projectiles;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.characters.Player;

public class RubberBullet extends ReflectingProjectile {
	public static final int MAX_BOUNCES = 3;
	
	private Image image;
	private Image largeImage;
	private int bulletSize = 3;
	
	public RubberBullet(){
		super(MAX_BOUNCES);
		try {
			image = ImageIO.read(RubberBullet.class.getResource("/Projectiles/RedBullet.png"));
			largeImage = image.getScaledInstance(bulletSize*2, bulletSize*2, Image.SCALE_FAST);
		} catch (IOException e) {
			System.out.println("Error loading projectile image: " + e.getMessage());
		}
	}
	
	private RubberBullet(Player p, int x, int y, double theta, Image image, Image largeImage){
		super(p, x, y, theta, MAX_BOUNCES);
		this.setSpeedMultiplier(0.8);
		this.image = image;
		this.largeImage = largeImage;
	}
	
	@Override
	public Projectile newInstance(Player p, double theta) {
		return new RubberBullet(p, p.getX(), p.getY(), theta, image, largeImage);
	}
	
	@Override
	protected void playerCollision(Player p){
		//if (p == this.player){return;} //Uncomment this to stop damaging owner player
		if (p == this.player && bounces == MAX_BOUNCES){
			return;
		}
		
		p.modifyHealth(damage, this);
		this.setActive(false);
	}

	@Override
	public Image getImage(int scale) {
		if (scale >= 2){
			return largeImage;
		} else {
			return image;
		}
	}

	@Override
	public int getSize() {
		return bulletSize;
	}

}
