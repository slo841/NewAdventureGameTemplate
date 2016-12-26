package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.gfx.Colours;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Player {
	private int x, y;
	private Level level;

	protected String name;
	protected int speed = 1;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;

	private InputHandler input;
	private int colour = Colours.get(-1, 111, 145, 543);
	private int scale = 1;
	protected boolean isSwimming = false;
	private int tickCount = 0;
	private String username;

	private SpriteSheet sheet;

	public Player(Level level, int x, int y, InputHandler input, String username,
			SpriteSheet sheet) {
		this.x = x;
		this.y = y;
		this.level = level;
		this.input = input;
		this.username = username;
		this.sheet = sheet;
	}

	public void move(int xa, int ya) {
		System.out.println("Moving: " + xa + " " + ya);
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;
		if (!hasCollided(xa, ya)) {
			if (ya < 0)
				movingDir = 0;
			if (ya > 0)
				movingDir = 1;
			if (xa < 0)
				movingDir = 2;
			if (xa > 0)
				movingDir = 3;
			x += xa * speed;
			y += ya * speed;
		}
	}

	protected boolean isSolidTile(int xa, int ya, int x, int y) {
		if (level == null) {
			return false;
		}
		Tile lastTile = level.getTile((this.x + x) >> 3, (this.y + y) >> 3);
		Tile newTile = level.getTile((this.x + x + xa) >> 3,
				(this.y + y + ya) >> 3);
		if (!lastTile.equals(newTile) && newTile.isSolid()) {
			return true;
		}
		return false;
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public String getName() {
		return name;
	}

	public int getNumSteps() {
		return numSteps;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getMovingDir() {
		return movingDir;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir) {
		this.movingDir = movingDir;
	}

	public void tick() {
		int xa = 0;
		int ya = 0;
		if (input != null) {
			if (input.up.isPressed()) {
				ya--;
			}
			if (input.down.isPressed()) {
				ya++;
			}
			if (input.left.isPressed()) {
				xa--;
			}
			if (input.right.isPressed()) {
				xa++;
			}
		}
		if (xa != 0 || ya != 0) {
			move(xa, ya);
			isMoving = true;
		} else {
			isMoving = false;
		}
		if (level.getTile(this.x >> 3, this.y >> 3).getId() == 3) {
			isSwimming = true;
		}
		if (isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3) {
			isSwimming = false;
		}
		tickCount++;
	}

	public void render(Screen screen) {
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		// if (movingDir == 1) {
		// xTile += 2;
		// } else if (movingDir > 1) {
		// xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
		// flipTop = (movingDir - 1) % 2;
		// }

		int xOffset = x;
		int yOffset = y;

		// if (isSwimming) {
		// int waterColour = 0;
		// yOffset += 4;
		// if (tickCount % 60 < 15) {
		// waterColour = Colours.get(-1, -1, 225, -1);
		// } else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
		// yOffset -= 1;
		// waterColour = Colours.get(-1, 225, 115, -1);
		// } else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
		// waterColour = Colours.get(-1, 115, -1, 225);
		// } else {
		// yOffset -= 1;
		// waterColour = Colours.get(-1, 225, 115, -1);
		// }
		// screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColour, 0x00, 1);
		// screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColour, 0x01,
		// 1);
		// }
		// screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile *
		// 32,
		// colour, flipTop, scale);
		// screen.render(xOffset + modifier - (modifier * flipTop), yOffset,
		// (xTile + 1) + yTile * 32, colour, flipTop, scale);

		// if (!isSwimming) {
		// screen.render(xOffset + (modifier * flipBottom), yOffset + modifier,
		// xTile + (yTile + 1) * 32, colour, flipBottom, scale);
		// screen.render(xOffset + modifier - (modifier * flipBottom),
		// yOffset + modifier, (xTile + 1) + (yTile + 1) * 32, colour,
		// flipBottom, scale);
		// }

		if (tickCount % 60 < 15) {
			screen.render(x, y, sheet, 0, 0, 0, Screen.MirrorDirection.NONE, 3);
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
			screen.render(x, y, sheet, 0, 1, 0, Screen.MirrorDirection.NONE, 3);
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
			screen.render(x, y, sheet, 0, 2, 0, Screen.MirrorDirection.NONE, 3);
		} else {
			screen.render(x, y, sheet, 0, 3, 0, Screen.MirrorDirection.NONE, 3);
		}
		
		if (username != null) {
			Font.render(username, screen, xOffset - ((username.length() - 1) / 2 * 8),
					yOffset - 10, Colours.get(-1, -1, -1, 555), 1);
		}
	}

	public boolean hasCollided(int xa, int ya) {
		int xMin = 0;
		int xMax = 7;
		int yMin = 3;
		int yMax = 7;
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMin)) {
				return true;
			}
		}
		for (int x = xMin; x < xMax; x++) {
			if (isSolidTile(xa, ya, x, yMax)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMin, y)) {
				return true;
			}
		}
		for (int y = yMin; y < yMax; y++) {
			if (isSolidTile(xa, ya, xMax, y)) {
				return true;
			}
		}
		return false;
	}

	public String getUsername() {
		return this.username;
	}
}