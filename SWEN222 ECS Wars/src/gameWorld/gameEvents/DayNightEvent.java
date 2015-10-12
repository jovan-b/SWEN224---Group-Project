package gameWorld.gameEvents;

import gameWorld.Controller;

public class DayNightEvent implements Event{
	
	private final int duration;
	private final Controller controller;
	
	public DayNightEvent(Controller c, int duration){
		this.controller = c;
		this.duration = duration;
	}
	
	@Override
	public void notify(int time) {
		if (time % duration == 0){
			controller.setDayTime(!controller.isDayTime());	
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
