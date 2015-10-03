package gameWorld.gameObjects.weapons.projectiles;

import java.awt.Rectangle;

import gameWorld.characters.Player;
import gameWorld.gameObjects.Room;

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
	protected Player player;
	protected Room room;
	protected boolean isActive;
	
	protected double x;
	protected double y;
	protected int row;
	protected int hitBox = 2;
	protected double theta;
	
	protected int damage = -10;
	
	protected double speedMulti = 1;
	protected double speed = BASE_SPEED * speedMulti;	//pixels per frame
	
	/**
	 * A basic constructor for producing new projectiles
	 */
	public BasicProjectile(){		
		//Assign non-values to all the fields
		player = null;
		x = -1;
		y = -1;
		theta = Double.NaN;
		isActive = false;
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
		this.isActive = true;
	}

	@Override
	public void update() {
		
		double dx = Projectile.xDiff(theta, speed);
		double dy = Projectile.yDiff(theta, speed);
		double newX = x + dx;
		double newY = y + dy;
		
		//Check to see if we've hit a player
		for (Player p : room.getAllCharacters()){
			if (p == player){continue;} //Players can't shoot themselves
			if (p.getBoundingBox().contains(this.getBoundingBox())){
				p.modifyHealth(damage);
				this.setActive(false);
				return;
			}
		}
		
		//Check to see if we've collided with an object
		if (!room.itemAt((int)newX, (int)newY).canWalk()){
			this.setActive(false);
		} else {
			x = newX;
			y = newY;
		}
	}
	
	@Override
	public Rectangle getBoundingBox(){
		return new Rectangle((int)x-hitBox, (int)y-hitBox, hitBox*2, hitBox*2);
	}
	
	@Override
	public int getX(){
		return (int)x;
	}
	
	@Override
	public int getY(){
		return (int)y;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	public boolean isActive(){
		return isActive;
	}
	
	public void setActive(boolean active){
		this.isActive = active;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public void setSpeedMultiplier(double multi){
		this.speedMulti = multi;
		this.speed = BASE_SPEED * multi;
	}

}
