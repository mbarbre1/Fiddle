package com.barbre.fiddle.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import com.barbre.fiddle.decorators.BorderDecorator;
import com.barbre.fiddle.elements.ISTMLbox;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

public class StmlBox extends ImagePanel {
	private BorderDecorator borderDecorator;

	public StmlBox(Component parent, IScreenPiece c) {
		super(parent, c);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		addText(Color.white);
		addText(Color.blue.darker());
		addText(Color.yellow);
		addText(Color.cyan);
		addText(Color.red);
		borderDecorator = new BorderDecorator(this, getEqObject(),  getBox().getDrawTemplateObject());		
	}

	
	/**
	 * Method addText.
	 * @param color
	 */
	private void addText(Color color) {
		JLabel label = new JLabel("Your faction with Fiddle has improved.");
		label.setOpaque(false);
		label.setFont(WidgetUtilities.getFont(getBox().getFont()));
		label.setForeground(color);
		add(label);
	}
	
	/**
	 * @see JComponent#paintBorder(Graphics)
	 */
	protected void paintBorder(Graphics g) {
		if (getBox().getStyle_Border()) {
			borderDecorator.paint(g);
		}
	}

	/**
	 * Method getBox.
	 * @return ISTMLbox
	 */
	private ISTMLbox getBox() {
		return (ISTMLbox) getEqObject();
	}
}