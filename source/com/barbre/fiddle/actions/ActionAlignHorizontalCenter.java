package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignHorizontalCenter extends ActionAlign {

	public ActionAlignHorizontalCenter() {
		super("Align horizontal center");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "Horizontal center");
	}

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		int x = endPoint.x - (target.getSize().width / 2);
		int y = target.getLocation().y;
		target.setLocation(x, y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */
	public Point getDelta(BaseWidget root) {
		int x = root.getLocation().x + (root.getSize().width / 2);
		return new Point(x, -1);
	}

}