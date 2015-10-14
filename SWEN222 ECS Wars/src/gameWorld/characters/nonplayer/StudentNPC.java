package gameWorld.characters.nonplayer;

import gameWorld.Room;
import gameWorld.characters.nonplayer.strategy.RespawnStrategy;
import gameWorld.characters.nonplayer.strategy.SentryCombatStrategy;
import gameWorld.characters.nonplayer.strategy.WanderingStrategy;

/**
 * Represents a Student NPC
 * 
 * @author Carl Anderson 300264124
 *
 */
public class StudentNPC extends NonPlayer {

	public StudentNPC(Room room, int posX, int posY) {
		super(room, posX, posY, new WanderingStrategy("This is a student. He looks confused"));
		
		this.setStrategy(Events.COMBAT, new SentryCombatStrategy(10));
		this.setStrategy(Events.DEATH, new RespawnStrategy(3000));
		
		//TODO: Load images
	}

}
