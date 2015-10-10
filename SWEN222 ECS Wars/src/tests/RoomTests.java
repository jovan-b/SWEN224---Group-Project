package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import gameWorld.*;
import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Desk;
import gameWorld.gameObjects.Wall;
import gameWorld.gameObjects.weapons.projectiles.LtsaBullet;
import gameWorld.gameObjects.weapons.projectiles.Projectile;
import gameWorld.gameObjects.weapons.projectiles.RubberBullet;

/**
 * A test class to verify qualities of the Room class.
 * @author Sarah Dobie 300315033
 *
 */
public class RoomTests {
	
	private static final int SQUARE_SIZE = 24;
	private Controller ctrl = new SinglePlayerController();
	private Room classroom103 = new Room("Classroom 103", ctrl);
	private Room classroom102 = new Room("Classroom 102", ctrl);
	private Room seHallway = new Room("SE Hallway", ctrl);
	private Room courtyard = new Room("Courtyard", ctrl);
	
	//
	// POSITIONAL CONVERSION
	//
	
	@Test
	/**
	 * Tests the conversion between x and column values.
	 */
	public void testXColConversion1(){
		Room room = classroom103;
		// check simple values
		assertEquals(room.colFromX(0), 0);
		assertEquals(room.colFromX(SQUARE_SIZE*2), 2);
		// check midway through square
		assertEquals(room.colFromX(SQUARE_SIZE*5-SQUARE_SIZE/2), 4);
		// check out of bounds values
		assertEquals(room.colFromX(-SQUARE_SIZE), 0);
		assertEquals(room.colFromX(SQUARE_SIZE*room.getCols()+10), room.getCols()-1);
	}
	
	@Test
	/**
	 * Tests the conversion between x and column values.
	 */
	public void testXColConversion2(){
		Room room = seHallway;
		// check simple values
		assertEquals(room.colFromX(0), 0);
		assertEquals(room.colFromX(SQUARE_SIZE*2), 2);
		// check midway through square
		assertEquals(room.colFromX(SQUARE_SIZE*5-SQUARE_SIZE/2), 4);
		// check out of bounds values
		assertEquals(room.colFromX(-SQUARE_SIZE), 0);
		assertEquals(room.colFromX(SQUARE_SIZE*room.getCols()+10), room.getCols()-1);
	}
	
	@Test
	/**
	 * Tests the conversion between y and row values
	 */
	public void testYRowConversion1(){
		Room room = classroom102;
		// check simple values
		assertEquals(room.rowFromY(0), 0);
		assertEquals(room.rowFromY(SQUARE_SIZE*2), 2);
		// check midway through square
		assertEquals(room.rowFromY(SQUARE_SIZE*5-SQUARE_SIZE/4), 4);
		// check out of bounds values
		assertEquals(room.rowFromY(-SQUARE_SIZE), 0);
		assertEquals(room.rowFromY(SQUARE_SIZE*room.getRows()+10), room.getRows()-1);
	}
	
	@Test
	/**
	 * Tests the conversion between y and row values
	 */
	public void testYRowConversion2(){
		Room room = courtyard;
		// check simple values
		assertEquals(room.rowFromY(0), 0);
		assertEquals(room.rowFromY(SQUARE_SIZE*2), 2);
		// check midway through square
		assertEquals(room.rowFromY(SQUARE_SIZE*5-SQUARE_SIZE/4), 4);
		// check out of bounds values
		assertEquals(room.rowFromY(-SQUARE_SIZE), 0);
		assertEquals(room.rowFromY(SQUARE_SIZE*room.getRows()+10), room.getRows()-1);
	}
	
	
	
	//
	// ADDING OBJECTS TO ROOM
	//
	
	@Test
	/**
	 * Tests adding of a player to a room
	 */
	public void testAddPlayer(){
		Room room = courtyard;
		Player p = new DavePlayer(room,0,0);
		room.addPlayer(p);
		Set<Player> players = room.getPlayers();
		assertTrue(players.contains(p));
	}
	
	@Test
	/**
	 * Tests adding of a projectile to a room
	 */
	public void testAddProjectile(){
		Room room = courtyard;
		Player pl = new DavePlayer(room,0,0);
		room.addPlayer(pl);
		Projectile p = new RubberBullet().newInstance(pl, 0);
		room.addProjectile(p);
		Set<Projectile> projectiles = room.getProjectiles();
		assertTrue(projectiles.contains(p));
	}
	
	
	
	//
	// REMOVING OBJECTS FROM ROOM
	//
	
	@Test
	/**
	 * Tests adding of a player to a room
	 */
	public void testRemovePlayer(){
		Room room = classroom103;
		Player p = new DavePlayer(room,0,0);
		room.addPlayer(p);
		room.removePlayer(p);
		Set<Player> players = room.getPlayers();
		assertFalse(players.contains(p));
	}
	
	@Test
	/**
	 * Tests removing of a projectile from a room
	 */
	public void testRemoveProjectile(){
		Room room = seHallway;
		Projectile p = new LtsaBullet();
		room.addProjectile(p);
		room.removeProjectile(p);
		Set<Projectile> projectiles = room.getProjectiles();
		assertFalse(projectiles.contains(p));
	}
	
	
	
	//
	// ACCESSING OBJECTS IN ROOM
	//
	
	@Test
	/**
	 * Tests getting item at certain position
	 */
	public void testItemAt1(){
		Room room = classroom103;
		// check out of bounds cases
		assertTrue(room.itemAt(-1,-1) instanceof Wall);
		assertTrue(room.itemAt(SQUARE_SIZE*room.getCols()+10,
				SQUARE_SIZE*room.getRows()+10) instanceof Wall);
	}
	
	@Test
	/**
	 * Tests getting item at certain position
	 */
	public void testItemAt2(){
		Room room = classroom103;
		assertTrue(room.itemAt(SQUARE_SIZE*2,
				SQUARE_SIZE*3) instanceof Desk);
	}
}
