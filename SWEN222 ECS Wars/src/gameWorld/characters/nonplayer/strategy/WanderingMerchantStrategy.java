package gameWorld.characters.nonplayer.strategy;

import java.util.Timer;
import java.util.TimerTask;

import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.MedicineBottle;
import gameWorld.gameObjects.PillBottle;
import gameWorld.gameObjects.Sellable;

public class WanderingMerchantStrategy extends WaitStrategy {
	public static final int MAX_WAIT_TIME = 2000;
	public static final int MAX_WANDER_TIME = 500;
	
	public static final int WAIT = 0;
	public static final int WANDER = 1;
	
	//alternate between wait and wander
	private int state = WAIT;
	private int direction = 0;
	
	private Timer timer = new Timer();
	private boolean activeTimer = false;
	
	private Sellable inventoryItem;
	private String description;
	
	public WanderingMerchantStrategy(){
		// decide which item this npc should sell
		double chance = Math.random();
		if(chance < 0.7){
			inventoryItem = new MedicineBottle();
			description = "I sell medicine!";
		} else {
			inventoryItem = new PillBottle();
			description = "I sell pills!";
		}
		description += " "+ inventoryItem.getCost() +" points each.";
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
	
	@Override
	public void interact(Player p, NonPlayer npc){
		for(int i=0; i<npc.getInventory().length; i++){
			npc.pickUp(inventoryItem);
		}
		// if player has enough points
		if(p.getPoints() >= inventoryItem.getCost()){
			npc.dropItem(0, null);
			p.removePoints(inventoryItem.getCost());
		} else {
			
		}
	}

	/**
	 * Get the tooltip text for an npc using this strategy.
	 * @return Appropriate tooltip depending on what the npc sells
	 */
	public String getDescription() {
		return description;
	}
}
