package gameObjects;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GUICanvas;

/**
 * Represents an area in the game. Holds contents relevant to that area and
 * is responsible for drawing itself and everything in it.
 * @author Sarah Dobie, Chris Read
 *
 */
public class Room implements Drawable {
	private Image background;
	private Image foreground;
	private Item[][] contents; // items in the room
	private int cols = 10; //FIXME temporarily hardcoded for testing
	private int rows = 10;
	
	public Room(String bgFile, String fgFile){
		contents = new Item[cols][rows];
		try {
			background = ImageIO.read(new File("Resources"+File.separator+bgFile));
			foreground = ImageIO.read(new File("Resources"+File.separator+fgFile));
		} catch (IOException e) {
			System.out.println("Error loading room image: "+e.getMessage());
		}
	}

	public void draw(Graphics g, GUICanvas c){
		g.drawImage(background, 0, 0, c);
		g.drawImage(foreground, 0, 0, c);
		for(int col=0; col<cols; col++){
			for(int row=0; row<rows; row++){
				if(contents[col][row] != null){
					contents[col][row].draw(g, c);
				}
			}
		}
	}
}
