package gameWorld.characters.nonplayer.strategy;

import gameWorld.Room;
import gameWorld.characters.Player;

/**
 * A combat strategy where the NPC stands still, and shoots at the
 * nearest player
 * 
 * @author Carl Anderson 300264124
 *
 */
public class SentryCombatStrategy extends WaitStrategy {
	
	private final double range;
	
	/**
	 * @param range The distance the npc will react from
	 */
	public SentryCombatStrategy(double range){
		this.range = range;
	}
	
	@Override
	public void update() {
		Room room = npc.getCurrentRoom();
		Player target = null;
		double min = Double.MAX_VALUE;
		
		//Find the closest player in the room
		for (Player p : room.getPlayers() ){			
			int dx = npc.getX() - p.getX();
			int dy = npc.getY() - p.getY();
			double dist = Math.sqrt((double)(dx*dx)+(double)(dy*dy));
			
			if (target == null || dist < min){
				target = p;
				min = dist;
			}
		}
		
		//we didn't find a target, so give up
		if (target == null){return; }
		
		//Cause the sentry to face the target
		int dx = Math.abs(npc.getX() - target.getX());
		int dy = Math.abs(npc.getY() - target.getY());
		
		if (npc.getX() < target.getX() && dx > dy){
			npc.setFacing(1);
		} else if (dx > dy) {
			npc.setFacing(3);
		}
		
		if (npc.getY() < target.getY() && dy >= dx){
			npc.setFacing(2);
		} else if (dy >= dx){
			npc.setFacing(0);
		}
		
		// If the player is in range, shoot at them
		if (min < range){
			npc.shoot(target.getX(), target.getY());
		}
	}
	
	@Override
	public String getDescription(){
		return "He looks angry, yet staunch";
	}

}
