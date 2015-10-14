package tests;

import static org.junit.Assert.*;
import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.gameObjects.Floor;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.SmallTreasure;
import gameWorld.gameObjects.containers.Cabinet;
import gameWorld.gameObjects.containers.Container;
import gameWorld.gameObjects.weapons.LTSAGun;
import gameWorld.gameObjects.weapons.Weapon;

import org.junit.*;

/**
 * Tests for Players
 * @author Carl Anderson 300264124
 *
 */
public class CharacterTests {
	
	@Test
	//Check player has empty inventory
	public void emptyInventoryOnSpawn(){
		Player p = new DavePlayer(null, 0, 0);
		
		int size = 0;
		for (Item i : p.getInventory()){
			if (i != null){
				size++;
			}
		}
		
		assertTrue(size == 0);
	}
	
	@Test
	//Check player correctly adds an item
	public void testPickupItems_1(){
		Player p = new DavePlayer(null, 0, 0);
		Item item = new SmallTreasure();
		
		p.pickUp(item);
		assertTrue(p.inventoryContains(item) == item);
	}
	
	@Test
	//Check player inventory size changes after picking up an item
	public void testPickupItems_2(){
		Player p = new DavePlayer(null, 0, 0);
		Item item = new SmallTreasure();
		
		p.pickUp(item);
		
		int size = 0;
		for (Item i : p.getInventory()){
			if (i != null){
				size++;
			}
		}
		
		assertTrue(size == 1);
	}
	
	@Test
	//Check player cannot pickup item with a full inventory
	public void testPickupItems_3(){
		Player p = new DavePlayer(null, 0, 0);
		p.pickUp(new SmallTreasure());
		p.pickUp(new SmallTreasure());
		p.pickUp(new SmallTreasure());
		
		assertFalse(p.pickUp(new SmallTreasure()));
	}
	
	@Test
	//Check inventory item goes to the first space in the inventory
	public void testPickupItems_4(){
		Player p = new DavePlayer(null, 0, 0);
		Item item = new SmallTreasure();
		p.pickUp(item);
		
		assertTrue(p.inventoryItemAt(0) == item);
	}
	
	@Test
	//Check player no longer has item after placing it in a container
	public void testDropItem_1(){
		Player p = new DavePlayer(null, 0, 0);
		Item item = new SmallTreasure();
		Container c = new Cabinet('f');
		
		p.pickUp(item);
		p.dropItem(0, c);
		
		assertTrue(p.inventoryContains(item) == null);
	}
	
	@Test
	//Test the player's weapon is the weapon they pick up
	public void testPickupWeapons_1(){
		Player p = new DavePlayer(null, 0, 0);
		Weapon w = new LTSAGun();
		Floor f = new Floor();
		
		f.addSpawnItem(w);
		
		p.pickUpWeapon(w, f);
		assertTrue(p.getWeapon() == w);
	}
	
	@Test
	//Test the floor contains the old weapon after a pickup
	public void testPickupWeapons_2(){
		Player p = new DavePlayer(null, 0, 0);
		Weapon w = new LTSAGun();
		Floor f = new Floor();
		
		Weapon old = p.getWeapon();
		
		f.addSpawnItem(w);
		
		p.pickUpWeapon(w, f);
		assertTrue(f.getItem() == old);
	}
	
	@Test
	//Test the weapons aren't swapped when the floor doesn't contain a weapon
	public void testPickupWeapons_3(){
		Player p = new DavePlayer(null, 0, 0);
		Weapon w = p.getWeapon();
		Floor f = new Floor();
						
		p.pickUpWeapon(null, f);
		assertTrue(p.getWeapon() == w);
	}
	
	@Test
	//Test player starts with max health
	public void testHealth_1(){
		Player p = new DavePlayer(null, 0, 0);
		
		assertTrue(p.getHealth() == Player.HEALTH_MAX);
	}
	
	@Test
	//Test player loses health properly
	public void testHealth_2(){
		Player p = new DavePlayer(null, 0, 0);
		p.modifyHealth(-50, null);
		
		assertTrue(p.getHealth() == Player.HEALTH_MAX-50);
	}
	
	@Test
	//Test player gains health properly
	public void testHealth_3(){
		Player p = new DavePlayer(null, 0, 0);
		p.modifyHealth(-50, null);
		p.modifyHealth(20, null);
		
		assertTrue(p.getHealth() == Player.HEALTH_MAX-30);
	}
	
	@Test
	//Test player cannot go above max health
	public void testHealth_4(){
		Player p = new DavePlayer(null, 0, 0);
		p.modifyHealth(50, null);
		
		assertTrue(p.getHealth() == Player.HEALTH_MAX);
	}
	
	@Test
	//Test player cannot go below 0 health
	public void testHealth_5(){
		Player p = new DavePlayer(null, 0, 0);
		p.modifyHealth(-(Player.HEALTH_MAX+10), null);
		
		assertTrue(p.getHealth() == 0);
	}
}
