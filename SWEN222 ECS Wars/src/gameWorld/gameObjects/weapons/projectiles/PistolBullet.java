package gameWorld.gameObjects.weapons.projectiles;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.characters.Player;

/**
 * A bullet for the Pistol gun. High speed and damage
 * @author Chris Read 300254724
 *
 */
public class PistolBullet extends BasicProjectile{
	
	private Image image;
	private Image largeImage;
	private int bulletSize = 3;
	
	/**
	 * The constructor for the PistolBullet Class
	 */
	public PistolBullet(){
		super();
		try {
			image = ImageIO.read(PistolBullet.class.getResource("/Projectiles/BlackBullet.png"));
			largeImage = image.getScaledInstance(bulletSize*2, bulletSize*2, Image.SCALE_FAST);
		} catch (IOException e) {
			System.out.println("Error loading projectile image: " + e.getMessage());
		}
	}
	
	/**
	 * Alternate constructor used for making a clone of this bullet
	 */
	private PistolBullet(Player p, int x, int y, double theta, Image image, Image largeImage){
		super(p, x, y, theta);
		this.setSpeedMultiplier(3.0);
		this.damage = -61;
		this.image = image;
		this.largeImage = largeImage;
	}

	/**
	 * Returns a new instance of this type of bullet
	 */
	@Override
	public Projectile newInstance(Player p, double theta) {
		return new PistolBullet(p, p.getX(), p.getY(), theta, image, largeImage);
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
