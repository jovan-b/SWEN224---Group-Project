package gameObjects;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import characters.Player;
import main.GUICanvas;

/**
 * Represents an area in the game. Holds contents relevant to that area and
 * is responsible for drawing itself and everything in it.
 * @author Sarah Dobie, Chris Read
 *
 */
public class Room {
	private String name;
	private String description;
	private Image[][] images;
	private Item[][] contents; // items in the room
	private int cols;
	private int rows;
	
	private Set<Player> players = new HashSet<>();
	
	public Room(String roomName){
		name = roomName;
		images = new Image[4][4];
		loadImages(roomName);
		parseFile();
	}

	private void parseFile() {
		try {
			Scanner s = new Scanner(new File("Resources"+File.separator+name+".txt"));
			description = s.nextLine();
			cols = Integer.parseInt(s.nextLine());
			rows = Integer.parseInt(s.nextLine());
			contents = new Item[cols][rows];
			
			for(int r=0; r<rows; r++){
				String line = s.nextLine();
				for(int c=0; c<cols; c++){
					char code = line.charAt(c);
					contents[c][r] = itemFromCode(code);
				}
			}
			s.close();
			
		} catch (IOException e) {
			System.out.println("Error loading room file: "+ e.getMessage());
		}
	}
	
	private Item itemFromCode(char code){
		switch(code){
		case '#' : return new Wall();
		case '_' :
		default: return null;
		}
	}

	private void loadImages(String roomName) {
		try {
			images[0][0] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"NorthBase.png"));
			images[0][1] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"NorthTop.png"));
			
			images[1][0] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"EastBase.png"));
			images[1][1] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"EastTop.png"));
			
			images[2][0] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"SouthBase.png"));
			images[2][1] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"SouthTop.png"));
			
			images[3][0] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"WestBase.png"));
			images[3][1] = ImageIO.read(new File("Resources"+File.separator+roomName+File.separator+"WestTop.png"));
		} catch (IOException e) {
			System.out.println("Error loading room images: "+e.getMessage());
		}
	}

	public void draw(Graphics g, GUICanvas c, Player player){
		int viewDirection = 0; //TODO get from player view direction (0=North, 1=East, 2=South, 3=West)
		int squareSize = 24; //TODO get this value from player view scale
		int playerX = player.getX(); //TODO replace these with player coordinates in room
		int playerY = player.getY(); //TODO replace these with player coordinates in room
		
		int drawX;
		int drawY;
		
		Item[][] rotated = contents;
		
		switch(viewDirection){
			case 1: // EAST
				drawX = (c.getWidth()/2)-playerY;
				drawY = (c.getHeight()/2)-((cols*24)-playerX);
				rotated = rotatedArrayClockwise(contents);
				break;
			case 2: // SOUTH
				drawX = (c.getWidth()/2)-((cols*24)-playerX);
				drawY = (c.getHeight()/2)-((rows*24)-playerY);
				rotated = rotatedArrayClockwise(contents); // need to rotate 180degrees
				rotated = rotatedArrayClockwise(rotated);
				break;
			case 3: // WEST
				drawX = (c.getWidth()/2)-((rows*24)-playerY);
				drawY = (c.getHeight()/2)-playerX;
				rotated = rotatedArrayAntiClockwise(contents);
				break;
			case 0: default: // DEFAULT TO NORTH
				drawX = (c.getWidth()/2)-playerX;
				drawY = (c.getHeight()/2)-playerY;
				break;
		}
		
		// Draw background Image
		g.drawImage(images[viewDirection][0], drawX, drawY-(squareSize*3), c);
		for(int col=0; col<rotated.length; col++){
			for(int row=0; row<rotated[0].length; row++){
				if(rotated[col][row] != null){
					rotated[col][row].draw(g, c);
				}
			}
		}
		
		g.setColor(Color.GREEN);
		g.fillRect(drawX+playerX-5, drawY+playerY-5, 10, 10);
		
		// Draw foreground Image
		g.drawImage(images[viewDirection][1], drawX, drawY-(squareSize*3), c);
	}
	
	/** 
	* Create a clone of the contents array which has then been
	* rotated 90 degrees clockwise
	* @return A clone of this.contents which has been rotated 90
	* degrees clockwise.
	*/
	public Item[][] rotatedArrayClockwise(Item[][] contents){
		int rows = contents.length;
		int cols = contents[0].length;
		
		 // Make a new array where width and height are swtiched
		Item[][] rotate = new Item[cols][rows];
		
		// Rows and cols for rotated array
		int rRows = cols;
		int rCols = rows;
		
		// Rotates 90 clockwise
		for(int r=0; r<rRows; r++){
			for(int c=0; c<rCols; c++){
				rotate[r][c] = contents[rCols-1-c][r];
			}
		}
		
		return rotate;
	}
	
	/** 
	* Create a clone of the contents array which has then been
	* rotated 90 degrees anti-clockwise
	* @return A clone of this.contents which has been rotated 90
	* degrees anti-clockwise.
	*/
	public Item[][] rotatedArrayAntiClockwise(Item[][] contents){
		int rows = contents.length;
		int cols = contents[0].length;

		// Makes a new array where width and height are swtiched
		Item[][] rotate = new Item[cols][rows];
		
		// Rows and cols for rotated array
		int rRows = cols;
		int rCols = rows;
		
		// Rotates 90 anticlockwise
		for(int r=0; r<rRows; r++){
			for(int c=0; c<rCols; c++){
				rotate[r][c] = contents[c][rRows-1-r];
			}
		}
		
		return rotate;
	}
	
	/** 
	* Create a clone of the contents array which has then been
	* rotated 180 degrees.
	* @return A clone of this.contents which has been rotated 180
	* degrees
	*/
	public Item[][] rotatedArray180(Item[][] contents){
		int rows = contents.length;
		int cols = contents[0].length;

		// Makes a new array where width and height are swtiched
		Item[][] rotate = new Item[rows][cols];
		
		//Rotates the image
        for(int r=0; r<rows; r++){
            for(int c=0; c<cols; c++){
                rotate[r][c] = contents[rows-1-r][cols-1-c];
            }
        }
		
		return rotate;
	}

	public void addPlayer(Player player) {
		players.add(player);
	}
}
