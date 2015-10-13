package gameWorld.characters;


import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.Room;


/**
 * Represents the David Streader playable character
 * 
 * @author Sarah Dobie 300315033
 * @author Chris Read 300254724
 *
 */
public class StreaderPlayer extends Player {
	
	public static final PlayerType type = PlayerType.MarcoPlayer;

	public StreaderPlayer(Room room, int posX, int posY){
		super(room, posX, posY);
		
		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++){
				for (int ani = 0; ani < 3; ani++){
					sprites[dir][ani] = ImageIO.read(StreaderPlayer.class.getResource("/Players/Streader"+dir+ani+".png"));
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
		scaledSprites = sprites;
	}

	@Override
	public PlayerType getType() {
		return type;
	}

	@Override
	public String getName() {
		return "Dave";
	}
}
