package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import com.barbre.fiddle.elements.IButton;
import com.barbre.fiddle.elements.IButtonDrawTemplate;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.ImageMediator;
import com.barbre44.util.Debug;

public class Button extends BaseWidget {

	/**
	 * Method Button.
	 * @param parent
	 * @param button
	 */
	public Button(Component parent, IScreenPiece button) {
		super(parent, button);
		setText(getEqObject().getText());
		Debug.println(this, "text=" + getText());
		assignIcons();
	}

	/**
	 * Method assignIcons.
	 */
	private void assignIcons() {
		IButtonDrawTemplate t = getButton().getButtonDrawTemplate();

		Image img = ImageMediator.getImage(t.getNormalObject());
		if (img != null) {
			setIcon(new SizedImageIcon(img));
		}

		img = ImageMediator.getImage(t.getPressedObject());
		if (img != null) {
			setPressedIcon(new SizedImageIcon(img));
		}

		img = ImageMediator.getImage(t.getFlybyObject());
		if (img != null) {
			setRolloverIcon(new SizedImageIcon(img));
		}

		img = ImageMediator.getImage(t.getDisabledObject());
		if (img != null) {
			setDisabledIcon(new SizedImageIcon(img));
		}
	}

	/**
	 * @see Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Image img = ImageMediator.getImage(getButton().getButtonDrawTemplate().getNormalDecalObject());
		if (img != null) {
			g.drawImage(img, getButton().getDecalOffset().getX(), getButton().getDecalOffset().getY(), this);
		}
	}

	/**
	 * Method getButton.
	 * @return IButton
	 */
	private IButton getButton() {
		return (IButton) getEqObject();
	}

	/**
	 * @see com.barbre.fiddle.widgets.UpdateCapable#update()
	 */
	public void update() {
		super.update();
		assignIcons();
	}


}