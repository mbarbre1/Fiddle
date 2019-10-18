package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignBottom extends ActionAlign {

	public ActionAlignBottom() {
		super("Align bottom");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "Bottom");
	}

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		int y = endPoint.y - target.getSize().height;
		int x = target.getLocation().x;
		target.setLocation(x, y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */
	public Point getDelta(BaseWidget root) {
		return new Point(-1, root.getLocation().y + root.getSize().height);
	}

}