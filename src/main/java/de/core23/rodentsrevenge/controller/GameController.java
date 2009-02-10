package de.core23.rodentsrevenge.controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;

import de.core23.rodentsrevenge.helper.Actions;
import de.core23.rodentsrevenge.helper.LevelManager;
import de.core23.rodentsrevenge.gui.AboutDialog;
import de.core23.rodentsrevenge.gui.GameFrame;
import de.core23.rodentsrevenge.helper.LanguageManager;
import de.core23.rodentsrevenge.model.BoxType;
import de.core23.rodentsrevenge.model.Clock;
import de.core23.rodentsrevenge.model.GameConst;
import de.core23.rodentsrevenge.model.StyleConst;
import de.core23.rodentsrevenge.model.player.Cat;
import de.core23.rodentsrevenge.model.player.Mouse;
import de.core23.rodentsrevenge.model.player.Yarn;

public class GameController implements ActionListener, GameConst, KeyListener {
	private static final Random RANDOM = new Random();

	private GameFrame _frame = null;

	private LevelManager _levelManager = null;

	private boolean _gameOver = true;

	private boolean _pause = false;

	private Timer _timer = null;

	private int _timerStep = 0;

	private BoxType[][] _map = new BoxType[23][23];

	private Mouse _mouse = new Mouse();

	private Clock _clock = new Clock();

	private LinkedList<Cat> _catList = new LinkedList<Cat>();

	private LinkedList<Yarn> _yarnList = new LinkedList<Yarn>();

	private LinkedList<JRadioButtonMenuItem> _styleList = new LinkedList<JRadioButtonMenuItem>();

	public GameController() {
		_frame = new GameFrame();
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Listener
		_frame.getJMenuItemNew().addActionListener(this);
		_frame.getJMenuItemLevel().addActionListener(this);
		_frame.getJMenuItemExit().addActionListener(this);
		_frame.getJMenuItemHighscore().addActionListener(this);
		_frame.getJMenuItemAbout().addActionListener(this);
		_frame.addKeyListener(this);

		_levelManager = new LevelManager();

		_timer = new Timer(TIME_INTERVAL, this);
		_timer.setActionCommand(Actions.TIMER);
		_timer.restart();

		createStyleList();

		_frame.setLocationRelativeTo(null);
		_frame.setVisible(true);

		newGame();
	}

	private void createStyleList() {
		ButtonGroup group = new ButtonGroup();

		for (int i = 0; i < StyleConst.STYLES.length; i++) {
			String text = StyleConst.STYLES[i][1];
			JRadioButtonMenuItem item = new JRadioButtonMenuItem();
			item.setText(text);
			item.setMnemonic(text.charAt(0));
			item.setActionCommand(Actions.SWITCH_STYLE);
			item.addActionListener(this);
			item.setSelected(i == 0);

			group.add(item);
			_styleList.add(item);
			_frame.getJMenuStyle().add(item);
		}
	}

	public void newGame() {
		newGame(1);
	}

	public void newGame(int level) {
		_gameOver = false;
		_mouse = new Mouse();

		// Load
		loadLevel(level);

		// Draw All
		drawLevel();
		drawScore();
		drawLifes();
	}

	private void loadLevel() {
		addScore(250);
		loadLevel(_levelManager.next());
	}

	private void loadLevel(int num) {
		// Last Level
		if (_levelManager.isLast()) {
			endGame(true);
			return;
		}

		_levelManager.setNumber(num);

		// Pause
		setTimer(false);
		setPause(true);

		// Map
		BoxType[][] map = _levelManager.getMap();
		for (int x = 0; x < 23; x++) {
			for (int y = 0; y < 23; y++) {
				setMapType(x, y, map[x][y]);
			}
		}

		// Mouse
		spawnMouse(false);

		// Enemies
		_catList.clear();
		_yarnList.clear();
		for (int i = 0; i < _levelManager.getMinCat(); i++)
			spawnCat();

		// Draw
		_frame.getGamePanel().setClockTick(_clock.getSeconds());
		_frame.getGamePanel().setClockSpawn(_clock.getSpawnTime());
		drawLevel();

		// Continue
		setTimer(true);
		setPause(false);

		_frame.getGamePanel().repaint();
	}

	public void endGame(boolean win) {
		_gameOver = true;

		setTimer(false);

		_frame.getGamePanel().repaint();

		setHighscore(_mouse.getScore());
	}

	public void setPause(boolean pause) {
		if (pause)
			_pause = pause;
		else
			_pause = _gameOver;

		_mouse.setNextMovement(0, 0);

		_frame.getGamePanel().setGray(_pause);
		_frame.getGamePanel().repaint();
	}

	private void setTimer(boolean status) {
		if (status) {
			_timer.restart();

			_clock.reset();
			_timerStep = 0;

		} else {
			_timer.stop();
		}
	}

	private Cat getCatAt(int x, int y) {
		Iterator<Cat> it = _catList.iterator();
		while (it.hasNext()) {
			Cat cat = it.next();
			if (cat.getX() == x && cat.getY() == y)
				return cat;
		}
		return null;
	}

	private void addScore(int score) {
		_mouse.addScore(score);
		drawScore();
	}

	private void killMouse() {
		if (getMapType(_mouse.getX(), _mouse.getY()) == BoxType.MOUSE || getMapType(_mouse.getX(), _mouse.getY()) == BoxType.MOUSE_HOLE)
			clearBox(_mouse.getX(), _mouse.getY());

		_mouse.kill();
		drawLifes();

		if (_mouse.getLifes() == 0) {
			endGame(false);
			return;
		}

		spawnMouse(true);
	}

	private void spawnMouse(boolean random) {
		_mouse.respawn();

		if (random) {
			Point pos = getFreeMouseSpawnField();
			_mouse.move(pos.x, pos.y);
		} else {
			_mouse.move(11, 11);
		}

		setMapType(_mouse.getX(), _mouse.getY(), BoxType.MOUSE);
	}

	private void spawnCat() {
		if (_catList.size() >= _levelManager.getMaxCat())
			return;

		Point pos = getFreeCatSpawnField();
		_catList.add(new Cat(pos));

		setMapType(pos.x, pos.y, BoxType.CAT);
	}

	private void spawnYarn() {
		if (_yarnList.size() >= YARN_MAX_COUNT || !_levelManager.hasYarn())
			return;

		Point pos = getFreeWall();
		_yarnList.add(new Yarn(pos));

		setMapType(pos.x, pos.y, BoxType.YARN);
	}

	private void setYarnDirection(Yarn yarn) {

		// Idle
		yarn.idle();

		if (!isYarnField(yarn.getX() + 1, yarn.getY() + 1) && !isYarnField(yarn.getX() - 1, yarn.getY() - 1) && !isYarnField(yarn.getX() - 1, yarn.getY() + 1)
			&& !isYarnField(yarn.getX() + 1, yarn.getY() - 1)) {
			yarn.setMoveDir(0, 0);
			return;
		}

		// New Direction
		final int[][] movements = { {-1, -1}, {1, -1}, {-1, 1}, {1, 1}};
		int pos = 0;
		do {
			pos = RANDOM.nextInt(4);
		} while (!isYarnField(yarn.getX() + movements[pos][0], yarn.getY() + movements[pos][1]));

		yarn.setMoveDir(movements[pos][0], movements[pos][1]);
	}

	private void moveMouse() {
		setMapType(_mouse.getX(), _mouse.getY(), _mouse.isTrapped() ? BoxType.MOUSE_HOLE : BoxType.MOUSE);

		if (_mouse.isTrapped()) {
			_mouse.idle();
			return;
		}

		int newX = _mouse.getX() + _mouse.getMoveX();
		int newY = _mouse.getY() + _mouse.getMoveY();

		// Out Of Map
		if (newX == 0 || newX == 22 || newY == 0 || newY == 22) {

			// No Movement
		} else if (_mouse.getMoveX() == 0 && _mouse.getMoveY() == 0) {

			// Move
		} else {
			switch (getMapType(newX, newY)) {
				// Wall
				case WALL:
					break;

				// Box
				case BOX:
					pushBox(_mouse.getMoveX(), _mouse.getMoveY());
					break;

				// Hole
				case HOLE:
					_mouse.freeze();
					clearBox(newX, newY);
					switchBox(_mouse.getX(), _mouse.getY(), newX, newY);
					_mouse.move(newX, newY);
					break;

				// Trap
				case TRAP:
					clearBox(newX, newY);
					killMouse();
					break;

				// Yarn / Cat
				case YARN:
				case CAT:
					killMouse();
					break;

				// Cheese
				case CHEESE:
					addScore(100);
					clearBox(newX, newY);
					switchBox(_mouse.getX(), _mouse.getY(), newX, newY);
					_mouse.move(newX, newY);
					break;

				// Floor
				default:
					switchBox(_mouse.getX(), _mouse.getY(), newX, newY);
					_mouse.move(newX, newY);
					break;
			}
		}

		// Next Movement
		_mouse.nextMovement();
	};

	private void moveCats() {
		int count = 0;
		boolean trappedCats = true;

		// Try Move
		for (Iterator<Cat> it = _catList.iterator(); it.hasNext();) {
			if (moveCat(it.next()))
				trappedCats = false;
			count++;
		}

		// Cats trapped
		if (trappedCats) {
			// Transform Cheese
			for (Iterator<Cat> it = _catList.iterator(); it.hasNext();) {
				Cat cat = it.next();
				initBox(cat.getX(), cat.getY(), BoxType.CHEESE);
			}
			_catList.clear();

			// Next Level
			if (count > _levelManager.getMaxCat() - 1) {
				loadLevel();
				return;
			}

			// Spawn Cats
			for (int i = 0; i < count + 1; i++)
				spawnCat();
		}
	}

	private boolean moveCat(Cat cat) {
		// Direction
		int moveX = 0;
		if (cat.getX() > _mouse.getX())
			moveX--;
		else if (cat.getX() < _mouse.getX())
			moveX++;

		int moveY = 0;
		if (cat.getY() > _mouse.getY())
			moveY--;
		else if (cat.getY() < _mouse.getY())
			moveY++;

		// Calculate Path
		double diag = Math.sqrt(Math.pow(cat.getX() - _mouse.getX(), 2) + Math.pow(cat.getY() - _mouse.getY(), 2));
		double linex = Math.abs(cat.getX() - _mouse.getX());
		double liney = Math.abs(cat.getY() - _mouse.getY());

		// Pathfinder
		int newX = cat.getX();
		int newY = cat.getY();
		do {
			// Shortest Diagonal
			if (diag < (linex + liney) && isCatField(newX + moveX, newY + moveY)) {
				newX += moveX;
				newY += moveY;

				// Shortest Lines
			} else if (linex > liney && isCatField(newX + moveX, newY)) {
				newX += moveX;
			} else if (isCatField(newX, newY + moveY)) {
				newY += moveY;
			} else if (isCatField(newX + moveX, newY)) {
				newX += moveX;

				// Middle Diagonal
			} else if (linex > liney && isCatField(newX + moveX, newY - moveY)) {
				newX += moveX;
				newY += -moveY;
			} else if (isCatField(newX - moveX, newY + moveY)) {
				newX += -moveX;
				newY += moveY;
			} else if (isCatField(newX + moveX, newY - moveY)) {
				newX += moveX;
				newY += -moveY;

				// Longest Line
			} else if (linex > liney && isCatField(newX - moveX, newY)) {
				newX += -moveX;
			} else if (isCatField(newX, newY - moveY)) {
				newY += -moveY;
			} else if (isCatField(newX - moveX, newY)) {
				newX += -moveX;

				// Contrary Path
			} else if (isCatField(newX - moveX, newY - moveY)) {
				newX += -moveX;
				newY += -moveY;
			}

			// Move Cat
			if (newX != cat.getX() || newY != cat.getY()) {
				break;

				// Dodge Mouse
			} else if (moveX == 0) {
				moveX = 1;
			} else if (moveY == 0) {
				moveY = 1;

				// Trapped Cat
			} else {
				return false;
			}

		} while (true);

		// Kill Mouse
		if (newX == _mouse.getX() && newY == _mouse.getY())
			killMouse();

		// Move Cat
		switchBox(cat.getX(), cat.getY(), newX, newY);
		cat.move(newX, newY);

		return true;
	}

	private void moveYarns() {
		for (Iterator<Yarn> it = _yarnList.iterator(); it.hasNext();) {
			if (!moveYarn(it.next()))
				it.remove();
		}
	}

	private boolean moveYarn(Yarn yarn) {
		int newX = yarn.getMoveX() + yarn.getX();
		int newY = yarn.getMoveY() + yarn.getY();

		// Age
		yarn.age();

		// Freezes
		if (yarn.isIdle()) {
			return true;

			// End of Lifetime or Hole
		} else if (yarn.isDead() || getMapType(newX, newY) == BoxType.HOLE) {
			clearBox(yarn.getX(), yarn.getY());
			return false;

			// Can Move
		} else if (!(yarn.getMoveX() == 0 && yarn.getMoveY() == 0) && isYarnField(newX, newY)) {
			switchBox(yarn.getX(), yarn.getY(), newX, newY);
			clearBox(newX - yarn.getMoveX(), newY - yarn.getMoveY());
			yarn.move(newX, newY);

			// New Direction
		} else {
			setYarnDirection(yarn);
		}

		// Kill Mouse
		if (yarn.getX() == _mouse.getX() && yarn.getY() == _mouse.getY())
			killMouse();

		return true;
	}

	private Point getFreeWall() {
		Point p = new Point();
		do {
			if (RANDOM.nextBoolean()) {
				p.x = RANDOM.nextBoolean() ? 0 : 22;
				p.y = RANDOM.nextInt(21) + 1;
				// Horizontal
			} else {
				p.x = RANDOM.nextInt(21) + 1;
				p.y = RANDOM.nextBoolean() ? 0 : 22;
			}
		} while (getMapType(p.x, p.y) != BoxType.WALL);
		return p;
	}

	private Point getFreeCatSpawnField() {
		Point pos = new Point();

		double diag;
		do {
			pos.x = RANDOM.nextInt(23);
			pos.y = RANDOM.nextInt(23);
			diag = Math.sqrt(Math.pow(pos.x - _mouse.getX(), 2) + Math.pow(pos.y - _mouse.getY(), 2));
		} while (getMapType(pos.x, pos.y) != BoxType.FLOOR || diag < 3);

		return pos;
	}

	private Point getFreeMouseSpawnField() {
		Point pos = new Point();

		boolean canSpawn = false;
		do {
			pos.x = RANDOM.nextInt(23);
			pos.y = RANDOM.nextInt(23);

			canSpawn = true;
			for (Iterator<Cat> it = _catList.iterator(); it.hasNext();) {
				Cat cat = it.next();
				double diag = Math.sqrt(Math.pow(pos.x - cat.getX(), 2) + Math.pow(pos.y - cat.getY(), 2));
				if (diag < 3)
					canSpawn = false;
			}
		} while (getMapType(pos.x, pos.y) != BoxType.FLOOR || !canSpawn);

		return pos;
	}

	private BoxType getMapType(int x, int y) {
		if (x < 0 || y < 0 || x > 22 || y > 22)
			return BoxType.WALL;
		return _map[x][y];
	}

	private boolean isCatField(int x, int y) {
		if (x < 0 || y < 0 || x > 22 || y > 22)
			return false;
		return _map[x][y] == BoxType.FLOOR || _map[x][y] == BoxType.MOUSE || _map[x][y] == BoxType.MOUSE_HOLE;
	}

	private boolean isYarnField(int x, int y) {
		if (x < 0 || y < 0 || x > 22 || y > 22)
			return false;
		return _map[x][y] == BoxType.FLOOR || _map[x][y] == BoxType.MOUSE || _map[x][y] == BoxType.MOUSE_HOLE || _map[x][y] == BoxType.HOLE;
	}

	private void switchBox(int fromX, int fromY, int toX, int toY) {
		if (fromX == toX && fromY == toY)
			return;

		// Box
		BoxType tempBox = _map[fromX][fromY];
		setMapType(fromX, fromY, _map[toX][toY]);
		setMapType(toX, toY, tempBox);
	}

	private void pushBox(int pushX, int pushY) {
		int i = 0;

		do
			i++;
		while (getMapType(_mouse.getX() + i * pushX, _mouse.getY() + i * pushY) == BoxType.BOX);

		// Push Cat
		Cat cat = getCatAt(_mouse.getX() + i * pushX, _mouse.getY() + i * pushY);
		if (cat != null) {
			if (!moveCat(cat))
				return;
		}

		// Last
		switch (getMapType(_mouse.getX() + i * pushX, _mouse.getY() + i * pushY)) {
			case WALL:
			case HOLE:
			case TRAP:
			case YARN:
				return;
			case CHEESE:
				clearBox(_mouse.getX() + i * pushX, _mouse.getY() + i * pushY);
				break;
		}

		// Push
		for (; i > 1; i--)
			switchBox((_mouse.getX() + pushX * (i - 1)), (_mouse.getY() + pushY * (i - 1)), _mouse.getX() + pushX * i, _mouse.getY() + pushY * i);

		switchBox(_mouse.getX(), _mouse.getY(), _mouse.getX() + pushX, _mouse.getY() + pushY);

		_mouse.move(_mouse.getX() + pushX, _mouse.getY() + pushY);
	}

	private void initBox(int x, int y, BoxType type) {
		if (x < 0 || y < 0 || x >= 23 || y >= 23)
			return;

		switch (type) {
			case BOX:
			case WALL:
			case HOLE:
			case TRAP:
			case CHEESE:
				setMapType(x, y, type);
				break;

			case FLOOR:
				setMapType(x, y, BoxType.FLOOR);
				break;

			default:
				break;
		}
	}

	private void clearBox(int x, int y) {
		if (x == 0 || y == 0 || x == 22 || y == 22)
			initBox(x, y, BoxType.WALL);
		else
			initBox(x, y, BoxType.FLOOR);
	}

	private void setMapType(int x, int y, BoxType type) {
		if (x < 0 || y < 0 || x > 22 || y > 22)
			return;
		_map[x][y] = type;

		_frame.getGamePanel().setMapType(x, y, type);
	}

	private void drawScore() {
		_frame.getGamePanel().setScore(_mouse.getScore());
	}

	private void drawLevel() {
		_frame.getGamePanel().setLevel(_levelManager.getNumber());
	}

	private void drawLifes() {
		_frame.getGamePanel().setLifes(_mouse.getLifes());
	}

	public void setStyle(int styleID) {
		// Pause
		boolean isPause = _pause;
		_pause = true;

		_frame.getGamePanel().setStyleID(styleID);

		// Continue
		setPause(isPause);
	}

	public void showInfo() {
		// Pause
		boolean isPause = _pause;
		setPause(true);

		AboutDialog dialog = new AboutDialog();
		dialog.setModal(true);
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(_frame);
		dialog.setVisible(true);

		// Continue
		setPause(isPause);
	}

	public void showHighscore() {
		// Pause
		boolean isPause = _pause;
		setPause(true);

		HighscoreController frame = new HighscoreController(_frame);
		frame.show();

		// Continue
		setPause(isPause);
	}

	public void setHighscore(int score) {
		// Pause
		boolean isPause = _pause;
		setPause(true);

		HighscoreController frame = new HighscoreController(_frame);
		frame.newHighscore(score);
		frame.show();

		// Continue
		setPause(isPause);
	}

	public void showLevelSelection() {
		// Pause
		boolean isPause = _pause;
		setPause(true);

		int maxLevel = _levelManager.getMax();

		// Input
		String input = null;
		do {
			input = JOptionPane.showInputDialog(null,
				String.format(LanguageManager.getString("game.level.dialog"), maxLevel), LanguageManager.getString("game.level.dialog.title"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				JOptionPane.QUESTION_MESSAGE);

			// Check Input
			try {
				int level = Integer.valueOf(input);
				if (level > 0 && level <= maxLevel) {
					newGame(Integer.valueOf(level));
					return;
				}
			} catch (Exception ex) {
			}
		} while (input != null);

		// Continue
		setPause(isPause);
	}

	private void fireTimer() {
		if (_pause)
			return;

		_timerStep++;

		// Movement
		if (_timerStep % MOUSE_SPEED == 0)
			moveMouse();
		if (_timerStep % CAT_SPEED == 0)
			moveCats();
		if (_timerStep % YARN_SPEED == 0)
			moveYarns();

		// Spawn
		if (_timerStep % SPAWN_CAT_SPEED == 0) {
			_clock.spawn();
			_frame.getGamePanel().setClockSpawn(_clock.getSpawnTime());
			spawnCat();
		}
		if (_timerStep % SPAWN_YARN_SPEED == 0) {
			if (RANDOM.nextInt(16) == 0)
				spawnYarn();
		}

		// Clock
		if (_timerStep % CLOCK_SPEED == 0) {
			_clock.tick();
			_frame.getGamePanel().setClockTick(_clock.getSeconds());
		}

		// Score
		if (_timerStep % ADD_SCORE_SPEED == 0)
			addScore(1);

		_frame.getGamePanel().repaint();
	}

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();

		if (cmd.equals(Actions.TIMER)) {
			fireTimer();
		} else if (cmd.equals(Actions.NEW_GAME)) {
			newGame();

		} else if (cmd.equals(Actions.SWITCH_STYLE)) {
			for (int i = 0; i < StyleConst.STYLES.length; i++) {
				JRadioButtonMenuItem item = _styleList.get(i);
				if (ae.getSource().equals(item)) {
					setStyle(i);
					break;
				}
			}
		} else if (cmd.equals(Actions.HIGHSCORE)) {
			showHighscore();

		} else if (cmd.equals(Actions.SHOW_INFO)) {
			showInfo();

		} else if (cmd.equals(Actions.CHOOSE_LEVEL)) {
			showLevelSelection();

		} else if (cmd.equals(Actions.EXIT)) {
			System.exit(0);
		}
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
			// Up
			case 103:
				_mouse.setNextMovement(-1, -1);
				break;

			case 104:
			case 38:
				_mouse.setNextMovement(0, -1);
				break;

			case 105:
				_mouse.setNextMovement(1, -1);
				break;

			// Left
			case 100:
			case 37:
				_mouse.setNextMovement(-1, 0);
				break;

			// Right
			case 102:
			case 39:
				_mouse.setNextMovement(1, 0);
				break;

			// Down
			case 97:
				_mouse.setNextMovement(-1, 1);
				break;

			case 98:
			case 40:
				_mouse.setNextMovement(0, 1);
				break;

			case 99:
				_mouse.setNextMovement(1, 1);
				break;

			// Pause
			case 19:
			case 80:
				setPause(!_pause);
				break;

			// Help
			case 112:
				showInfo();
		}
	}

	public void keyReleased(KeyEvent arg0) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
}
