package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import com.barbre.fiddle.decorators.BorderDecorator;
import com.barbre.fiddle.elements.IButtonDrawTemplate;
import com.barbre.fiddle.elements.ICombobox;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.ImageMediator;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

public class Combobox extends BaseWidget {
	private BorderDecorator borderDecorator;

	/**
	 * Constructor for Combobox.
	 */
	public Combobox(Component parent, IScreenPiece button) {
		super(parent, button);
		setHorizontalAlignment(LEFT);
		setText(getEqObject().getText());
		borderDecorator = new BorderDecorator(this, getEqObject(), getCombobox().getDrawTemplateObject());
	}

	/**
	 * Method assignIcons.
	 */
	private void assignIcons() {
		IButtonDrawTemplate t = getCombobox().getButtonObject();
		if (t == null)
			return;

		Image img = ImageMediator.getImage(t.getNormalObject());
		if (img != null) {
			setIcon(new ImageIcon(img));
		}

		img = ImageMediator.getImage(t.getPressedObject());
		if (img != null) {
			setPressedIcon(new ImageIcon(img));
		}

		img = ImageMediator.getImage(t.getFlybyObject());
		if (img != null) {
			setRolloverIcon(new ImageIcon(img));
		}

		img = ImageMediator.getImage(t.getDisabledObject());
		if (img != null) {
			setDisabledIcon(new ImageIcon(img));
		}
	}

	/**
	 * @see Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Dimension d = WidgetUtilities.getViewSize(getCombobox().getSize());
		if (d == null)
			d = getSize();
		if (getCombobox().getButton() != null) {
			Image img = ImageMediator.getImage(getCombobox().getButtonObject().getNormalObject());
			if (img != null) {
				int width = getCombobox().getSize() != null ? getCombobox().getSize().getCX() : getSize().width;
				g.drawImage(img, width - img.getWidth(this), 0, img.getWidth(this), d.height, this);
			}
		}
	}

	/**
	 * @see JComponent#paintBorder(Graphics)
	 */
	protected void paintBorder(Graphics g) {
		if (getCombobox().getStyle_Border()) {
			borderDecorator.paint(g);
		}
	}

	/**
	 * Method getCombobox.
	 * @return ICombobox
	 */
	private ICombobox getCombobox() {
		return (ICombobox) getEqObject();
	}

	/**
	 * @see com.barbre.fiddle.widgets.UpdateCapable#update()
	 */
	public void update() {
		super.update();
		assignIcons();
	}


}