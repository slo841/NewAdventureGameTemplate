package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Player {
	private boolean debug = false;
	private int x, y;
	private int width, height;
	private Level level;

	protected String name;
	protected int speed = 15;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;

	private InputHandler input;
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
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
	}

	/***
	 * Move the player by dx, dy
	 * 
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			numSteps--;
			return;
		}

		numSteps++;
		if (!willCollidedWithTile(dx, dy)) {
			if (dy < 0)
				movingDir = 0;
			if (dy > 0)
				movingDir = 1;
			if (dx < 0)
				movingDir = 2;
			if (dx > 0)
				movingDir = 3;
			x += dx;
			y += dy;
		}
	}

	public Tile getCurrentTileType() {
		return this.level.getTileTypeAtWorldCoordinates(x, y);
	}

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
		int xDir = 0;
		int yDir = 0;
		if (input != null) {
			if (input.up.isPressed()) {
				yDir--;
			}
			if (input.down.isPressed()) {
				yDir++;
			}
			if (input.left.isPressed()) {
				xDir--;
			}
			if (input.right.isPressed()) {
				xDir++;
			}
		}

		if (xDir != 0 || yDir != 0) {
			move(xDir * speed, yDir * speed);
			isMoving = true;
		} else {
			isMoving = false;
		}

		tickCount++;
	}

	public void render(Screen screen) {
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		int dx = x;
		int dy = y;

		if (tickCount % 60 < 15) {
			screen.render(dx, dy, sheet, 0, 0, Screen.MirrorDirection.NONE);
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
			screen.render(dx, dy, sheet, 0, 1, Screen.MirrorDirection.NONE);
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
			screen.render(dx, dy, sheet, 0, 2, Screen.MirrorDirection.NONE);
		} else {
			screen.render(dx, dy, sheet, 0, 3, Screen.MirrorDirection.NONE);
		}

		if (debug) {
			screen.highlightTileAtWorldCoordinates(leftX(), topY(),
					level.getTileDisplaySize());
			screen.highlightTileAtWorldCoordinates(leftX(), bottomY(),
					level.getTileDisplaySize());
			screen.highlightTileAtWorldCoordinates(rightX(), topY(),
					level.getTileDisplaySize());
			screen.highlightTileAtWorldCoordinates(rightX(), bottomY(),
					level.getTileDisplaySize());
			
			Font.render("" + x + ", " + y, screen,
					dx - ((username.length() - 1) / 2 * 8), dy - 10, 1);
		}

		if (username != null) {
			Font.render(username, screen, x - ((username.length() - 1) / 2 * 8),
					y - 10, 1);
		}
	}

	/***
	 * Check if player is going to collide with a solid tile if x changes by dx
	 * and y changes by dy
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */
	public boolean willCollidedWithTile(int dx, int dy) {
		// Calculate coordinates of all 4 corners of player sprite
		// Check each for collision

		Tile upperLeftTile = level.getTileTypeAtWorldCoordinates(leftX() + dx,
				topY() + dy);
		Tile lowerLeftTile = level.getTileTypeAtWorldCoordinates(leftX() + dx,
				bottomY() + dy);
		Tile upperRightTile = level.getTileTypeAtWorldCoordinates(rightX() + dx,
				topY() + dy);
		Tile lowerRightTile = level.getTileTypeAtWorldCoordinates(rightX() + dx,
				bottomY() + dy);

		if (upperLeftTile.isSolid() || lowerLeftTile.isSolid()
				|| upperRightTile.isSolid() || lowerRightTile.isSolid())
			return true;
		return false;
	}

	public String getUsername() {
		return this.username;
	}
}