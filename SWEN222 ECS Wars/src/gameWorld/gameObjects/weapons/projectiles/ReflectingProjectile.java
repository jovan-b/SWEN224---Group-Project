package gameWorld.gameObjects.weapons.projectiles;

import gameWorld.characters.Player;


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
		double newX = x + dx;
		double newY = y + dy;
		
		//Check to see if we've hit a player
		for (Player p : room.getAllCharacters()){
			if (p.getBoundingBox().contains(this.getBoundingBox())){
				this.playerCollision(p);
			}
		}
		
		//Check to see if we've collided with an object
		if (!room.itemAt((int)newX, (int)newY).canWalk()){
			if (bounces == 0){ //If we can't bounce anymore, stop firing
				this.setActive(false);
			} else {
				//calculate our angle of reflection, and update our direction
				//check if we hit on the horizontal
				
				if (!room.itemAt((int)oldX, (int)newY).canWalk()) { //Otherwise we must have hit vertical
					theta = Player.angleBetweenPlayerAndMouse(oldX, oldY, newX, oldY-dy);
				} else {
					theta = Player.angleBetweenPlayerAndMouse(oldX, oldY, oldX-dx, newY);
				}

				bounces--;
			}
		} else {
			x = newX;
			y = newY;
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
