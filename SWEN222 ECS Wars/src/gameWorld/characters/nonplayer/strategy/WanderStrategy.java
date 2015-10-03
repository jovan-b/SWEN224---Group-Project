package gameWorld.characters.nonplayer.strategy;

import java.util.Timer;
import java.util.TimerTask;

public class WanderStrategy extends WaitStrategy {
	public static final int MAX_WAIT_TIME = 2000;
	public static final int MAX_WANDER_TIME = 500;
	
	public static final int WAIT = 0;
	public static final int WANDER = 1;
	
	//alternate between wait and wander
	private int state = WAIT;
	private int direction = 0;
	
	private Timer timer = new Timer();
	private boolean activeTimer = false;
	
	@Override
	public void update(){
		//Alternate between two states
		if (state == WAIT){
			
			if (activeTimer){return;}
			startTimer(WANDER, MAX_WAIT_TIME);
			
		} else if (state == WANDER) {
			if (!activeTimer){
				//Start a timer, and pick a random direction to travel
				startTimer(WAIT, MAX_WANDER_TIME);
				direction = (int)(Math.random()*4);
			}
			
			npc.move(directionToString(direction));
		}
	}
	
	private void startTimer(int goalState, int max_delay){
		if (!activeTimer){
			activeTimer = true;
			timer.schedule(new TimerTask(){

			@Override
			public void run() {
				state = goalState;
				activeTimer = false;
			}
			
			}, (int)(Math.random()*max_delay));
		}
	}
	
	/**
	 * Converts the direction int to a string
	 * @param dir
	 * @return
	 */
	private String directionToString(int dir){
		//TODO: Put this somewhere else. It doesn't belong here.
		switch(dir){
		case 0: return "up";
		case 1: return "right";
		case 2: return "down";
		case 3: return "left";
		default: return "up";
		}
	}
}
