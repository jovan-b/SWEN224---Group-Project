package gameWorld.characters.nonplayer;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import gameWorld.PointValues;
import gameWorld.Room;
import gameWorld.characters.Player;
import gameWorld.characters.nonplayer.strategy.NonPlayerStrategy;
import gameWorld.characters.nonplayer.strategy.WaitStrategy;
import gameWorld.gameObjects.weapons.PaintballGun;
import gameWorld.gameObjects.weapons.projectiles.Projectile;

public class NonPlayer extends Player {
	public static final NonPlayerStrategy GLOBAL_DEFAULT = new WaitStrategy();
	public static final Type type = Player.Type.NonPlayer;
	public static final int NPC_HEALTH_MAX = 80;
	
	/**
	 * An enum to describe what events non-players respond to
	 * @author Carl
	 *
	 */
	public enum Events {
		COMBAT,
		DEATH,
		DEFAULT
	}
	
	protected Map<Events, NonPlayerStrategy> strategies;
	protected NonPlayerStrategy active;

	public NonPlayer(Room room, int posX, int posY, NonPlayerStrategy initial) {
		super(room, posX, posY);
		super.health = NPC_HEALTH_MAX;
		super.maxHealth = NPC_HEALTH_MAX;
		active = initial;
		strategies = new HashMap<Events, NonPlayerStrategy>();
		
		this.currentWeapon = new PaintballGun();
		
		this.setStrategy(Events.DEFAULT, initial);
		
		// Load sprites
		sprites = new Image[4][3];
		try {
			for (int dir = 0; dir < 4; dir++){
				for (int ani = 0; ani < 3; ani++){
					sprites[dir][ani] = ImageIO.read(new File("Resources"+File.separator+"NPC"+File.separator+"npc"+dir+ani+".png"));
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading player images: " + e.getMessage());
		}
		scaledSprites = sprites;
	}
	
	@Override
	public void update(){
		active.update();
	}
	
	@Override
	public void modifyHealth(int amt, Projectile p){
		if (amt < 0){this.respond(Events.COMBAT);}
		super.modifyHealth(amt, null);
		if (this.isDead()){
			// remove player's points for killing npc
			if(p != null && p.getPlayer() != this){
				p.getPlayer().removePoints(PointValues.NPC_DEATH);
			}
			this.respond(Events.DEATH);
		}
	}
	
	@Override
	public void shoot(int x, int y) {
		double theta = Player.angleBetweenPlayerAndMouse(this.getX(), this.getY(),
				x, y);
		
		//Correct theta based on view direction
		theta += Math.toRadians(90)*viewDirection;
		currentRoom.addProjectile(currentWeapon.fire(this, theta));
	}
	
	@Override
	public void setCurrentRoom(Room newRoom, int newX, int newY) {
		currentRoom.removeNPC(this);
		newRoom.addNPC(this);
		this.currentRoom = newRoom;
	}
	
	/**
	 * Causes the npc to respond to a specific type of event
	 * @param event
	 */
	public void respond(Events event){
		NonPlayerStrategy strat = strategies.get(event);
		if (strat == null){
			strat = strategies.get(Events.DEFAULT) != null ? strategies.get(Events.DEFAULT) : GLOBAL_DEFAULT;
		}

		active = strat;
		active.initialize();
	}
	
	/**
	 * Sets which strategy the NPC will use for an event
	 * @param event
	 * @param strategy
	 */
	public void setStrategy(Events event, NonPlayerStrategy strategy){
		strategies.put(event, strategy);
		strategy.setNPCReference(this);
	}

	@Override
	public Type getType() {
		return type;
	}
	
	/**
	 * Player interacts with this character.
	 * @param p The player interacting with me
	 */
	public void interact(Player p){
		active.interact(p, this);
	}
	
	public NonPlayerStrategy getStrategy(){
		return active;
	}
}
