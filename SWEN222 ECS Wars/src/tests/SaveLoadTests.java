package tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import gameWorld.*;
import gameWorld.characters.DavePlayer;
import gameWorld.characters.Player;
import gameWorld.gameObjects.*;
import gameWorld.gameObjects.weapons.LTSAGun;
import main.saveAndLoad.LoadManager;
import main.saveAndLoad.SaveManager;

import org.junit.Test;

public class SaveLoadTests {
	@Test
	public void saveLoadTest(){
		/*
		 * initialise
		 */
		
		//firstly, make a new game
		Controller base = new SinglePlayerController();
		
		//add player to controller
		Player testPlayer = new DavePlayer(null, 0, 0);
		base.getPlayers().add(testPlayer);
		base.setCurrentPlayer(testPlayer);
		
		base.initialise();
		
		//add items to the player inventory
		Item[] inventory = base.getCurrentPlayer().getInventory();
		inventory[0] = new KeyCard();
		inventory[1] = new Torch();
		inventory[2] = new Map();
		
		//change player's weapon
		base.getCurrentPlayer().setCurrentWeapon(new LTSAGun());
		
		/*
		 * now save and reload the game
		 */
		
		SaveManager.saveGame(base, "JUnitSaveTest.xml");
		
		Controller clone = new SinglePlayerController();
		clone.initialise();
		
		//File should be in the right location, load into clone controller
		LoadManager.loadGame(new File("JUnitSaveTest.xml"), clone);
		
		//check player is in right position
		checkPlayerPosition(base, clone);
		
		//check player has right items and inventory
		checkPlayerInventory(base, clone);
		
		//check room items on floor are the same
		checkRoomItems(base, clone);
	}
	
	/*
	 * Checks player spawns back into right place
	 */
	private void checkPlayerPosition(Controller base, Controller clone){
		
		assertTrue(base.getCurrentPlayer().getBoundingBox().equals(clone.getCurrentPlayer().getBoundingBox()));
		assertTrue(base.getCurrentPlayer().getX() == clone.getCurrentPlayer().getX());
		assertTrue(base.getCurrentPlayer().getY() == clone.getCurrentPlayer().getY());
	}

	/*
	 * Checks base player's inventory matches clone player's inventory
	 */
	private void checkPlayerInventory(Controller base, Controller clone){
		Item[] baseInventory = base.getCurrentPlayer().getInventory();
		Item[] cloneInventory = clone.getCurrentPlayer().getInventory();
		
		assertTrue(baseInventory[0].equals(cloneInventory[0]));
		assertTrue(baseInventory[1].equals(cloneInventory[1]));
		assertTrue(baseInventory[2].equals(cloneInventory[2]));
		
		assertTrue(base.getCurrentPlayer().getWeapon().equals(clone.getCurrentPlayer().getWeapon()));
	}

	/*
	 * Checks items on room floor are same in base and clone
	 */
	private void checkRoomItems(Controller base, Controller clone){
		List<Room> baseRooms = base.getRooms();
		List<Room> cloneRooms = clone.getRooms();
		
		for(int i = 0; i< baseRooms.size(); i++){
			Item[][] baseContents = baseRooms.get(i).getContents();
			Item[][] cloneContents = cloneRooms.get(i).getContents();
			
			for(int j = 0; j < baseContents.length; j++){
				for(int k = 0; k < baseContents[j].length; k++){
					if(!(baseContents[j][k] instanceof Floor) || 
							!(cloneContents[j][k] instanceof Floor)) continue;
					if(((Floor)baseContents[j][k]).getItem() != null &&
							((Floor)cloneContents[j][k]).getItem() != null){
					assertTrue(((Floor)baseContents[j][k]).getItem().equals
							(((Floor)cloneContents[j][k]).getItem()));
					}
				}
			}
		}
	}
}
