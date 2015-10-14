package gameWorld.gameEvents;

/**
 * Events are used in the game-clock
 * Represent dynamic events in the game
 * 
 * @author Carl Anderson 300264124
 *
 */
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
