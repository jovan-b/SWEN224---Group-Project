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

import characters.Player;
import main.Controller;

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
					//Move up
					case 1:
						player.move("up");
						break;
					//Move down
					case 2:
						player.move("down");
						break;
					//Move left
					case 3:
						player.move("left");
						break;
					//Move right
					case 4:
						player.move("right");
						break;
					//Player shoots
					case 5:
						int x = input.readInt();
						int y = input.readInt();
						//player.shoot(x, y);
						System.out.println(x + ":"+ y);
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
		try{
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_W){
				//Indicates player move up
				output.writeInt(1);
				output.writeInt(1);
			}
			else if(keyCode == KeyEvent.VK_S){
				//Indicates player move down
				output.writeInt(1);
				output.writeInt(2);
			}
			else if(keyCode == KeyEvent.VK_A){
				//Indicates player move left
				output.writeInt(1);
				output.writeInt(3);
			}
			else if(keyCode == KeyEvent.VK_D){
				//Indicates player move right
				output.writeInt(1);
				output.writeInt(4);
			}
		}
		catch(IOException ex){
			//Couldn't read button press, ignore
		}
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
			controller.getPlayer(uid).inventoryItem(0).use(controller.getPlayer(uid));
		}
		if(e.getKeyCode() == KeyEvent.VK_2){
			controller.getPlayer(uid).inventoryItem(1).use(controller.getPlayer(uid));
		}
		if(e.getKeyCode() == KeyEvent.VK_3){
			controller.getPlayer(uid).inventoryItem(2).use(controller.getPlayer(uid));
		}
		if (e.getKeyCode() == KeyEvent.VK_MINUS){
			controller.getGUI().getCanvas().setViewScale(1);
			controller.scaleEverything(1);
		}
		if (e.getKeyCode() == KeyEvent.VK_EQUALS){
			controller.getGUI().getCanvas().setViewScale(2);
			controller.scaleEverything(2);
		}
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