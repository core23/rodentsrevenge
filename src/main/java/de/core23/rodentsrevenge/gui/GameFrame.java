package de.core23.rodentsrevenge.gui;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JMenuItem;

import de.core23.rodentsrevenge.helper.Actions;
import de.core23.rodentsrevenge.helper.LanguageManager;
import de.core23.rodentsrevenge.model.StyleConst;

public class GameFrame extends JFrame implements StyleConst {
	private static final long serialVersionUID = 1L;

	private JPanel _jContentPane = null;

	private GamePanel _gamePanel = null;

	private JMenuBar _jJMenuBar = null;

	private JMenu _jMenuFile = null;

	private JMenuItem _jMenuItemExit = null;

	private JMenuItem _jMenuItemNew = null;

	private JMenu _jMenuInfo = null;

	private JMenuItem _jMenuItemAbout = null;

	private JMenuItem _jMenuItemLevel = null;

	private JMenu _jMenuStyle = null;

	private JMenuItem _jMenuItemHighscore = null;

	public GameFrame() {
		super();
		initialize();
	}

	private void initialize() {
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Rodent's Revenge"); //$NON-NLS-1$
		this.setResizable(false);
		setContentSize(getGamePanel().getWidth(), getGamePanel().getHeight());
	}

	public void setContentSize(int x, int y) {
		this.getContentPane().setPreferredSize(new Dimension(x, y));
		this.pack();
	}

	public JPanel getJContentPane() {
		if (_jContentPane == null) {
			_jContentPane = new JPanel();
			_jContentPane.setLayout(new BorderLayout());
			_jContentPane.add(getGamePanel(), null);
		}
		return _jContentPane;
	}

	public GamePanel getGamePanel() {
		if (_gamePanel == null) {
			_gamePanel = new GamePanel();
			_gamePanel.setLocation(0, 0);
		}
		return _gamePanel;
	}

	public JMenuBar getJJMenuBar() {
		if (_jJMenuBar == null) {
			_jJMenuBar = new JMenuBar();
			_jJMenuBar.setPreferredSize(new Dimension(0, 20));
			_jJMenuBar.add(getJMenuFile());
			_jJMenuBar.add(getJMenuStyle());
			_jJMenuBar.add(getJMenuHelp());
		}
		return _jJMenuBar;
	}

	public JMenu getJMenuFile() {
		if (_jMenuFile == null) {
			_jMenuFile = new JMenu();
			_jMenuFile.setText(LanguageManager.getString("menu.file")); //$NON-NLS-1$
			_jMenuFile.setMnemonic('D');
			_jMenuFile.add(getJMenuItemNew());
			_jMenuFile.add(getJMenuItemLevel());
			_jMenuFile.add(getJMenuItemHighscore());
			_jMenuFile.addSeparator();
			_jMenuFile.add(getJMenuItemExit());
		}
		return _jMenuFile;
	}

	public JMenu getJMenuHelp() {
		if (_jMenuInfo == null) {
			_jMenuInfo = new JMenu();
			_jMenuInfo.setText(LanguageManager.getString("menu.help")); //$NON-NLS-1$
			_jMenuInfo.add(getJMenuItemAbout());
		}
		return _jMenuInfo;
	}

	public JMenuItem getJMenuItemExit() {
		if (_jMenuItemExit == null) {
			_jMenuItemExit = new JMenuItem();
			_jMenuItemExit.setText(LanguageManager.getString("menu.exit")); //$NON-NLS-1$
			_jMenuItemExit.setMnemonic('E');
			_jMenuItemExit.setActionCommand(Actions.EXIT);
		}
		return _jMenuItemExit;
	}

	public JMenuItem getJMenuItemNew() {
		if (_jMenuItemNew == null) {
			_jMenuItemNew = new JMenuItem();
			_jMenuItemNew.setText(LanguageManager.getString("menu.newgame")); //$NON-NLS-1$
			_jMenuItemNew.setMnemonic('N');
			_jMenuItemNew.setActionCommand(Actions.NEW_GAME);
		}
		return _jMenuItemNew;
	}

	public JMenuItem getJMenuItemAbout() {
		if (_jMenuItemAbout == null) {
			_jMenuItemAbout = new JMenuItem();
			_jMenuItemAbout.setText(LanguageManager.getString("menu.info")); //$NON-NLS-1$
			_jMenuItemAbout.setMnemonic('I');
			_jMenuItemAbout.setActionCommand(Actions.SHOW_INFO);
		}
		return _jMenuItemAbout;
	}

	public JMenuItem getJMenuItemLevel() {
		if (_jMenuItemLevel == null) {
			_jMenuItemLevel = new JMenuItem();
			_jMenuItemLevel.setText(LanguageManager.getString("menu.level")); //$NON-NLS-1$
			_jMenuItemLevel.setActionCommand(Actions.CHOOSE_LEVEL);
			_jMenuItemLevel.setMnemonic('w');
		}
		return _jMenuItemLevel;
	}

	public JMenu getJMenuStyle() {
		if (_jMenuStyle == null) {
			_jMenuStyle = new JMenu();
			_jMenuStyle.setText(LanguageManager.getString("menu.style")); //$NON-NLS-1$
			_jMenuStyle.setMnemonic('S');
		}
		return _jMenuStyle;
	}

	public JMenuItem getJMenuItemHighscore() {
		if (_jMenuItemHighscore == null) {
			_jMenuItemHighscore = new JMenuItem();
			_jMenuItemHighscore.setText(LanguageManager.getString("menu.highscore")); //$NON-NLS-1$
			_jMenuItemHighscore.setActionCommand(Actions.HIGHSCORE);
			_jMenuItemHighscore.setMnemonic('S');
		}
		return _jMenuItemHighscore;
	}
}
