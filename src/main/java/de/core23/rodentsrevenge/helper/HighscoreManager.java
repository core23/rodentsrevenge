package de.core23.rodentsrevenge.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Vector;

import de.core23.rodentsrevenge.model.highscore.Score;
import de.core23.rodentsrevenge.model.highscore.ScoreComparator;

public class HighscoreManager {
	private static final String FILENAME = "highscore.dat"; //$NON-NLS-1$

	private Vector<Score> _scoreList;

	public HighscoreManager() {
		loadScoreFile();

		if (_scoreList == null)
			_scoreList = new Vector<Score>();
	}

	public Vector<Score> getScoreList() {
		return _scoreList;
	}

	public void addScore(String name, int score) {
		_scoreList.add(new Score(name, score));

		ScoreComparator comparator = new ScoreComparator();
		Collections.sort(_scoreList, comparator);

		if (_scoreList.size() > 10)
			_scoreList.remove(10);

		saveScoreFile();
	}

	public void clear() {
		getScoreList().clear();
		saveScoreFile();
	}

	@SuppressWarnings("unchecked")
	private void loadScoreFile() {
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new FileInputStream(FILENAME));
			_scoreList = (Vector<Score>) inputStream.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception e) {
			}
		}
	}

	private void saveScoreFile() {
		ObjectOutputStream outputStream = null;
		try {
			outputStream = new ObjectOutputStream(new FileOutputStream(FILENAME));
			outputStream.writeObject(_scoreList);
		} catch (FileNotFoundException e) {
			System.out.println("[Save] FileNotFound: " + e.getMessage()); //$NON-NLS-1$
		} catch (IOException e) {
			System.out.println("[Save] IOError: " + e.getMessage()); //$NON-NLS-1$
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
			}
		}
	}
}
