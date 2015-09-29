package main;

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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Displays the main menu for the game.
 * @author Sarah Dobie
 *
 */
public class MainMenu implements MouseListener, MouseMotionListener {
	
	private static final int IMAGE_SCALE = 8;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 40;
	
	private GUICanvas canvas;
	private Controller controller;
	private Image[] sprites;
	private int animState = 0; // the current animation frame
	private int animModifier = 1; // flicks between 1 and -1 to loop animation
	private int animCounter = 0; // counts each frame the player has moved
	private String[] buttonLabels;
	private int selectedButton = Integer.MAX_VALUE;

	/**
	 * Constructor for class MainMenu.
	 * @param canvas The GUICanvas the menu will be drawn on
	 */
	public MainMenu(GUICanvas canvas, Controller controller) {
		this.canvas = canvas;
		this.controller = controller;
		loadImages();
		loadFonts();
		buttonLabels = new String[]{"New Game", "Load Game", "New Server", "Connect", "Quit"};
	}

	/**
	 * Loads all required fonts.
	 */
	private void loadFonts() {
		try {
		     GraphicsEnvironment ge = 
		         GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Resources"+File.separator+"pixelmix.ttf")));
		} catch (IOException|FontFormatException e) {
		     System.out.println("Error loading fonts : "+e.getMessage());
		}
	}

	/**
	 * Loads all necessary images.
	 */
	private void loadImages() {
		// Load sprites
		sprites = new Image[3];
		try {
			for (int ani = 0; ani < 3; ani++){
				Image img = ImageIO.read(
						new File("Resources"+File.separator+"Players"+File.separator+"Dave"+2+ani+".png"));
				sprites[ani] = img.getScaledInstance(img.getWidth(canvas)*IMAGE_SCALE,
						img.getHeight(canvas)*IMAGE_SCALE, Image.SCALE_FAST);
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
	}
	
	/**
	 * Displays the main menu.
	 * @param g The Graphics object with which to draw the menu
	 */
	public void paint(Graphics g){
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		// paint background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		// draw little dave
		g.drawImage(getCurrentImage(), midX+100, midY-150, canvas);
		animate();
		// draw the buttons
		drawButtons(g);
	}
	
	/**
	 * Draws all buttons on the canvas.
	 * @param g The Graphics object with which to draw
	 */
	private void drawButtons(Graphics g){
		// calculate canvas centre
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		// change graphics settings
		g.setColor(Color.WHITE);
		Graphics2D g2 = ((Graphics2D)g);
		g2.setStroke(new BasicStroke(5));
		int textSize = BUTTON_HEIGHT-10;
		// set up loop values
		int buttonY = midY - (BUTTON_HEIGHT*4);
		int gap = 20 + BUTTON_HEIGHT;
		int buttonX = midX-(BUTTON_WIDTH/2)-100;
		
		// draw title
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize+10));
		g.drawString("The ECS Games", buttonX, buttonY-gap);
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize));
		
		// draw buttons
		for(int i=0; i<buttonLabels.length; i++){
			g.drawRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
			if(i == this.selectedButton){
				g.setColor(new Color(255, 255, 255, 128));
				g.fillRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
			}
			// draw text
			int textWidth = g.getFontMetrics().stringWidth(buttonLabels[i]);
			int labelX = buttonX + BUTTON_WIDTH/2 - textWidth/2;
			int labelY = buttonY + BUTTON_HEIGHT/2 + textSize/2;
			g.setColor(Color.WHITE);
			g.drawString(buttonLabels[i], labelX, labelY);
			// increment y
			buttonY += gap;
		}
		// return stroke to default
		g2.setStroke(new BasicStroke(1));
	}
	
	/**
	 * Get the next image in the animation.
	 * @return The next image to draw
	 */
	private Image getCurrentImage() {	
		return sprites[animState];
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
		// calculate positional values
		int midX = canvas.getWidth()/2;
		int midY = canvas.getHeight()/2;
		int x = e.getX();
		int y = e.getY();
		int buttonY = midY - (BUTTON_HEIGHT*4);
		int gap = 20 + BUTTON_HEIGHT;
		int buttonLeft = midX-(BUTTON_WIDTH/2)-100;
		
		Graphics g = canvas.getGraphics();
		
		// check if x is within button bounds
		if(buttonLeft <= x && x < buttonLeft+BUTTON_WIDTH){
			// check which y it is on
			for(int i=0; i<buttonLabels.length; i++){
				// check if we are on a button
				if(buttonY <= y && y < buttonY + BUTTON_HEIGHT){
					// figure out the button we're on
					switch(i){
					case 0 : newGame(); break;
					case 1 : loadGame(); break;
					case 2 : newServer(); break;
					case 3 : connect(); break;
					case 4 : quit(); break;
					}
				}
				// increment y
				buttonY += gap;
			}
		}
	}

	
	private void newGame() { // FIXME actually make a new game
		controller.initialiseGame();
		canvas.setMainMenu(false);
	}

	private void loadGame() {
		// TODO Auto-generated method stub
		
	}

	private void newServer() {
		// TODO Auto-generated method stub
		
	}

	private void connect() {
		// TODO Auto-generated method stub
		
	}

	private void quit() {
		System.exit(0);
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
		int buttonY = midY - (BUTTON_HEIGHT*4);
		int gap = 20 + BUTTON_HEIGHT;
		int buttonLeft = midX-(BUTTON_WIDTH/2)-100;
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
	
}
