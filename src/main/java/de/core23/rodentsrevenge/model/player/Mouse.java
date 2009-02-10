package de.core23.rodentsrevenge.model.player;

import java.awt.Point;

import de.core23.rodentsrevenge.model.GameConst;


public class Mouse extends AbstractPlayer implements GameConst {
	protected Point _nextMovement = new Point(0, 0);

	protected int _trap = 0;

	protected int _lifes = MOUSE_LIFES;

	protected int _score = 0;

	public Mouse() {
		move(11, 11);

		respawn();
	}

	public void respawn() {
		_movement = new Point(0, 0);
		_nextMovement = new Point(0, 0);

		unfreeze();
	}

	public void kill() {
		this._lifes--;

		unfreeze();
	}

	public int getScore() {
		return _score;
	}

	public void addScore(int score) {
		this._score += score;
	}

	public void setNextMovement(int nextX, int nextY) {
		this._nextMovement = new Point(nextX, nextY);
	}

	public boolean isTrapped() {
		return _trap > 0;
	}

	public void freeze() {
		_trap = TRAP_TIME;
	}

	private void unfreeze() {
		_trap = 0;
	}

	public void idle() {
		if (_trap > 0)
			_trap--;
	}

	public int getLifes() {
		return _lifes;
	}

	public void nextMovement() {
		this._movement = this._nextMovement;
		this._nextMovement = new Point(0, 0);
	}

	@Deprecated
	public void setMoveDir(int x, int y) {
	}
}
