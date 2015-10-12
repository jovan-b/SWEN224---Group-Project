package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameWorld.Controller;
import gameWorld.Room;
import gameWorld.SinglePlayerController;
import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.gameObjects.KeyCard;
import gameWorld.gameObjects.SmallTreasure;
import gameWorld.gameObjects.Torch;
import gameWorld.gameObjects.Wall;
import gameWorld.gameObjects.containers.Cabinet;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.containers.Pouch;

/**
 * A test class to verify the integrity of the Container classes
 * @author Chris Read 300254724
 *
 */
public class ContainerTests {
	public static final int CONTAINER_SIZE = 4;
	private Cabinet cabinet;
	private Pouch pouch;
	
	@Test
	/**
	 * Tests that a container is empty on creation
	 */
	public void testEmptyOnCreation(){
		cabinet = new Cabinet('f');
		pouch = new Pouch();
		// check that the cabinet is empty
		assertTrue(cabinet.getContents().isEmpty());
		// check that the pouch is empty
		assertTrue(pouch.getContents().isEmpty());
	}
	
	@Test
	/**
	 * Tests the addItem() method for each container
	 */
	public void testAddItem(){
		cabinet = new Cabinet('f');
		pouch = new Pouch();
		// test adding item from empty
		assertTrue(cabinet.addItem(new Torch()));
		assertTrue(pouch.addItem(new Torch()));
		
		for (int i = 1; i < CONTAINER_SIZE; i++){
			cabinet.addItem(new Torch());
			pouch.addItem(new Torch());
		}

		// test adding item when full
		assertFalse(cabinet.addItem(new Torch()));
		assertFalse(pouch.addItem(new Torch()));
	}

	@Test
	/**
	 * Tests that the remainingCapacity() method returns the correct amount
	 */
	public void testRemainingCapacity(){
		cabinet = new Cabinet('f');
		pouch = new Pouch();
		// test when empty
		assertTrue(cabinet.remainingCapacity() == 4);
		assertTrue(pouch.remainingCapacity() == 4);
		
		cabinet.addItem(new Torch());
		pouch.addItem(new Torch());
		
		// test after one item added
		assertTrue(cabinet.remainingCapacity() == 3);
		assertTrue(pouch.remainingCapacity() == 3);
		
		while(cabinet.remainingCapacity() > 0 && pouch.remainingCapacity() > 0){
			cabinet.addItem(new Torch());
			pouch.addItem(new Torch());
		}
		
		// test when full
		assertTrue(cabinet.remainingCapacity() == 0);
		assertTrue(pouch.remainingCapacity() == 0);
		
		cabinet.addItem(new Torch());
		pouch.addItem(new Torch());
		
		// test after attempting to add item while full
		assertTrue(cabinet.remainingCapacity() == 0);
		assertTrue(pouch.remainingCapacity() == 0);
	}
	
	@Test
	/**
	 * Tests the getItem() method of each container
	 */
	public void testGetItem(){
		cabinet = new Cabinet('f');
		
		cabinet.addItem(new Torch());
		cabinet.addItem(new KeyCard());
		cabinet.addItem(new SmallTreasure());
		
		// test getting items
		assertTrue(cabinet.getItem(0) instanceof Torch);
		assertTrue(cabinet.getItem(1) instanceof KeyCard);
		assertTrue(cabinet.getItem(2) instanceof SmallTreasure);
		
		// test that getting an empty item slot returns a blank wall
		assertTrue(cabinet.getItem(3) instanceof Wall);
		
		// test that an index outside the bounds returns a blank Wall
		assertTrue(cabinet.getItem(-1) instanceof Wall);
		assertTrue(cabinet.getItem(4) instanceof Wall);
	}
	
	@Test
	/**
	 * Tests the pickUpItem() method, where the player picks up an item from the cabinet
	 */
	public void testPickUpItem(){
		cabinet = new Cabinet('f');
		Controller ctrl = new SinglePlayerController();
		Player player = new DavePlayer(new Room("Classroom 103", ctrl), 0, 0);
		
		cabinet.addItem(new Torch());
		
		// test picking up first item
		cabinet.pickUpItem(0, player);
		assertTrue(player.inventoryContains(new Torch()) instanceof Torch);
		
		cabinet.addItem(new Torch());
		cabinet.addItem(new Torch());
		cabinet.addItem(new KeyCard());
		
		// Test picking up item with full inventory
		cabinet.pickUpItem(1, player);
		cabinet.pickUpItem(0, player);
		cabinet.pickUpItem(0, player);
		
		assertTrue(player.inventoryContains(new KeyCard()) == null);
		assertTrue(cabinet.getItem(0) instanceof KeyCard);
	}

}
