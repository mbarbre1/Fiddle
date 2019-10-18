package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Graphics;

import com.barbre.fiddle.decorators.BorderDecorator;
import com.barbre.fiddle.elements.IEditbox;
import com.barbre.fiddle.elements.IScreenPiece;

public class Editbox extends ImagePanel {
	private BorderDecorator borderDecorator;

	/**
	 * Method Label.
	 * @param parent
	 * @param c
	 */
	public Editbox(Component parent, IScreenPiece c) {
		super(parent, c);
		borderDecorator = new BorderDecorator(this,  getEqObject(), getEditbox().getDrawTemplateObject());
	}

	/**
	 * @see JComponent#paintBorder(Graphics)
	 */
	protected void paintBorder(Graphics g) {
		if (getEditbox().getStyle_Border()) {
			borderDecorator.paint(g);
		}
	}

	/**
	 * Method getEditbox.
	 * @return IEditbox
	 */
	private IEditbox getEditbox() {
		return (IEditbox) getEqObject();
	}
}