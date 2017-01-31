package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Wumpus extends MovingEntity {
	private static final int WUMPUS_TILE_ROW = 4;
	private static final int WUMPUS_TILE_COL = 0;
	private static final SpriteSheet sheet = SpriteSheet.DungeonCrawl;
	private Entity target;
	
	public Wumpus(int x, int y, Level level, int speed, Entity target) {
		super(x, y, speed, sheet, level);
		
		this.target = target;
	}

	public void tick() {
		tickCount++;
		
		move();
	}
	
	@Override
	public void render(Screen screen) {
		screen.render(x, y, sheet, WUMPUS_TILE_ROW, WUMPUS_TILE_COL, Screen.MirrorDirection.NONE);
	}

	@Override
	public void move() {
//		this.moveRandom();
//		this.moveTowardWorldCoordinates(10, 10);
		this.moveTowardEntity(target);
	}

}
