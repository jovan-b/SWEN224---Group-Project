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
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import gameWorld.Controller;
import gameWorld.MultiPlayerController;
import gameWorld.SinglePlayerController;
import network.Server;

/**
 * Displays the main menu for the game.
 * @author Sarah Dobie 300315033
 *
 */
public class MainMenu implements MouseListener, MouseMotionListener {
	
	private static final int IMAGE_SCALE = 8;
	private static final int BUTTON_WIDTH = 300;
	private static final int BUTTON_HEIGHT = 40;
	private static final int BUTTON_TOP_DIFF = BUTTON_HEIGHT*4;
	private static final int BUTTON_LEFT_DIFF = (BUTTON_WIDTH/2)+100;
	
	private GUICanvas canvas; // the canvas this draws on
	private Image[] sprites; // the sprite images to animate
	private int animState = 0; // the current animation frame
	private int animModifier = 1; // flicks between 1 and -1 to loop animation
	private int animCounter = 0; // counts each frame the player has moved
	private String[] buttonLabels; // the button text
	private int selectedButton = Integer.MAX_VALUE; // the button currently highlighted
	
	private static RedrawThread redraw;

	/**
	 * Constructor for class MainMenu.
	 * @param canvas The GUICanvas the menu will be drawn on
	 * @param controller The controller running this
	 */
	public MainMenu(GUICanvas canvas) {
		this.canvas = canvas;
		loadImages();
		loadFonts();
		buttonLabels = new String[]{"New Game", "Load Game", "New Server", "Connect", "Quit"};
		
		setRedrawLoop(true);
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
	
	public void setRedrawLoop(boolean looping){
		if (looping && redraw == null){
			redraw = new RedrawThread();
			redraw.start();
		} else if (!looping && redraw != null){
			redraw.stopRunning();
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
		g2.setStroke(new BasicStroke(5));
		int textSize = BUTTON_HEIGHT-10;
		// draw title
		drawTitle(g, textSize, midX, midY);
		// draw buttons
		drawButtons(g, textSize, midX, midY);
		// return stroke to default
		g2.setStroke(new BasicStroke(1));
	}

	/**
	 * Draws the title 'The ECS Games' at the top of the menu.
	 * @param g The graphics object with which to draw
	 * @param textSize The button text size
	 * @param midX The horizontal centre of the screen
	 * @param midY The vertical centre of the screen
	 */
	private void drawTitle(Graphics g, int textSize, int midX, int midY) {
		int buttonX = midX - BUTTON_LEFT_DIFF;
		int buttonY = midY - BUTTON_TOP_DIFF;
		int gap = 20 + BUTTON_HEIGHT;
		// draw title
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize+10));
		g.drawString("The ECS Games", buttonX, buttonY-gap);
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize));
	}

	/**
	 * Draws the menu buttons.
	 * @param g The graphics object with which to draw
	 * @param textSize The button text size
	 * @param midX The horizontal centre of the screen
	 * @param midY The vertical centre of the screen
	 */
	private void drawButtons(Graphics g, int textSize, int midX, int midY) {
		int buttonX = midX - BUTTON_LEFT_DIFF;
		int buttonY = midY - BUTTON_TOP_DIFF;
		int gap = 20 + BUTTON_HEIGHT;
		// draw buttons
		for(int i=0; i<buttonLabels.length; i++){
			g.drawRect(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);
			// highlight the button hovered over
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
		int buttonY = midY - BUTTON_TOP_DIFF;
		int gap = 20 + BUTTON_HEIGHT;
		int buttonX = midX - BUTTON_LEFT_DIFF;
		
		Graphics g = canvas.getGraphics();
		
		// check if x is within button bounds
		if(buttonX <= x && x < buttonX+BUTTON_WIDTH){
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

	
	private void newGame() {
		//controller.initialiseGame();
		//canvas.setMainMenu(false);
		
		this.setRedrawLoop(false);
		canvas.startGame(new SinglePlayerController(), 0);
	}

	private void loadGame() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "XML Files", "xml");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(canvas);
		
		if(chooser.getSelectedFile() == null || 
				returnVal != JFileChooser.APPROVE_OPTION){	//Player hasn't choosen a file
			return;
		}
		
		canvas.startGame(new SinglePlayerController(chooser.getSelectedFile()), 0);
		this.setRedrawLoop(false);
		
	}

	/**
	 * Creates a server for game clients to connect to.
	 */
	private void newServer() {
		try{
			String p = JOptionPane.showInputDialog(canvas, "Enter the "
					+ "port for the server");
			String numberOfClients = JOptionPane.showInputDialog(canvas, "Enter the "
					+ "number of clients for the server");
			int port = Integer.parseInt(p);
			int clients = Integer.parseInt(numberOfClients);
			Server server = new Server(port, clients, canvas);
			server.start();
		} catch (NumberFormatException ne){
			JOptionPane.showMessageDialog(canvas, "Error: port or "
					+ "number of clients entered is not a number");
		}
	}

	/**
	 * Gets which server the client wants to connect to then starts the game.
	 */
	private void connect() {
		try{
			String ip = JOptionPane.showInputDialog(canvas, "Enter the "
					+ "IP of the server");
			String p = JOptionPane.showInputDialog(canvas, "Enter the "
					+ "port for the server");
			int port = Integer.parseInt(p);
			
			Socket s = new Socket(ip, port);
			
			//Create the socket input stream to wait for the user input
			DataInputStream input = new DataInputStream(s.getInputStream());

			//Waits for the server to send an amount of players in the game
			int numberOfPlayers = input.readInt();
			int uid = input.readInt();
			
			//ClientConnection client = new ClientConnection(s);
			canvas.startGame(new MultiPlayerController(s, uid, numberOfPlayers), uid);
			redraw.stopRunning();
		} catch (IOException e){
			JOptionPane.showMessageDialog(canvas, "Error: could not find server");
		} catch (NumberFormatException ne){
			JOptionPane.showMessageDialog(canvas, "Error: port is not a number");
		}
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
		int buttonY = midY - BUTTON_TOP_DIFF;
		int gap = 20 + BUTTON_HEIGHT;
		int buttonX = midX - BUTTON_LEFT_DIFF;
		Graphics g = canvas.getGraphics();
		
		// check if x is within button bounds
		if(buttonX <= x && x < buttonX+BUTTON_WIDTH){
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
	
	/**
	 * A class to constantly redraw the canvas while the main menu is running
	 * before a controller is created
	 * @author Carl
	 *
	 */
	private class RedrawThread extends Thread {
		public boolean isRunning = true;
		
		@Override
		public void run(){
			//convert time to seconds
			double nextTime = (double)System.nanoTime()/1000000000.0;
			while(isRunning){
				//convert time to seconds
				double currentTime = (double)System.nanoTime()/1000000000.0;
				
				if(currentTime >= nextTime){
					nextTime += Controller.FRAME_RATE;
					if(currentTime < nextTime){
						canvas.repaint();
					}
				}
				else{
					// calculate the time to sleep
					int sleepTime = (int) (1000.0 * (nextTime - currentTime));
					// sanity check
					if (sleepTime > 0) {
						// sleep until the next update
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// do nothing
						}
					}
				}
			}
			
			redraw = null; //last thing is to destroy the parent reference to this object
		}
		
		public void stopRunning(){
			isRunning = false;
			//redraw = null;
		}
	}
	
	
}
