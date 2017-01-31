package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Player extends MovingEntity {
	private boolean debug = false;

	protected String name;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;

	private InputHandler input;
	protected boolean isSwimming = false;
	private String username;

	private int money;
	private static final int PLAYER_SPEED = 15;

	public Player(Level level, int x, int y, InputHandler input, String username, SpriteSheet sheet) {
		super(x, y, PLAYER_SPEED, sheet, level);
		this.input = input;
		this.username = username;
		this.money = 0;
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	@Override
	public void move() {
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
		if (!willCollideWithTile(dx, dy)) {
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
		return this.level.getTileTypeAtWorldCoordinates(x + width/2, y + 3*height/4);
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

	public void tick() {
		move();

		tickCount++;
	}

	public void render(Screen screen) {
		Tile current = this.getCurrentTileType();
		if (current == Tile.WATER) {
			screen.render(x, y, sheet, 6, 4, Screen.MirrorDirection.NONE);
		} else {
			renderAnimatedStanding(screen);
		}
		
		if (debug) {
			renderDebuggingElements(screen);
		}

		if (username != null) {
			screen.renderTextAtWorldCoordinates(username, Font.DEFAULT,
					centerX() - Font.DEFAULT.getWidthOf(username) / 2, y - 10, 1);
		}
	}

	private void renderAnimatedStanding(Screen screen) {
		if (tickCount % 60 < 15) {
			screen.render(x, y, sheet, 0, 0, Screen.MirrorDirection.NONE);
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
			screen.render(x, y, sheet, 0, 1, Screen.MirrorDirection.NONE);
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
			screen.render(x, y, sheet, 0, 2, Screen.MirrorDirection.NONE);
		} else {
			screen.render(x, y, sheet, 0, 3, Screen.MirrorDirection.NONE);
		}
	}


	public String getUsername() {
		return this.username;
	}

	private void renderDebuggingElements(Screen screen) {
		screen.highlightTileAtWorldCoordinates(leftX(), topY(), level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(leftX(), bottomY(), level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), topY(), level.getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), bottomY(), level.getTileDisplaySize());

		Font.DEFAULT.render("" + x + ", " + y, screen, x - ((username.length() - 1) / 2 * 8), y - 10, 1);
	}

	public void handleCollision(Entity e) {
		if (e instanceof Coin) {
			money += ((Coin) e).getValue();
			e.markAsDead();
		} else if (e instanceof Fireball) {
			this.markAsDead();
		}
		
	}
}