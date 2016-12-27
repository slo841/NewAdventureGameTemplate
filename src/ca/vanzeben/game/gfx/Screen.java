package ca.vanzeben.game.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.level.Level;

/***
 * Represents the graphics for the screen (what is actually displayed in the
 * window each frame). This does not represent the entire level which the screen
 * occupies a small part of.
 * 
 * There are two methods to use: setScreenPosition() sets the position of the
 * screen within the world coordinate system. render() is used to render
 * entities on the screen. This includes both background tiles and foreground
 * entities.
 * 
 * NOTE: You must run setGraphicsContext before rendering anything so the screen
 * renders to the correct image buffer. Very briefly, your game Canvas object
 * has a buffering strategy in which you render the next frame image before it
 * gets displayed; then the Canvas object tells that frame to display and you
 * will want to render to the next frame. In other words, you don't want to try
 * and render to the image that's being displayed as the current frame because
 * that can lead to visual artifacts.
 * 
 * All the buffer swaping is handled for you by the BufferStrategy in your
 * Canvas. But to be sure this class renders to the correct buffer, you must run
 * setGraphicsContext.
 * 
 * This currently happens in Game (which is a Canvas) with the line:
 * setGraphicsContext( this.getBufferStrategy().getDrawGraphics() );
 * 
 * @author David
 *
 */
public class Screen {
	public static final int MAP_WIDTH = 64;
	public static final int MAP_WIDTH_MASK = MAP_WIDTH - 1;

	public static enum MirrorDirection {
		Y, X, BOTH, NONE
	}

	public int[] nextFramePixels;

	public int x = 0; // Number of pixels to offset screen by (within the
										// level image)
	public int y = 0;

	public int width;
	public int height;

	private int mouseX, mouseY;

	private Graphics graphicsContext = null; // set by setGraphicsContext();

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;

		nextFramePixels = new int[width * height];
	}

	/***
	 * Highlights the tile at world coordinates x, y
	 * 
	 * @param tx
	 * @param ty
	 */
	public void highlightTileAtWorldCoordinates(int tx, int ty, int tileSize) {
		if (graphicsContext == null) {
			System.err.println("Graphics context is null in highlightTile()");
			return;
		}

		int dx = (tx / tileSize) * tileSize;
		int dy = (ty / tileSize) * tileSize;

		dx -= x; // change world coordinates (xPos, yPos) to screen coordinates
		dy -= y;

		int destx1 = dx;
		int desty1 = dy;
		int destx2 = destx1 + tileSize;
		int desty2 = desty1 + tileSize;

		this.graphicsContext.setColor(Color.YELLOW);
		this.graphicsContext.drawRect(destx1, desty1, tileSize, tileSize);
		this.graphicsContext.setColor(Color.BLACK);
	}

	/***
	 * Render a tile on the screen at a particular location.
	 * 
	 * @param xPos
	 *          world x coordinate to render the tile
	 * @param yPos
	 *          world y coordinate to render the tile
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
		if (graphicsContext == null) {
			System.err.println(
					"Graphics context is null. Canvas object for Game must call setGraphicsContext( getBufferStrategy().getDrawGraphics() );");
		}
		xPos -= x; // change world coordinates (xPos, yPos) to screen
								// coordinates
		yPos -= y;

		int sourcex1 = tileCol * sheet.getSpriteWidth();
		int sourcey1 = tileRow * sheet.getSpriteHeight();
		int sourcex2 = sourcex1 + sheet.getSpriteWidth();
		int sourcey2 = sourcey1 + sheet.getSpriteHeight();

		int destx1 = xPos;
		int desty1 = yPos;
		int destx2 = destx1 + sheet.getSpriteWidth() * scale;
		int desty2 = desty1 + sheet.getSpriteHeight() * scale;

		this.graphicsContext.drawImage(sheet.getImage(), destx1, desty1, destx2,
				desty2, sourcex1, sourcey1, sourcex2, sourcey2, null);

		this.graphicsContext.drawRect(destx1, desty1,
				sheet.getSpriteWidth() * scale, sheet.getSpriteHeight() * scale);

		// boolean mirrorX = (mirrorDir == MirrorDirection.BOTH
		// || mirrorDir == MirrorDirection.X);
		// boolean mirrorY = (mirrorDir == MirrorDirection.BOTH
		// || mirrorDir == MirrorDirection.Y);
		//
		// int scaleMap = scale - 1;
		//
		// int tileOffset = (tileCol * sheet.getSpriteWidth())
		// + (tileRow * sheet.getSpriteHeight()) * sheet.getPixelWidth();
		//
		// for (int y = 0; y < sheet.getSpriteHeight(); y++) {
		// int ySheet = y;
		// if (mirrorY)
		// ySheet = (sheet.getSpriteHeight() - 1) - y;
		//
		// int yPixel = y + yPos + (y * scaleMap)
		// - ((scaleMap * sheet.getSpriteHeight()) / 2);
		//
		// for (int x = 0; x < sheet.getSpriteWidth(); x++) {
		// int xSheet = x;
		// if (mirrorX)
		// xSheet = (sheet.getSpriteWidth() - 1) - x;
		// int xPixel = x + xPos + (x * scaleMap)
		// - ((scaleMap * sheet.getSpriteWidth()) / 2);
		//
		// // int col = (colour >> (sheet.getPixels()[xSheet
		// // + ySheet * sheet.getPixelWidth() + tileOffset] * 8)) & 255;
		//
		// int col = sheet.getPixels()[xSheet + ySheet * sheet.getPixelWidth()
		// + tileOffset];
		//
		// for (int yScale = 0; yScale < scale; yScale++) {
		// if (yPixel + yScale < 0 || yPixel + yScale >= height)
		// continue;
		// for (int xScale = 0; xScale < scale; xScale++) {
		// if (xPixel + xScale < 0 || xPixel + xScale >= width)
		// continue;
		// nextFramePixels[(xPixel + xScale)
		// + (yPixel + yScale) * width] = col;
		// }
		// }
		// }
		// }
	}

	public void render(int xPos, int yPos, SpriteSheet sheet, int tileId,
			int colour, MirrorDirection mirrorDir, int scale) {
		render(xPos, yPos, sheet, tileId / sheet.getNumSpritesWidth(),
				tileId % sheet.getNumSpritesWidth(), colour, mirrorDir, scale);
	}

	/**
	 * Sets the screen position in the global (x, y) coordinate system.
	 * 
	 * @param xOffset
	 * @param yOffset
	 */
	public void setScreenPosition(int xOffset, int yOffset) {
		this.x = xOffset;
		this.y = yOffset;
	}

	/***
	 * Copy pixels from nextFrame array to live image array. Do this before
	 * displaying.
	 */
	public void updateScreenPixels() {
		// for (int y = 0; y < height; y++) {
		// for (int x = 0; x < width; x++) {
		// int colourCode = nextFramePixels[x + y * width];
		// // if (colourCode < 255)
		// // pixels[x + y * WIDTH] = colours[colourCode];
		// screenPixels[x + y * width] = colourCode;
		// }
		// }
	}

	public void setGraphicsContext(Graphics g) {
		this.graphicsContext = g;
	}

	/**
	 * Return world y-coordinate of upper left of screen.
	 * 
	 * @return
	 */
	public int getY() {
		return this.y;
	}

	public int getTopY() {
		return getY();
	}

	public int getBottomY() {
		return getY() + this.height;
	}

	/**
	 * Return world x-coordinate of upper left of screen.
	 * 
	 * @return
	 */
	public int getX() {
		return this.x;
	}

	public int getLeftX() {
		return getX();
	}

	public int getRightX() {
		return getX() + this.width;
	}

	public void displayPixelScale(int increment) {

		for (int dx = 0; dx < this.width; dx += increment) {
			int worldX = (x + dx);

			Font.render("" + worldX, this, worldX, y + 10,
					Colours.get(-1, -1, -1, 555), 1);
		}

		for (int dy = 0; dy < this.height; dy += increment) {
			int worldY = (y + dy);

			Font.render("" + worldY, this, x + 10, worldY,
					Colours.get(-1, -1, -1, 555), 1);
		}
	}

	public void displayMouseCoordinatesAtMouse() {
		this.graphicsContext.drawRect(mouseX, mouseY, 3, 3);
		Font.render("" + mouseX + ", " + mouseY, this,
				screenXCoordToWorldCoord(mouseX), screenYCoordToWorldCoord(mouseY - 10),
				Colours.get(-1, -1, -1, 555), 1);
	}

	public int worldXCoordToScreenCoord(int wx) {
		return wx - x;
	}

	public int screenXCoordToWorldCoord(int sx) {
		return (x + sx);
	}

	public int worldYCoordToScreenCoord(int wy) {
		return wy - y;
	}

	public int screenYCoordToWorldCoord(int sy) {
		return (y + sy);
	}

	public void highlightTileAtScreenCoordinates(int sx, int sy,
			int tileDisplaySize) {

		this.highlightTileAtWorldCoordinates(screenXCoordToWorldCoord(sx),
				screenYCoordToWorldCoord(sy), tileDisplaySize);
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseCoordinates(int x2, int y2) {
		mouseX = x2;
		mouseY = y2;
	}
}