package com.barbre.fiddle.io.image;

import java.awt.Frame;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.barbre.fiddle.io.image.z.ZImageLoader;
import com.barbre.fiddle.io.image.z.ZNoSuchLoaderException;
import com.barbre.fiddle.io.image.z.ZProgressStatus;

import com.barbre44.util.Debug;

/**
 * This mediator isolates the odd 1997 code of Java 1.0 found in the z
 * classes.  Even though this code is so old, it is the best I've found
 * for loading Targa images.
 */
public class ZMediator extends Frame implements ZProgressStatus {
	private static ZMediator instance = null;
	private ZImageLoader picLoader = null;

	/**
	 * @see Object#Object()
	 */
	private ZMediator() {
		super();
		picLoader = new ZImageLoader();
	}

	/**
	 * Method getInstance.
	 * @return ZMediator
	 */
	public static ZMediator getInstance() {
		if (instance == null) {
			instance = new ZMediator();
		}
		return instance;
	}

	/**
	 * Method getImage.
	 * @param filename
	 * @return Image
	 */
	public Image getImage(String filename) {
		picLoader.setClient(this); // prepare for status callbacks
		picLoader.debug(false);
		picLoader.newImage = null;
		try {
			Debug.println(this, filename);
			Image img = picLoader.load(filename, this);
			return img;
		} catch (ZNoSuchLoaderException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @see ZProgressStatus#imageLoaded(int, Image, String)
	 */
	public void imageLoaded(int percent, Image picture, String text) {
	}

	/**
	 * Method main.
	 * @param arguments
	 */
	public static void main(String[] arguments) {
		String filename = "e:\\program files\\everquest\\uifiles\\default\\wnd_bg_light_rock.tga";
		Image image = ZMediator.getInstance().getImage(filename);
		if (image == null)
			System.out.println("No image loaded");
		else 
			JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(image)));

		filename = "e:\\program files\\everquest\\uifiles\\default\\wnd_bg_dark_rock.tga";
		image = ZMediator.getInstance().getImage(filename);
		if (image == null)
			System.out.println("No image loaded");
		else 
			JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(image)));
		System.exit(0);
	}
}