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
	private ServerHandler[] serverHandlers;

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
						int direction = input.readInt();
						for(int i = 0; i<serverHandlers.length; i++){
							Socket socket = serverHandlers[i].getSocket();
							DataOutputStream socketOut = new DataOutputStream(socket.getOutputStream());
							socketOut.writeInt(uid);
							socketOut.writeInt(direction);
						}
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

	public Socket getSocket(){
		return socket;
	}

	public void setServerHandlers(ServerHandler[] serverHandlers){
		this.serverHandlers = serverHandlers;
	}
}
