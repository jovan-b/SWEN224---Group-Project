package gameWorld.characters;


import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import gameWorld.Room;


/**
 * Represents the Dave playable character
 * @author Chris Read 300254724
 *
 */
public class DavePlayer extends Player {
	
	public static final PlayerType type = PlayerType.DavePlayer;

	public DavePlayer(Room room, int posX, int posY){
		super(room, posX, posY);
		
		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++){
				for (int ani = 0; ani < 3; ani++){
					URL url = DavePlayer.class.getResource("/Players/Dave"+dir+ani+".png");
					
					sprites[dir][ani] = ImageIO.read(url);
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
