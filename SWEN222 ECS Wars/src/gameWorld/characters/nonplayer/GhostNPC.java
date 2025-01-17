package gameWorld.characters.nonplayer;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import gameWorld.PointValues;
import gameWorld.Room;
import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.strategy.GhostStrategy;
import gameWorld.characters.nonplayer.strategy.RespawnStrategy;

/**
 * NPC describing a Ghost
 * It follows a player whenever a player is within range
 * GhostNPC cannot fire
 * GhostNPC deal a large amount damage when making contact with player
 * 
 * @author Carl Anderson 300264124
 */
public class GhostNPC extends NonPlayer {

	public GhostNPC(Room room, int posX, int posY) {
		super(room, posX, posY, new GhostStrategy(240));
		
		this.speed = 1;
		this.setStrategy(Events.DEATH, new RespawnStrategy(10000));
	}
	
	@Override
	protected void loadSprites(){
		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++){
				for (int ani = 0; ani < 3; ani++){
					sprites[dir][ani] = ImageIO.read(NonPlayer.class.getResource("/NPC/npc5"+dir+ani+".png"));
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
		scaledSprites = sprites;
	}
	
	@Override
	public void onDeath(Player p){
		p.givePoints(PointValues.NPC_HOSTILE_DEATH);
		active.interact(null, this);
	}
	
	@Override
	public boolean canMove(int x, int y){
		return true;
	}
}
