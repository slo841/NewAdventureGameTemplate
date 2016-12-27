package ca.vanzeben.game.level.tiles;

public class BasicSolidTile extends BasicTile {

    public BasicSolidTile(int id, int x, int y, int levelColour) {
        super(id, x, y, levelColour);
        this.solid = true;
    }

}
