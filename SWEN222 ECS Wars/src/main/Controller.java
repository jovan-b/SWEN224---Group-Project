package main;

import gameObjects.Item;
import gameObjects.Room;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

import characters.Player;
import characters.DavePlayer;

/**
 * Main controller for ECS Wars
 * Runs the main game loop which:
 * 		deals with user input
 * 		updates the game logic
 * 
 * @author Jah Seng Lee
 *
 */
public class Controller implements KeyListener, MouseListener{
	
	public static final double FRAME_RATE = 1.0/60;	//a 60th of a second
	public boolean isRunning = false;

	private GUIFrame gui;
	private Player player;
	private Set<Room> rooms;
	
	private BitSet keyBits = new BitSet(256);	//set of keys being pressed right now
	private int[] mouseLocation = new int[2];	//position of mouse if it is being clicked
												//mouseLocation[0] is x
												//mouseLocation[1] is y
	
	public Controller(){
		initialise();
		run(FRAME_RATE);
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
	private void run(double frameRate) {

		//convert time to seconds
		double nextTime = (double)System.nanoTime()/1000000000.0;
		
		while(isRunning){
			//convert time to seconds
			double currentTime = (double)System.nanoTime()/1000000000.0;
			
			if(currentTime >= nextTime){
				//assign time for the next update
				nextTime += frameRate;
				update();
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
	 * Update the game logic
	 * Take in user input and call appropriate methods
	 * Update the positions of non-input deterministic objects
	 * 
	 */
	private void update() {
		dealWithInput();//deal with user input
		updateAndCollide();//deal with other objects and with collision detection
	}
	
	private void updateAndCollide() {
		player.update();
	}

	/**
	 * Updates player appropriately depending on current keys pressed
	 */
	private void dealWithInput() {
		// Player Movement
		if(isKeyPressed(KeyEvent.VK_RIGHT) || isKeyPressed(KeyEvent.VK_D)){
			player.move("right");
		}
		if(isKeyPressed(KeyEvent.VK_LEFT) || isKeyPressed(KeyEvent.VK_A)){
			player.move("left");
		}
		if(isKeyPressed(KeyEvent.VK_UP) || isKeyPressed(KeyEvent.VK_W)){
			player.move("up");
		}
		if(isKeyPressed(KeyEvent.VK_DOWN) || isKeyPressed(KeyEvent.VK_S)){
			player.move("down");
		}
		if(isLeftMousePressed()){
			player.shoot(mouseLocation[0], mouseLocation[1]);
		}
	}
	
	/**
	 * Returns true if key 'keycode' is being pressed 
	 * Otherwise returns false
	 * 
	 * @param keyCode
	 * @return
	 */
	private boolean isKeyPressed(int keyCode) {
		return keyBits.get(keyCode);
	}
	
	/**
	 * Returns true if user has pressed left mouse button and not released it
	 * Otherwise returns false (left mouse button is not being pressed down)
	 */
	private boolean isLeftMousePressed(){
		return mouseLocation[0] != 0 && mouseLocation[1] != 0;
	}

	/**
	 * Initialise the fields of this class
	 */
	private void initialise() {
		isRunning = true;
		rooms = new HashSet<>();
		Room room = new Room("Classroom");
		rooms.add(room);
		player = new DavePlayer(room, 48, 48);
		room.addPlayer(player);
		gui = new GUIFrame(this, player);
		
		SoundManager.playSong("test.wav");
	}

	/**
	 * If key is pressed by user, add to the keyBits set
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		keyBits.set(e.getKeyCode());
	}

	/**
	 * If key is released by user, clear from the keyBits set
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// View Rotation
		if(e.getKeyCode() == KeyEvent.VK_Q){
			player.rotateViewLeft();
		}
		if(e.getKeyCode() == KeyEvent.VK_E){
			player.rotateViewRight();
		}
		if(e.getKeyCode() == KeyEvent.VK_1){
			gui.canvas.setViewScale(1);
			scaleEverything(1);
		}
		if(e.getKeyCode() == KeyEvent.VK_2){
			gui.canvas.setViewScale(2);
			scaleEverything(2);
		}
		keyBits.clear(e.getKeyCode());
	}

	private void scaleEverything(int scale) {
		GUICanvas c = gui.canvas;
		int viewScale = c.getViewScale();
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
			Item[][] contents = r.getContents();
			for (int i = 0; i < contents.length; i++){
				for (int j = 0; j < contents[0].length; j++){
					for (int v = 0; v < 4; v++){
						image = contents[i][j].getImage(v);
						if (image != null){
							contents[i][j].setScaledImage(v, scaleImage(image, c, scale));
						}
					}
				}
			}
			
			// scale players
			for (Player p : r.getPlayers()){
				images = p.getImages();
				scaled = new Image[4][3];
				for (int i = 0; i < 4; i++){
					for (int j = 0; j < 3; j++){
						scaled[i][j] = scaleImage(images[i][j], c, scale);
					}
				}
				p.setScaledImages(scaled);
			}
		}
	}
	
	public Image scaleImage(Image image, GUICanvas c, int scale){
		return image.getScaledInstance(image.getWidth(c)*scale, image.getHeight(c)*scale, Image.SCALE_FAST);
	}

	//Don't care about this method
	@Override
	public void keyTyped(KeyEvent e) {

		
	}
	
	//don't care about this method
	@Override
	public void mouseClicked(MouseEvent e) {	
	}	
	//don't care about this method
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	//don't care about this method
	@Override
	public void mouseExited(MouseEvent e) {	
	}

	/**
	 * When mouse event is fired
	 * 	clear mouselocation
	 * 	get x, y position of mouse
	 * 	add new xy to mouseLocation
	 * 
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		mouseLocation[0] = e.getX();
		mouseLocation[1] = e.getY();
	}

	/**
	 * clear mouselocation, so that nothing is being pressed
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseLocation = new int[2];
	}

	/**
	 * Returns the player
	 * @return
	 */
	public Player getPlayer(){
		return player;
	}
	
	public static void main(String[] args){
		new Controller();
	}

}
