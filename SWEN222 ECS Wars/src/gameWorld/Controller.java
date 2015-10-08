package gameWorld;

import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;
import gameWorld.characters.nonplayer.strategy.RespawnStrategy;
import gameWorld.characters.nonplayer.strategy.SentryCombatStrategy;
import gameWorld.characters.nonplayer.strategy.WanderingMerchantStrategy;
import gameWorld.gameObjects.*;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.containers.Pouch;
import gui.GUICanvas;
import gui.GUIFrame;
import main.SoundManager;
import main.saveAndLoad.SaveManager;
import network.ClientConnection;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Main controller for ECS Wars
 * Runs the main game loop which:
 * 		deals with user input
 * 		updates the game logic
 * 
 * @author Jah Seng Lee
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public abstract class Controller extends Thread implements KeyListener, MouseListener, MouseMotionListener{
	
	public static final double FRAME_RATE = 1.0/60;	//a 60th of a second
	protected boolean isRunning = false;
	
	protected int uid;
	
	protected GUIFrame gui;
	protected List<Player> players;

	protected ClientConnection client;
	
	protected ArrayList<Room> rooms;
	protected Set<Door> doors;
	protected List<ItemSpawner> itemSpawners;
	protected List<Item> itemsToSpawn;
	protected List<CharacterSpawner> charSpawners = new ArrayList<>();
	
	
	protected BitSet keyBits = new BitSet(256);	//set of keys being pressed right now
	protected int[] mouseLocation = new int[2];	//position of mouse if it is being clicked
												//mouseLocation[0] is x
												//mouseLocation[1] is y
	protected int mouseX = 0;
	protected int mouseY = 0;
		
	/**
	 * Controller constructor for a singeplayer game
	 */
	public Controller(int uid){
		isRunning = true;
		rooms = new ArrayList<>();
		doors = new HashSet<>();
		itemSpawners = new ArrayList<>();
		itemsToSpawn = new ArrayList<>();
		players = new ArrayList<Player>();
		
		this.uid = uid;
	}
	
	/**
	 * Controller constructor for a multiplayer client
	 */
	public Controller(ClientConnection client, int numberOfClients, int uid){
		this.client = client;
		this.uid = uid;
		MPInitialise(client, client, client, numberOfClients, uid);
	}

	/**
	 * Initialises the fields of a multiplayer game
	 * @param key The KeyListener for the game
	 * @param mouse The MouseListener for the game
	 * @param mouse2 The MouseMotionListener for the game
	 * @param numberOfClients The number of clients in the game
	 * @param uid The user id
	 */
	public void MPInitialise(KeyListener key, MouseListener mouse, MouseMotionListener mouse2,
			int numberOfClients, int uid) {
		
		spawnPlayers();
		
		for(Player p: players){
			p.setCanvas(gui.getCanvas());
		}
				
		SoundManager.playSong("battle_1.mp3");
	}
	
	/**
	 * Initialise the pre-game fields of this class
	 */
	protected void initialise() {
		setupRooms();
		loadItemsToSpawn();
		setupSpawnItems();
	}

	/**
	 * Initialise the fields of the game
	 */
	public void initialiseGame() {
		isRunning = true;
		Room room = rooms.get(0); //FIXME
		//room.addPlayer(player);
		//player.setCanvas(gui.getCanvas());
		
		NonPlayer npc = new NonPlayer(room, 5*24, 7*24, new WanderingMerchantStrategy());
		npc.setStrategy(NonPlayer.Events.DEATH, new RespawnStrategy(5000));
		room.addNPC(npc);
		
		SaveManager.saveGame(this, "test_save.xml");
	}
	
	public void startGame(){
		spawnPlayers();
		SoundManager.playSong("battle_1.mp3");
		
		this.start();
	}
	
	/**
	 * Main game loop
	 * Should update the game every frameRate seconds
	 * If it's time to update the logic:
	 * 		update logic
	 * 		check if it's time to redraw
	 * 
	 * Otherwise wait till it's time to update
	 * 
	 * @param frameRate seconds per frame
	 */
	public void run() {

		//convert time to seconds
		double nextTime = (double)System.nanoTime()/1000000000.0;
		
		while(isRunning){
			//convert time to seconds
			double currentTime = (double)System.nanoTime()/1000000000.0;
			
			if(currentTime >= nextTime){
				//assign time for the next update
				nextTime += FRAME_RATE;
				update();
				// check if game is over
				if(checkForWinner() != null){
					gui.getCanvas().setWinnerView(true);
				}
				if(currentTime < nextTime) gui.draw();
			}
			else{
				// calculate the time to sleep
				int sleepTime = (int) (1000.0 * (nextTime - currentTime));
				// sanity check
				if (sleepTime > 0) {
					// sleep until the next update
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						// do nothing
					}
				}
			}
		}
	}

	/**
	 * Checks if any player has won the game, and if they have,
	 * return the winning player.
	 * @return The Player that has won, or null if no player has won.
	 */
	private Player checkForWinner() {
		for(Player p : players){
			if(p.getPoints() >= PointValues.END_GAME_TARGET){
				return p;
			}
		}
		return null;
	}

	/**
	 * Update the game logic
	 * Take in user input and call appropriate methods
	 * Update the positions of non-input deterministic objects
	 * 
	 */
	protected void update() {
		//Hook to run on game tick
	}

	
	/**
	 * Parses all the room objects in the game.
	 */
	private void setupRooms(){
		try {
			Scanner s = new Scanner(new File("Resources"+File.separator+"RoomIndex.txt"));
			String roomName;
			while (s.hasNextLine()){
				roomName = s.nextLine();
				rooms.add(new Room(roomName, this));
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error loading RoomIndex file: " + e.getMessage());
		}
	}

	/**
	 * Scales all drawn objects based on the current view scale.
	 * @param scale The scale (1 or 2) to update the game to.
	 */
	public void scaleEverything(int scale) {
		GUICanvas c = gui.getCanvas();
		c.scaleUI();
		Image image;
		for (Room r : rooms){
			Image[][] images = r.getImages();
			Image[][] scaled = new Image[4][2];
			for (int i = 0; i < 4; i++){
				for (int j = 0; j < 2; j++){
					scaled[i][j] = scaleImage(images[i][j], c, scale);
				}
			}
			r.setScaledImages(scaled);
			
			// scale items
			Item item;
			Item[][] contents = r.getContents();
			for (int i = 0; i < contents.length; i++){
				for (int j = 0; j < contents[0].length; j++){
					item = contents[i][j];
					for (int v = 0; v < 4; v++){
						image = item.getImage(v);
						if (image != null){
							contents[i][j].setScaledImage(v, scaleImage(image, c, scale));
						}
					}
					if (item instanceof Container){
						for (Item contentsItem : ((Container)item).getContents()){
							if (contentsItem != null){
								image = contentsItem.getImage(0);
								contentsItem.setScaledImage(0, scaleImage(image, c, scale));
								image = contentsItem.getImage(1);
								contentsItem.setScaledImage(1, scaleImage(image, c, scale));
							}
						}
					}
				}
			}
			
			// scale players
			for (Player p : r.getAllCharacters()){
				images = p.getImages();
				scaled = new Image[4][3];
				for (int i = 0; i < 4; i++){
					for (int j = 0; j < 3; j++){
						scaled[i][j] = scaleImage(images[i][j], c, scale);
					}
				}
				p.setScaledImages(scaled);
				for (Item i : p.getInventory()){
					if (i != null){
						image = i.getImage(0);
						i.setScaledImage(0, scaleImage(image, c, scale));
						image = i.getImage(1);
						i.setScaledImage(1, scaleImage(image, c, scale));
						if (i instanceof Container){
							for (Item contentsItem : ((Container)i).getContents()){
								if (contentsItem != null){
									image = contentsItem.getImage(0);
									contentsItem.setScaledImage(0, scaleImage(image, c, scale));
									image = contentsItem.getImage(1);
									contentsItem.setScaledImage(1, scaleImage(image, c, scale));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Resizes an image based on the given scale.
	 * @param image The image to resize
	 * @param c The canvas the image will be drawn on
	 * @param scale The scale (1 or 2) to resize by
	 * @return The resized image
	 */
	public Image scaleImage(Image image, GUICanvas c, int scale){
		return image.getScaledInstance(image.getWidth(c)*scale, image.getHeight(c)*scale, Image.SCALE_FAST);
	}

	/**
	 * Loads all items that will be spawned and stores them in
	 * the itemsToSpawn List.
	 */
	private void loadItemsToSpawn() {
		try	{
			Scanner s = new Scanner(new File("Resources"+File.separator+"ItemsToSpawn.txt"));
			// iterate over file
			while(s.hasNextLine()){
				String nextLine = s.nextLine();
				Item toAdd = null;
				// create item to spawn
				switch(nextLine){
				case "KeyCard" : toAdd = new KeyCard(); break;
				case "Torch" : toAdd = new Torch(); break;
				case "Pouch" : toAdd = new Pouch(); 
							for (int i = 0; i < 3; i++){
								itemSpawners.add((ItemSpawner) toAdd);
							} break;
				case "Map" : toAdd = new Map(); break;
				case "Treasure" : toAdd = new SmallTreasure(); break;
				}
				// add the item if it's not null, otherwise print error message
				if(toAdd != null){
					itemsToSpawn.add(toAdd);
				} else {
					System.out.println("Parse error: could not parse spawn item - "+ nextLine);
				}
				
			}
		} catch(IOException e){
			System.out.println("Error loading spawn items: "+e.getMessage());
		}
	}
	
	/**
	 * Randomly distributes spawned items.
	 */
	private void setupSpawnItems() {
		// shuffle spawn item lists
		Collections.shuffle(itemSpawners);
		Collections.shuffle(itemsToSpawn);
		// while there is an item or container left, add item to container
		for (int i = 0; i < itemsToSpawn.size(); i++){
			if (i >= itemSpawners.size()){
				System.out.println("Ran out of item spawners");
				break;
			}
			// get random container and item
			ItemSpawner holder = itemSpawners.get(i);
			Item toSpawn = itemsToSpawn.get(i);
			// add item if there's room
			if(holder.remainingCapacity() > 0){
				holder.addSpawnItem(toSpawn);
			}
		}
	}
	
	/**
	 * Spawns a given item at a random item spawn location
	 * @param itemToSpawn
	 */
	public void spawnItem(Item itemToSpawn){
		Collections.shuffle(itemSpawners);
		ItemSpawner holder = itemSpawners.get(0);
		while (holder instanceof Pouch || holder.remainingCapacity() <= 0){
			Collections.shuffle(itemSpawners);
			holder = itemSpawners.get(0);
		}
		holder.addSpawnItem(itemToSpawn);
	}

	/**
	 * Distributes players over the game world.
	 */
	private void spawnPlayers(){
		Collections.shuffle(players);
		Collections.shuffle(charSpawners);
		for(int i=0; i<players.size(); i++){
			if(i >= charSpawners.size()){break;}
			CharacterSpawner spawner = charSpawners.get(i);
			Player p = players.get(i);
			p.getCurrentRoom().removePlayer(p);
			p.setCurrentRoom(spawner.getRoom(), spawner.getX(), spawner.getY());
			spawner.getRoom().addPlayer(p);
		}
	}
	
	/**
	 * Adds an ItemSpawner to the list of all such types in
	 * the game.
	 * @param spawner The ItemSpawner to add.
	 */
	public void addItemSpawner(ItemSpawner spawner){
		itemSpawners.add(spawner);
	}

	/**
	 * Gets all the doors in the game.
	 * @return A Set of all doors in the game
	 */
	public Set<Door> getDoors() {
		return doors;
	}
	
	public Room getRoom(String roomName) {
		for(Room r: rooms){
			if(r.getName().equals(roomName)){
				return r;
			}
		}
		
		return null;
	}
	
	public List<Player> getPlayers() {
		return players;
	}


	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public void setCurrentPlayer(Player player) {
		this.uid = players.indexOf(player);
	}
	
	/**
	 * Returns the player with the specified user id.
	 * @param uid The user id of the player
	 * @return The player with the given uid
	 */
	public Player getPlayer(int uid){
		return players.get(uid);
	}
	
	/**
	 * Returns the GUIFrame this game is using.
	 * @return The current GUIFrame
	 */
	public GUIFrame getGUI(){
		return gui;
	}
	
	/**
	 * Sets the frame for this controller
	 * @param gui
	 */
	public void setGUI(GUIFrame gui){
		this.gui = gui;
	}

	/**
	 * Returns all the rooms in the game.
	 * @return An ArrayList containing every room in the game
	 */
	public ArrayList<Room> getRooms(){
		return rooms;
	}

	public void addCharacterSpawner(CharacterSpawner characterSpawner) {
		charSpawners.add(characterSpawner);
	}

	public CharacterSpawner getSpawner() {
		Collections.shuffle(charSpawners);
		return charSpawners.get(0);
	}
	
	


}
