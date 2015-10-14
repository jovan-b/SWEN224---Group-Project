package gameWorld;

/**
 * A class to define the point values and costs of ingame events
 * @author Chris Read 300254724
 * @author Sarah Dobie 300315033
 *
 */
public class PointValues {
	// if a player has this many points, they win the game
	public static final int END_GAME_TARGET = 32768;
	// a player gains this many points for killing another player
	public static final int KILL_PLAYER = 2500;
	// a player loses this many points if they die
	public static final int DEATH = 1000;
	// a player loses this many points if they kill an NPC
	public static final int NPC_DEATH = 875;
	public static final int NPC_HOSTILE_DEATH = 500;
	// costs of healing items
	public static final int MEDICINE_COST = 300;
	public static final int PILL_COST = 800;
	// base value for random treasures
	public static final int TREASURE_BASE = 1500;
}
