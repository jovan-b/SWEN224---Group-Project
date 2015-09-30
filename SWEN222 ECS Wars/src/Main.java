import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.Controller;
import network.ClientConnection;
import network.ServerHandler;

/**
 * Main class to run the game off of.
 * Starts in single player if no cmd line arguements are specified,
 * or starts the starts the server for clients to join.
 *
 * @author Jovan Bogoievski
 *
 */
public class Main {

	/**
	 * Parses the command line arguments to choose which mode to start the game in
	 *
	 * Design will probably change after more parts of the program are functional
	 * @param args
	 */
	public static void main(String[] args) {
		boolean server = false; //Server mode, start as false unless specified in the cmd line arguments
		String url = null; //The url which the client connects to
		int clients = 0; //Number of clients for multiplayer
		int port = 7777; //Default port TODO: Be able to change the port

		for (int i = 0; i != args.length; ++i) {
			if (args[i].startsWith("-")) {
				String arg = args[i];
				if(arg.equals("-help")) {
					//Display the help menu
					help();
					System.exit(0);
				} else if(arg.equals("-server")) {
					//Server mode, get how many clients by the number specified
					server = true;
					//TODO: check if user puts a number
					clients = Integer.parseInt(args[++i]);
				} else if(arg.equals("-connect")) {
					url = args[++i];
				}
			}
			//Else nothing yet, no flags change so start the game in single player mode
		}

		//Check if the user tried to connect to a url while in server mode
		if(url != null && server) {
			System.out.println("Cannot be a server and connect to another server!");
			System.exit(1);
		}

		try {
			if(server) {
				// Run in Server mode
				runServer(port, clients);
			} else if(url != null) {
				// Run in client mode
				runClient(url,port);
			} else {
				// single user game
				singlePlayer();
			}
		} catch(IOException ioe) {
			//Something went wrong when connecting
			System.out.println("I/O error: " + ioe.getMessage());
			ioe.printStackTrace();
			System.exit(1);
		}

		System.exit(0);

	}

	/**
	 * Displays what the command line arguments do and how to use them
	 */
	public static void help() {
		System.out.println("\n"
				+ "-server <n> : creates a server for clients to join with n number of clients\n"
				+ "-connect <url> : connects to the server with the given url.\n"
				+ "					use localhost as the url to connect to connect to the server on your own machine\n");
	}

	/**
	 * Connects the client to the server and initialises them as a player
	 * @param url
	 * @param port
	 * @throws IOException
	 */
	public static void runClient(String url, int port) throws IOException {
		//Create a new socket for the client and start the player in the game
		Socket s = new Socket(url, port);
		System.out.println("CLIENT CONNECTED TO " + url + ":" + port);
		ClientConnection client = new ClientConnection(s);
		client.run();
	}

	/**
	 * Starts the server and waits for client connections, then starts the game
	 * in multiplayer mode
	 * @param port
	 * @param clients
	 */
	public static void runServer(int port, int clients) {
		System.out.println("SERVER LISTENING ON PORT " + port);
		System.out.println("SERVER AWAITING " + clients + " CLIENTS");
		try {
			//Create the server socket
			ServerSocket server = new ServerSocket(port);
			//Create an array of server handlers for every client
			ServerHandler[] clientsConnected = new ServerHandler[clients];
			int i = 0;
			while (true) {
				//Wait for a client to connect
				Socket s = server.accept();
				int uid = i;
				clientsConnected[i] = new ServerHandler(s, uid);
				//clientsConnected[i].start();
				i++;

				System.out.println("ACCEPTED CONNECTION FROM: " + s.getInetAddress());
				clients -= 1;

				if(clients == 0) {
					System.out.println("ALL CLIENTS ACCEPTED --- GAME BEGINS");
					multiPlayer(clientsConnected);
					System.out.println("ALL CLIENTS DISCONNECTED --- GAME OVER");
					break; // game over
				}
			}

			server.close();
		} catch(IOException e) {
			System.err.println("I/O error: " + e.getMessage());
		} finally {
		}
	}

	/**
	 * Set up a single player game
	 */
	public static void singlePlayer() {
		new Controller();
	}

	/**
	 * Sets up a multiplayer game for each client
	 * @param clientsConnected
	 */
	public static void multiPlayer(ServerHandler[] clientsConnected) {
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


}
