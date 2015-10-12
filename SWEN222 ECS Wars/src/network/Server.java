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
 * @author Jovan Bogoievski
 *
 */
public class Server extends Thread{

	private int port;
	private int numberOfClients;
	private ServerSocket server;
	private GUICanvas canvas;
	
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
				int uid = i;
				clientsConnected[i] = new ServerHandler(s, uid);
				i++;

				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());
				clients -= 1;

				if(clients == 0) {
					System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
					startMultiplayer(clientsConnected);
					System.out.println("ALL CLIENTS DISCONNECTED --- GAME OVER");
					break; // game over
				}
			}
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				//Should never get here
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Sets up a multiplayer game for each client
	 * @param clientsConnected
	 */
	public static void startMultiplayer(ServerHandler[] clientsConnected) {
		Socket socket;
		DataOutputStream output;

		//Go through the server handlers for each client to start their game
		for(int i = 0; i<clientsConnected.length; i++){
			clientsConnected[i].setServerHandlers(clientsConnected);
			clientsConnected[i].start();
			socket = clientsConnected[i].getSocket();
			try{
				output = new DataOutputStream(socket.getOutputStream());
				//Tells the player how many players are in the game and their user ID
				output.writeInt(clientsConnected.length);
				output.writeInt(i);
			}
			catch(IOException e){
				System.err.println("I/O error: " + e.getMessage());
			}
		}
		while(true){
			//do some stuff to run the game here
		}
	}
	
	public ServerSocket getServer(){
		return server;
	}
}
