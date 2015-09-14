package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.BitSet;

import characters.Player;

/**
 * Main controller for ECS Wars
 * Runs the main game loop which:
 * 		deals with user input
 * 		updates the game logic
 * 
 * @author Jah Seng Lee
 *
 */
public class Controller implements KeyListener{
	
	public static final double FRAME_RATE = 1.0/60;	//a 60th of a second
	public boolean isRunning = false;
	GUI gui;
	Player player;
	
	private BitSet keyBits = new BitSet(256);	//set of keys being pressed right now
	
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
		//deal with other objects
	}
	
	/**
	 * Updates player appropriately depending on current keys pressed
	 */
	private void dealWithInput() {
		if(isKeyPressed(KeyEvent.VK_RIGHT)){
			player.move("right");
		}
		if(isKeyPressed(KeyEvent.VK_LEFT)){
			player.move("left");
		}
		if(isKeyPressed(KeyEvent.VK_UP)){
			player.move("up");
		}
		if(isKeyPressed(KeyEvent.VK_DOWN)){
			player.move("down");
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
	 * Initialise the fields of this class
	 */
	private void initialise() {
		isRunning = true;
		gui = new GUI(this);
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
		keyBits.clear(e.getKeyCode());
	}

	//Don't care about this method
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	

}
