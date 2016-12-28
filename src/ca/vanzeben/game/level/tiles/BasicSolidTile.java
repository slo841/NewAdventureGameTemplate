package ca.vanzeben.game.level.tiles;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(int x, int y, int levelColour) {
        super(x, y, levelColour);
        this.solid = true;
    }

}
