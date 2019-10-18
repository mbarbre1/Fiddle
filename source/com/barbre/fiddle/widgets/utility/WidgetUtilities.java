package com.barbre.fiddle.widgets.utility;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import com.barbre.fiddle.AutoStretcher;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.elements.IPoint;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.ISize;
import com.barbre44.util.Debug;

public final class WidgetUtilities {
	
	
	public static Font FONT = new Font("Arial", Font.PLAIN, 7);

	/**
	 * Method getFont.
	 * @param size
	 * @return Font
	 */
	public static final Font getFont(int size) {
		int zoom = Integer.parseInt(System.getProperty(FiddleConstants.ZOOM, "1"));
		float fSize = (FONT.getSize() + size) * zoom;
		Font f = FONT.deriveFont(fSize);
		Debug.println(null, "font = " + f);
		return f;
	}

	/**
	 * Method setLocationAndSize.
	 * @param component
	 * @param control
	 */
	public static void setLocationAndSize(Component component, IScreenPiece control) {
		IPoint p = control.getLocation();
		if (p != null)
			component.setLocation(getViewLocation(p));

		ISize s = control.getSize();
		if (s != null)
			component.setSize(getViewSize(s));
	}

	public static Dimension getViewSize(ISize s) {
		if (s == null)
			return null;

		int zoom = Integer.parseInt(System.getProperty(FiddleConstants.ZOOM, "1"));
		int width = s.getCX() * zoom;
		int height = s.getCY() * zoom;
		return new Dimension(width, height);
	}

	public static Point getViewLocation(IPoint s) {
		int zoom = Integer.parseInt(System.getProperty(FiddleConstants.ZOOM, "1"));
		int x = s.getX() * zoom;
		int y = s.getY() * zoom;
		return new Point(x, y);
	}

	

	/**
	 * Configure common elements
	 */
	public static final void configureWidget(Component parent, Component comp, IScreenPiece eqObject) {
		AutoStretcher autoStretcher = new AutoStretcher(eqObject, comp, parent);
		WidgetUtilities.setLocationAndSize(comp, eqObject);

		setWidgetColors(comp, eqObject);
		setWidgetFont(comp, eqObject);
		autoStretcher.autoStretch(parent);
		comp.addMouseListener(SelectionManager.MODIFIER_LISTENER); // AWT workaround
	}

	/**
	 * Method setWidgetFont.
	 * @param comp
	 * @param eqObject
	 */
	public static void setWidgetFont(Component comp, IScreenPiece eqObject) {
		comp.setFont(WidgetUtilities.getFont(eqObject.getFont()));
	}

	/**
	 * Method setWidgetColors.
	 * @param comp
	 * @param eqObject
	 */
	public static void setWidgetColors(Component comp, IScreenPiece eqObject) {
		if (eqObject.getTextColor() != null) {
			Color c = eqObject.getTextColor().getColor();
			Debug.println(null, eqObject.getitem() + " TextColor = " + c + ", alpha = " + c.getAlpha());
			comp.setForeground(c);
		} else {
			Debug.println(null, "TextColor is null for " + eqObject.getitem());
		}
	}

}