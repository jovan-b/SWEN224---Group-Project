package gameWorld.gameObjects;

import java.awt.Image;

import gameWorld.Controller;
import gameWorld.characters.Player;


/**
 * A blank item used for room boundaries - stops player movement
 * 
 * @author Chris Read 300254724
 *
 */
public class Wall implements Item {

	@Override
	public void use(Player p, Controller ctrl) {
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

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Type getType() {
		return null;
	}

}
