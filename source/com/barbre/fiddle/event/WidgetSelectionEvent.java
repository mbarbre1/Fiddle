package com.barbre.fiddle.event;

import java.util.EventObject;

import com.barbre.fiddle.widgets.BaseWidget;

/**
 * Description of class
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Feb 10, 2003  Created class.
 *
 * </ul><p>
 */
public class WidgetSelectionEvent extends EventObject {	

	/**
	 * Constructor for WidgetSelectionEvent.
	 * @param source
	 */
	public WidgetSelectionEvent(Object source) {
		super(source);
	}

	public boolean isSelected() {
		return ((BaseWidget)getSource()).isSelected();
	}
}

