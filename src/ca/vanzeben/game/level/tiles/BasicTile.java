package ca.vanzeben.game.level.tiles;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.level.Level;

public class BasicTile extends Tile {
    protected int tileId;
    protected int tileColour;
    
    public BasicTile(int id, int x, int y, int tileColour, int levelColour) {
        super(id, false, false, levelColour);
        this.tileId = x + y * 32;
        this.tileColour = tileColour;
    }

    public void tick() {
    }

    /***
     * World (x, y) coordinates.
     */
    public void render(Screen screen, Level level, int x, int y, int scale) {
        screen.render(x, y, tileSheet, tileId, tileColour, Screen.MirrorDirection.NONE, scale);
    }
}