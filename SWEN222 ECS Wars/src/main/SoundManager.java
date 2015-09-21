package main;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class SoundManager {
	public static final String SONG_DIR = "Resources"+File.separator+"Sounds"+File.separator;
	public static final float FADE_INCR = 0.05f;
	public static final float MAX_VOLUME = -0f;
	public static final float NO_VOLUME = -50f;
	
	private static float volume = MAX_VOLUME;
	private static Clip track = null;
	
	private SoundManager(){
		//prevent instantiation
	}
	
	public static void playSong(String name){
		boolean doDelay = track != null;
		if (track != null){
			//Fade the current song out
			new FadeThread(track, volume, NO_VOLUME).start();
		}
		
		try {
			//Open the file
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(SONG_DIR+name));
			track = AudioSystem.getClip();
			track.open(audioIn);
			
			//Start the song, and fade it in if we need to
			((FloatControl)track.getControl(FloatControl.Type.MASTER_GAIN)).setValue(doDelay ? NO_VOLUME : volume);
			track.start();
			track.loop(Clip.LOOP_CONTINUOUSLY);

			if (doDelay){
				new FadeThread(track, volume, MAX_VOLUME, (int)((volume-NO_VOLUME)/FADE_INCR*5)).start();
			}
		} catch (UnsupportedAudioFileException | IOException e) {
			System.err.println("Could not find audio file "+name);
			//e.printStackTrace();
		} catch (LineUnavailableException e) {
			System.err.println("Error reading from audio stream");
			e.printStackTrace();
		}
	}
	
	public static void playSound(String name){
		try {
			//open the file
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(SoundManager.class.getResource(name));
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			
			//start the clip
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			System.err.println("Could not find audio file "+name);
			//e.printStackTrace();
		} catch (LineUnavailableException e) {
			System.err.println("Error reading from audio stream");
			e.printStackTrace();
		}		
	}
	
	public static void test(){
		new Thread(){
			public void run(){
				try{
					Thread.sleep(10000);
				} catch(Exception e){
					
				}
				
				SoundManager.playSong("happpy.wav");
			}
		}.start();
	}
	
	
	/**
	 * Private class to slowly fade in and fade out songs
	 * @author Carl
	 *
	 */
	private static class FadeThread extends Thread {
		private FloatControl volume;
		private Clip clip;
		private float start;
		private float finish;
		
		private int delay = -1;
		
		public FadeThread(Clip clip, float start, float finish){
			this.clip = clip;
			this.start = start;
			this.finish = finish;
			
			this.volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		
		public FadeThread(Clip clip, float start, float finish, int delay){
			this(clip, start, finish);
			
			this.delay = delay;
		}
		
		@Override
		public void run(){
			//Cause this thread to wait if a delay has been provided
			if (delay > 0){
				try {Thread.sleep(delay);} 
				catch (InterruptedException e) {}
			}
			
			//if start is less than finish
			while (start < finish){
				start += FADE_INCR;
				volume.setValue(start);
				
				try {Thread.sleep(10);} 
				catch (InterruptedException e) {}
			}
			
			//if finish is less than start
			while (start > finish){
				start -= FADE_INCR;
				volume.setValue(start);
				
				try {Thread.sleep(10);} 
				catch (InterruptedException e) {}
			}
			
			// if the finish is quiet enough, just stop it
			if (finish <= NO_VOLUME){
				clip.stop();
			}
		}
	}
}
