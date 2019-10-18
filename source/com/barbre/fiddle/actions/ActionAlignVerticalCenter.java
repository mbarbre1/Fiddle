package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignVerticalCenter extends ActionAlign {

	public ActionAlignVerticalCenter() {
		super("Align vertical center");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "Vertical center");
	}

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		int y = endPoint.y - (target.getSize().height/ 2);
		int x = target.getLocation().x;
		target.setLocation(x, y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */
	public Point getDelta(BaseWidget root) {
		int y = root.getLocation().y + (root.getSize().height / 2);
		return new Point(-1, y);
	}

}