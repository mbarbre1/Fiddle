package com.barbre.fiddle.decorators;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.IFrame;

public abstract class Decorator {
	private static final Rectangle EMPTY_RECTANGLE = new Rectangle(0, 0, 0, 0);
	private JComponent parent = null;
	protected IClass eqParent;

	/**
	 * Method Decorator.
	 * @param parent
	 * @param eqParent
	 */
	public Decorator(JComponent parent, IClass eqParent) {
		super();
		this.parent = parent;
		this.eqParent = eqParent;
	}

	/**
	 * paint to a graphic
	 */
	abstract void paint(Graphics g);

	/**
	 * Load information from the EQ elements needed
	 */
	abstract void refresh();

	/**
	 * get a bounds for an image.  If the image is null, then an empty rectangle is returned.
	 */
	protected Rectangle getImageBounds(Image img) {
		if (img == null)
			return EMPTY_RECTANGLE;
		return new Rectangle(0, 0, img.getWidth(parent), img.getHeight(parent));
	}

	/**
	 * Custom draw helper
	 */
	protected void draw(Image img, Graphics g, Rectangle bounds) {
		if (img != null)
			g.drawImage(img, bounds.x, bounds.y, bounds.width, bounds.height, parent);
	}

	/**
	 * Custom draw helper
	 */
	protected void draw(Image img, Graphics g, IFrame f) {
		draw(img, g, new Rectangle(f.getLocation().getX(), f.getLocation().getY(), f.getSize().getCX(), f.getSize().getCY()));
	}

	/**
	 * Custom draw helper
	 */
	protected void draw(Image img, Graphics g, int x, int y) {
		draw(img, g, new Rectangle(x, y, img.getWidth(parent), img.getHeight(parent)));
	}

	/**
	 * Gets the parent.
	 * @return Returns a JComponent
	 */
	public JComponent getParent() {
		return parent;
	}

}
