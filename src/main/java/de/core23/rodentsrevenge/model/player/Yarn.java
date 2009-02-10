package de.core23.rodentsrevenge.model.player;

import java.awt.Point;

import de.core23.rodentsrevenge.model.GameConst;


public class Yarn extends AbstractPlayer implements GameConst {
	private int _idle = YARN_IDLE_TIME;

	private int _life = YARN_MOVE_TIME;

	public Yarn(Point pos) {
		super(pos);
	}

	public void age() {
		if (_idle > 0)
			_idle--;
		else
			_life--;
	}

	public void idle() {
		_idle = YARN_IDLE_TIME;
	}

	public boolean isDead() {
		return _life < 0;
	}

	public boolean isIdle() {
		return _idle > 0;
	}
}
