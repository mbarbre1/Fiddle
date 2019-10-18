package com.barbre.fiddle.widgets;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;

import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.SelectionManager;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

import com.barbre44.util.Debug;

public abstract class BaseWidget extends JToggleButton implements UpdateCapable, ActionListener, PropertyChangeListener {
	private static final Insets NO_MARGIN = new Insets(0, 0, 0, 0);
	private IScreenPiece eqObject;

	/**
	 * Constructor for BaseWidget.
	 */
	public BaseWidget(Component parent, IScreenPiece c) {
		super();
		setEqObject(c);
		addActionListener(this);
		setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(false);
		setHorizontalTextPosition(CENTER);
		WidgetUtilities.configureWidget(parent, this, getEqObject());
		c.addPropertyChangeListener(this);
	}

	/**
	 * Gets the eqObject.
	 * @return Returns a IScreenPiece
	 */
	public IScreenPiece getEqObject() {
		return eqObject;
	}

	/**
	 * Sets the eqObject.
	 * @param eqObject The eqObject to set
	 */
	public void setEqObject(IScreenPiece eqObject) {
		this.eqObject = eqObject;
	}

	/**
	 * @see UpdateCapable#update()
	 */
	public void update() {
		if (getEqObject().getSize() != null)
			setSize(WidgetUtilities.getViewSize(getEqObject().getSize()));

// Since property change runs through here it updates wrong when dragging (before the drag 
// is posted to the bean.
//		if (getEqObject().getLocation() != null)
//			setLocation(WidgetUtilities.getViewLocation(getEqObject().getLocation()));

		if (getEqObject().getFont() != -1) {
			setFont(WidgetUtilities.getFont(getEqObject().getFont()));
		}

		WidgetUtilities.setWidgetColors(this, getEqObject());
		WidgetUtilities.setWidgetFont(this, getEqObject());
		setText(getEqObject().getText());
		repaint();
	}

	/**
	 * @see AbstractButton#getMargin()
	 */
	public Insets getMargin() {
		return NO_MARGIN;
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		checkSelection();
	}

	/**
	 * Method checkSelection.
	 */
	private void checkSelection() {
		if (isSelected()) {
			setBorder(BorderFactory.createLineBorder(Color.white));
			SelectionManager.addSelection(this);
		}
		setBorderPainted(isSelected());
		revalidate();
		repaint();
	}

	/**
	 * @see AbstractButton#setSelected(boolean)
	 */
	public void setSelected(boolean b) {
		super.setSelected(b);
		checkSelection();
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		Debug.println(BaseWidget.class, "Property name = " + evt.getPropertyName());
		update();
	}

//	/**
//	 * @see java.awt.Component#paint(Graphics)
//	 */
//	public void paintComponent(Graphics g) {
//		super.paint(g);
//		if (isSelected()) {
//			g.setColor(Color.white);
//			g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
//		}
//	}
//
//

}
