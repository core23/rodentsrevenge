package de.core23.rodentsrevenge;

import java.io.IOException;

import javax.swing.SwingUtilities;

import de.core23.rodentsrevenge.controller.GameController;
import de.core23.rodentsrevenge.helper.LanguageManager;

public class RRAppDelegate {
	public static void main(String args[]) {
		// Load Language File
		try {
			LanguageManager.load("de"); //$NON-NLS-1$
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// MacOS Properties
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Rodent's Revenge"); //$NON-NLS-1$ //$NON-NLS-2$

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GameController();
			}
		});
	}
}
