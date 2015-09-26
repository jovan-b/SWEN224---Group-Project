package characters.nonplayer.strategy;

import characters.Player;
import gameObjects.Room;

/**
 * A combat strategy where the NPC stands still, and shoots at the
 * nearest player
 * 
 * @author Carl
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
		
		System.out.println("Found nearest player at distance "+min);
		
		// If the player is in range, shoot at them
		if (min < range){
			System.out.println("Attempting to shoot at player");
			npc.shoot(target.getX(), target.getY());
		}
	}

}
