package ca.vanzeben.game.level.tiles;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.level.Level;

public class BasicTile extends Tile {

	/***
	 * Constructor for basic tile.
	 * 
	 * @param imageLocations
	 *          array with each row a pair of coordinates for a tile image in the
	 *          tile SpriteSheet. Will be displayed row 0 up, so place
	 *          non-transparent tile at row 0, and any transparent overlays in
	 *          subsequent rows.
	 * 
	 * @param levelColour
	 */
	public BasicTile(int[][] imageLocations, int levelColour) {
		super(false, false, levelColour);
		this.imageLocLayers = imageLocations;
	}

	/***
	 * Constructor for basic tile
	 * 
	 * @param x
	 *          x coordinate for tile in sprite sheet. NOTE: not pixel coordinate.
	 *          If tile is the 3rd, tile over, x should be 2 (since tiles are
	 *          numbered starting at 0)
	 * @param y
	 *          y coordinate for tile in sprite sheet. (See note above)
	 * @param levelColour
	 *          color this tile will be represented by in level image.
	 */
	public BasicTile(int x, int y, int levelColour) {
		this(new int[][] { { x, y } }, levelColour);
	}

	public void tick() {
	}

	/***
	 * World (x, y) coordinates.
	 */
	public void render(Screen screen, Level level, int x, int y, int scale) {
		for (int layer = 0; layer < this.imageLocLayers.length; layer++) {
			screen.render(x, y, tileSheet, imageLocLayers[layer][0],
					imageLocLayers[layer][1], Screen.MirrorDirection.NONE, scale);
		}
	}
}