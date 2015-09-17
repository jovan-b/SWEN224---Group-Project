package gameObjects;

import java.awt.Image;

/**
 * Represents all in-game items eg. desks, weapons, books etc
 * @author 
 *
 */
public interface Item extends Drawable {
	public void use();

	public Image getImage();

	public boolean canWalk();
}
