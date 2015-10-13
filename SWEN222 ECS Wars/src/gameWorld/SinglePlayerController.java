package gameWorld;

import gameWorld.characters.DavePlayer;
import gameWorld.characters.MarcoPlayer;
import gameWorld.characters.Player;
import gameWorld.characters.PondyPlayer;
import gameWorld.characters.StreaderPlayer;
import gameWorld.characters.nonplayer.NonPlayer;
import gameWorld.characters.nonplayer.strategy.ChaseCombatStrategy;
import gameWorld.characters.nonplayer.strategy.RespawnStrategy;
import gameWorld.characters.nonplayer.strategy.WanderingMerchantStrategy;
import gameWorld.gameEvents.DayNightEvent;
import gameWorld.gameEvents.GameClock;
import gameWorld.gameEvents.SlowUpdateEvent;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.containers.Container;
import gui.GUICanvas;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.BitSet;

import main.saveAndLoad.LoadManager;
import main.saveAndLoad.SaveManager;

/**
 * A controller implementation for single player games
 * 
 * @author Jah Seng Lee
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 * @author Carl Anderson 300264124
 *
 */
public class SinglePlayerController extends Controller {
	

	private Player player;

	private BitSet keyBits = new BitSet(256); // set of keys being pressed right
												// now

	private int mouseX = 0;
	private int mouseY = 0;

	/**
	 * Controller constructor for a singleplayer game
	 */
	public SinglePlayerController() {
		super(0);
		initialise();
	}

	/**
	 * Load single player game information from file
	 * @param selectedFile
	 */
	public SinglePlayerController(File selectedFile) {
		super(0);
		super.initialise();
		LoadManager.loadGame(selectedFile, this);
	}

	/**
	 * Initialise the pre-game fields of this class
	 */
	protected void initialise() {
		player = new DavePlayer(null, 2 * 24, 2 * 24);
		players.add(player);
		
		super.initialise();
	}
	
	@Override
	public void startGame(){
		GameClock.getInstance().scheduleEvent(new DayNightEvent(this, DAY_LENGTH));
		GameClock.getInstance().scheduleEvent(new SlowUpdateEvent(this, 1));
		
		super.startGame();
	}

	@Override
	protected void update() {
		dealWithInput();// deal with user input
		checkTooltip(player); // check if a tooltip should be displayed
		player.update();
	}

	/**
	 * Updates player appropriately depending on current keys pressed
	 */
	private void dealWithInput() {
		// Player Movement
		if (isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_D)) {
			player.move(GUICanvas.convertStringToDir("right", gui.getCanvas()
					.getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if (isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_A)) {
			player.move(GUICanvas.convertStringToDir("left", gui.getCanvas()
					.getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if (isKeyPressed(KeyEvent.VK_UP) || isKeyPressed(KeyEvent.VK_W)) {
			player.move(GUICanvas.convertStringToDir("up", gui.getCanvas()
					.getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if (isKeyPressed(KeyEvent.VK_DOWN) || isKeyPressed(KeyEvent.VK_S)) {
			player.move(GUICanvas.convertStringToDir("down", gui.getCanvas()
					.getViewDirection()));
			gui.getCanvas().setCurrentContainer(null);
		}
		if (isKeyPressed(KeyEvent.VK_SHIFT)) {
			player.setSpeedModifier(1);
		} else {
			player.setSpeedModifier(0);
		}
		if (shooting) {
			player.shoot(mouseX, mouseY);
		}
	}

	/**
	 * Returns true if key 'keycode' is being pressed Otherwise returns false
	 * 
	 * @param keyCode
	 *            The key to check
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
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			gui.getCanvas().rotateViewLeft();
		}
		if (e.getKeyCode() == KeyEvent.VK_E) {
			gui.getCanvas().rotateViewRight();
		}
		if (e.getKeyCode() == KeyEvent.VK_1) {
			player.inventoryItemAt(0).use(player, this);
		}
		if (e.getKeyCode() == KeyEvent.VK_2) {
			player.inventoryItemAt(1).use(player, this);
		}
		if (e.getKeyCode() == KeyEvent.VK_3) {
			player.inventoryItemAt(2).use(player, this);
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS) {
			gui.getCanvas().setViewScale(1);
			scaleEverything(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_EQUALS) {
			gui.getCanvas().setViewScale(2);
			scaleEverything(2);
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gui.getCanvas().toggleEscMenu();
		}
		keyBits.clear(e.getKeyCode());
	}

	// Don't care about this method
	@Override
	public void keyTyped(KeyEvent e) {
	}

	// don't care about this method
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	// don't care about this method
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	// don't care about this method
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * When mouse event is fired clear mouselocation get x, y position of mouse
	 * add new xy to mouseLocation
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			shooting = true;
			mouseX = e.getX();
			mouseY = e.getY();
		} else if (e.getButton() == 3) {
			int x = e.getX();
			int y = e.getY();
			rightClickInteract(x, y, player);
		}
	}

	/**
	 * clear mouselocation, so that nothing is being pressed
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
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
		super.mouseX = e.getX();
		super.mouseY = e.getY();
	}

	public void setCurrentPlayer(Player player) {
		super.setCurrentPlayer(player);
		this.player = player;
		players.set(0, player);
	}

	/**
	 * Returns the player with the specified user id.
	 * 
	 * @param uid
	 *            The user id of the player
	 * @return The player with the given uid
	 */
	public Player getPlayer(int uid) {
		return players.get(0);
	}
}
