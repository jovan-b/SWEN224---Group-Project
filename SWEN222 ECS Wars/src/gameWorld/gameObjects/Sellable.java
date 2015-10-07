package gameWorld.gameObjects;

public abstract class Sellable implements Item {
	protected int cost;

	public Sellable(int cost){
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}
}
