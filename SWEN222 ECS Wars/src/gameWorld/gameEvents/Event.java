package gameWorld.gameEvents;

public interface Event {
	/**
	 * Notify the event what the time is, so the event can decide whether to run
	 * @param time
	 */
	public void notify(int time);
	
	/**
	 * Runs this event
	 */
	public void activate();
	
	/**
	 * Determines whether this event still has conditions to run
	 * @return
	 */
	public boolean isComplete();
}
