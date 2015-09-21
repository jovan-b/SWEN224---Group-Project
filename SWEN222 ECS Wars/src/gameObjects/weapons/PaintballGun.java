package gameObjects.weapons;

import gameObjects.weapons.projectiles.PaintBall;

import java.awt.Graphics;
import java.awt.Image;

import main.GUICanvas;

public class PaintballGun extends Weapon {
	
	public PaintballGun(){
		super(1, new PaintBall());
	}

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
	public int yOffset() {
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
	public void draw(Graphics g, GUICanvas c) {
		// TODO Auto-generated method stub

	}

}
