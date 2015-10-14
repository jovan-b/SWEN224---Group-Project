package gameWorld.gameEvents;

import gameWorld.Controller;
import gameWorld.Room;

/**
 * A timer event which allows update events to occur in non-loaded rooms
 * 
 * @author Carl Anderson 300264124
 */
public class SlowUpdateEvent implements Event {
	private int ticks;
	private Controller controller;
	
	public SlowUpdateEvent(Controller controller, int seconds){
		this.ticks = GameClock.secondsToTicks(seconds);
		this.controller = controller;
	}
	
	@Override
	public void notify(int time) {
		if (time % ticks == 0){
			for (Room r : controller.getRooms()){
				if (r.getPlayers().size() == 0){
					r.update();
				}
			}
		}
	}

	@Override
	public void activate() {
	}

	@Override
	public boolean isComplete() {
		return false;
	}

}
