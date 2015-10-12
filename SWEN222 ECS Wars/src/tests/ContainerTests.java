package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import gameWorld.Controller;
import gameWorld.SinglePlayerController;
import gameWorld.gameObjects.Torch;
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

}
