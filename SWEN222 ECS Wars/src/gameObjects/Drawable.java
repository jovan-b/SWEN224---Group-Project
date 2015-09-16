package gameObjects;

import java.awt.Graphics;

import main.GUICanvas;

/**
 * An interface to represent an object which can be drawn on the canvas.
 * @author Sarah Dobie, Chris Read
 *
 */
public interface Drawable {
	/**
	 * Draws this object on the given canvas.
	 * @param g The graphics object to draw with
	 * @param c The canvas to draw on
	 */
	public void draw(Graphics g, GUICanvas c); //TODO probably needs to be passed player - to know view rotation
	
}
