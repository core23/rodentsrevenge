package de.core23.rodentsrevenge.controller;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import de.core23.rodentsrevenge.gui.HighscoreFrame;
import de.core23.rodentsrevenge.helper.HighscoreManager;
import de.core23.rodentsrevenge.helper.LanguageManager;

public class HighscoreController implements ActionListener {
	private HighscoreFrame _frame = null;

	private Vector<JLabel> _labelList = new Vector<JLabel>();

	private static String _lastName = ""; //$NON-NLS-1$

	private HighscoreManager _highscore = new HighscoreManager();

	public HighscoreController(Window parent) {
		_frame = new HighscoreFrame(parent);
		_frame.setLocationRelativeTo(parent);
		_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		_frame.setModal(true);

		_frame.getJButtonClear().addActionListener(this);

		showScores();
	}

	public void show() {
		_frame.setVisible(true);
	}

	public void newHighscore(int score) {
		if (score < 0)
			return;

		String name = JOptionPane.showInputDialog(null, LanguageManager.getString("highscore.message.dialog"), _lastName); //$NON-NLS-1$
		if (name != null && !name.isEmpty()) {
			_highscore.addScore(name, score);
			_lastName = name;
			showScores();
		}
	}

	private void showScores() {
		for (JLabel label : _labelList)
			_frame.getJContentPane().remove(label);
		_labelList.clear();

		for (int i = 0; i < _highscore.getScoreList().size(); i++) {
			JLabel tempLabel = new JLabel();
			tempLabel.setBounds(new Rectangle(20, 10 + (i + 1) * 25, 160, 20));
			tempLabel.setFont(new Font("Nervana", Font.PLAIN, 13)); //$NON-NLS-1$
			tempLabel.setText(_highscore.getScoreList().get(i).getName());
			_frame.getJContentPane().add(tempLabel, null);
			_labelList.add(tempLabel);

			tempLabel = new JLabel();
			tempLabel.setBounds(new Rectangle(200, 10 + (i + 1) * 25, 70, 20));
			tempLabel.setHorizontalAlignment(JLabel.RIGHT);
			tempLabel.setFont(new Font("Nervana", Font.PLAIN, 13)); //$NON-NLS-1$
			tempLabel.setText(String.valueOf(_highscore.getScoreList().get(i).getScore()));
			_frame.getJContentPane().add(tempLabel, null);
			_labelList.add(tempLabel);
		}

		_frame.getJButtonClear().setEnabled(_highscore.getScoreList().size() > 0);
		_frame.getJContentPane().updateUI();
		_frame.getJContentPane().repaint();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _frame.getJButtonClear()) {
			if (JOptionPane.showConfirmDialog(null,
				LanguageManager.getString("highscore.delete.dialog"), LanguageManager.getString("highscore.title"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { //$NON-NLS-1$ //$NON-NLS-2$
				_highscore.clear();
				showScores();
				JOptionPane.showMessageDialog(_frame,
					LanguageManager.getString("highscore.delete.message"), LanguageManager.getString("highscore.title"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
}
