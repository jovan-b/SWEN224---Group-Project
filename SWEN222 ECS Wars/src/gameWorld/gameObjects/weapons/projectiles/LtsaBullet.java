package gameWorld.gameObjects.weapons.projectiles;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.characters.Player;

/**
 * A large projectile which resembles an LTSA state.
 * 
 * @author Sarah Dobie 300315033
 *
 */
public class LtsaBullet extends BasicProjectile{
	
	private Image[] images;
	private Image[] largeImages;
	private int bulletSize = 9;
	private int state;
	
	/**
	 * Constructor for class LtsaBullet.
	 */
	public LtsaBullet(){
		super();
		images = new Image[4];
		largeImages = new Image[4];
		state = 0;
		try {
			for(int i=0; i<images.length; i++){
				largeImages[i] = ImageIO.read(LtsaBullet.class.getResource("/Projectiles/ltsa_"+(i-1)+".png"));
				images[i] = largeImages[i].getScaledInstance(bulletSize, bulletSize, Image.SCALE_FAST);
			}
		} catch (IOException e) {
			System.out.println("Error loading projectile image: " + e.getMessage());
		}
	}
	
	/**
	 * Private constructor used for making a clone.
	 */
	private LtsaBullet(Player p, int x, int y, int state, double theta, Image[] images, Image[] largeImages){
		super(p, x, y, theta);
		this.setSpeedMultiplier(0.5);
		this.state = state;
		this.images = images;
		this.largeImages = largeImages;
	}

	@Override
	public Projectile newInstance(Player p, double theta) {
		return new LtsaBullet(p, p.getX(), p.getY(), state, theta, images, largeImages);
	}

	@Override
	public Image getImage(int scale) {
		if (scale >= 2){
			return largeImages[state];
		} else {
			return images[state];
		}
	}

	@Override
	public int getSize() {
		return bulletSize;
	}
	
	/**
	 * Returns an incremented version of the state field or wraps it around to zero
	 * if it reaches maximum state.
	 * @return The next animation state of a bullet.
	 */
	public int nextState(){
		int newState = state+1;
		if(newState >= images.length){
			newState = 0;
		}
		state = newState;
		return newState;
	}
	
}
