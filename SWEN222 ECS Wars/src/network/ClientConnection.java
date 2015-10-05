package network;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.BitSet;

import gameWorld.Controller;
import gameWorld.characters.Player;

/**
 * The client connection handles the user input and writes it to
 * the socket for the server to update what the player does to the
 * rest of the game
 *
 * @author Jovan Bogoievski
 *
 */
public class ClientConnection extends Thread implements KeyListener, MouseListener, MouseMotionListener{

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private Controller controller;
	private int uid;
	private BitSet keyBits = new BitSet(256);

	public ClientConnection(Socket socket){
		this.socket = socket;
	}

	/**
	 * Runs the client side game and sends updates of key presses
	 */
	@Override
	public void run(){
		try{
			//Create the socket input and output to write to for the server
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			//Waits for the server to send an amount of players in the game
			int numberOfPlayers = input.readInt();
			uid = input.readInt();

			//Keep running the game until the player disconnects or loses connection to the server
			controller = new Controller(this, numberOfPlayers, uid);

			//While the game is running, take incoming updates of other clients
			while(true){
				//Read which player is trying to perform an action
				int user = input.readInt();
				Player player = controller.getPlayer(user);
				int action = input.readInt();
				switch(action){
					//Move
					case 1:
						int x = input.readInt();
						int y = input.readInt();
						player.setXY(x, y);
						break;
					case 5:
						int mouseX = input.readInt();
						int mouseY = input.readInt();
						player.shoot(mouseX, mouseY);
				}
			}
		}
		catch(IOException e){
			System.err.println("I/O Error: " + e.getMessage());
			e.printStackTrace(System.err);
		}
		finally{
			try{
				//Disconnect player
				socket.close();
			} catch(IOException e){
				//Do nothing
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyBits.set(e.getKeyCode());
	}

	public void dealWithInput() {
		try{
			boolean posChanged = false;
			// Player Movement
			if(isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_D)){
				controller.getPlayer(uid).move("right");
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_A)){
				controller.getPlayer(uid).move("left");
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_UP) || isKeyPressed(KeyEvent.VK_W)){
				controller.getPlayer(uid).move("up");
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_DOWN) || isKeyPressed(KeyEvent.VK_S)){
				controller.getPlayer(uid).move("down");
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_SHIFT)){
				controller.getPlayer(uid).setSpeedModifier(2);
			} else {
				controller.getPlayer(uid).setSpeedModifier(1);
			}
	//		if(isLeftMousePressed()){
	//			player.shoot(mouseLocation[0], mouseLocation[1]);
	//		}

			if(posChanged){
				int x = controller.getPlayer(uid).getX();
				int y = controller.getPlayer(uid).getY();
				output.writeInt(1);
				output.writeInt(x);
				output.writeInt(y);
			}

		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private boolean isKeyPressed(int keyCode) {
		return keyBits.get(keyCode);
	}

	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_Q){
			controller.getPlayer(uid).rotateViewLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_E){
			controller.getPlayer(uid).rotateViewRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_1){
			controller.getPlayer(uid).inventoryItemAt(0).use(controller.getPlayer(uid), controller);
		}
		if(e.getKeyCode() == KeyEvent.VK_2){
			controller.getPlayer(uid).inventoryItemAt(1).use(controller.getPlayer(uid), controller);
		}
		if(e.getKeyCode() == KeyEvent.VK_3){
			controller.getPlayer(uid).inventoryItemAt(2).use(controller.getPlayer(uid), controller);
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS){
			controller.getGUI().getCanvas().setViewScale(1);
			controller.scaleEverything(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_EQUALS){
			controller.getGUI().getCanvas().setViewScale(2);
			controller.scaleEverything(2);
		}
		keyBits.clear(e.getKeyCode());
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		try{
			if (e.getButton() == 1){
				//Tell the server they sent a mouse click
				output.writeInt(2);
				output.writeInt(e.getX());
				output.writeInt(e.getY());
			}
		} catch(IOException ex){
			//Couldn't read mouse press, ignore
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		try{
			if (e.getButton() == 1){
				//Tell the server they sent a mouse click
				output.writeInt(2);
				output.writeInt(e.getX());
				output.writeInt(e.getY());
			}
		} catch(IOException ex){
			//Couldn't read mouse press, ignore
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}