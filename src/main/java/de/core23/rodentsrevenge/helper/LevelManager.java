package de.core23.rodentsrevenge.helper;

import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

import de.core23.rodentsrevenge.model.BoxType;
import de.core23.rodentsrevenge.model.StyleConst;

public class LevelManager implements StyleConst {
	private static final long serialVersionUID = 1L;

	private static final int LEVEL_LARGE = 1;

	private static final int LEVEL_MEDIUM = 2;

	private static final int LEVEL_SMALL = 3;

	private static final String FILENAME = "levels.txt"; //$NON-NLS-1$

	private static final Random RANDOM = new Random();

	private int _currentNum = 0;

	private int _minCat = 1;

	private int _maxCat = 5;

	private boolean _yarn = false;

	private BoxType[][] _map = new BoxType[23][23];

	public int next() {
		return _currentNum + 1;
	}

	public boolean setNumber(int num) {
		this._currentNum = num;

		// Wall
		for (int x = 0; x < 23; x++) {
			for (int y = 0; y < 23; y++) {
				if (x == 0 || x == 22 || y == 0 || y == 22)
					_map[x][y] = BoxType.WALL;
				else
					_map[x][y] = BoxType.FLOOR;
			}
		}

		try {
			return loadFromFile(num);
		} catch (Exception e) {
			JOptionPane
				.showMessageDialog(
					null,
					String.format(LanguageManager.getString("level.exception"), num), LanguageManager.getString("level.exception.title"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return false;
		}
	}

	public int getMax() {
		try {
			Scanner in = new Scanner(getClass().getClassLoader().getResourceAsStream(FILENAME));
			in.reset();

			int count = 0;
			while (in.hasNextLine()) {
				count++;
				in.nextLine();
			}
			in.close();

			return count;
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean isLast() {
		return _currentNum + 1 == getMax();
	}

	public BoxType[][] getMap() {
		return _map;
	}

	public int getMaxCat() {
		return _maxCat;
	}

	public int getMinCat() {
		return _minCat;
	}

	public boolean hasYarn() {
		return _yarn;
	}

	public int getNumber() {
		return _currentNum;
	}

	private boolean loadFromFile(int level) throws Exception {
		Scanner in = new Scanner(getClass().getClassLoader().getResourceAsStream(FILENAME));

		String line = null;
		for (int i = 0; i < level; i++)
			line = in.nextLine();
		in.close();

		if (line == null)
			return false;

		String[] parts = line.toUpperCase().split(";"); //$NON-NLS-1$

		if (parts.length < 4)
			throw new Exception(LanguageManager.getString("level.config.exception")); //$NON-NLS-1$

		if (!parts[0].isEmpty()) {
			String levelType = parts[0].split(" ")[0]; //$NON-NLS-1$
			int levelSize = 0;

			if (parts[0].split(" ")[1].equals("LARGE")) //$NON-NLS-1$ //$NON-NLS-2$
				levelSize = LEVEL_LARGE;
			else if (parts[0].split(" ")[1].equals("MEDIUM")) //$NON-NLS-1$ //$NON-NLS-2$
				levelSize = LEVEL_MEDIUM;
			else if (parts[0].split(" ")[1].equals("SMALL")) //$NON-NLS-1$ //$NON-NLS-2$
				levelSize = LEVEL_SMALL;
			else
				throw new Exception(LanguageManager.getString("level.config.exception")); //$NON-NLS-1$

			// Level Size
			if (levelType.equals("SQUARE")) //$NON-NLS-1$
				createBoxSqaure(levelSize);
			else if (levelType.equals("BOXCHESS")) //$NON-NLS-1$
				createBoxChess(levelSize);
			else if (levelType.equals("WALLCHESS")) //$NON-NLS-1$
				createWallChess(levelSize);
			else
				throw new Exception(LanguageManager.getString("level.config.exception")); //$NON-NLS-1$
		}

		// Cats / Yarns
		_minCat = Integer.valueOf(parts[1]);
		_maxCat = Integer.valueOf(parts[2]);
		_yarn = Boolean.valueOf(parts[3]);

		if (parts.length > 4) {
			for (int i = 4; i < parts.length; i++) {
				String method = parts[i].split(" ")[0]; //$NON-NLS-1$
				int value = Integer.valueOf(parts[i].split(" ")[1]); //$NON-NLS-1$

				if (method.equals("RNDWALL")) //$NON-NLS-1$
					createWallRandom(value);
				else if (method.equals("RNDBOX")) //$NON-NLS-1$
					createBoxRandom(value);
				else if (method.equals("RNDHOLE")) //$NON-NLS-1$
					createHoleRandom(value);
				else if (method.equals("RNDTRAP")) //$NON-NLS-1$
					createTrapRandom(value);
				else
					throw new Exception(LanguageManager.getString("level.config.exception")); //$NON-NLS-1$
			}
		}
		return true;
	}

	private void createBoxSqaure(int size) {
		int d;
		if (size == LEVEL_LARGE)
			d = 0;
		else if (size == LEVEL_MEDIUM)
			d = 1;
		else if (size == LEVEL_SMALL)
			d = 3;
		else
			return;

		for (int x = 4 + d; x < 19 - d; x++) {
			for (int y = 4 + d; y < 19 - d; y++) {
				_map[x][y] = BoxType.BOX;
			}
		}
	}

	private void createWallChess(int size) {
		int d;
		if (size == LEVEL_LARGE)
			d = 0;
		else if (size == LEVEL_MEDIUM)
			d = 1;
		else if (size == LEVEL_SMALL)
			d = 3;
		else
			return;

		for (int x = 5 + d; x < 18 - d; x++) {
			for (int y = 5 + d; y < 18 - d; y++) {
				if ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1))
					_map[x][y] = BoxType.WALL;
			}
		}
	}

	private void createBoxChess(int size) {
		int d;
		if (size == LEVEL_LARGE)
			d = 0;
		else if (size == LEVEL_MEDIUM)
			d = 3;
		else if (size == LEVEL_SMALL)
			d = 5;
		else
			return;

		for (int x = 2 + d; x < 21 - d; x++) {
			for (int y = 2 + d; y < 21 - d; y++) {
				if ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1))
					_map[x][y] = BoxType.BOX;
			}
		}
	}

	private void createBoxRandom(int count) {
		int x, y;
		for (int i = 0; i < count; i++) {
			do {
				x = RANDOM.nextInt(19) + 2;
				y = RANDOM.nextInt(19) + 2;
			} while (_map[x][y] != BoxType.FLOOR && _map[x][y] != BoxType.BOX);
			_map[x][y] = BoxType.BOX;
		}
	}

	private void createWallRandom(int count) {
		int x, y;
		for (int i = 0; i < count; i++) {
			do {
				x = RANDOM.nextInt(19) + 2;
				y = RANDOM.nextInt(19) + 2;
			} while (_map[x][y] != BoxType.FLOOR && _map[x][y] != BoxType.BOX);
			_map[x][y] = BoxType.WALL;
		}
	}

	private void createHoleRandom(int count) {
		int x, y;
		for (int i = 0; i < count; i++) {
			do {
				x = RANDOM.nextInt(19) + 2;
				y = RANDOM.nextInt(19) + 2;
			} while (_map[x][y] == BoxType.HOLE);
			_map[x][y] = BoxType.HOLE;
		}
	}

	private void createTrapRandom(int count) {
		int x, y;
		for (int i = 0; i < count; i++) {
			do {
				x = RANDOM.nextInt(19) + 2;
				y = RANDOM.nextInt(19) + 2;
			} while (_map[x][y] == BoxType.TRAP);
			_map[x][y] = BoxType.TRAP;
		}
	}
}
