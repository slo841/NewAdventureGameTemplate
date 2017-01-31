package ca.vanzeben.game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ca.vanzeben.game.entities.Coin;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Game extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	public static final int SCREEN_WIDTH = 240 * 4;
	public static final int SCREEN_HEIGHT = SCREEN_WIDTH / 12 * 9;
	public static final int SCALE = 1;

	public static final String NAME = "Game";
	public static final Dimension DIMENSIONS = new Dimension(SCREEN_WIDTH * SCALE,
			SCREEN_HEIGHT * SCALE);

	private Thread thread;
	private WindowHandler windowHandler;
	private static Game game;
	private static Screen screen;
	private static Level level;
	private static InputHandler input;

	public JFrame frame;

	private boolean running = false;
	private int tickCount = 0;

	private Player player;
	private ArrayList<Coin> coinList;
	private Coin coin;

	private boolean debug = true;
	public boolean isApplet = false;

	public void init() {
		game = this;

		SpriteSheet.characterSheet.displayInfo();

		screen = new Screen(SCREEN_WIDTH, SCREEN_HEIGHT);
		input = new InputHandler(this);
		level = new Level("/levels/water_test_level.png");
		level.setTileAt(3, 3, Tile.STONE);
		level.setTileAt(10, 2, Tile.GRASS2);
		level.setTileAt(5, 1, Tile.WHALE);

		player = new Player(level, 100, 100, input,
				JOptionPane.showInputDialog(this, "Please enter a username"),
				SpriteSheet.characterSheet);
		coin = new Coin(100, 500, 5, level);
		
		level.addPlayer(player);
		level.addTower(10*16, 20*16);
		
		level.addCoin(5*16, 5*16, 3);
		level.addCoin(5*16, 6*16, 10);
		
		
		
		for (int i = 0; i < 10; i++) {
			int x = (int)(Math.random()*1000);
			int y = (int)(Math.random()*1000);
			level.addWumpus(x, y);
		}
	}

	public synchronized void start() {
		running = true;

		thread = new Thread(this, NAME + "_main");
		thread.start();
	}

	public synchronized void stop() {
		running = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D / 60D; // Desired 60 frames per second

		int ticks = 0;
		int frames = 0;

		long lastTimer = System.currentTimeMillis();
		double gameStepsToRun = 0;

		init();

		while (running) {
			long now = System.nanoTime();
			gameStepsToRun += (now - lastTime) / nsPerTick;
			
			lastTime = now;
			boolean shouldRender = true;

			while (gameStepsToRun >= 1) {
				ticks++;
				tick();
				gameStepsToRun -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			// if one second has passed, display frames per second (for informational purposes)
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				debug(DebugLevel.INFO, ticks + " ticks, " + frames + " frames");
				frames = 0;
				ticks = 0;
			}
		}
	}

	/***
	 * Updates the game state once per frame.
	 */
	public void tick() {
		tickCount++;
		level.tick();
	}

	/***
	 * Render the game
	 */
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		screen.reset(); // You must call this BEFORE you render
										// anything!

		// Set screen position centered on player
		int screenX = player.x() - (screen.getWidth() / 2);
		int screenY = player.y() - (screen.getHeight() / 2);

		// Limit the screen position
		screenX = Math.max(0, screenX); // if < 0 set to 0
		screenY = Math.max(0, screenY);
		screenX = Math.min((level.getLevelWidth() - screen.getWidth()), screenX);
		screenY = Math.min((level.getLevelHeight() - screen.getHeight()), screenY);

		screen.setScreenPosition(screenX, screenY);

		// *****************************************************************************************
		// Do all rendering with screen here, after setting it graphics context

		level.renderTiles(screen);
		level.renderEntities(screen);

		String msg = "Wizard Adventure";
		screen.renderTextAtScreenCoordinates(msg, Font.DEFAULT,
				screen.getWidth() - Font.DEFAULT.getWidthOf(msg) * 3, 10, 3);

		if (debug) {
			screen.highlightTileAtScreenCoordinates(screen.getMouseX(),
					screen.getMouseY(), level.getTileDisplaySize());
			screen.displayMouseCoordinatesAtMouse();
			screen.displayPixelScale(50);
		}

		// *****************************************************************************************
		// Dispose of current context and show the rendered buffer

		g.drawImage(screen.getImage(), 0, 0, null);

		g.dispose();
		bs.show();
	}

	public static Screen getScreen() {
		return screen;
	}

	public void debug(DebugLevel level, String msg) {
		switch (level) {
		default:
		case INFO:
			if (debug) {
				System.out.println("[" + NAME + "] " + msg);
			}
			break;
		case WARNING:
			System.out.println("[" + NAME + "] [WARNING] " + msg);
			break;
		case SEVERE:
			System.out.println("[" + NAME + "] [SEVERE]" + msg);
			this.stop();
			break;
		}
	}

	public static enum DebugLevel {
		INFO, WARNING, SEVERE;
	}

	public static Level getLevel() {
		return level;
	}

	public void setWindowHandler(WindowHandler wh) {
		this.windowHandler = wh;
	}
}
