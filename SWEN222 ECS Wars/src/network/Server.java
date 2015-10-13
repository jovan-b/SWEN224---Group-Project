package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import gui.GUICanvas;

/**
 * Main thread that runs the server for clients to join
 * 
 * Keeps running until no more clients are in the game.
 * 
 * @author Jovan Bogoievski 300305140
 *
 */
public class Server extends Thread{

	private int port;
	private int numberOfClients;
	private ServerSocket server;
	private GUICanvas canvas;
	private ServerHandler[] clientsConnected;
	
	public Server(int port, int numberOfClients, GUICanvas canvas){
		this.port = port;
		this.numberOfClients = numberOfClients;
		this.canvas = canvas;
	}
	
	@Override
	public void run(){
		try {
			int clients = numberOfClients;
			//Create the server socket
			server = new ServerSocket(port);
			JOptionPane.showMessageDialog(canvas, "Server started on " +
					server.getInetAddress() + ":" +
					server.getLocalPort());
			//Create an array of server handlers for every client
			ServerHandler[] clientsConnected = new ServerHandler[clients];
			int i = 0;
			while (true) {
				//Wait for a client to connect
				Socket s = server.accept();
				
				//Give the client a user ID and add create a new server handler for them
				int uid = i;
				clientsConnected[i] = new ServerHandler(s, uid);
				i++;

				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());
				clients -= 1;
				
				//When all clients have joined, start the game.
				if(clients == 0) {
					System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
					startMultiplayer(clientsConnected);
					System.out.println("ALL CLIENTS DISCONNECTED --- GAME OVER");
					break; // game over
				}
			}
		} catch(IOException e) {
			JOptionPane.showMessageDialog(canvas, "Error: address already "
					+ "in use");
		}
	}
	
	/**
	 * Sets up a multiplayer game for each client
	 * @param clientsConnected
	 */
	public void startMultiplayer(ServerHandler[] clientsConnected) {
		Socket socket;
		DataOutputStream output;
		try{
			//Go through the server handlers for each client to start their game
			for(int i = 0; i<clientsConnected.length; i++){
				clientsConnected[i].setServerHandlers(clientsConnected);
				clientsConnected[i].start();
				socket = clientsConnected[i].getSocket();
		
				output = new DataOutputStream(socket.getOutputStream());
				//Tells the player how many players are in the game and their user ID
				output.writeInt(clientsConnected.length);
				output.writeInt(i);
			}
			while(clientsConnected(clientsConnected)){
				//Keep running the game
			}
		} catch(IOException e){
			//Will not reach here
			e.printStackTrace(System.err);
		} finally{
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Checks if there's still players connected to the server
	 * @param clientsConnected
	 * @return true if players in the server, false if not
	 */
	private boolean clientsConnected(ServerHandler[] clientsConnected) {
		for(ServerHandler sh: clientsConnected){
			if(!sh.isDisconnected()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the server
	 * @return
	 */
	public ServerSocket getServer(){
		return server;
	}
}
