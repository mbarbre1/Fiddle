
package com.barbre.fiddle.actions;

import java.awt.Point;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAlignTop extends ActionAlign {

	public ActionAlignTop() {
		super("Align top");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION ,"Top");
	}		

	/**
	 * @see ActionAlign#alignObject(BaseWidget, Point)
	 */
	public void alignObject(BaseWidget target, Point endPoint) {
		int x = target.getLocation().x;		
		int y = endPoint.y;
		target.setLocation(x,y);
	}

	/**
	 * @see ActionAlign#getDelta(BaseWidget)
	 */	
	public Point getDelta(BaseWidget root) {
		int y = root.getLocation().y;
		return new Point(-1, y);
	}

}
