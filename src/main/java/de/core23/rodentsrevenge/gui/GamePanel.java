package de.core23.rodentsrevenge.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.JPanel;

import de.core23.rodentsrevenge.helper.ImageManager;
import de.core23.rodentsrevenge.helper.LanguageManager;
import de.core23.rodentsrevenge.model.BoxType;
import de.core23.rodentsrevenge.model.GameConst;
import de.core23.rodentsrevenge.model.StyleConst;

public class GamePanel extends JPanel implements StyleConst {
	private static final long serialVersionUID = 1L;

	private final int MAP_SIZE = BOX_PADDING + (BOX_SIZE + BOX_PADDING) * 23;

	private BufferedImage _bufferImage;

	private Graphics _bufferGraphics;

	private FontMetrics _fontMetrics;

	private ColorConvertOp _colorConvert;

	private boolean _gray = false;

	private BoxType[][] _map = new BoxType[23][23];

	private int _styleID = 0;

	private int _score = 0;

	private int _level = 0;

	private int _lifes = 0;

	private int _clockTick = 0;

	private int _clockSpawn = 0;

	public GamePanel() {
		super();
		setSize(MAP_SIZE, MAP_SIZE + PANEL_HEIGHT);

		initBuffer();

		for (int x = 0; x < 23; x++) {
			for (int y = 0; y < 23; y++) {
				setMapType(x, y, BoxType.FLOOR);
			}
		}

		drawAll();
	}

	private void initBuffer() {
		_bufferImage = new BufferedImage(MAP_SIZE, MAP_SIZE + PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
		_bufferGraphics = _bufferImage.getGraphics();
		Graphics2D g = _bufferImage.createGraphics();

		g.setColor(stylePanel[_styleID]);
		g.fillRect(0, 0, MAP_SIZE, MAP_SIZE + PANEL_HEIGHT);

		g.setColor(styleBackground[_styleID]);
		g.fillRect(0, PANEL_HEIGHT, MAP_SIZE, MAP_SIZE + PANEL_HEIGHT);

		_bufferGraphics.setFont(PANEL_FONT);
		_fontMetrics = _bufferGraphics.getFontMetrics();
		_colorConvert = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
	}

	public void setGray(boolean gray) {
		_gray = gray;
	}

	public void setStyleID(int styleID) {
		if (styleID < 0)
			styleID = STYLES.length - 1;
		if (styleID >= STYLES.length)
			styleID = 0;
		_styleID = styleID;

		ImageManager.setStyleID(styleID);

		initBuffer();

		drawAll();
	}

	public void setScore(int score) {
		_score = score;
		drawScore(score);
	}

	public void setLevel(int level) {
		_level = level;
		drawLevel(level);
	}

	public void setLifes(int lifes) {
		_lifes = lifes;
		drawLife(lifes);
	}

	public void setClockTick(int tick) {
		_clockTick = tick;
		drawClock(_clockTick, _clockSpawn);
	}

	public void setClockSpawn(int spawn) {
		_clockSpawn = spawn;
		drawClock(_clockTick, _clockSpawn);
	}

	public void setMapType(int x, int y, BoxType type) {
		_map[x][y] = type;
		drawBlock(x, y, type);
	}

	private void drawAll() {
		for (int x = 0; x < 23; x++) {
			for (int y = 0; y < 23; y++) {
				drawBlock(x, y, _map[x][y]);
			}
		}

		drawLevel(_level);
		drawLife(_lifes);
		drawScore(_score);
		drawClock(_clockTick, _clockSpawn);

		repaint();
	}

	private void drawBlock(int x, int y, BoxType type) {
		_bufferGraphics.setColor(styleBackground[_styleID]);
		_bufferGraphics.fillRect(BOX_PADDING + (BOX_SIZE + BOX_PADDING) * x, BOX_PADDING + (BOX_SIZE + BOX_PADDING) * y + PANEL_HEIGHT, BOX_SIZE, BOX_SIZE);
		_bufferGraphics.drawImage(type.image(), BOX_PADDING + (BOX_SIZE + BOX_PADDING) * x, BOX_PADDING + (BOX_SIZE + BOX_PADDING) * y + PANEL_HEIGHT,
			styleBackground[_styleID], this);
	}

	private void drawLife(int life) {
		_bufferGraphics.setColor(stylePanel[_styleID]);
		for (int i = 0; i < GameConst.MOUSE_LIFES; i++) {
			_bufferGraphics.fillRect(PANEL_PADDING + i * (BOX_SIZE + BOX_PADDING), BOX_PADDING + (PANEL_HEIGHT - BOX_SIZE) / 2, BOX_SIZE, BOX_SIZE);

			if (i < life)
				_bufferGraphics.drawImage(BoxType.LIFE.image(), PANEL_PADDING + i * (BOX_SIZE + BOX_PADDING), BOX_PADDING + (PANEL_HEIGHT - BOX_SIZE) / 2,
					stylePanel[_styleID], this);
		}
	}

	private void drawClock(int seconds, int spawn) {
		int clockSize = PANEL_HEIGHT - PANEL_PADDING * 2;
		int centerX = MAP_SIZE / 2;
		int centerY = PANEL_HEIGHT / 2;

		// Clock
		_bufferGraphics.setColor(Color.WHITE);
		_bufferGraphics.fillArc(centerX - clockSize / 2, centerY - clockSize / 2, clockSize, clockSize, 0, 360);
		_bufferGraphics.setColor(COLOR_CLOCK_MIN);
		_bufferGraphics.drawArc(centerX - clockSize / 2, centerY - clockSize / 2, clockSize, clockSize, 0, 360);

		// Steps
		for (int i = 0; i < 12; i++) {
			int stepX1 = (int) (Math.cos(i * 5 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.45 + centerX);
			int stepY1 = (int) (Math.sin(i * 5 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.45 + centerY);
			int stepX2 = (int) (Math.cos(i * 5 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.50 + centerX);
			int stepY2 = (int) (Math.sin(i * 5 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.50 + centerY);
			_bufferGraphics.drawLine(stepX1, stepY1, stepX2, stepY2);
		}

		// Spawn
		if (spawn > 0) {
			_bufferGraphics.setColor(COLOR_CLOCK_SPAWN);
			int spawnX1 = (int) (Math.cos(spawn / 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.35 + centerX);
			int spawnY1 = (int) (Math.sin(spawn / 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.35 + centerY);
			int spawnX2 = (int) (Math.cos(spawn / 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.50 + centerX);
			int spawnY2 = (int) (Math.sin(spawn / 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.50 + centerY);
			_bufferGraphics.drawLine(spawnX1, spawnY1, spawnX2, spawnY2);
		}

		// Seconds
		_bufferGraphics.setColor(COLOR_CLOCK_SEC);
		int secX = (int) (Math.cos(seconds % 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.4 + centerX);
		int secY = (int) (Math.sin(seconds % 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.4 + centerY);
		_bufferGraphics.drawLine(centerX, centerY, secX, secY);

		// Minutes
		_bufferGraphics.setColor(COLOR_CLOCK_MIN);
		int minX = (int) (Math.cos(seconds / 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.40 + centerX);
		int minY = (int) (Math.sin(seconds / 60 * Math.PI / 30 - Math.PI / 2) * clockSize * 0.40 + centerY);
		_bufferGraphics.drawLine(centerX, centerY, minX, minY);
	}

	private void drawScore(int score) {
		String scoreText = LanguageManager.getString("game.score"); //$NON-NLS-1$

		int width = _fontMetrics.stringWidth(scoreText + ": 00000"); //$NON-NLS-1$

		// Reset
		_bufferGraphics.setColor(stylePanel[_styleID]);
		_bufferGraphics.fillRect(MAP_SIZE - width - PANEL_PADDING, PANEL_PADDING, width, _fontMetrics.getHeight());

		// Text
		_bufferGraphics.setColor(styleText[_styleID]);
		_bufferGraphics.drawString(scoreText + ":", MAP_SIZE - width - PANEL_PADDING, _fontMetrics.getHeight() + PANEL_PADDING); //$NON-NLS-1$

		width = _fontMetrics.stringWidth(String.valueOf(score));
		_bufferGraphics.drawString(String.valueOf(score), MAP_SIZE - width - PANEL_PADDING, _fontMetrics.getHeight() + PANEL_PADDING);
	}

	private void drawLevel(int level) {
		String levelText = LanguageManager.getString("game.level"); //$NON-NLS-1$
		int width = _fontMetrics.stringWidth(levelText + ": 00000"); //$NON-NLS-1$

		// Reset
		_bufferGraphics.setColor(stylePanel[_styleID]);
		_bufferGraphics.fillRect(MAP_SIZE - width - PANEL_PADDING, PANEL_HEIGHT - PANEL_PADDING - _fontMetrics.getHeight(), width, _fontMetrics.getHeight());

		// Text
		_bufferGraphics.setColor(styleText[_styleID]);
		_bufferGraphics.drawString(levelText + ":", MAP_SIZE - width - PANEL_PADDING, PANEL_HEIGHT - PANEL_PADDING - _fontMetrics.getDescent()); //$NON-NLS-1$

		width = _fontMetrics.stringWidth(String.valueOf(level));
		_bufferGraphics.drawString(String.valueOf(level), MAP_SIZE - width - PANEL_PADDING, PANEL_HEIGHT - PANEL_PADDING - _fontMetrics.getDescent());
	}

	@Override
	public void paint(Graphics g) {
		if (_gray) {
			BufferedImage grayImage = new BufferedImage(_bufferImage.getWidth(), _bufferImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			_colorConvert.filter(_bufferImage, grayImage);
			g.drawImage(grayImage, 0, 0, this);
		} else {
			g.drawImage(_bufferImage, 0, 0, this);
		}
	}
}
