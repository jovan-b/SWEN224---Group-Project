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
	
	private String toolTip;
	private int toolTipX;
	private int toolTipY;
	
	private int viewScale = 1; // view drawing scale
	
	private boolean mainMenuView; // true if we are looking at main menu
	private MainMenu mainMenu;
	
	// Static UI Images
	private Image compassControls;
	
	/**
	 * Constructor for class GUICanvas.
	 * @param frame The frame containing this canvas
	 * @param player The current client's player.
	 */
	public GUICanvas(GUIFrame frame, Player player){
		this.frame = frame;
		this.player = player;
		this.compass = new Compass();
		this.toolTip = null;
		
		this.player.setCompass(compass);
		
		try {
			compassControls = ImageIO.read(new File("Resources"+File.separator+"CompassControls.png"));
		} catch (IOException e) {
			System.out.println("Error loading UI Images: " + e.getMessage());
		}
		
		this.mainMenu = new MainMenu(this);
		setMainMenu(true);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(GUIFrame.INIT_WIDTH, GUIFrame.INIT_HEIGHT);
	}
	
	@Override
	public void paint(Graphics g){
		if(mainMenuView){
			mainMenu.paint(g);
			return;
		}
		//paint background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		Room r = player.getCurrentRoom();
		r.draw(g, this, player);
		drawHUD(g, r);
	}

	/**
	 * Draws the heads up display, ie. UI components like health, score etc.
	 * @param g The graphics object with which to draw
	 * @param r The room to draw
	 */
	private void drawHUD(Graphics g, Room r) {
		// Draw compass
		compass.update();
		g.drawImage(compass.getImage(), getWidth()-96-20, 20, this);
		g.drawImage(compassControls, getWidth()-96-20, 20, this);
		if (toolTip != null){
			showToolTip(g);
		}
	}
	
	/**
	 * Gets the current view scale of this canvas.
	 * @return The current view scale: either 1 or 2
	 */
	public int getViewScale() {
		return viewScale;
	}

	/**
	 * Sets the current view scale.
	 * @param viewScale The new view scale: either 1 or 2.
	 */
	public void setViewScale(int viewScale) {
		this.viewScale = viewScale;
	}

	public void setToolTip(String toolTip, int x, int y) {
		this.toolTip = toolTip;
		this.toolTipX = x;
		this.toolTipY = y;
	}
	
	private void showToolTip(Graphics g){
		// determine which of line1 and line2 is shorter in pixels
		int lineWidth = g.getFontMetrics().stringWidth(toolTip);
		// set up variables for drawing
		int boxX = toolTipX+20;
		int boxY = toolTipY+5;
		int boxWidth = lineWidth+10;
		int lineX = boxX+5;
		int lineY = boxY+15;
		// check if box is too far to right to draw
		if(toolTipX > frame.getWidth() - lineWidth){ // mouse is too far to the right
			boxX -= lineWidth;
			lineX -= lineWidth;
		}
		// check if box is too close to the bottom to draw
		if(toolTipY > frame.getHeight() - 20){ // mouse too far down
			boxY -= 20;
			lineY -= 20;
		}
		// draw the tooltip
		g.setColor(Color.WHITE);
		g.fillRect(boxX, boxY, boxWidth, 20);
		g.setColor(Color.BLACK);
		g.drawRect(boxX, boxY, boxWidth, 20);
		g.drawString(toolTip, lineX, lineY);
	}
	
	/**
	 * Gets the current player.
	 * @return the current player
	 */
	public Player getCurrentPlayer(){
		return player;
	}

	/**
	 * Opens or hides the main menu.
	 * @param isMainMenu true to show the main menu, false otherwise
	 */
	public void setMainMenu(boolean isMainMenu) {
		this.mainMenuView = isMainMenu;
		if(isMainMenu){
			addMouseListener(mainMenu);
			addMouseMotionListener(mainMenu);
		} else {
			removeMouseListener(mainMenu);
			removeMouseMotionListener(mainMenu);
		}
	}
}
