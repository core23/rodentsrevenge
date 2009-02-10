package de.core23.rodentsrevenge.model;

import java.awt.Image;

import de.core23.rodentsrevenge.helper.ImageManager;

public enum BoxType {
	FLOOR, WALL, BOX, HOLE, TRAP, CHEESE, MOUSE, MOUSE_HOLE, CAT, YARN, LIFE;

	public Image image() {
		switch (this) {
			case FLOOR:
				return null;
			case WALL:
				return ImageManager.get("wall.png"); //$NON-NLS-1$
			case BOX:
				return ImageManager.get("box.png"); //$NON-NLS-1$
			case HOLE:
				return ImageManager.get("hole.png"); //$NON-NLS-1$
			case TRAP:
				return ImageManager.get("trap.png"); //$NON-NLS-1$
			case CHEESE:
				return ImageManager.get("cheese.png"); //$NON-NLS-1$
			case MOUSE:
				return ImageManager.get("mouse.png"); //$NON-NLS-1$
			case MOUSE_HOLE:
				return ImageManager.get("mouse_hole.png"); //$NON-NLS-1$
			case CAT:
				return ImageManager.get("cat.png"); //$NON-NLS-1$
			case YARN:
				return ImageManager.get("yarn.png"); //$NON-NLS-1$
			case LIFE:
				return ImageManager.get("life.png"); //$NON-NLS-1$
		}
		return null;
	}
}
