package gameObjects;

import java.awt.Graphics;
import java.awt.Image;

import main.GUICanvas;


/**
 * A blank item used for room boundaries - stops player movement
 * @author Chris Read
 *
 */
public class Wall implements Item {

	public Wall() {
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
		return false;
	}

	@Override
	public int yOffset() {
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return null;
	}

}
