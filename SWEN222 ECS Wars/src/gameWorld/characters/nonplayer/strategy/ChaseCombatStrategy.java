package gameWorld.characters.nonplayer.strategy;

import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;

public class ChaseCombatStrategy extends WaitStrategy {
	public static double THRESHOLD = 5; //degree where it doesn't do anything
	private double range;
	
	private Player target = null;
	
	public ChaseCombatStrategy(double range){
		this.range = range;
	}
	
	@Override
	public void update() {
		//check to see if we found a target, or if the target has left the room
		if (target == null || target.isDead() || target.getCurrentRoom() != npc.getCurrentRoom()){
			target = null;
			npc.respond(NonPlayer.Events.DEFAULT);
			return; 
		}
		
		double min = Double.MAX_VALUE;

		int dx = npc.getX() - target.getX();
		int dy = npc.getY() - target.getY();
		min = Math.sqrt((double)(dx*dx)+(double)(dy*dy));
		
		//Cause the npc to move towards the target
		double theta = Math.toDegrees(Player.angleBetweenPlayerAndMouse(target.getX(), target.getY(),
				npc.getX(), npc.getY()));
		
		if (theta-THRESHOLD > 0){
			npc.move(0);
		} else if (theta+THRESHOLD < 0) {
			npc.move(2);
		}
		
		if (Math.abs(theta)+THRESHOLD < 90){
			npc.move(3);
		} else if (Math.abs(theta)-THRESHOLD > 90) {
			npc.move(1);
		}
				
		// If the player is in range, shoot at them
		if (min < range){
			npc.shoot(target.getX(), target.getY());
		}
	}
	
	@Override
	public void interact(Player p, NonPlayer npc){
		target = p;
	}
	
	@Override
	public String getDescription(){
		return "He's after you!";
	}
	
}
