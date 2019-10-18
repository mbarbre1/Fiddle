package com.barbre.fiddle.widgets;

import java.awt.Component;

import com.barbre.fiddle.elements.ILabel;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre44.util.Debug;

public class Label extends BaseWidget {
	private ILabel iLabel = null;

	/**
	 * Method Label.
	 * @param parent
	 * @param c
	 */
	public Label(Component parent, IScreenPiece c) {
		super(parent, c);
		iLabel = (ILabel) c;
		setHorizontalTextPosition(CENTER);
		setText(iLabel.getText());
		Debug.println(this, "text=" + getText());
		align();
	}

	private void align() {
		if (iLabel.getAlignCenter()) {
			setHorizontalAlignment(CENTER);
		} else if (iLabel.getAlignRight()) {
			setHorizontalAlignment(RIGHT);
		} else {
			setHorizontalAlignment(LEFT);
		}
	}

	/**
	 * @see com.barbre.fiddle.widgets.UpdateCapable#update()
	 */
	public void update() {
		super.update();
		align();
	}


}