package gui;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.Map;
import gameWorld.gameObjects.Torch;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.weapons.Weapon;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import main.SoundManager;

/**
 * The main canvas inside the game window in which the game is drawn.
 * 
 * @author Jah Seng Lee 300279468
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 * @author Carl Anderson 300264124
 *
 */
@SuppressWarnings("serial")
public class GUICanvas extends JComponent{
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	
	private GUIFrame frame; // the frame containing this
	private Controller controller;
	
	private Player player; // the current player
	private Compass compass; // the compass being displayed
	private Sundial sundial; // the sundial being displayed
	private Container currentContainer; // the container being used by the player
	
	private String toolTip; // current tooltip text
	private int toolTipX; // current tooltip position
	private int toolTipY; //
	
	private int viewScale = 1; // view drawing scale
	private int viewDirection = 0; //The screen view direction
	
	private boolean mainMenuView; // true if we are looking at main menu
	private MainMenu mainMenu; // the main menu to display
	private boolean escMenuView = false; // true if we are looking at esc menu
	private EscMenu escMenu; // the esc menu to display
	private boolean winnerMenuView = false; // true if we are looking at winner menu
	private WinnerMenu winnerMenu; // the winner menu to display
	private boolean playerSelectView; // true if looking at player select menu
	private PlayerSelectMenu playerSelectMenu; // the player select menu to display
	private static RedrawThread redraw;
	

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
	
	private Image mapImage;
	private Image scaledMapImage;
	
	/**
	 * Constructor for class GUICanvas.
	 * @param frame The frame containing this canvas
	 * @param player The current client's player.
	 */
	public GUICanvas(GUIFrame frame){
		this.frame = frame;
		this.compass = new Compass();
		this.sundial = new Sundial(this);
		this.toolTip = null;
				
		torchLight = new Image[4];
		
		loadImages();
		
		mainMenu = new MainMenu(this);
		this.setMainMenu(true);

	}
	
	/**
	 * Starts a game with the given controller
	 * @param controller the game controller
	 * @param uid the id of the player who is using this canvas
	 */
	public void startGame(Controller controller, int uid){
		if (this.controller != null){
			this.removeController(this.controller);
		}
		
		this.controller = controller;
		controller.setGUI(frame);
		
		this.player = controller.getPlayer(uid);
		this.player.setCanvas(this);
		
		this.addMouseListener(controller);
		this.addMouseMotionListener(controller);
		frame.addKeyListener(controller);
		
		this.escMenuView = false;
		this.escMenu = new EscMenu(this, controller);
		
		setRedrawLoop(false);
		controller.startGame();
	}

	/**
	 * Parses and stores all images used in the UI.
	 */
	private void loadImages() {
		try {
			compassControls = ImageIO.read(GUICanvas.class.getResource("/CompassControls.png"));
			healthInventBack = ImageIO.read(GUICanvas.class.getResource("/HUDBackground.png"));
			healthInventFront = ImageIO.read(GUICanvas.class.getResource("/HUDForeground.png"));
			noTorch = ImageIO.read(GUICanvas.class.getResource("/noTorch.png"));
			containerBg = ImageIO.read(GUICanvas.class.getResource("/ContainerBackground.png"));
			containerFg = ImageIO.read(GUICanvas.class.getResource("/ContainerForeground.png"));
			mapImage = ImageIO.read(GUICanvas.class.getResource("/Items/Map.png"));
			for (int dir = 0; dir < 4; dir++){
				torchLight[dir] = ImageIO.read(GUICanvas.class.getResource("/torchLightDirectional"
								+"/torchLight"+dir+".png"));
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
		scaledMapImage = mapImage;
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(GUIFrame.INIT_WIDTH, GUIFrame.INIT_HEIGHT);
	}

	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	@Override
	/**
	 * Draws the game screen
	 * If the player is in a menu, displays that,
	 * otherwise draws the game state.
	 * Draws room and contents first then draws
	 * HUD and UI elements on top
	 */
	public void paint(Graphics graphics){
		Graphics2D g = (Graphics2D) graphics;
		// paint other menu if one is active
		if(mainMenuView){
			mainMenu.paint(g);
			return;
		} else if(winnerMenuView){
			winnerMenu.paint(g);
			return;
		} else if(playerSelectView){
			playerSelectMenu.paint(g);
			return;
		}
		//paint background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		Room r = controller.getCurrentPlayer().getCurrentRoom();
		r.draw(g, this, controller.getCurrentPlayer(), viewDirection);
		drawHUD(g, r);
		
		if(escMenuView){
			escMenu.paint(g);
		}
	}

	/**
	 * Draws the heads up display, ie. UI components like health, score etc.
	 * Includes elements like the compass and sundial
	 * @param g The graphics object with which to draw
	 * @param r The room to draw
	 */
	private void drawHUD(Graphics2D g, Room r) {
		// draw server room overlays
		String roomName = r.getName();
		if (roomName.equals("Server Room") ||
				roomName.equals("Courtyard")){
			drawDarknessOverlay(r, g);
		}
		
		// Draw Sundial
		sundial.update();
		g.drawImage(sundial.getImage(), getWidth()-(96*viewScale)-20, 20, this);
		
		// Draw Compass
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
		
		// draw points
		drawPoints(g);
		
		// draw container inventory if one is selected
		if(currentContainer != null){
			drawContainer(g);
		}
		
		// draw tooltip
		if (toolTip != null){
			showToolTip(g);
		}
		
		// draw Map overlay
		Item map = controller.getCurrentPlayer().inventoryContains(new Map());
		if (map != null && ((Map)map).isOpen()){
			drawMap(g);
		}
	}


	/**
	 * Draws the Map overlay if the player has a Map open
	 * @param g The Graphics object with which to draw
	 */
	private void drawMap(Graphics g) {
		int x = (this.getWidth()/2)-(24*9*viewScale)-(12*viewScale);
		int y = (this.getHeight()/2)-(24*7*viewScale)-(12*viewScale);
		g.drawImage(scaledMapImage, x, y, this);
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
				itemImage = item.getScaledImage(0);
				g.drawImage(itemImage, x+(24*viewScale*(index+1)), y+(24*viewScale), this);
			}
		}
		
		g.drawImage(scaledContainerFg, x, y, this);
	}


	/**
	 * Draws the name and description of the current room in the
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
	 * Draws the number of points the player has in the bottom-right
	 * corner.
	 * @param g The graphics object with which to draw.
	 */
	private void drawPoints(Graphics g) {
		g.setColor(Color.WHITE);
		String points = ""+controller.getCurrentPlayer().getPoints();
		int textSize = 15;
		g.setFont(new Font("pixelmix", Font.PLAIN, textSize));
		int textWidth = g.getFontMetrics().stringWidth(points);
		int baseX = getWidth() - textWidth - 10;
		int baseY = this.getHeight() - textSize*2;
		g.drawString(points, baseX, baseY);
	}

	/**
	 * Draw a black overlay over the server room to make it appear dark.
	 * Also draws a transparent overlay over the Courtyard according to the
	 * time of day.
	 * If the player is carrying a torch, there is a directional 'hole'
	 * in the overlay to simulate light.
	 * @param r The server room or courtyard
	 * @param g The graphics object with which to draw
	 */
	private void drawDarknessOverlay(Room r, Graphics2D g) {
		// calculate drawing variables
		String roomName = r.getName();
		float alpha = 1;
		if (roomName.equals("Courtyard")){
			alpha = controller.getNightAlpha();
		} 	
		g.setColor(new Color(0.0f, 0.0f, 0.0f, alpha));

		Image image = scaledNoTorch;
		int width = this.getWidth();
		int height = this.getHeight();
		int xMid = width/2;
		int yMid = height/2;
		int xView = 40*viewScale;
		int yView = 40*viewScale;
		// check if the player has a torch in their inventory
		Torch torch = (Torch)controller.getCurrentPlayer().inventoryContains(new Torch());
		if (torch != null && torch.isOn()){
			xView = 160*viewScale;
			yView = 160*viewScale;
			image = scaledTorchLight[controller.getCurrentPlayer().getFacing()];
		} 
		// draw the overlay
		g.fillRect(0, 0, width, yMid-yView);
		g.fillRect(0, yMid+yView, width, yMid-yView);
		g.fillRect(0, yMid-yView, xMid-xView, yView*2);
		g.fillRect(xMid+xView, yMid-yView, xMid-xView, yView*2);
		
		// Set image alpha
		Composite temp = g.getComposite();
		AlphaComposite ac = java.awt.AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
        g.setComposite(ac);
		g.drawImage(image, xMid-xView, yMid-yView, this);
        g.setComposite(temp);
	}

	/**
	 * Draw the player's inventory and weapon in the top-left corner.
	 * @param g The graphics object with which to draw
	 */
	private void drawInventory(Graphics g) {
		Item[] inventory = controller.getCurrentPlayer().getInventory();
		for (int i = 0; i < Player.INVENTORY_SIZE; i++){
			if (inventory[i] != null){
				Image itemImage = inventory[i].getScaledImage(0);
				g.drawImage(itemImage, (24*viewScale)*(1+i), (24*viewScale)*2, this);
			}
		}
		// draws the players current weapon
		Weapon weapon = controller.getCurrentPlayer().getWeapon();
		Image weaponImage = weapon.getScaledImage(0);
		g.drawImage(weaponImage, (24*viewScale), (24*viewScale)*3, this);
		g.setFont(new Font("pixelmix", Font.PLAIN, 10*viewScale));
		g.setColor(Color.WHITE);
		g.drawString(weapon.getName(), (24*viewScale)*2+(6*viewScale), (24*viewScale)*3+(18*viewScale));
	}

	/**
	 * Draws the player's current health
	 * @param g The Graphics object to draw with
	 */
	private void drawHealth(Graphics g) {
		int health = controller.getCurrentPlayer().getHealth();
		if (health <= 50){
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.GREEN);
		}
		g.fillRect(24*viewScale, 32*viewScale, health*viewScale, 8*viewScale);
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
	 * Sets UI image sizes based on the current view scale.
	 */
	public void scaleUI(){
		// scale compass
		scaledCompassCont = compassControls.getScaledInstance(compassControls.getWidth(this)*viewScale, 
				compassControls.getHeight(this)*viewScale, Image.SCALE_FAST);
		compass.scaleImage(viewScale, this);
		sundial.scaleImage(viewScale, this);
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
		// scale map overlay
		scaledMapImage = mapImage.getScaledInstance(mapImage.getWidth(this)*viewScale,
				mapImage.getHeight(this)*viewScale, Image.SCALE_FAST);
		Image temp[] = torchLight;
		for (int dir = 0; dir < 4; dir++){
			scaledTorchLight[dir] = temp[dir].getScaledInstance(temp[dir].getWidth(this)*viewScale, 
					temp[dir].getHeight(this)*viewScale, Image.SCALE_FAST);
		}
	}

	/**
	 * Rotates the player's view clockwise
	 */
	public void rotateViewRight() {
		viewDirection--;
		if (viewDirection < 0){
			viewDirection = 3;
		}
		compass.rotate(90);
	}

	/**
	 * Rotates the player's view anticlockwise
	 */
	public void rotateViewLeft() {
		viewDirection++;
		if (viewDirection > 3){
			viewDirection = 0;
		}
		compass.rotate(-90);
	}
	
	/**
	  * Rotates the sundial by the specified number of degrees
	  * @param degrees The degrees to rotate
	  */
	public void rotateSundial(double degrees){
		sundial.rotate(degrees);
	}

	/**
	 * Utility method to convert a direction relative to the view direction
	 * back to the direction of the global coordinates
	 * @param toConvert the input value to convert
	 * @return the converted direction value
	 */
	public static int convertFromViewDir(int toConvert, int viewDirection) {
		int temp = toConvert;
		for (int i = 1; i <= viewDirection; i++){
			temp++;
			if (temp > 3){
				temp = 0;
			}
		}
		return temp;
	}
	
	/**
	 * Utility method to convert a global direction
	 * to a direction relative to the view
	 * @param toConvert the input value to convert
	 * @param viewDirection2 
	 * @return the converted direction value
	 */
	public static int convertToViewDir(int toConvert, int viewDir) {
		int temp = toConvert;
		for (int i = 1; i <= viewDir; i++){
			temp--;
			if (temp < NORTH){
				temp = WEST;
			}
		}
		return temp;
	}
	
	/**
	 * Utility method converting a string representing a direction to 
	 * a global direction
	 * @param dir The string representing a direction
	 * @param viewDirection The current view direction
	 * @return the global direction integer
	 */
	public static int convertStringToDir(String dir, int viewDirection){
		switch(dir){
		case "up": return convertFromViewDir(NORTH, viewDirection);
		case "right": return convertFromViewDir(EAST, viewDirection);
		case "down": return convertFromViewDir(SOUTH, viewDirection);
		case "left": return convertFromViewDir(WEST, viewDirection);
		default: return convertFromViewDir(NORTH, viewDirection);
		}
	}

	/**
	 * Opens or hides the main menu.
	 * @param isMainMenu true to show the main menu, false otherwise
	 */
	public void setMainMenu(boolean isMainMenu) {
		this.mainMenuView = isMainMenu;
		
		if(isMainMenu){
			SoundManager.playSong("MainMenu/8-bit Survivor Eye Of The Tiger.mp3");
			
			this.removeController(controller);
			this.addMouseListener(mainMenu);
			this.addMouseMotionListener(mainMenu);
			
			this.setRedrawLoop(true);
		} else {
			this.removeMouseListener(mainMenu);
			this.removeMouseMotionListener(mainMenu);
			
			this.setRedrawLoop(false);
		}
	}

	/**
	 * Opens or hides the player select menu.
	 */
	public void togglePlayerSelectMenu(boolean multiplayer) {
		if(playerSelectMenu == null){
			playerSelectMenu = new PlayerSelectMenu(this, multiplayer);
		}
		playerSelectView = !playerSelectView; // toggle boolean
		// change settings
		if(playerSelectView){
			removeMouseListener(controller);
			removeMouseMotionListener(controller);
			addMouseListener(playerSelectMenu);
			addMouseMotionListener(playerSelectMenu);
			setRedrawLoop(true);
		} else {
			removeMouseListener(playerSelectMenu);
			removeMouseMotionListener(playerSelectMenu);
			setRedrawLoop(false);
		}
	}

	/**
	 * Opens or hides the esc menu.
	 */
	public void toggleEscMenu() {
		escMenuView = !escMenuView; // toggle boolean
		// change settings
		if(escMenuView){
			removeMouseListener(controller);
			removeMouseMotionListener(controller);
			addMouseListener(escMenu);
			addMouseMotionListener(escMenu);
		} else {
			removeMouseListener(escMenu);
			removeMouseMotionListener(escMenu);
			addMouseListener(controller);
			addMouseMotionListener(controller);
		}
	}

	/**
		 * Opens or hides the end-game winner view.
		 * @param display true to display menu, false to hide it
		 */
		public void setWinnerView(boolean display) {
			if(winnerMenu == null){
				this.winnerMenu = new WinnerMenu(this, controller);
			}
			winnerMenuView = display; // toggle boolean
			// change settings
			if(winnerMenuView){			
				removeMouseListener(controller);
				removeMouseMotionListener(controller);
				
				addMouseListener(winnerMenu);
				addMouseMotionListener(winnerMenu);
				
				SoundManager.playQueue(SoundManager.CREDIT_SONGS);
			} else {
				removeMouseListener(winnerMenu);
				removeMouseMotionListener(winnerMenu);
			
	//			addMouseListener(controller);
	//			addMouseMotionListener(controller);
			}
		}

	/**
	 * Clean up the controller, removing it from this 
	 * canvas
	 * @param controller
	 */
	public void removeController(Controller controller) {
		if (controller == null){return;}
		controller.setRunning(false);
		
		this.removeMouseListener(controller);
		this.removeMouseMotionListener(controller);
		frame.removeKeyListener(controller);
		
		if (this.controller == controller){
			this.controller = null;
		}
	}

	public void removeEscMenu(EscMenu menu){
		if(menu == null){return;}
		
		this.removeMouseListener(menu);
		this.removeMouseMotionListener(menu);
	}
	
	public Controller getController(){
		return controller;
	}
		
	public MainMenu getMainMenu(){
		return mainMenu;
	}

	public Container getCurrentContainer(){
		return currentContainer;
	}

	/**
	 * Gets the current player.
	 * @return the current player
	 */
	public Player getCurrentPlayer(){
		return controller.getCurrentPlayer();
	}

	/**
	 * Gets the current view scale of this canvas.
	 * @return The current view scale: either 1 or 2
	 */
	public int getViewScale() {
		return viewScale;
	}

	public int getViewDirection() {
		return viewDirection;
	}

	public boolean isWinnerView(){
		return this.winnerMenuView;
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

	/**
	 * Sets the current view scale.
	 * @param viewScale The new view scale: either 1 or 2.
	 */
	public void setViewScale(int viewScale) {
		this.viewScale = viewScale;
	}

	/**
	 * Change the view direction.
	 * @param dir The new view direction
	 */
	public void setViewDirection(int dir){
		if (dir < 0 || dir > 3){return;}
		viewDirection = dir;
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
						GUICanvas.this.repaint();
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
	
	public void setRedrawLoop(boolean looping){
		if (looping && redraw == null){
			redraw = new RedrawThread();
			redraw.start();
		} else if (!looping && redraw != null){
			redraw.stopRunning();
		}
	}
}
