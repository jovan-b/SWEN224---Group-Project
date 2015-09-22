package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The server handler reads the actions of the client
 * then updates to the server to display their action
 * for everyone else in the game.
 * 
 * @author Jovan Bogoievski
 *
 */
public class ServerHandler extends Thread{
	
	private Socket socket;
	private int uid;
	private DataInputStream input;
	private DataOutputStream output;
	
	public ServerHandler(Socket socket, int uid){
		this.socket = socket;
		this.uid = uid;
	}
	
	/**
	 * Runs the game taking inputs from the client to send to the server
	 * to update the main game
	 */
	@Override
	public void run(){
		try{
			//Create the socket input and output to write to the client
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
			boolean running = true;
			
			//Continue to run the game reading the clients actions
			while(running){
				int action = input.readInt();
				switch(action){
					//Move up
					case 1:
						System.out.println(uid + " move up");
						break;
					//Move down	
					case 2:
						System.out.println(uid + " move down");
						break;
					//Move left	
					case 3:
						System.out.println(uid + " move left");
						break;
					//Move right
					case 4:
						System.out.println(uid + " move right");
						break;
				}
			}
			socket.close();
		}
		catch(IOException e){
			System.out.println("CLEINT " + uid + "DISCONNECTED");
			//TODO: Remove the player from the game
		}
		finally{
			try{
				//Disconnect player
				socket.close();
			}
			catch(IOException e){
				//Do nothing
			}
		}
	}
	
}
