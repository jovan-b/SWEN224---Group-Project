package gameObjects.weapons.projectiles;

import java.awt.Rectangle;

import gameObjects.Room;
import characters.Player;

/**
 * A basic projectile which this weapon can fire
 * Should travel in a linear fashion in theta angle last x, y position
 * 
 * @author Jah Seng Lee
 * @author Carl Anderson
 *
 */
public abstract class BasicProjectile implements Projectile {
	//The owner of the projectile
	private Player player;
	private Room room;
	
	private int x;
	private int y;
	private int hitBox = 2;
	private double theta;
	
	private int damage = -10;
	
	private int speedMulti = 1;
	private int speed = BASE_SPEED * speedMulti;	//pixels per frame
	
	/**
	 * A basic constructor for producing new projectiles
	 */
	public BasicProjectile(){		
		//Assign non-values to all the fields
		player = null;
		x = -1;
		y = -1;
		theta = Double.NaN;
	}
	
	/**
	 * A constructor to produce a new projectile with position and direction
	 * @param x
	 * @param y
	 * @param theta direction of travel
	 */
	protected BasicProjectile(Player p, int x, int y, double theta){
		this.player = p;
		this.room = p.getCurrentRoom();
		
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	@Override
	public void update() {
				
		x += Projectile.xDiff(theta, speed);
		y += Projectile.yDiff(theta, speed);
		
		//Check to see if we've hit a player
		for (Player p : room.getPlayers()){
			if (p == player){continue;} //Players can't shoot themselves
			if (p.getBoundingBox().contains(this.getBoundingBox())){
				p.modifyHealth(damage);
				room.removeProjectile(this);
				return;
			}
		}
		
		//Check to see if we've collided with an object
		if (!room.itemAt(x, y).canWalk()){
			room.removeProjectile(this);
		}
	}
	
	@Override
	public Rectangle getBoundingBox(){
		return new Rectangle(x-hitBox, y-hitBox, hitBox*2, hitBox*2);
	}
	
	@Override
	public int getX(){
		return x;
	}
	
	@Override
	public int getY(){
		return y;
	}

}
