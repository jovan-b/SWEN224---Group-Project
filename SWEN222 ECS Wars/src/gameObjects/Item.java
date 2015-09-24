package gameObjects;

import java.awt.Image;

import characters.Player;

/**
 * Represents all in-game items eg. desks, weapons, books etc
 * @author 
 *
 */
public interface Item {
	public void use(Player p);

	public Image getImage(int viewDirection);

	public boolean canWalk();
	
	public int yOffset(int viewDirection);

	public int xOffset(int viewDirection);

	public void setScaledImage(int viewDirection, Image scaledImage);
	public Image getScaledImage(int viewDirection);
}
