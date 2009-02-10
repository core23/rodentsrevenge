package de.core23.rodentsrevenge.model.player;

import java.awt.Point;

public abstract class AbstractPlayer {
	protected Point _position = new Point(-1, -1);

	protected Point _movement = new Point();

	public AbstractPlayer() {
	}

	public AbstractPlayer(Point p) {
		_position = p;
	}

	public void move(int x, int y) {
		_position.x = x;
		_position.y = y;
	}

	public int getX() {
		return _position.x;
	}

	public int getY() {
		return _position.y;
	}

	public void setMoveDir(int x, int y) {
		_movement.x = x;
		_movement.y = y;
	}

	public int getMoveX() {
		return _movement.x;
	}

	public int getMoveY() {
		return _movement.y;
	}
}