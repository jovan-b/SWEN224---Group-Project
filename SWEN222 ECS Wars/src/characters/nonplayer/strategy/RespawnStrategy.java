package characters.nonplayer.strategy;

import gameEvents.Event;
import gameEvents.GameClock;
import gameEvents.RespawnEvent;
import characters.nonplayer.NonPlayer.Events;


public class RespawnStrategy extends WaitStrategy {
	private final int time;
	
	public RespawnStrategy(int time){
		this.time = time;
	}

	@Override
	public void initialize() {
		Event respawn = new RespawnEvent(npc, npc.getCurrentRoom(), npc.getX(), npc.getY());
		GameClock.getInstance().scheduleEvent(respawn , time);
		
		npc.respond(Events.DEFAULT);
	}

}
