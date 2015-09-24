package characters;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameObjects.Room;

public class PondyPlayer extends Player {

	public PondyPlayer(Room room, int posX, int posY) {
		super(room, posX, posY);

		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++) {
				for (int ani = 0; ani < 3; ani++) {
					sprites[dir][ani] = ImageIO.read(new File("Resources"
							+ File.separator + "Players" + File.separator
							+ "Dave" + dir + ani + ".png"));
				}
			}
		} catch (IOException e) {
			System.out
					.println("Error loading player images: " + e.getMessage());
		}
		scaledSprites = sprites;
	}

}