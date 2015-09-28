package gameObjects.weapons.projectiles;

import characters.Player;

public class RubberBullet extends ReflectingProjectile {
	public static final int MAX_BOUNCES = 5;
	
	public RubberBullet(){
		super(MAX_BOUNCES);
	}
	
	private RubberBullet(Player p, int x, int y, double theta){
		super(p, x, y, theta, MAX_BOUNCES);
		this.setSpeedMultiplier(0.8);
	}
	
	@Override
	public Projectile newInstance(Player p, double theta) {
		return new RubberBullet(p, p.getX(), p.getY(), theta);
	}
	
	@Override
	protected void playerCollision(Player p){
		//if (p == this.player){return;} //Uncomment this to stop damaging owner player
		if (p == this.player && bounces == MAX_BOUNCES){
			return;
		}
		
		p.modifyHealth(damage);
		this.setActive(false);
	}

}
