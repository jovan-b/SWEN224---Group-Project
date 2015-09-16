package main;

import gameObjects.Room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * The main canvas inside the game window in which the game is drawn.
 * 
 * @author Sarah Dobie, Chris Read
 *
 */
public class GUICanvas extends JComponent{
	
	private GUIFrame frame;
	
	public GUICanvas(GUIFrame frame){
		this.frame = frame;
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(GUIFrame.INIT_WIDTH, GUIFrame.INIT_HEIGHT);
	}
	
	/**
	 * Draws all the graphics on the screen
	 * Mainly calls each objects own draw method
	 */
	@Override
	public void paint(Graphics g){
		//paint background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		Room r = new Room("EastBase.png", "EastTop.png"); //FIXME get passed the client-side room
		r.draw(g, this);
	}
}
