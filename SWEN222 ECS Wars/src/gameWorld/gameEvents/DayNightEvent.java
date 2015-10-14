package gameWorld.gameEvents;

import gameWorld.Controller;

/**
 * Represents the day/night cycle in the game
 * When outdoors and nighttime, visibility will decrease
 * 
 * @author Chris Read 300254724
 * @author Carl Anderson 300264124
 *
 */
public class DayNightEvent implements Event{
	
	private final int duration;
	private final Controller controller;
		
	public DayNightEvent(Controller c, int duration){
		this.controller = c;
		this.duration = GameClock.secondsToTicks(duration);
	}
	
	@Override
	public void notify(int time) {
//		if (time % duration == 0){
//			controller.setDayTime(!controller.isDayTime());	
//		}
		controller.updateNightAlpha();
		controller.getGUI().getCanvas().rotateSundial(180/duration);
	}

	@Override
	public void activate() {
		
	}

	@Override
	public boolean isComplete() {
		return false;
	}

}
