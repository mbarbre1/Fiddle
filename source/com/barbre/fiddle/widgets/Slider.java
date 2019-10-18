package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.ISlider;
import com.barbre.fiddle.elements.ISliderDrawTemplate;
import com.barbre.fiddle.widgets.utility.ImageMediator;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

public class Slider extends ImagePanel {
	private static Rectangle aRectangle = new Rectangle();	

	/**
	 * Method Gauge.
	 * @param parent
	 * @param c
	 */
	public Slider(Component parent, IScreenPiece c) {
		super(parent, c);
	}

	/**
	 * @see Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		ISliderDrawTemplate t = getSlider().getSliderArtObject();
		Dimension d = WidgetUtilities.getViewSize(getSlider().getSize());

		Image img = ImageMediator.getImage(t.getBackgroundObject());
		if (img != null) {
			g.drawImage(img, 0, 0, d.width, d.height, this);
		}			

		img = ImageMediator.getImage(t.getEndCapLeftObject());
		if (img != null) {
			g.drawImage(img, 0, 0, img.getWidth(this), d.height, this);
		}

		img = ImageMediator.getImage(t.getEndCapRightObject());
		if (img != null) {
			g.drawImage(img, getSlider().getSize().getCX() - img.getWidth(this), 0, img.getWidth(this), d.height, this);
		}

		img = ImageMediator.getImage(t.getThumb().getNormalObject());
		if (img != null) {
			g.drawImage(img, (getSlider().getSize().getCX() - img.getWidth(this))/2, 0, img.getWidth(this), d.height, this);
		}

	}

	/**
	 * Method getSlider.
	 * @return ISlider
	 */
	private ISlider getSlider() {
		return (ISlider) getEqObject();
	}
}