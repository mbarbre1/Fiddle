package com.barbre.fiddle.widgets;

import java.awt.Component;

import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.ISpellGem;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public class SpellGem extends BaseWidget {
	private ISpellGem slot = null;

	/**
	 * Method Button.
	 * @param parent
	 * @param button
	 */
	public SpellGem(Component parent, IScreenPiece button) {
		super(parent, button);
		slot = (ISpellGem) button;
		setHorizontalTextPosition(CENTER);
		assignIcons();
		setText(slot.getText());
	}

	private void assignIcons() {
		setIcon(new SizedImageIcon(ImageMediator.getImage(slot.getSpellGemDrawTemplate().getBackgroundObject())));
		setRolloverIcon(new SizedImageIcon(ImageMediator.getImage(slot.getSpellGemDrawTemplate().getHighlightObject())));
		setPressedIcon(new SizedImageIcon(ImageMediator.getImage(slot.getSpellGemDrawTemplate().getHolderObject())));
	}
	/**
	 * @see com.barbre.fiddle.widgets.UpdateCapable#update()
	 */
	public void update() {
		super.update();
		assignIcons();
	}


}