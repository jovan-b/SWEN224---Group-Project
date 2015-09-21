package gameObjects;

import gameObjects.weapons.projectiles.Projectile;

import java.awt.*;
import java.io.File;
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
	private Image[][] scaledImages;
	private Item[][] contents; // items in the room
	private int cols;
	private int rows;
	private int squareSize = 24; //TODO get this value from player view scale
	private int width;
	private int height;
	
	private Set<Projectile> projectiles = new HashSet<Projectile>();
	private Set<Player> players = new HashSet<>();
	
	public Room(String roomName){
		name = roomName;
		images = new Image[4][2];
		loadImages(roomName);
		scaledImages = images;
		parseFile();
		width = cols*squareSize;
		height = rows*squareSize;
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
		case 'P' : return new Pillar();
		case '_' : return new Floor();
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
		int viewScale = c.getViewScale();
		int viewDirection = player.getViewDirection(); 
		int playerX = player.getX(); 
		int playerY = player.getY(); 
		
		int drawX;
		int drawY;
		
		Item[][] rotated = contents;
		
		switch(viewDirection){
			case 1: // EAST
				drawX = (c.getWidth()/2)-(playerY*viewScale);
				drawY = (c.getHeight()/2)-((width*viewScale)-(playerX*viewScale));
				rotated = rotatedArrayClockwise(contents);
				break;
			case 2: // SOUTH
				drawX = (c.getWidth()/2)-((width*viewScale)-(playerX*viewScale));
				drawY = (c.getHeight()/2)-((height*viewScale)-(playerY*viewScale));
				rotated = rotatedArray180(contents);
				break;
			case 3: // WEST
				drawX = (c.getWidth()/2)-((height*viewScale)-(playerY*viewScale));
				drawY = (c.getHeight()/2)-(playerX*viewScale);
				rotated = rotatedArrayAntiClockwise(contents);
				break;
			case 0: default: // DEFAULT TO NORTH
				drawX = (c.getWidth()/2)-(playerX*viewScale);
				drawY = (c.getHeight()/2)-(playerY*viewScale);
				break;
		}
		
		// Draw background Image
		g.drawImage(scaledImages[viewDirection][0], drawX, drawY-(squareSize*viewScale*3), c);
		
		checkPlayers(viewDirection);
		
		// Draw items in room, also draw players at correct depth level
		Image image;
		for(int row=0; row<rotated[0].length; row++){
			for(int col=0; col<rotated.length; col++){
				Item item = rotated[col][row];
				if(!(item instanceof Floor) && !(item instanceof Wall)){
					image = rotated[col][row].getScaledImage(viewDirection);
					g.drawImage(image, drawX+(col*squareSize*viewScale), 
							drawY+(row*squareSize*viewScale)-(item.yOffset()*squareSize*viewScale), c);
				}
				for (Player p : players){
					if (p.getRow() == row-1){ // Ensures the player is drawn above their current row
						drawPlayer(g, c, viewDirection, drawX, drawY, p);
						p.setRow(-1);
					}
				}
			}
		}
		
		// Draw foreground Image
		g.drawImage(scaledImages[viewDirection][1], drawX, drawY-(squareSize*viewScale*3), c);
	}

	private void drawPlayer(Graphics g, GUICanvas c, int viewDirection, int drawX, int drawY, Player p) {
		Image playerImage = p.getImage();
		int viewScale = c.getViewScale();
		int playerX = p.getX();
		int playerY = p.getY();
		switch(viewDirection){
			case 1:
				g.drawImage(playerImage, drawX+(playerY*viewScale)-(16*viewScale), drawY+((width-playerX)*viewScale)-(24*viewScale), c);
				break;
			case 2:
				g.drawImage(playerImage, drawX+((width-playerX)*viewScale)-(16*viewScale), drawY+((height-playerY)*viewScale)-(24*viewScale), c);
				break;
			case 3:
				g.drawImage(playerImage, drawX+((height-playerY)*viewScale)-(16*viewScale), drawY+(playerX*viewScale)-(24*viewScale), c);
				break;
			default:
				g.drawImage(playerImage, drawX+(playerX*viewScale)-(16*viewScale), drawY+(playerY*viewScale)-(24*viewScale), c);
		}
	}
	
	// sets the current row of the players for drawing
	private void checkPlayers(int viewDirection) {
		switch(viewDirection){
		case 1: // EAST
			for (Player p : players){
				p.setRow(getRow(width-p.getX()));
			}
			break;
		case 2: // SOUTH
			for (Player p : players){
				p.setRow(getRow(height-p.getY()));
			}
			break;
		case 3: // WEST
			for (Player p : players){
				p.setRow(getRow(p.getX()));
			}
			break;
		case 0: default: // DEFAULT TO NORTH
			for (Player p : players){
				p.setRow(getRow(p.getY()));
			}
			break;
		}
	}
	
	public int getCol(int x){
		double xCol = (double)x/(double)squareSize;
		if (xCol > contents.length-1){
			return contents.length-1;
		}
		return (int)xCol;
	}
	
	public int getRow(int y){
		double yRow = (double)y/(double)squareSize;
		if (yRow > contents[0].length-1){
			return contents[0].length-1;
		}
		return (int)yRow;
	}

	/** 
	* Create a clone of the contents array which has then been
	* rotated 90 degrees clockwise
	* @return A clone of this.contents which has been rotated 90
	* degrees clockwise.
	*/
	private Item[][] rotatedArrayClockwise(Item[][] contents){
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
	private Item[][] rotatedArrayAntiClockwise(Item[][] contents){
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
	private Item[][] rotatedArray180(Item[][] contents){
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
	
	public void removePlayer(Player player){
		players.remove(player);
	}
	
	public void addProjectile(Projectile p){
		projectiles.add(p);
	}
	
	public void removeProjectile(Projectile p){
		projectiles.remove(p);
	}

	public Item itemAt(int x, int y) {
		return contents[getCol(x)][getRow(y)];
	}

	public Image[][] getImages() {
		return images;
	}
	
	public void setScaledImages(Image[][] newImages){
		scaledImages = newImages;
	}

	public Item[][] getContents() {
		return contents;
	}

	//FIXME: Do we want to hand back the actual collection, or a copy?
	public Set<Player> getPlayers() {
		return players;
	}
	
	public Set<Projectile> getProjectiles() {
		return projectiles;
	}
}
