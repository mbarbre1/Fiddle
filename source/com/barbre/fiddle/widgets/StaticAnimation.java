package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.IStaticAnimation;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public class StaticAnimation extends BaseWidget {

	/**
	 * Method StaticAnimation.
	 * @param parent
	 * @param c
	 */
	public StaticAnimation(Component parent, IScreenPiece c) {
		super(parent, c);
		setHorizontalTextPosition(CENTER);
		setText(getEqObject().getText());
	}

	/**
	 * @see Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Image img = ImageMediator.getImage(((IStaticAnimation) getEqObject()).getAnimationObject());
		if (img != null) {
			Rectangle bounds = getBounds();
			g.drawImage(img, 0, 0, bounds.width, bounds.height, this);
		}
	}

}