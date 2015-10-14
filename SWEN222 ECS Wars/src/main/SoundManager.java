package main;

import java.net.URL;

import javafx.animation.Transition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

/**
 * A class that manages the playing of music tracks, and sound files.
 * 
 * @author Carl Anderson 300264124
 *
 */
public final class SoundManager {
	public static final String SONG_DIR = "/Sounds/";
	
	public static final double MAX_VOLUME = 1;
	public static final double NO_VOLUME = 0;
	
	public static final long TRANSITION_DURATION = 1000; //Time in between songs, with no sound playing
	public static final long FADE_IN_DURATION = 3000; //in ms
	public static final long FADE_OUT_DURATION = 1500;
	
	//Song collections
	public static final String[] BATTLE_SONGS = new String[]{
		"battle_1.mp3",
		"organ.mp3"
	};
	
	public static final String[] CREDIT_SONGS = new String[]{
		"Credits/8-Bit Rick Astley Never Gonna Give You Up.mp3",
		"Credits/8-Bit David Glen Eisley Sweet Victory.mp3"
	};
	
	public static final String[] NON_COMBAT_SONGS = new String[]{
		
	};
	
	private static MediaPlayer player;
	private static double volume = MAX_VOLUME;
	
	private static String[] queue = null;
	private static int queueIndex = 0;
	
	//We use this to set up the JavaFX environment
	private static JFXPanel _init = null;
	
	private SoundManager(){
		//prevent instantiation
	}
	
	/**
	 * Continuously play a provided song, stopping the current song if
	 * there is one
	 * 
	 * @param name file name
	 */
	public static void playSong(String name){
		SoundManager.playSong(name, true);
	}
	
	/**
	 * Play a provided song, stopping the current song if
	 * there is one
	 * 
	 * @param name file name
	 * @param repeat whether the song repeats on finish
	 */
	public static void playSong(String name, boolean repeat){
		//Set up the JavaFX environment, if it hasn't already been set up
		if (_init == null){
			_init = new JFXPanel();
		}
		
		//If there's already a song playing, fade it out before starting the
		//next song, otherwise just start the song
		boolean playing = player != null;
		if (playing){
			player.setOnStopped(new Runnable(){
				public void run(){
					startSong(name, playing, repeat);
				}
			});
			new FadeThread(player, FADE_OUT_DURATION, volume, NO_VOLUME).start();
			
		} else {
			startSong(name, playing, repeat);
		}
	}
	
	/**
	 * Start the new song
	 * 
	 * This is in a separate method so we can time it with
	 * stopping the previous song, if necessary
	 * 
	 * @param name filename
	 * @param playing whether a previous song had been playing
	 */
	private static void startSong(String name, boolean playing, boolean repeat){
		//Find the sound file
		URL dir = SoundManager.class.getResource(SONG_DIR+name);
		if (dir == null){
			System.err.println("Could not find resource "+name);
			return;
		}
				
		//Create the media components
		Media media = new Media(dir.toString());
		player = new MediaPlayer(media);
		if (repeat){
			player.setCycleCount(MediaPlayer.INDEFINITE);
		} else {
			player.setCycleCount(1);
		}
				
		//Fade the new song in
		//(New runnable object to ensure the thread is started correctly)
		Runnable run = new Runnable(){
			@Override
			public void run() {
				FadeThread ft = new FadeThread(player, FADE_IN_DURATION, NO_VOLUME, volume);
				if (playing){ft.setDelay(TRANSITION_DURATION);}
				ft.start();
			}
		};
		player.setOnReady(run);
	}
	
	/**
	 * Play the provided sound once
	 * 
	 * @param name file name
	 */
	public static void playSound(String name){
		//Set up the JavaFX environment if it hasn't already been set up
		if (_init == null){
			_init = new JFXPanel();
		}
		
		//Find the sound file
		URL dir = SoundManager.class.getResource(SONG_DIR+name);
		if (dir == null){
			System.err.println("Could not find resource "+name);
			return;
		}
		
		//Create the media components, and start them
		Media media = new Media(dir.toString());
		MediaPlayer player = new MediaPlayer(media);
		player.setVolume(volume);
		player.play();
	}
	
	/**
	 * Plays a collection of songs
	 * @param queue
	 */
	public static void playQueue(String[] queue){
		queueIndex = 0;
		SoundManager.queue = queue;
		playSong(queue[0], false);
		
		Runnable nextSong = new Runnable(){
			@Override
			public void run() {
				queueIndex = ++queueIndex % SoundManager.queue.length;
				playSong(queue[queueIndex]);
				
				player.setOnEndOfMedia(this);
			}
		};
		
		player.setOnEndOfMedia(nextSong);
	}
	
	/**
	 * Play a random song from a collection
	 * @param songs
	 * @param repeat
	 */
	public static void playRandom(String[] songs, boolean repeat){
		int value = (int)(Math.random()*songs.length);
		playSong(songs[value], repeat);
	}
	
	/**
	 * A class to slowly fade in and fade out songs
	 * @author Carl
	 *
	 */
	private static class FadeThread extends Thread {
		private long wait = 0;
		
		private MediaPlayer player;
		private long duration;
		private double start;
		private double finish;
		
		public FadeThread(MediaPlayer player, long duration, double start, double finish){
			this.player = player;
			this.duration = duration;
			this.start = start;
			this.finish = finish;
		}
		
		public void run(){
			if (duration > 0){
				player.setVolume(start);
			}
			
			//Wait, if a duration is present
			if (wait > 0){
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			double diff = Math.abs(finish - start);
			double modifier = start > finish ? diff : 0;
			
			//If the player hasn't started, start it
			if (start == NO_VOLUME){
				player.play();
			}
			
			//Define the transition
			Transition t = new Transition(){
				{
					setCycleDuration(Duration.millis(duration));
				}

				@Override
				protected void interpolate(double arg0) {
					player.setVolume(Math.abs(modifier-arg0*diff));
				}
				
			};
			
			//Handle cleanup if this is a complete fade-out
			t.setOnFinished(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent arg0) {
					if (finish == NO_VOLUME){
						player.stop();
					}
				}
				
			});
			
			//Start the transition
			t.play();
		}
		
		public void setDelay(long ms){
			this.wait = ms;
		}
	}
	
	
}
