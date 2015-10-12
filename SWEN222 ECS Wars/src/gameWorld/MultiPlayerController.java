package gameWorld;

import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.gameObjects.CharacterSpawner;
import main.SoundManager;
import network.ClientConnection;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.Socket;

public class MultiPlayerController extends Controller {
	
	private int numPlayers;

	public MultiPlayerController(Socket socket, int uid, int numPlayers) {
		super(uid);
		this.numPlayers = numPlayers;
		initialise();
		
		client = new ClientConnection(socket, this, uid);
	}
	
	@Override
	public void initialise(){
		super.initialise();
		
		for (int i=0; i<numPlayers; i++){
			players.add(new DavePlayer(rooms.get(0), (i+2)*24, 2*24));
		}
	}
	
	@Override
	public void startGame(){
		spawnPlayers();
		
		//SoundManager.playSong("battle_1.mp3");
		
		start();
	}
	
	public void spawnPlayers(){
		for(int i=0; i<players.size(); i++){
			if(i >= charSpawners.size()){break;}
			CharacterSpawner spawner = charSpawners.get(0);
			Player p = players.get(i);
			p.getCurrentRoom().removePlayer(p);
			p.setCurrentRoom(spawner.getRoom(), spawner.getX(), spawner.getY());
			spawner.getRoom().addPlayer(p);
		}

	}

	@Override
	public void update(){
		client.dealWithInput();
		//checkToolTip();
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
			//controller.getPlayer(uid).rotateViewLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_E){
			this.getGUI().getCanvas().rotateViewRight();
			//controller.getPlayer(uid).rotateViewRight();
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
		keyBits.clear(e.getKeyCode());
	}

	public void keyTyped(KeyEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
