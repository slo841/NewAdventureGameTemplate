package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Tower extends Entity {
	private static final SpriteSheet sheet = SpriteSheet.DungeonCrawl;
	private Entity target;
	
	public Tower(int x, int y, Level level) {
		super(x, y, sheet, level);
	}

	@Override
	public void tick() {
		level.addFireball(x(), y());
		
		tickCount++;
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, sheet, 11, 44, Screen.MirrorDirection.NONE);
		
	}

}
