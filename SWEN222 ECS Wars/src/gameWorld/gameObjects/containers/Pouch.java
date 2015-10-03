package gameWorld.gameObjects.containers;

import java.awt.Image;

import gameWorld.Controller;
import gameWorld.characters.Player;

public class Pouch extends Container {

	public Pouch(int size) {
		super(size);
		loadImages();
	}

	private void loadImages() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void use(Player p, Controller ctrl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getImage(int viewDirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canWalk() {
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
