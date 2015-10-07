package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Item.Type;

public class MedicineBottle extends Sellable implements Item {
	
	private String description = "Medicine: Restores half your health.";
	private int health = Player.HEALTH_MAX/2;
	private Image image;
	private Image scaledImage;
	
	public MedicineBottle(){
		super(500);
		loadImages();
	}

	private void loadImages() {
		try{
			image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+"Medicine.png"));
			scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading image file: "+e.getMessage());
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		p.modifyHealth(health,null);
		p.removeItem(this);
	}

	@Override
	public Image getImage(int viewDirection) {
		return image;
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
		this.scaledImage = scaledImage;
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return scaledImage;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public Type getType() {
		return Type.MedicineBottle;
	}

}
