package gameWorld;

import gameWorld.characters.DavePlayer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MultiPlayerController extends Controller {
	
	private int numPlayers;

	public MultiPlayerController(int uid, int numPlayers) {
		super(uid);
		this.numPlayers = numPlayers;
	}
	
	@Override
	public void initialise(){
		super.initialise();
		
		for (int i=0; i<numPlayers; i++){
			players.add(new DavePlayer(rooms.get(0), 2*24, 2*24));
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

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
