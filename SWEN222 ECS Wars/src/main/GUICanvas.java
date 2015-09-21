package main;

import gameObjects.Compass;
import gameObjects.Room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import characters.Player;

/**
 * The main canvas inside the game window in which the game is drawn.
 * 
 * @author Sarah Dobie, Chris Read
 *
 */
@SuppressWarnings("serial")
public class GUICanvas extends JComponent{
	
	private GUIFrame frame;
	
	private Player player;
	private Compass compass;
	
	private int viewScale = 1; // view drawing scale
	
	// Static UI Images
	private Image compassControls;
	
	public GUICanvas(GUIFrame frame, Player player){
		this.frame = frame;
		this.player = player;
		this.compass = new Compass();
		
		this.player.setCompass(compass);
		
		try {
			compassControls = ImageIO.read(new File("Resources"+File.separator+"CompassControls.png"));
		} catch (IOException e) {
			System.out.println("Error loading UI Images: " + e.getMessage());;
		}
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
		Room r = player.getCurrentRoom();
		r.draw(g, this, player);
		drawHUD(g, this, r);
	}

	private void drawHUD(Graphics g, GUICanvas guiCanvas, Room r) {
		// Draw compass
		compass.update();
		g.drawImage(compass.getImage(), getWidth()-96-20, 20, this);
		g.drawImage(compassControls, getWidth()-96-20, 20, this);
	}
	
	public int getViewScale() {
		return viewScale;
	}

	public void setViewScale(int viewScale) {
		this.viewScale = viewScale;
	}
}
