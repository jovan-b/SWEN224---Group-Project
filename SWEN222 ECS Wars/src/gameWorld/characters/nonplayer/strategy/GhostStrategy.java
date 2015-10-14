package gameWorld.characters.nonplayer.strategy;

import gameWorld.Room;
import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;

public class GhostStrategy extends WaitStrategy {
	public static final int DAMAGE = 3;
	
	private double range;
	private Player target = null;
	
	private NonPlayerStrategy alternate;
	
	public GhostStrategy(double range){
		description = "Ooh. It's a spooky ghost";
		this.range = range;
		
		this.alternate = new WanderingStrategy();
	}
	
	@Override
	public void update() {
		Room room = npc.getCurrentRoom();
		double min = Double.MAX_VALUE;
		
		//If we have no target, search for a target in range
		if (target == null){
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
			
			if (min < range){ 
				target = null;
			}
		}
		
		if (target == null || target.isDead() || target.getCurrentRoom() != npc.getCurrentRoom()){
			target = null;
		}
		
		if (target == null){
			alternate.update();
			return;
		}
		
		int dx = npc.getX() - target.getX();
		int dy = npc.getY() - target.getY();
		min = Math.sqrt((double)(dx*dx)+(double)(dy*dy));
		
		//Cause the npc to move towards the target
		double theta = Math.toDegrees(Player.angleBetweenPlayerAndMouse(target.getX(), target.getY(),
				npc.getX(), npc.getY()));
		
		if (theta > 0){
			npc.move(0);
		} else if (theta < 0) {
			npc.move(2);
		}
		
		if (Math.abs(theta) < 90){
			npc.move(3);
		} else if (Math.abs(theta) > 90) {
			npc.move(1);
		}
		
		//If this ghost is making contact with the target, deal damage
		if (npc.getBoundingBox().intersects(target.getBoundingBox())){
			target.modifyHealth(-DAMAGE, null);
		}
	}
	
	@Override
	public void interact(Player p, NonPlayer npc){
		target = p;
	}
	
	@Override
	public void setNPCReference(NonPlayer npc){
		super.setNPCReference(npc);
		alternate.setNPCReference(npc);
	}
}
