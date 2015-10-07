package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * An in-game menu which gives options to resume, save, or disconnect.
 * @author Sarah Dobie 300315033
 *
 */
public class WinnerMenu implements MouseListener, MouseMotionListener{
	private static final int BUTTON_WIDTH = 400;
	private static final int BUTTON_HEIGHT = 50;
	private static final int BUTTON_TOP_DIFF = BUTTON_HEIGHT*3;
	private static final int BUTTON_LEFT_DIFF = BUTTON_WIDTH/2;
	private static final int SPRITE_WIDTH = 32;
	private static final int TEXT_SIZE = BUTTON_HEIGHT/2-10;

	private GUICanvas canvas;
	private Controller controller;
	private Player[] orderedPlayers;
	private int selectedButton = Integer.MAX_VALUE;
	
	private Image[] daveImages;
	private Image[] pondyImages;
	private Image[] marcoImages;
	private Image[] streaderImages;
	private int animState = 0; // the current animation frame
	private int animModifier = 1; // flicks between 1 and -1 to loop animation
	private int animCounter = 0; // counts each frame the player has moved

	/**
	 * Constructor for class EscMenu.
	 * @param canvas The canvas to draw on
	 * @param ctrl The controller running the game
	 */
	public WinnerMenu(GUICanvas canvas, Controller ctrl){
		this.canvas = canvas;
		this.controller = ctrl;
		orderPlayers();
		loadImages();
	}

	/**
	 * Sets up the orderedPlayers field so that players are ordered by
	 * the number of points they have, from highest to lowest.
	 */
	private void orderPlayers() {
		List<Player> players = controller.getPlayers();
		orderedPlayers = new Player[players.size()];
		for(int i=0; i<orderedPlayers.length; i++){
			orderedPlayers[i] = players.get(i);
		}
		Arrays.sort(orderedPlayers, new PlayerPointComp());
	}

	/**
	 * Loads all images needed for this menu.
	 */
	private void loadImages() {
		// dave
		daveImages = new Image[3];
		try {
			for (int ani = 0; ani < 3; ani++){
				daveImages[ani] = ImageIO.read(new File("Resources"+File.separator+"Players"+File.separator+"Dave"+2+ani+".png"));
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
		// TODO other players
	}

	/**
	 * Draw the winner menu.
	 * @param g The graphics object with which to draw
	 */
	public void paint(Graphics g){
		// draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		drawResults(g);
	}

	/**
	 * Draw the buttons of this menu.
	 * @param g The graphics object with which to draw
	 */
	private void drawResults(Graphics g) {
		g.setColor(Color.WHITE);
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		// set up positions
		int gap = 20 + BUTTON_HEIGHT;
		int slotX = midX - BUTTON_LEFT_DIFF;
		int nameX = slotX + SPRITE_WIDTH;
		int slotY = midY - BUTTON_TOP_DIFF;
		// edit graphics settings
		Graphics2D g2 = ((Graphics2D)g);
		g2.setStroke(new BasicStroke(2));
		g.setColor(Color.WHITE);
		g.setFont(new Font("pixelmix", Font.PLAIN, TEXT_SIZE));
		// draw results
		for(Player p : orderedPlayers){
			g.drawRect(slotX, slotY, BUTTON_WIDTH, BUTTON_HEIGHT);
			Player.Type type = p.getType();
			Image toDraw;
			switch(type){ //TODO add other player types
			case DavePlayer : toDraw = daveImages[animState]; break;
			default : toDraw = daveImages[animState];
			}
			int nameWidth = g.getFontMetrics().stringWidth(p.getName());
			int pointsWidth = g.getFontMetrics().stringWidth(""+p.getPoints());
			g.drawImage(toDraw, slotX+10, slotY+(BUTTON_HEIGHT/2)-(SPRITE_WIDTH/2), canvas);
			g.drawString(p.getName(), midX-(nameWidth/2), slotY+TEXT_SIZE+5);
			g.drawString(""+p.getPoints(), midX-(pointsWidth/2), slotY+TEXT_SIZE*2+10);
			slotY += gap;
		}
		animate();
	}
	
	/**
	 * Increments the animation state.
	 */
	private void animate() {
		animCounter++;
		if (animCounter > 10){
			animState += animModifier;
			animCounter = 0;
			if (animState <= 0 || animState >= 2){
				animModifier *= -1;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// calculate positional values
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		int x = e.getX();
		int y = e.getY();
		int buttonX = midX - BUTTON_LEFT_DIFF;
		int buttonY = midY - BUTTON_TOP_DIFF;
		int gap = 20 + BUTTON_HEIGHT;

		Graphics g = canvas.getGraphics();

		
	}

	/**
	 * Return to the main menu
	 */
	private void mainMenu() {
		canvas.setWinnerView(false);
		canvas.setMainMenu(true);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		int x = e.getX();
		int y = e.getY();
		int buttonY = midY - BUTTON_TOP_DIFF;
		int gap = 20 + BUTTON_HEIGHT;
		int buttonLeft = midX - BUTTON_LEFT_DIFF;
		Graphics g = canvas.getGraphics();

		// check if x is within button bounds
		if(buttonLeft <= x && x < buttonLeft+BUTTON_WIDTH){
			// check which y it is on
			for(int i=0; i<orderedPlayers.length; i++){
				if(buttonY <= y && y < buttonY + BUTTON_HEIGHT){
					// found the selected button
					this.selectedButton = i;
					return;
				}
				buttonY += gap;
			}
		}
		// deselect buttons
		this.selectedButton = Integer.MAX_VALUE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	/**
	 * A simple comparator that orders players based their points,
	 * from highest to lowest.
	 * @author Sarah Dobie 300315033
	 *
	 */
	private class PlayerPointComp implements Comparator<Player>{

		@Override
		public int compare(Player a, Player b) {
			// check neither player is null
			if(a == null){
				return -1;
			} else if(b == null){
				return 1;
			}
			// get player points
			int aPoints = a.getPoints();
			int bPoints = b.getPoints();
			// compare values
			if(aPoints > bPoints){return -1;}
			else if(aPoints == bPoints){return 0;}
			else {return 1;}
		}
		
	}

}
