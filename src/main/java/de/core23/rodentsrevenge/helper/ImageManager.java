package de.core23.rodentsrevenge.helper;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.core23.rodentsrevenge.model.StyleConst;

public class ImageManager implements StyleConst {
	private static HashMap<String, Image> IMAGEMAP = new HashMap<String, Image>();

	private static int PATH_ID = 0;

	public static void setStyleID(int pathID) {
		ImageManager.PATH_ID = pathID;
		ImageManager.IMAGEMAP.clear();
	}

	public static Image get(String name) {
		if (!IMAGEMAP.containsKey(name))
			IMAGEMAP.put(name, loadImage(name));
		return IMAGEMAP.get(name);
	}

	private static BufferedImage loadImage(String src) {
		try {
			src = "img/" + STYLES[PATH_ID][0] + "/" + src; //$NON-NLS-1$ //$NON-NLS-2$
			BufferedImage image = ImageIO.read(ImageManager.class.getClassLoader().getResource(src));
			return resizeImage(image, BOX_SIZE, BOX_SIZE);
		} catch (IOException e) {
			System.err.println("Image " + src + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedImage resizeImage(BufferedImage image, int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
}
