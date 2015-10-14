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
	private static final int SLOT_WIDTH = 400;
	private static final int SLOT_HEIGHT = 50;
	private static final int SLOT_TOP_DIFF = SLOT_HEIGHT*3;
	private static final int SLOT_LEFT_DIFF = SLOT_WIDTH/2;
	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_BOT_DIFF = BUTTON_HEIGHT*2;
	private static final int SPRITE_WIDTH = 32;
	private static final int TEXT_SIZE = SLOT_HEIGHT/2-10;

	private GUICanvas canvas;
	private Controller controller;
	private Player[] orderedPlayers;
	private boolean buttonSelected = false;
	
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
				daveImages[ani] = ImageIO.read(WinnerMenu.class.getResource("/Players/Dave"+2+ani+".png"));
				pondyImages[ani] = ImageIO.read(WinnerMenu.class.getResource("/Players/Pondy"+2+ani+".png"));
				marcoImages[ani] = ImageIO.read(WinnerMenu.class.getResource("/Players/Marco"+2+ani+".png"));
				streaderImages[ani] = ImageIO.read(WinnerMenu.class.getResource("/Players/Streader"+2+ani+".png"));
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
	}

	/**
	 * Draw the winner menu.
	 * @param g The graphics object with which to draw
	 */
	public void paint(Graphics g){
		// draw background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// draw title
		g.setColor(Color.WHITE);
		g.setFont(new Font("pixelmix", Font.PLAIN, TEXT_SIZE*2));
		String title = "GAME OVER";
		int nameWidth = g.getFontMetrics().stringWidth(title);
		g.drawString(title, canvas.getWidth()/2-nameWidth/2, 50);
		drawResults(g);
		drawButton(g);
	}

	/**
	 * Draws the button that returns user to main menu.
	 * @param g The graphics object with which to draw
	 */
	private void drawButton(Graphics g) {
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		int buttonX = midX - BUTTON_WIDTH/2;
		int buttonY = canvas.getHeight() - BUTTON_BOT_DIFF;
		g.setFont(new Font("pixelmix", Font.PLAIN, TEXT_SIZE));
		// edit graphics settings
		Graphics2D g2 = ((Graphics2D)g);
		g2.setStroke(new BasicStroke(1));
		g.setColor(Color.WHITE);
		String buttonText = "Main Menu";
		int textWidth = g.getFontMetrics().stringWidth(buttonText);
		g.drawRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
		g.drawString(buttonText, buttonX+(BUTTON_WIDTH/2)-(textWidth/2), buttonY+(BUTTON_HEIGHT/2)+(TEXT_SIZE/2));
		if(buttonSelected){
			g.setColor(new Color(255, 255, 255, 128));
			g.fillRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
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
		int gap = 20 + SLOT_HEIGHT;
		int slotX = midX - SLOT_LEFT_DIFF;
		int nameX = slotX + SPRITE_WIDTH;
		int slotY = midY - SLOT_TOP_DIFF;
		// edit graphics settings
		Graphics2D g2 = ((Graphics2D)g);
		g2.setStroke(new BasicStroke(2));
		g.setColor(Color.WHITE);
		g.setFont(new Font("pixelmix", Font.PLAIN, TEXT_SIZE));
		// draw results
		for(Player p : orderedPlayers){
			drawPlayerInfo(g, midX, slotX, slotY, p);
			slotY += gap;
		}
		animate();
	}

	private void drawPlayerInfo(Graphics g, int midX, int slotX, int slotY, Player p) {
		// draw border
		g.drawRect(slotX, slotY, SLOT_WIDTH, SLOT_HEIGHT);
		// determine which player type to draw
		Player.PlayerType type = p.getType();
		Image toDraw;
		switch(type){
		case PondyPlayer : toDraw = pondyImages[animState]; break;
		case MarcoPlayer : toDraw = marcoImages[animState]; break;
		case StreaderPlayer : toDraw = streaderImages[animState]; break;
		default : toDraw = daveImages[animState];
		}
		// draw player name and points
		int nameWidth = g.getFontMetrics().stringWidth(p.getName());
		int pointsWidth = g.getFontMetrics().stringWidth(""+p.getPoints());
		g.drawString(p.getName(), midX-(nameWidth/2), slotY+TEXT_SIZE+5);
		g.drawString(""+p.getPoints(), midX-(pointsWidth/2), slotY+TEXT_SIZE*2+10);
		// draw player images
		g.drawImage(toDraw, slotX+10, slotY+(SLOT_HEIGHT/2)-(SPRITE_WIDTH/2), canvas);
		g.drawImage(toDraw, slotX+SLOT_WIDTH-SPRITE_WIDTH-10, slotY+(SLOT_HEIGHT/2)-(SPRITE_WIDTH/2), canvas);
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
		if(buttonSelected){
			buttonSelected = false;
			mainMenu();
		}
	}

	/**
	 * Return to the main menu
	 */
	private void mainMenu() {
		canvas.setMainMenu(true);
		canvas.setWinnerView(false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int buttonY = canvas.getHeight() - BUTTON_BOT_DIFF;
		int buttonX = midX - BUTTON_WIDTH/2;

		// check if x is within button bounds
		if(buttonX <= x && x < buttonX + BUTTON_WIDTH){
			// check which y it is on
			if(buttonY <= y && y < buttonY + BUTTON_HEIGHT){
				// found the selected button
				this.buttonSelected = true;
				return;
			}
		}
		// deselect button
		this.buttonSelected = false;
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
