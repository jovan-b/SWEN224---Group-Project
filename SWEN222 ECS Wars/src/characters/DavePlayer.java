package characters;


import gameObjects.Room;
import main.GUICanvas;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * Represents the Dave playable character
 * @author Chris Read
 *
 */
public class DavePlayer extends Player {
	
	private int firerateMulti = 1;
	private int firerate = FIRERATE * firerateMulti;	//projectiles per second

	public DavePlayer(Room room, int posX, int posY){
		super(room, posX, posY);
		
		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++){
				for (int ani = 0; ani < 3; ani++){
					sprites[dir][ani] = ImageIO.read(new File("Resources"+File.separator+"Players"+File.separator+"Dave"+dir+ani+".png"));
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
		scaledSprites = sprites;
	}
}
