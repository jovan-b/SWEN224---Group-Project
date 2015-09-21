package gameObjects.weapons.projectiles;

import characters.Player;

public class PaintBall extends BasicProjectile{
	
	public PaintBall(){
		super();
	}
	
	private PaintBall(Player p, int x, int y, double theta){
		super(p, x, y, theta);
	}

	@Override
	public Projectile newInstance(Player p, double theta) {
		return new PaintBall(p, p.getX(), p.getY(), theta);
	}
	
}
