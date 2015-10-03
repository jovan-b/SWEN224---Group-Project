package gameWorld.gameEvents;

import gameWorld.characters.Player;
import gameWorld.gameObjects.Room;

/**
 * A one-time respawn event that spawns the given player at x,y in a given room
 * @author Carl
 *
 */
public class RespawnEvent implements Event {
	private Player player;
	private Room room;
	private int x;
	private int y;
	
	private boolean complete = false;
	
	public RespawnEvent(Player p, Room r, int x, int y){
		this.player = p;
		this.room = r;
		this.x = x;
		this.y = y;
	}

	@Override
	public void notify(int time) {
		return; //do nothing on notify, since this is a one-time event
	}

	@Override
	public void activate() {
		//Since this is a one-time event, we use activate
		player.setCurrentRoom(room, x, y);
		player.setHealth(player.HEALTH_MAX);
		complete = true;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

}
