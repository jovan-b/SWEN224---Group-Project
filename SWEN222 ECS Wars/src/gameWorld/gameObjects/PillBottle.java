package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;

public class PillBottle implements Item {
	
	private String description = "Miracle pill: Restores all your health.";
	private int cost = 2000;
	private int health = Player.HEALTH_MAX/2;
	private Image image;
	private Image scaledImage;
	
	public PillBottle(){
		loadImages();
	}

	private void loadImages() {
		try{
			image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"PillBottle.png"));
			scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading KeyCard file: "+e.getMessage());
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		p.setHealth(health);
		p.removeItem(this);
	}

	@Override
	public Image getImage(int viewDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canWalk() {
		return true;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

