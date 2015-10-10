package gameWorld;

import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;
import gameWorld.characters.nonplayer.strategy.RespawnStrategy;
import gameWorld.characters.nonplayer.strategy.WanderingMerchantStrategy;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.containers.Container;
import gui.GUICanvas;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.BitSet;




import main.saveAndLoad.LoadManager;
import main.saveAndLoad.SaveManager;

public class SinglePlayerController extends Controller {
	private boolean shooting = false;
	
	private Player player;
	
	private BitSet keyBits = new BitSet(256);	//set of keys being pressed right now

	private int mouseX = 0;
	private int mouseY = 0;
		
	/**
	 * Controller constructor for a singleplayer game
	 */
	public SinglePlayerController(){
		super(0);
		initialise();		
	}
	
	public SinglePlayerController(File selectedFile) {
		super(0);
		LoadManager.loadGame(selectedFile, this);
	}

	/**
	 * Initialise the pre-game fields of this class
	 */
	protected void initialise() {
		super.initialise();
		player = new DavePlayer(rooms.get(0), 2*24, 2*24);
		players.add(player);
		
		NonPlayer npc = new NonPlayer(rooms.get(0), 5*24, 7*24, new WanderingMerchantStrategy());
		npc.setStrategy(NonPlayer.Events.DEATH, new RespawnStrategy(5000));
		rooms.get(0).addNPC(npc);
	}

	/**
	 * Initialise the fields of the game
	 */
	public void initialiseGame() {
		isRunning = true;
		Room room = rooms.get(0); //FIXME
		room.addPlayer(player);
		player.setCanvas(gui.getCanvas());
		
		SaveManager.saveGame(this, "test_save.xml");
	}
	
	/**
	 * Update the game logic
	 * Take in user input and call appropriate methods
	 * Update the positions of non-input deterministic objects
	 * 
	 */
	protected void update() {
		dealWithInput();//deal with user input
		checkTooltip(); // check if a tooltip should be displayed
		player.update();
	}

	/**
	 * Updates player appropriately depending on current keys pressed
	 */
	private void dealWithInput() {
		// Player Movement
		if(isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_D)){
			player.move(GUICanvas.convertStringToDir("right", gui.getCanvas().getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if(isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_A)){
			player.move(GUICanvas.convertStringToDir("left", gui.getCanvas().getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if(isKeyPressed(KeyEvent.VK_UP) || isKeyPressed(KeyEvent.VK_W)){
			player.move(GUICanvas.convertStringToDir("up", gui.getCanvas().getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if(isKeyPressed(KeyEvent.VK_DOWN) || isKeyPressed(KeyEvent.VK_S)){
			player.move(GUICanvas.convertStringToDir("down", gui.getCanvas().getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if(isKeyPressed(KeyEvent.VK_SHIFT)){
			player.setSpeedModifier(1);
		} else {
			player.setSpeedModifier(0);
		}
		if(shooting){
			player.shoot(mouseX, mouseY);
		}
	}
	
	/**
	 * Returns true if key 'keycode' is being pressed 
	 * Otherwise returns false
	 * 
	 * @param keyCode The key to check
	 * @return true if the given keycode is being pressed
	 */
	private boolean isKeyPressed(int keyCode) {
		return keyBits.get(keyCode);
	}

	/**
	 * If key is pressed by user, add to the keyBits set
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keyBits.set(e.getKeyCode());
	}

	/**
	 * If key is released by user, clear from the keyBits set
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// View Rotation
		if(e.getKeyCode() == KeyEvent.VK_Q){
			gui.getCanvas().rotateViewLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_E){
			gui.getCanvas().rotateViewRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_1){
			player.inventoryItemAt(0).use(player, this);
		}
		if(e.getKeyCode() == KeyEvent.VK_2){
			player.inventoryItemAt(1).use(player, this);
		}
		if(e.getKeyCode() == KeyEvent.VK_3){
			player.inventoryItemAt(2).use(player, this);
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS){
			gui.getCanvas().setViewScale(1);
			scaleEverything(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_EQUALS){
			gui.getCanvas().setViewScale(2);
			scaleEverything(2);
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			gui.getCanvas().toggleEscMenu();
		}
		keyBits.clear(e.getKeyCode());
	}

	//Don't care about this method
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	//don't care about this method
	@Override
	public void mouseClicked(MouseEvent e) {	
	}	
	//don't care about this method
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	//don't care about this method
	@Override
	public void mouseExited(MouseEvent e) {	
	}

	/**
	 * When mouse event is fired
	 * 	clear mouselocation
	 * 	get x, y position of mouse
	 * 	add new xy to mouseLocation
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1){
			shooting = true;
			mouseX = e.getX();
			mouseY = e.getY();
		} else if (e.getButton() == 3){
			int x = e.getX();
			int y = e.getY();
			rightClickInteract(x, y);
		}
	}

	private void rightClickInteract(int x, int y) {
		int viewScale = gui.getCanvas().getViewScale();
		int viewDirection = gui.getCanvas().getViewDirection();
		Container container = gui.getCanvas().getCurrentContainer();
		int xMid = gui.getCanvas().getWidth()/2;
		int yMid = gui.getCanvas().getHeight()/2;
		// check if mousing over merchant NPC
		Room currentRoom = player.getCurrentRoom();
		NonPlayer npc = currentRoom.wanderingNpcAtMouse(x, y, player, viewScale, viewDirection);
		if(npc != null){
			WanderingMerchantStrategy strat = (WanderingMerchantStrategy) npc.getStrategy();
			strat.interact(player, npc);
		}
		// check if the player has clicked on their inventory
				else if (24*2*viewScale < y && y < 24*3*viewScale){
			if (24*viewScale < x && x < 24*(Player.INVENTORY_SIZE+1)*viewScale){
				int index = (x-(24*viewScale))/(24*viewScale);
				player.dropItem(index, container);
			} else {
				Room room = player.getCurrentRoom();
				Item item = room.itemAtMouse(x, y, viewScale, player, viewDirection);
				item.use(player, this);
			}
		} else if ((yMid-(24*2*viewScale) < y && y < yMid-(24*viewScale)) && container != null){
			if (xMid-(24*2*viewScale) < x && x < xMid+(24*2*viewScale)){
				// the player has clicked on an open container
				int index = (x-(xMid-(24*2*viewScale)))/(24*viewScale);
				container.pickUpItem(index, player);
			} else {
				Room room = player.getCurrentRoom();
				Item item = room.itemAtMouse(x, y, viewScale, player, viewDirection);
				item.use(player, this);
			}
		} else {
			Room room = player.getCurrentRoom();
			Item item = room.itemAtMouse(x, y, viewScale, player, viewDirection);
			item.use(player, this);
		}
	}

	/**
	 * clear mouselocation, so that nothing is being pressed
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1){
			shooting = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	/**
	 * Checks the mouse position to see if it is hovering over an item
	 * which should display a tooltip.
	 */
	private void checkTooltip() {
		int viewScale = gui.getCanvas().getViewScale();
		int viewDirection = gui.getCanvas().getViewDirection();
		int x = mouseX;
		int y = mouseY;
		int xMid = gui.getCanvas().getWidth()/2;
		int yMid = gui.getCanvas().getHeight()/2;
		Container container = gui.getCanvas().getCurrentContainer();
		String desc = "";
		// check if mousing over merchant NPC
		Room currentRoom = player.getCurrentRoom();
		NonPlayer npc = currentRoom.wanderingNpcAtMouse(x, y, player, viewScale, viewDirection);
		if(npc != null){
			WanderingMerchantStrategy strat = (WanderingMerchantStrategy) npc.getStrategy();
			desc = strat.getDescription();
		}
		// checks if the player is currently mousing over their inventory
		else if (24*2*viewScale < y && y < 24*3*viewScale){
			if (24*viewScale < x && x < 24*(Player.INVENTORY_SIZE+1)*viewScale){
				// hovering over inventory
				int index = (x-(24*viewScale))/(24*viewScale);
				desc = player.inventoryItemAt(index).getDescription();
			} else {
				// not hovering over inventory - check for items on floor
				desc = player.getCurrentRoom().itemAtMouse(x, y, viewScale, player, viewDirection).getDescription();
			}
		// or if player is mousing over another inventory
		} else if ((yMid-(24*2*viewScale) < y && y < yMid-(24*viewScale)) && container != null){
			if (xMid-(24*2*viewScale) < x && x < xMid+(24*2*viewScale)){
				// hovering over inventory
				int index = (x-(xMid-(24*2*viewScale)))/(24*viewScale);
				Item itemAtIndex = container.getItem(index);
				if(itemAtIndex != null){
					desc = itemAtIndex.getDescription();
				}
			} else {
				// not hovering over inventory - check for items on floor
				desc = player.getCurrentRoom().itemAtMouse(x, y, viewScale, player, viewDirection).getDescription();
			}
		
		} else {
			// not hovering over inventory - check for items on floor
			desc = player.getCurrentRoom().itemAtMouse(x, y, viewScale, player, viewDirection).getDescription();
		}
		gui.getCanvas().setToolTip(desc, x, y);
	}

	public void setCurrentPlayer(Player player) {
		players.set(0, player);
	}
	
	/**
	 * Returns the player with the specified user id.
	 * @param uid The user id of the player
	 * @return The player with the given uid
	 */
	public Player getPlayer(int uid){
		return players.get(0);
	}
}
