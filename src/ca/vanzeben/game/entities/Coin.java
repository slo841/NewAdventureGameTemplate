package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Coin extends Entity {
	private int value;
	private static final SpriteSheet sheet = SpriteSheet.tileSheet;
	
	public Coin(int x, int y, int value, Level level) {
		super(x, y, sheet, level);
		
		this.value = value;
		
	}
	
	public void tick() {
		tickCount++;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public void render(Screen screen) {
		screen.render(x, y, sheet, 16, 12, Screen.MirrorDirection.NONE);
	}
}
