package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Controller;
import gameWorld.SinglePlayerController;
import gameWorld.characters.DavePlayer;
import gameWorld.characters.MarcoPlayer;
import gameWorld.characters.Player;
import gameWorld.characters.PondyPlayer;
import gameWorld.characters.StreaderPlayer;

/**
 * A menu which allows the player to select which character they want to play as.
 * 
 * @author Sarah Dobie 300315033
 * @author Carl Anderson 300264124
 *
 */
public class PlayerSelectMenu implements MouseListener, MouseMotionListener {

	private static final int BUTTON_WIDTH = 200;
	private static final int BUTTON_HEIGHT = 120;
	private static final int BUTTON_TOP_DIFF = BUTTON_HEIGHT;
	private static final int BUTTON_LEFT_DIFF = BUTTON_WIDTH;
	private static final int TEXT_SIZE = 15;

	private GUICanvas canvas; // the canvas this draws on
	private Controller controller;
	private Image[] spritesDave; // the sprite images to animate
	private Image[] spritesPondy; // the sprite images to animate
	private Image[] spritesMarco; // the sprite images to animate
	private Image[] spritesStreader; // the sprite images to animate
	private int animState = 0; // the current animation frame
	private int animModifier = 1; // flicks between 1 and -1 to loop animation
	private int animCounter = 0; // counts each frame the player has moved
	private String[][] buttonLabels; // the button text
	private int selectedButtonRow = Integer.MAX_VALUE; // the button row currently highlighted
	private int selectedButtonCol = Integer.MAX_VALUE; // the button col currently highlighted
	private boolean loading; // true if the game is currently loading


	/**
	 * Constructor for class PlayerSelectMenu.
	 * @param canvas The GUICanvas the menu will be drawn on
	 */
	public PlayerSelectMenu(GUICanvas canvas) {
		this.canvas = canvas;
		this.controller = new SinglePlayerController();
		loadImages();
		loadFonts();
		buttonLabels = new String[][]{{"David Pearce", "Peter Andrae"},
			{"Marco Servetto", "David Streader"}};
	}

	/**
	 * Loads all required fonts.
	 */
	private void loadFonts() {
		try {
			GraphicsEnvironment ge = 
					GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, PlayerSelectMenu.class.getResourceAsStream("/pixelmix.ttf")));
		} catch (IOException|FontFormatException e) {
			System.out.println("Error loading fonts : "+e.getMessage());
		}
	}

	/**
	 * Loads all necessary images.
	 */
	private void loadImages() {
		// Load sprites
		spritesDave = new Image[3];
		spritesPondy = new Image[3];
		spritesMarco = new Image[3];
		spritesStreader = new Image[3];
		try {
			for (int ani = 0; ani < 3; ani++){
				spritesDave[ani] = ImageIO.read(
						PlayerSelectMenu.class.getResource("/Players/Dave"+2+ani+".png"));
				spritesPondy[ani] = ImageIO.read(
						PlayerSelectMenu.class.getResource("/Players/Pondy"+2+ani+".png"));
				spritesMarco[ani] = ImageIO.read(
						PlayerSelectMenu.class.getResource("/Players/Marco"+2+ani+".png"));
				spritesStreader[ani] = ImageIO.read(
						PlayerSelectMenu.class.getResource("/Players/Streader"+2+ani+".png"));
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
	}

	/**
	 * Displays the menu.
	 * @param g The Graphics object with which to draw the menu
	 */
	public void paint(Graphics g){
		// paint background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// draw the buttons
		drawMenuItems(g);
	}

	/**
	 * Draws the title and buttons.
	 * @param g The Graphics object with which to draw
	 */
	private void drawMenuItems(Graphics g){
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		// change graphics settings
		g.setColor(Color.WHITE);
		Graphics2D g2 = ((Graphics2D)g);
		g2.setStroke(new BasicStroke(3));
		// draw title
		drawTitle(g, midX, midY);
		// draw buttons
		drawButtons(g, midX, midY);
		// return stroke to default
		g2.setStroke(new BasicStroke(1));
		if(loading){
			int loadingX = 10;
			int loadingY = canvas.getHeight() - TEXT_SIZE - 10;
			g.drawString("Loading...", loadingX, loadingY);
		}
	}

	/**
	 * Draws the title 'Player Select' at the top of the menu.
	 * @param g The graphics object with which to draw
	 * @param midX The horizontal centre of the screen
	 * @param midY The vertical centre of the screen
	 */
	private void drawTitle(Graphics g, int midX, int midY) {
		int buttonX = midX - BUTTON_LEFT_DIFF;
		int buttonY = midY - BUTTON_TOP_DIFF/2;
		int gap = 20 + BUTTON_HEIGHT;
		// draw title
		g.setFont(new Font("pixelmix", Font.PLAIN, TEXT_SIZE+20));
		g.drawString("Player Select", buttonX, buttonY-gap);
		g.setFont(new Font("pixelmix", Font.PLAIN, TEXT_SIZE));
	}

	/**
	 * Draws the menu buttons.
	 * @param g The graphics object with which to draw
	 * @param textSize The button text size
	 * @param midX The horizontal centre of the screen
	 * @param midY The vertical centre of the screen
	 */
	private void drawButtons(Graphics g, int midX, int midY) {
		int gapY = 20 + BUTTON_HEIGHT;
		int gapX = 20 + BUTTON_WIDTH;
		// draw buttons
		int buttonY = midY - gapY;
		for(int i=0; i<buttonLabels.length; i++){
			int buttonX = midX - gapX;
			for(int j=0; j<buttonLabels[0].length; j++){
				g.drawRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
				// highlight the button hovered over
				if(i == selectedButtonRow && j == selectedButtonCol){
					g.setColor(new Color(255, 255, 255, 128));
					g.fillRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
				}
				// draw text
				int textWidth = g.getFontMetrics().stringWidth(buttonLabels[i][j]);
				int labelX = buttonX + BUTTON_WIDTH/2 - textWidth/2;
				int labelY = buttonY + BUTTON_HEIGHT/2 + TEXT_SIZE/2;
				g.setColor(Color.WHITE);
				g.drawString(buttonLabels[i][j], labelX, labelY);
				// draw sprite
				drawSprite(g, buttonX, buttonY, buttonLabels[i][j]);
				// increment values
				buttonX += gapX;
				animate();
			}
			buttonY += gapY;
		}
	}

	/**
	 * Draws a sprite inside a button.
	 * @param g The graphics object with which to draw
	 * @param buttonX The left pos of the button
	 * @param buttonY The top pos of the button
	 * @param player The player name of the sprite to draw
	 */
	private void drawSprite(Graphics g, int buttonX, int buttonY, String player) {
		Image toDraw;
		switch(player){
		case "Peter Andrae" : toDraw = spritesPondy[animState]; break;
		case "Marco Servetto" : toDraw = spritesMarco[animState]; break;
		case "David Streader" : toDraw = spritesStreader[animState]; break;
		default : toDraw = spritesDave[animState]; break;
		}
		int imageX = buttonX + BUTTON_WIDTH/2 - toDraw.getWidth(canvas)/2;
		int imageY = buttonY + BUTTON_HEIGHT/2 + toDraw.getHeight(canvas)/2;
		g.drawImage(toDraw, imageX, imageY, canvas);
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
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		Player selectedPlayer = null;
		if(selectedButtonRow == 0){
			if(selectedButtonCol == 0){
				// select dave
				selectedPlayer = new DavePlayer(null, 2 * 24, 2 * 24);
			} else if(selectedButtonCol == 1){
				// select pondy
				selectedPlayer = new PondyPlayer(null, 2 * 24, 2 * 24);
			}
		} else if(selectedButtonRow == 1) {
			if(selectedButtonCol == 0){
				// select marco
				selectedPlayer = new MarcoPlayer(null, 2 * 24, 2 * 24);
			} else if(selectedButtonCol == 1){
				// select streader
				selectedPlayer = new StreaderPlayer(null, 2 * 24, 2 * 24);
			}
		} else {
			return;
		}
		
		// draw the text 'loading'
		loading = true;
		drawMenuItems(canvas.getGraphics());
		
		canvas.setRedrawLoop(false);
		if(controller instanceof SinglePlayerController){
			((SinglePlayerController)controller).initialise(selectedPlayer);
		} else {
			controller.initialise();
		}
		
		canvas.togglePlayerSelectMenu();
		canvas.startGame(controller, 0);
		loading = false;
		
		selectedButtonRow = Integer.MAX_VALUE;
		selectedButtonCol = Integer.MAX_VALUE;
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		int x = e.getX();
		int y = e.getY();
		int gapY = 20 + BUTTON_HEIGHT;
		int gapX = 20 + BUTTON_WIDTH;
		int buttonY = midY - gapY;
		int buttonX = midX - gapX;
		
		// check which x,y it is on
		for(int i=0; i<buttonLabels.length; i++){
			for(int j=0; j<buttonLabels[0].length; j++){
				if(buttonY <= y && y < buttonY + BUTTON_HEIGHT
						&& buttonX <= x && x < buttonX + BUTTON_WIDTH){
					// found the selected button
					this.selectedButtonRow = i;
					this.selectedButtonCol = j;
					return;
				}
				buttonX += gapX;
			}
			buttonY += gapY;
			buttonX = buttonX - (gapX*2);
		}
			
		// deselect buttons
		this.selectedButtonRow = Integer.MAX_VALUE;
	}


}
