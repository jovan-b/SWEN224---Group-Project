package gameObjects.weapons.projectiles;

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
	private int x;
	private int y;
	private double theta;
	
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
		
		this.x = x;
		this.y = y;
		this.theta = theta;
	}

	@Override
	public void update() {
		//TODO: May have to change all this, since we
		// have no consistent coordinate system other than
		// the array
		
		//TODO: Collision detection
		
		x += Projectile.xDiff(theta, speed);
		y += Projectile.yDiff(theta, speed);
		
	}

}
