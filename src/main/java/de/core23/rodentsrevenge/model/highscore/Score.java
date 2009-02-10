package de.core23.rodentsrevenge.model.highscore;

import java.io.Serializable;

public class Score implements Serializable {
	private static final long serialVersionUID = 1L;

	private String _name;

	private int _score;

	public Score(String name, int score) {
		this._name = name;
		this._score = score;
	}

	public int getScore() {
		return _score;
	}

	public String getName() {
		return _name;
	}
}
