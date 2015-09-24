package gameObjects.containers;

import java.awt.Graphics;
import java.awt.Image;

import main.GUICanvas;

/**
 * A backpack which represents the player's inventory
 * Backpack should be able to hold 10 items
 * 
 * @author Jah Seng Lee
 *
 */
public class Backpack extends Container {
	
	public static final int SIZE = 10;
	
	public Backpack(){
		super(SIZE);
	}
	
	@Override
	public void use() {
		
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int xOffset(int viewDirection) {
		// TODO Auto-generated method stub
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
}
