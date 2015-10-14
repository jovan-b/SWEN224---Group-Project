package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.PointValues;
import gameWorld.characters.Player;

/**
 * A class representing a buyable health recover item
 * @author Sarah Dobie 300315033
 *
 */
public class PillBottle extends Sellable implements Item {
	
	private String description = "Miracle pill: Restores all your health.";
	private int health = Player.HEALTH_MAX;
	private Image image;
	private Image scaledImage;
	
	public PillBottle(){
		super(PointValues.PILL_COST);
		loadImages();
	}

	private void loadImages() {
		try{
			image = ImageIO.read(PillBottle.class.getResource("/Items/PillBottle.png"));
			scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading image file: "+e.getMessage());
		}
	}

	@Override
	public void use(Player p, Controller ctrl) {
		p.modifyHealth(health, null);
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
		return Type.PillBottle;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Item){
			return this.getType() == ((Item) o).getType();
		}
		return false;
	}
}

