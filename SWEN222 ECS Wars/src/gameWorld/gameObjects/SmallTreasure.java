package gameWorld.gameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.PointValues;
import gameWorld.characters.Player;

/**
 * A 1x1 treasure worth points that can be found on floors and in containers.
 * 
 * @author Sarah Dobie 300315033
 *
 */
public class SmallTreasure implements Item {

	private Image image;
	private Image scaledImage;
	private String description;
	private int points;
	
	/**
	 * Constructor for class KeyCard.
	 */
	public SmallTreasure(){
		double randomQuality = Math.random();
		double randomType = Math.random();
		String quality = "rough";
		String type = "Sapphire";
		points = PointValues.TREASURE_BASE;
		if (randomType > 0.9){
			type = "Diamond";
			points += PointValues.TREASURE_BASE*0.95;
		} else if (randomType > 0.7){
			type = "Ruby";
			points += PointValues.TREASURE_BASE*0.7;
		} else if (randomType > 0.4){
			type = "Emerald";
			points += PointValues.TREASURE_BASE*0.4;
		} 
		
		if (randomQuality > 0.9){
			quality = "exquisite";
			points += PointValues.TREASURE_BASE*0.95;
		} else if (randomQuality > 0.7){
			quality = "rare";
			points += PointValues.TREASURE_BASE*0.7;
		} else if (randomQuality > 0.4){
			quality = "good";
			points += PointValues.TREASURE_BASE*0.4;
		}
		
		this.description = "A "+ type + " of " + quality + " quality worth " + points + " points";
		loadImages(type);
	}

	/**
	 * Parse and store all required images.
	 */
	private void loadImages(String fileName) {
		try{
			image = ImageIO.read(new File("Resources"+File.separator+"Items"+File.separator+fileName+".png"));
			scaledImage = image;
		} catch(IOException e){
			System.out.println("Error loading treasure image file: "+e.getMessage());
		}
	}
	
	@Override
	public void use(Player p, Controller ctrl) {}

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

	public int getPoints() {
		return points;
	}

	@Override
	public Type getType() {
		return Type.SmallTreasure;
	}
}
