package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * An in-game menu which gives options to resume, save, or disconnect.
 * @author Sarah Dobie 300315033
 *
 */
public class WinnerView implements MouseListener, MouseMotionListener{
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_TOP_DIFF = BUTTON_HEIGHT*2;
	private static final int BUTTON_LEFT_DIFF = BUTTON_HEIGHT/2;
	private static final int TEXT_SIZE = 25;

	private GUICanvas canvas;
	private Controller controller;
	private Player[] orderedPlayers;
	private int selectedButton = Integer.MAX_VALUE;

	/**
	 * Constructor for class EscMenu.
	 * @param canvas The canvas to draw on
	 * @param ctrl The controller running the game
	 */
	public WinnerView(GUICanvas canvas, Controller ctrl){
		this.canvas = canvas;
		this.controller = ctrl;
		orderPlayers();
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
	 * Draw the winner menu.
	 * @param g The graphics object with which to draw
	 */
	public void paint(Graphics g){
		// draw background
		g.setColor(new Color(0f,0f,0f,0.6f));
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		drawButtons(g);
	}

	/**
	 * Draw the buttons of this menu.
	 * @param g The graphics object with which to draw
	 */
	private void drawButtons(Graphics g) {
		g.setColor(Color.WHITE);
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		int gap = 20 + BUTTON_HEIGHT;
		int buttonX = midX - BUTTON_LEFT_DIFF;
		int buttonY = midY - BUTTON_TOP_DIFF;
		
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
		canvas.toggleWinnerView();
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
