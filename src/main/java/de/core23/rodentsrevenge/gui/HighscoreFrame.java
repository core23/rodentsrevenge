package de.core23.rodentsrevenge.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JButton;

import de.core23.rodentsrevenge.helper.LanguageManager;

public class HighscoreFrame extends JDialog {
	private static final long serialVersionUID = 1L;

	private JPanel _jContentPane = null;

	private JLabel _jLabelName = null;

	private JLabel _jLabelScore = null;

	private JButton _jButtonClear = null;

	public HighscoreFrame(Window parent) {
		super(parent);

		initialize();
	}

	public void initialize() {
		this.setSize(300, 350);
		this.setContentPane(getJContentPane());
		this.setTitle(LanguageManager.getString("highscore.title")); //$NON-NLS-1$
		this.setResizable(false);
	}

	public JPanel getJContentPane() {
		if (_jContentPane == null) {
			_jLabelScore = new JLabel();
			_jLabelScore.setBounds(new Rectangle(200, 10, 70, 20));
			_jLabelScore.setFont(new Font("Nervana", Font.BOLD, 13)); //$NON-NLS-1$
			_jLabelScore.setText(LanguageManager.getString("game.score")); //$NON-NLS-1$
			_jLabelScore.setHorizontalAlignment(JLabel.RIGHT);
			_jLabelName = new JLabel();
			_jLabelName.setBounds(new Rectangle(20, 10, 160, 20));
			_jLabelName.setFont(new Font("Nervana", Font.BOLD, 13)); //$NON-NLS-1$
			_jLabelName.setText(LanguageManager.getString("highscore.name")); //$NON-NLS-1$
			_jContentPane = new JPanel();
			_jContentPane.setLayout(null);
			_jContentPane.add(_jLabelName, null);
			_jContentPane.add(_jLabelScore, null);
			_jContentPane.add(getJButtonClear(), null);
		}
		return _jContentPane;
	}

	public JButton getJButtonClear() {
		if (_jButtonClear == null) {
			_jButtonClear = new JButton();
			_jButtonClear.setBounds(new Rectangle(70, 280, 160, 25));
			_jButtonClear.setText(LanguageManager.getString("highscore.delete")); //$NON-NLS-1$
		}
		return _jButtonClear;
	}
}
