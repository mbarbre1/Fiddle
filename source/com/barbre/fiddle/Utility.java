package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public final class Utility {

	/**
	 * Method center.
	 * @param c
	 * @return Point
	 */
	public static final Point center(Component c) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension compSize = c.getSize();
		Point point = new Point();
		point.x = (screenSize.width - compSize.width) / 2;
		point.y = (screenSize.height - compSize.height) / 2;
		return point;
	}

	/**
	 * Method getZoom.
	 * @param value
	 * @return int
	 */
	public static final int getZoom(int value) {
		return value * getZoom();
	}

	/**
	 * Method getZoom.
	 * @return int
	 * @throws NumberFormatException
	 */
	public static int getZoom() throws NumberFormatException {
		int zoom = Integer.parseInt(System.getProperty(FiddleConstants.ZOOM, "1"));
		return zoom;
	}

	public static void setSplitPaneDividerLocation(final JSplitPane p, final int loc) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				p.setDividerLocation(loc);
			}
		});
	}

	public static void setSplitPaneDividerLocation(final JSplitPane p, final String loc) {
		int value = 100;
		if (loc != null)
			value = Integer.parseInt(loc);
		setSplitPaneDividerLocation(p, value);
	}

}