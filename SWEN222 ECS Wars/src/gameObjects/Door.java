package gameObjects;

import java.awt.Image;

public class Door implements Item {

	@Override
	public void use() {
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
	
}
