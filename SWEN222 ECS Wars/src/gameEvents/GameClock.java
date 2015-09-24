package gameEvents;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A clock which times clock based events
 * @author Carl
 *
 */
public final class GameClock {
	public static final int CLOCK_TICK = 1000; //in ms
	public static final int START_TIME = 0;
	private static GameClock _instance = null;
	
	private Timer clockTimer;
	private int time;
	private Set<Event> events;
	
	private GameClock(){
		time = START_TIME;
		events = new HashSet<Event>();
		clockTimer = new Timer();
	}
	
	/**
	 * Starts the clock
	 */
	public void start(){
		clockTimer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				time++;
				Iterator<Event> iter = events.iterator();
				
				while(iter.hasNext()){
					Event e = iter.next();
					e.notify(time);
					
					if (e.isComplete()){
						iter.remove();
					}
				}
			}
			
		}, 0, CLOCK_TICK);
	}
	
	/**
	 * Adds an event that runs at a specific time
	 * @param e
	 */
	public void scheduleEvent(Event e){
		events.add(e);
	}
	
	/**
	 * Adds an event that runs a single time after a time delay
	 * @param e
	 * @param delay
	 */
	public void scheduleEvent(Event e, int delay){
		clockTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				e.activate();
			}
			
		}, delay);
	}
	
	public int getTime(){
		return time;
	}
	
	/**
	 * Returns the instance of the game clock
	 * @return
	 */
	public static GameClock getInstance(){
		if (_instance == null){
			_instance = new GameClock();
		}
		
		return _instance;
	}
}
