package main;

import gameObjects.Compass;
import gameObjects.Container;
import gameObjects.Item;
import gameObjects.Room;
import gameObjects.Torch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import characters.Player;

/**
 * The main canvas inside the game window in which the game is drawn.
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
@SuppressWarnings("serial")
public class GUICanvas extends JComponent{
	
	private GUIFrame frame; // the frame containing this
	
	private Player player; // the current player
	private Compass compass; // the compass being displayed
	private Container currentContainer;
	
	private String toolTip; // current tooltip text
	private int toolTipX; // current tooltip position
	private int toolTipY; //
	
	private int viewScale = 1; // view drawing scale
	
	private boolean mainMenuView; // true if we are looking at main menu
	private MainMenu mainMenu; // the main menu to display
	
	// Static UI Images
	private Image[] torchLight;
	private Image noTorch;
	private Image compassControls;
	private Image healthInventBack;
	private Image healthInventFront;
	
	private Image[] scaledTorchLight;
	private Image scaledNoTorch;
	private Image scaledCompassCont;
	private Image scaledHealthBack;
	private Image scaledHealthFront;
	
	private Image containerBg;
	private Image containerFg;
	private Image scaledContainerBg;
	private Image scaledContainerFg;
	
	/**
	 * Constructor for class GUICanvas.
	 * @param frame The frame containing this canvas
	 * @param player The current client's player.
	 */
	public GUICanvas(GUIFrame frame, Controller controller, Player player){
		this.frame = frame;
		this.player = player;
		this.compass = new Compass();
		this.toolTip = null;
		
		this.player.setCompass(compass);
		
		this.torchLight = new Image[4];
		
		loadImages();
		
		this.mainMenu = new MainMenu(this, controller);
		setMainMenu(true);
	}


	/**
	 * Parses and stores all images used in the UI.
	 */
	private void loadImages() {
		try {
			compassControls = ImageIO.read(new File("Resources"+File.separator+"CompassControls.png"));
			healthInventBack = ImageIO.read(new File("Resources"+File.separator+"HUDBackground.png"));
			healthInventFront = ImageIO.read(new File("Resources"+File.separator+"HUDForeground.png"));
			noTorch = ImageIO.read(new File("Resources"+File.separator+"noTorch.png"));
			containerBg = ImageIO.read(new File("Resources"+File.separator+"ContainerBackground.png"));
			containerFg = ImageIO.read(new File("Resources"+File.separator+"ContainerForeground.png"));
			for (int dir = 0; dir < 4; dir++){
				torchLight[dir] = ImageIO.read(new File("Resources"+File.separator+"torchLightDirectional"
								+File.separator+"torchLight"+dir+".png"));
			}
		} catch (IOException e) {
			System.out.println("Error loading UI Images: " + e.getMessage());
		}
		scaledCompassCont = compassControls;
		scaledHealthBack = healthInventBack;
		scaledHealthFront = healthInventFront;
		scaledNoTorch = noTorch;
		scaledTorchLight = Arrays.copyOf(torchLight, 4);
		scaledContainerBg = containerBg;
		scaledContainerFg = containerFg;
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
		// draw server room overlays
		if (r.getName().equals("Server Room")){
			drawServerRoomOverlay(r, g);
		}
		
		// Draw compass
		compass.update();
		g.drawImage(compass.getImage(), getWidth()-(96*viewScale)-20, 20, this);
		g.drawImage(scaledCompassCont, getWidth()-(96*viewScale)-20, 20, this);
		
		// draw health and inventory displays
		g.drawImage(scaledHealthBack, 0, 24*viewScale, this);
		drawHealth(g);
		drawInventory(g);
		g.drawImage(scaledHealthFront, 0, 24*viewScale, this);
		
		// draw room information
		drawRoomInfo(g, r);
		
		// draw tooltip
		if (toolTip != null){
			showToolTip(g);
		}
		
		// draw container inventory if one is selected
		if(currentContainer != null){
			drawContainer(g);
		}
	}
	
	/**
	 * Draws the contents of the selected container.
	 * @param g The graphics object with which to draw
	 */
	private void drawContainer(Graphics g) {
		int x = (this.getWidth()/2)-(24*3*viewScale);
		int y = (this.getHeight()/2)-(24*3*viewScale);
		List<Item> contents = currentContainer.getContents();
		Item item;
		Image itemImage;
		g.drawImage(scaledContainerBg, x, y, this);
		
		// draw contents of container
		for (int index = 0; index < contents.size(); index++){
			item = contents.get(index);
			if (item != null){
				itemImage = item.getImage(0);
				g.drawImage(itemImage, x+(24*viewScale*(index+1)), y+(24*viewScale), this);
			}
		}
		
		g.drawImage(scaledContainerFg, x, y, this);
	}


	/**
	 * Draws the name and rescription of the current room in the
	 * bottom-left corner.
	 * @param g The graphics object with which to draw
	 * @param r The room whose details to display
	 */
	private void drawRoomInfo(Graphics g, Room r) {
		g.setColor(Color.WHITE);
		// show name
		String name = r.getName();
		int textSize = 15;
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize));
		int baseX = 10;
		int baseY = this.getHeight() - textSize*2;
		g.drawString(name, baseX, baseY);
		
		// show description
		textSize -= 4;
		String desc = r.getDescription();
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize));
		g.drawString(desc, baseX, baseY + textSize + 2);
		
	}

	/**
	 * Draw a black overlay over the server room to make it appear dark.
	 * If the player is carrying a torch, there is a directional 'hole'
	 * in the overlay to simulate light.
	 * @param r The server room
	 * @param g The graphics object with which to draw
	 */
	private void drawServerRoomOverlay(Room r, Graphics g) {
		// calculate drawing variables
		g.setColor(Color.BLACK);
		Image image = scaledNoTorch;
		int width = this.getWidth();
		int height = this.getHeight();
		int xMid = width/2;
		int yMid = height/2;
		int xView = 40*viewScale;
		int yView = 40*viewScale;
		// check if the player has a torch in their inventory
		if (player.inventoryContains(new Torch())){
			xView = 160*viewScale;
			yView = 160*viewScale;
			image = scaledTorchLight[player.getFacing()];
		} 
		// draw the overlay
		g.fillRect(0, 0, width, yMid-yView);
		g.fillRect(0, yMid+yView, width, yMid-yView);
		g.fillRect(0, yMid-yView, xMid-xView, yView*2);
		g.fillRect(xMid+xView, yMid-yView, xMid-xView, yView*2);
		g.drawImage(image, xMid-xView, yMid-yView, this);
	}

	/**
	 * Draw the player's inventory in the top-left corner.
	 * @param g The graphics object with which to draw
	 */
	private void drawInventory(Graphics g) {
		Item[] inventory = player.getInventory();
		for (int i = 0; i < Player.INVENTORY_SIZE; i++){
			if (inventory[i] != null){
				Image itemImage = inventory[i].getScaledImage(0);
				g.drawImage(itemImage, (24*viewScale)*(1+i), (24*viewScale)*2, this);
			}
		}
	}

	/**
	 * Draws the player's current health
	 * @param g The Graphics object to draw with
	 */
	private void drawHealth(Graphics g) {
		int health = player.getHealth();
		if (health <= 50){
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		g.fillRect(24*viewScale, 32*viewScale, health*viewScale, 8*viewScale);
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

	/**
	 * Update the tooltip info
	 * @param toolTip The tooltip text
	 * @param x The tooltip's x position
	 * @param y The tooltip's y position
	 */
	public void setToolTip(String toolTip, int x, int y) {
		this.toolTip = toolTip;
		this.toolTipX = x;
		this.toolTipY = y;
	}
	
	/**
	 * Display the current tooltip.
	 * @param g The graphics object with which to draw
	 */
	private void showToolTip(Graphics g){
		g.setFont(new Font("pixelmix", Font.PLAIN, 10));
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
	 * Sets UI image sizes based on the current view scale.
	 */
	public void scaleUI(){
		// scale compass
		scaledCompassCont = compassControls.getScaledInstance(compassControls.getWidth(this)*viewScale, 
				compassControls.getHeight(this)*viewScale, Image.SCALE_FAST);
		compass.scaleImage(viewScale, this);
		// scale health bar
		scaledHealthBack = healthInventBack.getScaledInstance(healthInventBack.getWidth(this)*viewScale, 
				healthInventBack.getHeight(this)*viewScale, Image.SCALE_FAST);
		scaledHealthFront = healthInventFront.getScaledInstance(healthInventFront.getWidth(this)*viewScale, 
				healthInventFront.getHeight(this)*viewScale, Image.SCALE_FAST);
		// scale container inventory
		scaledContainerBg = containerBg.getScaledInstance(containerBg.getWidth(this)*viewScale, 
				containerBg.getHeight(this)*viewScale, Image.SCALE_FAST);
		scaledContainerFg = containerFg.getScaledInstance(containerFg.getWidth(this)*viewScale, 
				containerFg.getHeight(this)*viewScale, Image.SCALE_FAST);
		// scale torch light
		scaledNoTorch = noTorch.getScaledInstance(noTorch.getWidth(this)*viewScale, 
				noTorch.getHeight(this)*viewScale, Image.SCALE_FAST);
		Image temp[] = torchLight;
		for (int dir = 0; dir < 4; dir++){
			scaledTorchLight[dir] = temp[dir].getScaledInstance(temp[dir].getWidth(this)*viewScale, 
					temp[dir].getHeight(this)*viewScale, Image.SCALE_FAST);
		}
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

	/**
	 * Sets the container inventory to display.
	 * Null = display nothing.
	 * @param currentContainer The container to show
	 */
	public void setCurrentContainer(Container container) {
		if (currentContainer == container){
			currentContainer = null;
		} else {
			this.currentContainer = container;
		}
	}
}