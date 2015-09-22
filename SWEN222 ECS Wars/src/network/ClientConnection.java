package network;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 *
 * @author bogoiejova
 *
 */
public class ClientConnection extends Thread implements KeyListener{

	private Socket socket;
	private DataInputStream input;
	private DataOutputStream output;

	public ClientConnection(Socket socket){
		this.socket = socket;
	}

	public void run(){
		try{
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			boolean disconnect = false;
			while(!disconnect){
				int keyPressed = input.readInt();
				System.out.println(keyPressed);
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
				output.writeInt(1);
			}
			else if(keyCode == KeyEvent.VK_S){
				output.writeInt(2);
			}
			else if(keyCode == KeyEvent.VK_A){
				output.writeInt(3);
			}
			else if(keyCode == KeyEvent.VK_D){
				output.writeInt(4);
			}
		}
		catch(IOException ex){
			//Couldn't read button press, ignore
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
