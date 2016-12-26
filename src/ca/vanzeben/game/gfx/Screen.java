package ca.vanzeben.game.gfx;

public class Screen {
	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	public static enum MirrorDirection {
		Y, X, BOTH, NONE
	}

	public int[] pixels;

	public int xOffset = 0; // Number of pixels to offset screen by (within the
													// level image)
	public int yOffset = 0;

	public int width;
	public int height;

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;

		pixels = new int[width * height];
	}

	/***
	 * Render a tile on the screen at a particular location.
	 * 
	 * @param xPos
	 *          x coordinate to render the tile
	 * @param yPos
	 *          y coordinate to render the tile
	 * @param tileRow
	 *          row # in sprite sheet for the tile to be rendered
	 * @param tileCol
	 *          col # in the sprite sheet for the tile to be rendered
	 * @param colour
	 *          color
	 * @param mirrorDir
	 *          1 mirrors x, 2 mirrors y, 0 mirrors none, 3 mirrors both
	 * @param scale
	 */
	public void render(int xPos, int yPos, SpriteSheet sheet, int tileRow,
			int tileCol, int colour, MirrorDirection mirrorDir, int scale) {
		xPos -= xOffset;
		yPos -= yOffset;

		boolean mirrorX = (mirrorDir == MirrorDirection.BOTH
				|| mirrorDir == MirrorDirection.X);
		boolean mirrorY = (mirrorDir == MirrorDirection.BOTH
				|| mirrorDir == MirrorDirection.Y);

		int scaleMap = scale - 1;

		int tileOffset = (tileCol * sheet.getSpriteWidth())
				+ (tileRow * sheet.getSpriteHeight()) * sheet.getPixelWidth();

		for (int y = 0; y < sheet.getSpriteHeight(); y++) {
			int ySheet = y;
			if (mirrorY)
				ySheet = (sheet.getSpriteHeight() - 1) - y;

			int yPixel = y + yPos + (y * scaleMap)
					- ((scaleMap * sheet.getSpriteHeight()) / 2);

			for (int x = 0; x < sheet.getSpriteWidth(); x++) {
				int xSheet = x;
				if (mirrorX)
					xSheet = (sheet.getSpriteWidth() - 1) - x;
				int xPixel = x + xPos + (x * scaleMap)
						- ((scaleMap * sheet.getSpriteWidth()) / 2);

				// int col = (colour >> (sheet.getPixels()[xSheet
				// + ySheet * sheet.getPixelWidth() + tileOffset] * 8)) & 255;

				int col = sheet.getPixels()[xSheet + ySheet * sheet.getPixelWidth()
						+ tileOffset];

				for (int yScale = 0; yScale < scale; yScale++) {
					if (yPixel + yScale < 0 || yPixel + yScale >= height)
						continue;
					for (int xScale = 0; xScale < scale; xScale++) {
						if (xPixel + xScale < 0 || xPixel + xScale >= width)
							continue;
						pixels[(xPixel + xScale) + (yPixel + yScale) * width] = col;
					}
				}
			}
		}
	}

	public void render(int xPos, int yPos, SpriteSheet sheet, int tileId,
			int colour, MirrorDirection mirrorDir, int scale) {
		render(xPos, yPos, sheet, tileId / sheet.getNumSpritesWidth(),
				tileId % sheet.getNumSpritesWidth(), colour, mirrorDir, scale);
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
