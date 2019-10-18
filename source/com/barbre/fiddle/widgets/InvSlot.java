package com.barbre.fiddle.widgets;

import java.awt.Component;

import com.barbre.fiddle.elements.IInvSlot;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public class InvSlot extends BaseWidget {
	private IInvSlot slot = null;

	/**
	 * Method Button.
	 * @param parent
	 * @param button
	 */
	public InvSlot(Component parent, IScreenPiece button) {
		super(parent, button);
		slot = (IInvSlot) button;
		setIcon(new SizedImageIcon(ImageMediator.getImage(slot.getBackgroundObject())));
		setText(slot.getText());
	}

}