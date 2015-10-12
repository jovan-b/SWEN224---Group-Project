package gameWorld.characters.nonplayer.strategy;

import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;
import gameWorld.gameObjects.Floor;
import gameWorld.gameObjects.Item;
import gameWorld.gameObjects.MedicineBottle;
import gameWorld.gameObjects.PillBottle;
import gameWorld.gameObjects.Sellable;

public class WanderingMerchantStrategy extends WanderingStrategy {
	
	private Sellable inventoryItem;
	
	public WanderingMerchantStrategy(){
		// decide which item this npc should sell
		double chance = Math.random();
		if(chance < 0.7){
			inventoryItem = new MedicineBottle();
			description = "I sell medicine!";
		} else {
			inventoryItem = new PillBottle();
			description = "I sell pills!";
		}
		description += " "+ inventoryItem.getCost() +" points each.";
	}
	
	@Override
	public void interact(Player p, NonPlayer npc){
		for(int i=0; i<npc.getInventory().length; i++){
			npc.pickUp(inventoryItem);
		}
		// if player has enough points
		if(p.getPoints() >= inventoryItem.getCost()){
			
			//Check the floor has space
			Item square = npc.getCurrentRoom().itemAt(npc.getX(), npc.getY());
			if (square instanceof Floor){
				if (((Floor) square).getItem() == null){
					
					//Drop the item and remove the points from the buying player
					npc.dropItem(0, null);
					p.removePoints(inventoryItem.getCost());
				}
			}
		}
	}
}
