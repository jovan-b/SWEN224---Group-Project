package network;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import characters.DavePlayer;
import main.Controller;
import main.GUIFrame;

/**
 * The client connection handles the user input and writes it to
 * the socket for the server to update what the player does to the
 * rest of the game
 *
 * @author bogoiejova
 *
 */
public class ClientConnection extends Thread implements KeyListener, MouseListener{

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;
	private Controller controller;

	public ClientConnection(Socket socket){
		this.socket = socket;
		controller = new Controller(this);
	}

	/**
	 * Runs the client side game and sends updates of key presses
	 */
	public void run(){
		try{
			//Create the socket input and output to write to for the server
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

			//Keep running the game until the player disconnects or loses connection to the server
			boolean disconnect = false;
			while(!disconnect){
				//Run the game
				//TODO: Show some feedback here from the input stream
			}
			socket.close(); //Player disconnected
		}
		catch(IOException e){
			System.err.println("I/O Error: " + e.getMessage());
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		try{
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_W){
				//Indicates player move up
				output.writeInt(1); 
			}
			else if(keyCode == KeyEvent.VK_S){
				//Indicates player move down
				output.writeInt(2);
			}
			else if(keyCode == KeyEvent.VK_A){
				//Indicates player move left
				output.writeInt(3);
			}
			else if(keyCode == KeyEvent.VK_D){
				//Indicates player move right
				output.writeInt(4);
			}
		}
		catch(IOException ex){
			//Couldn't read button press, ignore
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

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
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}