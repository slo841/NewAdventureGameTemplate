package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Fireball extends MovingEntity {
	private static final SpriteSheet sheet = SpriteSheet.DungeonCrawl;
	private double angle;
	
	public Fireball(int x, int y, Level level, int speed) {
		super(x, y, speed, sheet, level);
		
		angle = angleTowardEntity(level.getPlayer());
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, sheet, 6, 37, Screen.MirrorDirection.NONE);
	}

	@Override
	public void tick() {
		move();
		tickCount++;
		int ticks = 0;
		ticks++;
		
		if (ticks > 200) {
			markAsDead();
		}
		
	}

	@Override
	public void move() {
		this.moveInDirection(angle);
	}
}
