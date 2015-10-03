package gameWorld.characters.nonplayer.strategy;

import gameWorld.characters.nonplayer.NonPlayer;

public class WaitStrategy implements NonPlayerStrategy {
	
	protected NonPlayer npc;
	
	@Override
	public void update() {
		//do absolutely nothing
	}

	@Override
	public void setNPCReference(NonPlayer p) {
		npc = p;
	}

	@Override
	public NonPlayer getNPCReference() {
		return npc;
	}

	@Override
	public void initialize() {
		//do nothing
	}
	

}
