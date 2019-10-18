
package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignLeft extends ActionAlign {

	public ActionAlignLeft() {
		super("Align left");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION ,"Left");
	}		

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		int x = endPoint.x;		
		int y = target.getLocation().y;
		target.setLocation(x,y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */	
	public Point getDelta(BaseWidget root) {
		int x = root.getLocation().x;
		return new Point(x, -1);
	}

}
