package com.barbre.fiddle.io.loaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.barbre.fiddle.io.image.ZMediator;
import com.barbre44.swing.image.GammaCorrection;
import com.barbre44.util.Debug;

public final class ImageLoader {
	private static final String BUFFERED_KEY = "BI:";
	private static final Map LOADED_IMAGES = new HashMap();
	private static GammaCorrection gammaCorrector = new GammaCorrection();

	/**
	 * Method getImage.
	 * @param filename
	 * @return Image
	 */
	public static Image getImage(String filename) {
		try {
			if (LOADED_IMAGES.containsKey(filename)) {
				return (Image) LOADED_IMAGES.get(filename);
			}
			Image img = null;

			Debug.println(null, "getImage(" + filename + ")");

			if (filename.toLowerCase().endsWith("bmp")) {
				img = ZMediator.getInstance().getImage(filename);
			} else if (filename.toLowerCase().endsWith("tga")) {
				img = ZMediator.getInstance().getImage(filename);
				//				return ZMediator.getInstance().getImage(filename);
			} else {
				img = new ImageIcon(filename).getImage();
			}

			img = gammaCorrect(img);

			LOADED_IMAGES.put(filename, img);
			return img;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Method getBufferedImage.
	 * @param filename
	 * @return BufferedImage
	 */
	public static BufferedImage getBufferedImage(String filename) {
		String key = BUFFERED_KEY + filename;
		if (LOADED_IMAGES.containsKey(key))
			return (BufferedImage) LOADED_IMAGES.get(key);
		Debug.println(null, key);
		Image img = getImage(filename);
		BufferedImage bi = convertToBufferedImage(img);
		LOADED_IMAGES.put(key, bi);
		return bi;
	}

	/**
	 * Method getSubImage.
	 * @param key
	 * @param img
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return Image
	 */
	public static Image getSubImage(String key, BufferedImage img, int x, int y, int width, int height) {
		if (LOADED_IMAGES.containsKey(key))
			return (Image) LOADED_IMAGES.get(key);

		Image sub = img.getSubimage(x, y, width, height);
		LOADED_IMAGES.put(key, sub);
		return sub;
	}

	/**
	 * Method gammaCorrect.
	 * @param img
	 * @return Image
	 */
	private static Image gammaCorrect(Image img) {
		BufferedImage bi = convertToBufferedImage(img);		
		if (bi != null) {
			gammaCorrector.gammaCorrect(bi);
			img.flush();			
		}
		return bi;
	}

	/**
	 * Method showMe.
	 * @param img
	 */
	public static void showMe(Image img) {
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(img)));
	}

	/**
	 * Method convertToBufferedImage.
	 * @param image
	 * @return BufferedImage
	 */
	private static BufferedImage convertToBufferedImage(Image image) {
		if (image == null)
			return null;
		ImageIcon icon = new ImageIcon(image);
		Debug.println(null, "icon=" + icon + "; image="+image);
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(image, 0, 0, null);
		return bi;
	}

	/**
	* Returns the gamma.
	* @return double
	*/
	public static double getGamma() {
		return gammaCorrector.getGamma();
	}

	/**
	 * Sets the gamma.
	 * @param gamma The gamma to set
	 */
	public static void setGamma(double g) {
		gammaCorrector.setGamma(g);
	}

	public static void flush() {
		Iterator i = LOADED_IMAGES.values().iterator();
		while (i.hasNext()) {
			Image element = (Image) i.next();
			element.flush();
		}
		LOADED_IMAGES.clear();
		System.gc();
	}
	
	/**
	 * Method main.
	 * @param args
	 */
	public static void main(String[] args) {
		setGamma(2.5);
		Image theImage = getImage("c:\\program files\\EverQuest\\uifiles\\default\\window_pieces01.tga");
		showMe(theImage);
		System.exit(0);
	}
}