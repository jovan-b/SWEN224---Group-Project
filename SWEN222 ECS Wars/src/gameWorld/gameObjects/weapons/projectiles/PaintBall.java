package gameWorld.gameObjects.weapons.projectiles;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.characters.Player;

public class PaintBall extends BasicProjectile{
	
	private Image image;
	private Image largeImage;
	private int bulletSize = 3;
	
	public PaintBall(){
		super();
		try {
			image = ImageIO.read(new File("Resources"+File.separator+"Projectiles"+File.separator+"RedBullet.png"));
			largeImage = image.getScaledInstance(bulletSize*2, bulletSize*2, Image.SCALE_FAST);
		} catch (IOException e) {
			System.out.println("Error loading projectile image: " + e.getMessage());
		}
	}
	
	private PaintBall(Player p, int x, int y, double theta, Image image, Image largeImage){
		super(p, x, y, theta);
	}

	@Override
	public Projectile newInstance(Player p, double theta) {
		return new PaintBall(p, p.getX(), p.getY(), theta, image, largeImage);
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