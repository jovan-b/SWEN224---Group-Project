package gameObjects;

import java.awt.Image;


/**
 * A blank item used for room boundaries - stops player movement
 * @author Chris Read
 *
 */
public class Wall implements Item {

	public Wall() {
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
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int yOffset(int viewDirection) {
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
