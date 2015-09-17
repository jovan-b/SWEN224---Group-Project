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
	public Image getImage() {
		return null;
	}

	@Override
	public boolean canWalk() {
		return false;
	}

}
