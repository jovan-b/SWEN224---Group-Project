package gameWorld.characters.nonplayer.strategy;

import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.NonPlayer;

public interface NonPlayerStrategy {
	
	/**
	 * Run the strategy
	 */
	public void update();
	
	/**
	 * Initialization code for this strategy
	 */
	public void initialize();
	
	/**
	 * Set the NPC referenced by this strategy
	 * @param p
	 */
	public void setNPCReference(NonPlayer p);
	
	/**
	 * Get the NPC referenced by this strategy
	 * @return
	 */
	public NonPlayer getNPCReference();
	
	/**
	 * Player interacts with this character.
	 * @param p The player interacting with me
	 */
	public void interact(Player p, NonPlayer npc);
}
