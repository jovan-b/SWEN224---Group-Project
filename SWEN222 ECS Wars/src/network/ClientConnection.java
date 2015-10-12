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
import java.net.SocketException;
import java.util.BitSet;

import gameWorld.Controller;
import gameWorld.MultiPlayerController;
import gameWorld.Room;
import gameWorld.SinglePlayerController;
import gameWorld.characters.Player;
import gui.GUICanvas;
import gui.GUIFrame;

/**
 * The client connection handles the user input and writes it to
 * the socket for the server to update what the player does to the
 * rest of the game
 *
 * @author Jovan Bogoievski
 *
 */
public class ClientConnection extends Thread{

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private MultiPlayerController controller;
	private int uid;

	public ClientConnection(Socket socket, MultiPlayerController controller, int uid){
		this.socket = socket;
		this.controller = controller;
		this.uid = uid;
		try {
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	/**
	 * Runs the client side game and sends updates of key presses
	 */
	@Override
	synchronized public void run(){
		try{
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
					int direction = input.readInt();
					int roomNumber = input.readInt();
					Room newRoom = controller.getRooms().get(roomNumber);
					player.setPosition(x, y, direction, newRoom);
					break;
				//Shoot
				case 2:
					int mouseX = input.readInt();
					int mouseY = input.readInt();
					player.shoot(mouseX, mouseY);
					break;
				case 3:
					//Tell the server handler to remove writing to the disconnected player
					controller.getPlayer(user).disconnect();
					output.writeInt(3);
					output.writeInt(user);
					break;
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

	public void dealWithInput() {
		try{
			boolean posChanged = false;
			GUIFrame gui = controller.getGUI();
			Player player = controller.getPlayer(uid);
			// Player Movement
			if(isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_D)){
				player.move(GUICanvas.convertStringToDir("right", gui.getCanvas().getViewDirection()));
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_A)){
				player.move(GUICanvas.convertStringToDir("left", gui.getCanvas().getViewDirection()));
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_UP) || isKeyPressed(KeyEvent.VK_W)){
				player.move(GUICanvas.convertStringToDir("up", gui.getCanvas().getViewDirection()));
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_DOWN) || isKeyPressed(KeyEvent.VK_S)){
				player.move(GUICanvas.convertStringToDir("down", gui.getCanvas().getViewDirection()));
				posChanged = true;
			}
			if(isKeyPressed(KeyEvent.VK_SHIFT)){
				player.setSpeedModifier(2);
			}
			
			if (controller.isShooting()) {
				int mx = controller.getMouseX();
				int my = controller.getMouseY();
				player.shoot(mx, my);
				//Write that the player is shooting at the mouse positions
				output.writeInt(2);
				output.writeInt(mx);
				output.writeInt(my);
			}

			//If there players position has changed, send there new position to every client in the game
			if(posChanged){
				int x = player.getX();
				int y = player.getY();
				int direction = player.getFacing();
				int roomNumber = controller.getRooms().indexOf(player.getCurrentRoom());
				output.writeInt(1);
				output.writeInt(x);
				output.writeInt(y);
				output.writeInt(direction);
				output.writeInt(roomNumber);
			}

		} catch(IOException e){
			e.printStackTrace();
		}
	}

	private boolean isKeyPressed(int keyCode) {
		return controller.getKeysPressed().get(keyCode);
	}

//	@Override
//	public void mousePressed(MouseEvent e) {
//		try{
//			if (e.getButton() == 1){
//				//Tell the server they sent a mouse click
//				output.writeInt(2);
//				output.writeInt(e.getX());
//				output.writeInt(e.getY());
//			}
//		} catch(IOException ex){
//			//Couldn't read mouse press, ignore
//		}
//	}

//	@Override
//	public void mouseDragged(MouseEvent e) {
//		try{
//			if (e.getButton() == 1){
//				//Tell the server they sent a mouse click
//				output.writeInt(2);
//				output.writeInt(e.getX());
//				output.writeInt(e.getY());
//			}
//		} catch(IOException ex){
//			//Couldn't read mouse press, ignore
//		}
//	}
}