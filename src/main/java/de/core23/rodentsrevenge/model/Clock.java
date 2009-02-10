package de.core23.rodentsrevenge.model;

public class Clock implements GameConst {
	private final int spawn = SPAWN_CAT_SPEED * TIME_INTERVAL / 1000;

	private int seconds = 0;

	private int spawnTime = spawn;

	public void reset() {
		seconds = 0;
		spawnTime = spawn;
	}

	public void tick() {
		seconds++;
	}

	public void spawn() {
		spawnTime += spawn;
	}

	public int getSeconds() {
		return seconds;
	}

	public int getSpawnTime() {
		return spawnTime;
	}
}