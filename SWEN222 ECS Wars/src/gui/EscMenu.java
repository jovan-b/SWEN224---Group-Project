package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.saveAndLoad.SaveManager;
import gameWorld.Controller;

/**
 * An in-game menu which gives options to resume, save, or disconnect.
 * @author Sarah Dobie 300315033
 *
 */
public class EscMenu implements MouseListener, MouseMotionListener{
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_TOP_DIFF = BUTTON_HEIGHT*2;
	private static final int BUTTON_LEFT_DIFF = BUTTON_HEIGHT/2;
	private static final int TEXT_SIZE = 25;

	private GUICanvas canvas;
	private Controller controller;
	private String[] buttonLabels;
	private int selectedButton = Integer.MAX_VALUE;

	/**
	 * Constructor for class EscMenu.
	 * @param canvas The canvas to draw on
	 * @param ctrl The controller running the game
	 */
	public EscMenu(GUICanvas canvas, Controller ctrl){
		this.canvas = canvas;
		this.controller = ctrl;
		buttonLabels = new String[]{"Resume", "Save", "Disconnect"};
	}

	/**
	 * Draw the escape menu.
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
		// draw buttons
		for(int i=0; i<buttonLabels.length; i++){
			g.drawRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
			// highlight the button hovered over
			if(i == this.selectedButton){
				g.setColor(new Color(255, 255, 255, 128));
				g.fillRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
			}
			// draw text
			g.setColor(Color.WHITE);
			g.setFont(new Font("pixelmix", Font.PLAIN, BUTTON_HEIGHT-10));
			int textWidth = g.getFontMetrics().stringWidth(buttonLabels[i]);
			int labelX = (int) (buttonX + BUTTON_WIDTH/2 - (double)textWidth/2.0);
			int labelY = buttonY + BUTTON_HEIGHT/2 + TEXT_SIZE/4;
			g.drawString(buttonLabels[i], labelX, labelY);
			// increment y
			buttonY += gap;
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

		// check if x is within button bounds
		if(buttonX <= x && x < buttonX+BUTTON_WIDTH){
			// check which y it is on
			for(int i=0; i<buttonLabels.length; i++){
				// check if we are on a button
				if(buttonY <= y && y < buttonY + BUTTON_HEIGHT){
					// figure out the button we're on
					switch(i){
					case 0 : resume(); break;
					case 1 : saveGame(); break;
					case 2 : disconnect(); break;
					}
				}
				// increment y
				buttonY += gap;
			}
		}
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
			for(int i=0; i<buttonLabels.length; i++){
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

	private void resume() {
		canvas.toggleEscMenu();
	}

	private void saveGame() {
		//TODO: change so player can specify name
		SaveManager.saveGame(controller, "test_save.xml");
	}

	private void disconnect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

}
