package gameWorld.characters.nonplayer.strategy;

import java.util.Timer;
import java.util.TimerTask;

import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;
import gameWorld.gameObjects.Floor;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.MedicineBottle;
import gameWorld.gameObjects.PillBottle;
import gameWorld.gameObjects.Sellable;

/**
 * NPC strategy describing random movement
 * 
 * @author Carl Anderson 300264124
 */
public class WanderingStrategy extends WaitStrategy {

	public static final int MAX_WAIT_TIME = 2000;
	public static final int MAX_WANDER_TIME = 500;
	
	public static final int WAIT = 0;
	public static final int WANDER = 1;
	
	//alternate between wait and wander
	private int state = WAIT;
	private int direction = 0;
	
	private Timer timer = new Timer();
	private boolean activeTimer = false;
	
	public WanderingStrategy(){}
	
	public WanderingStrategy(String description){
		this.description = description;
	}
	
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
			
			npc.move(direction);
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
	
	@Override
	public void interact(Player p, NonPlayer npc){
		//Do nothing
	}
}
