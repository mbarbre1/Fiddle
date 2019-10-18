package com.barbre.fiddle.widgets;

import java.awt.Component;

import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.IStaticText;

public class StaticText extends BaseWidget {
	private IStaticText iLabel = null;

	/**
	 * Method StaticText.
	 * @param parent
	 * @param c
	 */
	public StaticText(Component parent, IScreenPiece c) {
		super(parent, c);
		iLabel = (IStaticText) c;
		setHorizontalTextPosition(CENTER);
		setText(iLabel.getText());
		if (iLabel.getAlignCenter()) {
			setHorizontalAlignment(CENTER);
		} else if (iLabel.getAlignRight()) {
			setHorizontalAlignment(RIGHT);
		} else {
			setHorizontalAlignment(LEFT);
		}
	}
}