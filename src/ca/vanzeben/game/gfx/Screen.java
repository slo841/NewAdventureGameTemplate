package ca.vanzeben.game.gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.gfx.Screen.MirrorDirection;
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
	private boolean debug = false;

	public static enum MirrorDirection {
		Y, X, BOTH, NONE
	}

	private int x = 0; // Number of pixels to offset screen by (within the
											// level image)
	private int y = 0;

	private int width;
	private int height;

	private int mouseX, mouseY;

	private Graphics graphicsContext = null; // set by setGraphicsContext();

	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
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
			int tileCol, MirrorDirection mirrorDir, int displayWidth,
			int displayHeight) {
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
		int destx2 = destx1 + displayWidth;
		int desty2 = desty1 + displayHeight;

		this.graphicsContext.drawImage(sheet.getImage(), destx1, desty1, destx2,
				desty2, sourcex1, sourcey1, sourcex2, sourcey2, null);

		// ****** DEBUG ******
		if (debug) {
			this.graphicsContext.drawRect(destx1, desty1, displayWidth,
					displayHeight);
		}

		// TODO: mirroring?
	}

	/***
	 * Render a tile to the screen at world coordinates xPos, yPos. This version
	 * does not re-scale the tile image. Call the overloaded render() to specify
	 * the final display size.
	 * 
	 * @param xPos
	 * @param yPos
	 * @param sheet
	 * @param tileRow
	 * @param tileCol
	 * @param mirrorDir
	 */
	public void render(int xPos, int yPos, SpriteSheet sheet, int tileRow,
			int tileCol, MirrorDirection mirrorDir) {

		render(xPos, yPos, sheet, tileRow, tileCol, mirrorDir,
				sheet.getSpriteWidth(), sheet.getSpriteHeight());
	}

	public void render(int xPos, int yPos, SpriteSheet sheet, int tileId,
			MirrorDirection mirrorDir, int displayWidth, int displayHeight) {
		render(xPos, yPos, sheet, tileId / sheet.getNumSpritesWidth(),
				tileId % sheet.getNumSpritesWidth(), mirrorDir, displayWidth,
				displayHeight);
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

			Font.render("" + worldX, this, worldX, y + 10, 1);
		}

		for (int dy = 0; dy < this.height; dy += increment) {
			int worldY = (y + dy);

			Font.render("" + worldY, this, x + 10, worldY, 1);
		}
	}

	public void displayMouseCoordinatesAtMouse() {
		this.graphicsContext.drawRect(mouseX, mouseY, 3, 3);
		Font.render("" + mouseX + ", " + mouseY, this,
				screenXCoordToWorldCoord(mouseX), screenYCoordToWorldCoord(mouseY - 10),
				1);
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}