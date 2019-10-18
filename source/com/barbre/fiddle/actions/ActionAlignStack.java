package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignStack extends ActionAlign {

	public ActionAlignStack() {
		super("Align Stack");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "stack");
	}

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		target.setLocation(endPoint.x, endPoint.y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */
	public Point getDelta(BaseWidget root) {
		return new Point(root.getLocation().x, root.getLocation().y);
	}

}