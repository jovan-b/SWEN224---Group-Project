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
 * @author Carl
 *
 */
public final class SoundManager {
	public static final String SONG_DIR = "/Sounds/";
	
	public static final double MAX_VOLUME = 1;
	public static final double NO_VOLUME = 0;
	
	public static final long TRANSITION_DURATION = 1000; //Time in between songs, with no sound playing
	public static final long FADE_IN_DURATION = 3000; //in ms
	public static final long FADE_OUT_DURATION = 1500;
	
	private static MediaPlayer player;
	private static double volume = MAX_VOLUME;
	//private static Clip track = null;
	
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
					startSong(name, playing);
				}
			});
			new FadeThread(player, FADE_OUT_DURATION, volume, NO_VOLUME).start();
			
		} else {
			startSong(name, playing);
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
	private static void startSong(String name, boolean playing){
		//Find the sound file
		URL dir = SoundManager.class.getResource(SONG_DIR+name);
		if (dir == null){
			System.err.println("Could not find resource "+name);
			return;
		}
				
		//Create the media components
		Media media = new Media(dir.toString());
		player = new MediaPlayer(media);
		player.setCycleCount(MediaPlayer.INDEFINITE);
				
		//Fade the new song in
		FadeThread ft = new FadeThread(player, FADE_IN_DURATION, NO_VOLUME, volume);
		if (playing){ft.setDelay(TRANSITION_DURATION);}
		player.setOnReady(ft);
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
