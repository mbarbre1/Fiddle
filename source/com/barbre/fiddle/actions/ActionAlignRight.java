package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignRight extends ActionAlign {

	public ActionAlignRight() {
		super("Align right");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "Right");
	}

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		int x = endPoint.x - target.getSize().width;
		int y = target.getLocation().y;
		target.setLocation(x, y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */
	public Point getDelta(BaseWidget root) {
		return new Point(root.getLocation().x + root.getSize().width, -1);
	}

}