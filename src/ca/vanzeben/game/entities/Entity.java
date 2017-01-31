package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class Entity {
	protected int x, y;
	protected int width, height;
	protected Level level;
	protected int tickCount = 0;
	protected SpriteSheet sheet;
	private boolean isDead = false;
	
	public Entity(int x, int y, SpriteSheet sheet, Level level) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.sheet = sheet;
		
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
		tickCount = 0;
	}
	
	public boolean isHitting(Entity other) {
		return (areIntervalsOverlapping(this.leftX(), this.rightX(), other.leftX(), other.rightX())
				&& areIntervalsOverlapping(this.topY(), this.bottomY(), other.topY(), other.bottomY()));
	}
	
	public boolean areIntervalsOverlapping(int low1, int high1, int low2, int high2) {
		if (high1 < low2) return false;
		if (high2 < low1) return false;
		return true;
	}
	
	public abstract void tick();
	
	public abstract void render(Screen screen);

	public int x() {
		return x;
	}

	public int leftX() {
		return x;
	}

	public int rightX() {
		return leftX() + this.width;
	}

	public int y() {
		return y;
	}

	public int topY() {
		return y;
	}

	public int bottomY() {
		return topY() + this.height;
	}

	public int centerX() {
		return x + this.width / 2;
	}

	public int centerY() {
		return y + this.height / 2;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void markAsDead() {
		isDead = true;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
}
