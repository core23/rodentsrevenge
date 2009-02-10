package de.core23.rodentsrevenge.model;

import java.awt.Color;
import java.awt.Font;

public interface StyleConst {
	final int BOX_PADDING = 1;

	final int BOX_SIZE = 20;

	final int PANEL_HEIGHT = 50;

	final int PANEL_PADDING = 5;

	final Font PANEL_FONT = new Font("Helvetica", Font.BOLD, 12); //$NON-NLS-1$

	final Color COLOR_CLOCK_SEC = Color.RED;

	final Color COLOR_CLOCK_MIN = Color.DARK_GRAY;

	final Color COLOR_CLOCK_SPAWN = Color.BLUE;

	final Color[] styleBackground = {new Color(128, 128, 0), new Color(230, 224, 210), new Color(210, 230, 240)};

	final Color[] stylePanel = {new Color(190, 190, 190), new Color(110, 70, 50), new Color(120, 180, 160)};

	final Color[] styleText = {new Color(70, 70, 70), new Color(230, 224, 210), new Color(210, 230, 240)};

	final String[][] STYLES = { {"old", "Classic"}, {"new", "Remake"}, {"gary", "Spongebob"}}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
}
