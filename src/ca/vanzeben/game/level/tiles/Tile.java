package ca.vanzeben.game.level.tiles;

import java.util.ArrayList;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class Tile {

	public static final ArrayList<Tile> tiles = new ArrayList<Tile>();
	private static int nextId = 0;
	public static final Tile VOID = new BasicSolidTile(SpriteSheet.tileSheet, 4,
			0, 0xFF000000);
	public static final Tile STONE = new BasicSolidTile(SpriteSheet.tileSheet, 10,
			8, 0xFF555555);
	public static final Tile GRASS = new BasicTile(SpriteSheet.tileSheet, 1, 0,
			0xFF00FF00);
	public static final Tile GRASS2 = new BasicTile(SpriteSheet.tileSheet,
			new int[][] { { 1, 0 }, { 11, 1 } }, 0x00FF00FF);

	public static final Tile WATER = new AnimatedTile(SpriteSheet.tileSheet,
			new int[][] { { 9, 0 }, { 0, 1 } }, 0xFF0000FF, 1000);

	protected int id;
	protected ArrayList<Loc> imageLocLayers;
	protected boolean solid;
	protected boolean emitter;
	private int levelColour;
	protected SpriteSheet tileSheet;

	public Tile(SpriteSheet sheet, boolean isSolid, boolean isEmitter,
			int levelColour) {
		this.id = nextId;
		nextId++;

		this.tileSheet = sheet;
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
			int displayWidth, int displayHeight);

	public int getId() {
		return id;
	}

	/***
	 * Add a new tile image to display on top of the existing ones. Automatically
	 * adds on top of all existing layers.
	 * 
	 * @param x
	 *          column of tile in tilesheet to add (note: not pixel coordinate)
	 * @param y
	 *          row of tile in tilesheet to add
	 */
	public void addDisplayLayer(int x, int y) {
		this.imageLocLayers.add(new Loc(x, y));
	}

	/***
	 * Removes the top display layer.  Returns the Loc representing the tile coordinates for that layer.
	 * @return
	 */
	public Loc removeDisplayLayer() {
		return this.imageLocLayers.remove(imageLocLayers.size()-1);
	}

	/***
	 * Removes layer from display. Returns the Loc representing the tile
	 * coordinates for that layer.
	 * 
	 * @param layer
	 *          layer index to remove
	 * @return
	 */
	public Loc removeDisplayLayer(int layer) {
		return this.imageLocLayers.remove(layer);
	}
}
