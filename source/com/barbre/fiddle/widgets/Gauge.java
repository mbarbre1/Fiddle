package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import com.barbre.fiddle.elements.IGauge;
import com.barbre.fiddle.elements.IGaugeDrawTemplate;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.ImageMediator;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

public class Gauge extends BaseWidget {

	/**
	 * Method Gauge.
	 * @param parent
	 * @param c
	 */
	public Gauge(Component parent, IScreenPiece c) {
		super(parent, c);
		setOpaque(false);
		setHorizontalTextPosition(CENTER);
		setText(getEqObject().getText());
	}

	/**
		 * Method assignIcons.
		 */
	private void assignIcons() {
		IGaugeDrawTemplate t = getGauge().getGaugeDrawTemplate();
		setIcon(new SizedImageIcon(ImageMediator.getImage(t.getBackgroundObject())));
	}

	/**
	 * @see Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		IGaugeDrawTemplate t = getGauge().getGaugeDrawTemplate();
		Dimension d = WidgetUtilities.getViewSize(getGauge().getSize());

		Image img = ImageMediator.getImage(t.getFillObject());
		if (img != null) {

			g.drawImage(img, getGauge().getGaugeOffsetX(), getGauge().getGaugeOffsetY(), d.width, d.height, this);
		}

		img = ImageMediator.getImage(t.getLinesObject());
		if (img != null) {
			g.drawImage(img, getGauge().getGaugeOffsetX(), getGauge().getGaugeOffsetY(), d.width, d.height, this);
		}

		if (getGauge().getDrawLinesFill()) {
			img = ImageMediator.getImage(t.getLinesFillObject());
			if (img != null) {
				g.drawImage(img, getGauge().getGaugeOffsetX(), getGauge().getGaugeOffsetY(), d.width, d.height, this);
			}
		}

		img = ImageMediator.getImage(t.getEndCapLeftObject());
		if (img != null) {
			g.drawImage(img, getGauge().getGaugeOffsetX(), getGauge().getGaugeOffsetY(), this);
		}

		img = ImageMediator.getImage(t.getEndCapRightObject());
		if (img != null) {
			g.drawImage(img, getGauge().getSize().getCX() - img.getWidth(this), getGauge().getGaugeOffsetY(), this);
		}
	}

	/**
	 * Method getGauge.
	 * @return IGauge
	 */
	private IGauge getGauge() {
		return (IGauge) getEqObject();
	}
	/**
	 * @see com.barbre.fiddle.widgets.UpdateCapable#update()
	 */
	public void update() {
		super.update();
		assignIcons();
	}


}