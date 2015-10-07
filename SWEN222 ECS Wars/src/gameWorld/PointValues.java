package gameWorld;

public class PointValues {
	// if a player has this many points, they win the game
	public static final int END_GAME_TARGET = 32768;
	// a player gains this many points for killing another player
	public static final int KILL_PLAYER = 1000;
	// a player loses this many points if they die
	public static final int DEATH = 1000;
	// a player loses this many points if they kill an NPC
	public static final int NPC_DEATH = 875;
	// costs of healing items
	public static final int MEDICINE_COST = 300;
	public static final int PILL_COST = 800;
	// base value for random treasures
	public static final int TREASURE_BASE = 1000;
}
