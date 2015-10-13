package gameWorld.gameObjects;

/**
 * An abstract class to identify items that are buyable from NPCs
 * @author Sarah Dobie 300315033
 *
 */
public abstract class Sellable implements Item {
	protected int cost;

	public Sellable(int cost){
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}
}
