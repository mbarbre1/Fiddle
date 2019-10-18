
package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class SizedImageIcon extends ImageIcon {

	/**
	 * Constructor for SizedImageIcon.
	 * @param filename
	 * @param description
	 */
	public SizedImageIcon(String filename, String description) {
		super(filename, description);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param filename
	 */
	public SizedImageIcon(String filename) {
		super(filename);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param location
	 * @param description
	 */
	public SizedImageIcon(URL location, String description) {
		super(location, description);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param location
	 */
	public SizedImageIcon(URL location) {
		super(location);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param image
	 * @param description
	 */
	public SizedImageIcon(Image image, String description) {
		super(image, description);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param image
	 */
	public SizedImageIcon(Image image) {
		super(image);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param imageData
	 * @param description
	 */
	public SizedImageIcon(byte[] imageData, String description) {
		super(imageData, description);
	}

	/**
	 * Constructor for SizedImageIcon.
	 * @param imageData
	 */
	public SizedImageIcon(byte[] imageData) {
		super(imageData);
	}

	/**
	 * Constructor for SizedImageIcon.
	 */
	public SizedImageIcon() {
		super();
	}

	/**
	 * @see Icon#paintIcon(Component, Graphics, int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Dimension d = c.getSize();
		g.drawImage(super.getImage(), 0, 0, d.width, d.height, getImageObserver());
	}

}
