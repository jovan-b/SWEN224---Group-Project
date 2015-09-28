package gameObjects;

import java.awt.Graphics;
import java.awt.Image;

import characters.Player;
import main.GUICanvas;

public class Door implements Item {
	
	private Room room1;
	private Room room2;
	private String parseCode;
	private int room1Col;
	private int room1Row;
	private int room2Col;
	private int room2Row;
//	private boolean horizontal;
	private boolean unlocked;
	
	public Door(String parseCode, /*boolean hz,*/ boolean unlocked,
			Room room1, int room1Col, int room1Row){
		this.parseCode = parseCode;
//		this.horizontal = hz;
		this.unlocked = unlocked;
		this.room1 = room1;
		this.room1Col = room1Col;
		this.room1Row = room1Row;
		System.out.println("New Door: " + parseCode + " " + room1.getName());
	}
	
	public void addRoom2(Room room2, int room2Col, int room2Row){
		this.room2 = room2;
		this.room2Col = room2Col;
		this.room2Row = room2Row;
		System.out.println("Connecting to Door: " + parseCode + " " + room2.getName());
	}
	
	@Override
	public void use(Player p) {
		int x;
		int y;
		if(unlocked){
			if(room1 == p.getCurrentRoom()){
				x = p.getX()-(room1Col*24);
				y = p.getY()-(room1Row*24);
				p.setCurrentRoom(room2, (room2Col*24)+x, (room2Row*24)+y);
			} else if(room2 == p.getCurrentRoom()){
				x = p.getX()-(room2Col*24);
				y = p.getY()-(room2Row*24);
				p.setCurrentRoom(room1, (room1Col*24)+x, (room1Row*24)+y);
			}
		} else {
			// TODO locked door case
		}
		
	}

	@Override
	public Image getImage(int viewDirection) {
		return null;
	}

	@Override
	public boolean canWalk() {
		return unlocked;
	}

	@Override
	public int yOffset(int viewDirection) {
		return 0;
	}

	@Override
	public int xOffset(int viewDirection) {
		return 0;
	}

	@Override
	public void setScaledImage(int viewDirection, Image scaledImage) {
	}

	@Override
	public Image getScaledImage(int viewDirection) {
		return null;
	}

	public Room getRoom1() {
		return room1;
	}

	public Room getRoom2() {
		return room2;
	}

	public String getParseCode() {
		return parseCode;
	}

	public int getRoom1Col() {
		return room1Col;
	}

	public int getRoom1Row() {
		return room1Row;
	}

	public int getRoom2Col() {
		return room2Col;
	}

	public int getRoom2Row() {
		return room2Row;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	@Override
	public String getDescription() {
		return null;
	}
	
}
