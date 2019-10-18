package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Graphics;

import com.barbre.fiddle.decorators.BackgroundDecorator;
import com.barbre.fiddle.elements.IControl;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre44.util.Debug;

public class ImagePanel extends BaseWidget {
	private boolean paintBackground = false;
	private BackgroundDecorator backgroundDecorator;
	private IControl control;

	/**
	 * Constructor for ImagePanel.
	 */
	public ImagePanel(Component parent, IScreenPiece control) {
		super(parent, control);
		this.control = (IControl) control;

		if (this.control.getDrawTemplate() == null)
			return;

		paintBackground = this.control.getDrawTemplateObject().getBackground() != null;
		Debug.println(this, control.getitem() + " = " + paintBackground);
		if (paintBackground) {
			backgroundDecorator = new BackgroundDecorator(this,  getEqObject(), this.control.getDrawTemplateObject());
		} 
	}

	/**
	 * @see JComponent#paintComponent(Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (paintBackground) {
			Debug.println(this, "painting background...");
			backgroundDecorator.paint(g);
		}
	}

}