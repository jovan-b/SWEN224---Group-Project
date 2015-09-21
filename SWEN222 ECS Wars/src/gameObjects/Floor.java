package gameObjects;

import java.awt.Graphics;
import java.awt.Image;

import main.GUICanvas;


/**
 * A blank item used for empty squares - player can walk over
 * @author Chris Read
 *
 */
public class Floor implements Item {

	public Floor() {
	}

	@Override
	public void draw(Graphics g, GUICanvas c) {
	}

	@Override
	public void use() {
	}

	@Override
	public Image getImage(int viewDirection) {
		return null;
	}

	@Override
	public boolean canWalk() {
		return true;
	}

	@Override
	public int yOffset() {
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaleImage) {
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return null;
	}

}