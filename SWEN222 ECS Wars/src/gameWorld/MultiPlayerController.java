package gameWorld;

import gameWorld.characters.Player;
import gui.GUICanvas;
import network.ClientConnection;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 
 * Controller for a multiplayer game that will update the player's state
 * 
 * @author Jovan Bogoievski 300305140
 *
 */
public class MultiPlayerController extends Controller {
	
	//Number of players in the game
	private int numPlayers;
	private GUICanvas canvas;

	public MultiPlayerController(Socket socket, int uid, int numPlayers,
			GUICanvas canvas, ArrayList<Player> players) {
		super(uid);
		this.canvas = canvas;
		this.numPlayers = numPlayers;
		this.players = players;
		initialise();
		client = new ClientConnection(socket, this, uid);
	}
	
	@Override
	public void initialise(){
		//Set the canvas of the players in game locally, this is needed so the weapons can shoot
		for(int i=0; i<numPlayers; i++){
			Player player = players.get(i);
			if(i == uid){
				continue;
			} else{
				player.setCanvas(canvas);
			}
		}
		super.initialise();
	}

	/**
	 * Deal with player input and update a clients state (position, room, etc
	 */
	@Override
	public void update(){
		client.checkForUpdates();
		checkTooltip(getPlayer(uid));
		players.get(uid).update();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		keyBits.set(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_Q){
			this.getGUI().getCanvas().rotateViewLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_E){
			this.getGUI().getCanvas().rotateViewRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_1){
			this.getPlayer(uid).inventoryItemAt(0).use(this.getPlayer(uid), this);
		}
		if(e.getKeyCode() == KeyEvent.VK_2){
			this.getPlayer(uid).inventoryItemAt(1).use(this.getPlayer(uid), this);
		}
		if(e.getKeyCode() == KeyEvent.VK_3){
			this.getPlayer(uid).inventoryItemAt(2).use(this.getPlayer(uid), this);
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS){
			this.getGUI().getCanvas().setViewScale(1);
			this.scaleEverything(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_EQUALS){
			this.getGUI().getCanvas().setViewScale(2);
			this.scaleEverything(2);
		}
		players.get(uid).setSpeedModifier(1);
		keyBits.clear(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	
	/**
	 * clear mouselocation, so that nothing is being pressed
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) {
			shooting = false;
		} else if (e.getButton() == 3) {
			int x = e.getX();
			int y = e.getY();
			rightClickInteract(x, y, players.get(uid));
			//TODO: network sync
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
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			shooting = true;
			mouseX = e.getX();
			mouseY = e.getY();
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
