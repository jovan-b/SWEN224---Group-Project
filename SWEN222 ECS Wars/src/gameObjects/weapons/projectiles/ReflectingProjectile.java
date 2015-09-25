package gameObjects.weapons.projectiles;

import characters.Player;


/**
 * A projectile that bounces off walls
 * @author Carl
 *
 */
public abstract class ReflectingProjectile extends BasicProjectile {
	
	protected int bounces;
	
	protected ReflectingProjectile(int bounces){
		super();
		this.bounces = bounces;
	}
	
	protected ReflectingProjectile(Player p, int x, int y, double theta, int bounces){
		super(p, x, y, theta);
		this.bounces = bounces;
	}

	@Override
	public void update() {
		double dx = Projectile.xDiff(theta, speed);
		double dy = Projectile.yDiff(theta, speed);
		double oldX = x;
		double oldY = y;
		x += dx;
		y += dy;
		
		//Check to see if we've hit a player
		for (Player p : room.getPlayers()){
			if (p.getBoundingBox().contains(this.getBoundingBox())){
				this.playerCollision(p);
			}
		}
		
		//Check to see if we've collided with an object
		if (!room.itemAt((int)x, (int)y).canWalk()){
			if (bounces == 0){ //If we can't bounce anymore, stop firing
				this.setActive(false);
			} else {
				//calculate our angle of reflection, and update our direction
				//check if we hit on the horizontal
				
				if (!room.itemAt((int)oldX, (int)y).canWalk()) { //Otherwise we must have hit vertical
					theta = Player.angleBetweenPlayerAndMouse(oldX, oldY, x, oldY-dy);
				} else {
					theta = Player.angleBetweenPlayerAndMouse(oldX, oldY, oldX-dx, y);
				}

				bounces--;
			}
		}
	}
	
	/**
	 * A hook method which gets called when this projectile collides with a player
	 * @param p
	 */
	protected void playerCollision(Player p){
		if (p == player){return;} //Players can't shoot themselves
		p.modifyHealth(damage);
		this.setActive(false);
	}

}
