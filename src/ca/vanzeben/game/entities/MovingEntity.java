package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public abstract class MovingEntity extends Entity {
	protected int xSpeed, ySpeed, speed, drag;
	
	public MovingEntity(int x, int y, int speed, SpriteSheet sheet, Level level) {
		super(x, y, sheet, level);
		
		this.speed = speed;
		xSpeed = 0;
		ySpeed = 0;
	}
	
	public abstract void move();
	
	public void setRandomDirection() {
		double angle = Math.random()*2*Math.PI;
		double xcomp = Math.cos(angle);
		double ycomp = Math.sin(angle);
		xSpeed = (int)(xcomp*speed);
		ySpeed = (int)(ycomp*speed);
	}
	
	public void moveRandom() {
		if (tickCount/60 != ((tickCount + 1)/60)) {
			setRandomDirection();
		}
		
		updateCoordinates();
	}

	/***
	 * Move in the direction of target x and target y
	 * @param tx target x coordinate
	 * @param ty target y coordinate
	 */
	public void moveTowardWorldCoordinates(int tx, int ty) {
		double angle = getAngleToward(tx, ty);
		xSpeed = (int) (speed*Math.cos(angle));
		ySpeed = (int) (speed*Math.sin(angle));
		
		updateCoordinates();
	}
	
	private double getAngleToward(int tx, int ty) {
		double dx = tx - x;
		double dy = ty - y;
		return Math.atan2(dy, dx);
	}

	/***
	 * Move toward another entity
	 * @param entity
	 */
	
	public void moveTowardEntity(Entity entity) {
		moveTowardWorldCoordinates(entity.centerX(), entity.centerY());
	}
	
	public void moveInDirection(double angle) {
		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
	}
	
	public double angleTowardEntity(Entity other) {
		return getAngleToward(other.getX(), other.getY());
	}
	
	public void setDrag() {
		this.drag = (int)(Math.random()*15);
	}
	
	public int getDrag() {
		return this.drag;
	}
	
	private void updateCoordinates() {
		if (willCollideWithTile(x, y)) {
			return;
		}
		
		x += xSpeed;
		y += ySpeed;
	}

	protected boolean willCollideWithTile(int dx, int dy) {
		Tile rightTile = level.getTileTypeAtWorldCoordinates(centerX() + 4 + dx, centerY() + dy);
		Tile leftTile = level.getTileTypeAtWorldCoordinates(centerX() - 4 + dx, centerY() + dy);
		Tile topTile = level.getTileTypeAtWorldCoordinates(centerX() + dx, centerY() + 10 + dy);
		Tile bottomTile = level.getTileTypeAtWorldCoordinates(centerX() + dx, centerY() - 10 + dy);

		if (rightTile.isSolid() || leftTile.isSolid() 
				|| topTile.isSolid() || bottomTile.isSolid()) {
			return true;
			
		}
		return false;
	}
}
