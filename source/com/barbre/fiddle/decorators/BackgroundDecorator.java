
package com.barbre.fiddle.decorators;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.IWindowDrawTemplate;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public class BackgroundDecorator extends Decorator {
	private IWindowDrawTemplate template;
	private Image background;
	private static Rectangle aRectangle = new Rectangle();

	/**
	 * Constructor for backgroundDecorator.
	 * @param parent
	 */
	public BackgroundDecorator(JComponent parent, IClass eqParent, IWindowDrawTemplate template) {
		super(parent, eqParent);
		this.template = template;
		refresh();
	}

	/**
	 * @see Decorator#paint(Graphics)
	 */
	public void paint(Graphics g) {
		if (template == null)
			return;
		aRectangle.x = 0;
		aRectangle.y = 0;
		aRectangle.width = getParent().getSize().width;
		aRectangle.height = getParent().getSize().height;
		draw(background, g, aRectangle);
	}

	/**
	 * @see Decorator#refresh()
	 */ 
	public void refresh() {
		if (template == null)
			return;
		background = ImageMediator.getImage(template.getBackgroundObject());
	}

}
