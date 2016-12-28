package ca.vanzeben.game.level.tiles;

public class AnimatedTile extends BasicTile {

	private int[][] animationTileCoords;
	private int currentAnimationIndex;
	private long lastIterationTime;
	private int animationSwitchDelay;

	public AnimatedTile(int[][] animationCoords, int levelColour,
			int animationSwitchDelay) {
		super(animationCoords[0][0], animationCoords[0][1], levelColour);
		this.animationTileCoords = animationCoords;
		this.currentAnimationIndex = 0;
		this.lastIterationTime = System.currentTimeMillis();
		this.animationSwitchDelay = animationSwitchDelay;
	}

	public void tick() {
		if ((System.currentTimeMillis()
				- lastIterationTime) >= (animationSwitchDelay)) {
			lastIterationTime = System.currentTimeMillis();
			currentAnimationIndex = (currentAnimationIndex + 1)
					% animationTileCoords.length;
		}
	}
}