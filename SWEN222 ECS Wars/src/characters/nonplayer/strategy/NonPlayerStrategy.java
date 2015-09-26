package characters.nonplayer.strategy;

import characters.nonplayer.NonPlayer;

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
}
