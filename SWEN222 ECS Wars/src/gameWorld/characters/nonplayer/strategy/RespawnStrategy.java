package gameWorld.characters.nonplayer.strategy;

import gameWorld.characters.nonplayer.NonPlayer.Events;
import gameWorld.gameEvents.Event;
import gameWorld.gameEvents.GameClock;
import gameWorld.gameEvents.RespawnEvent;


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
