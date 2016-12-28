package ca.vanzeben.game.level.tiles;

import java.util.ArrayList;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class Tile {
	public static final ArrayList<Tile> tiles = new ArrayList<Tile>();
	private static int nextId = 0;
	public static final Tile VOID = new BasicSolidTile(4, 0, 0xFF000000);
	public static final Tile STONE = new BasicSolidTile(10, 8, 0xFF555555);
	public static final Tile GRASS = new BasicTile(1, 0, 0xFF00FF00);
	public static final Tile GRASS2 = new BasicTile(
			new int[][] { { 1, 0 }, { 11, 1 } }, 0x00FF00FF);

	public static final Tile WATER = new AnimatedTile(
			new int[][] { { 9, 0 }, { 0, 1 } }, 0xFF0000FF, 1000);

	protected int id;
	protected int[][] imageLocLayers;
	protected boolean solid;
	protected boolean emitter;
	private int levelColour;

	protected static SpriteSheet tileSheet = new SpriteSheet("tiles",
			"/tilesheet.png", 19, 20);

	public Tile(boolean isSolid, boolean isEmitter, int levelColour) {
		this.id = nextId;
		nextId++;

		this.solid = isSolid;
		this.emitter = isEmitter;
		this.levelColour = levelColour;

		tiles.add(this);
	}

	public boolean isSolid() {
		return solid;
	}

	public boolean isEmitter() {
		return emitter;
	}

	public int getLevelColour() {
		return levelColour;
	}

	public abstract void tick();

	public abstract void render(Screen screen, Level level, int x, int y,
			int scale);

	public int getId() {
		return id;
	}
}
