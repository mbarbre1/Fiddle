
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionExit extends AbstractAction {

	/**
	 * Constructor for ActionExit.
	 */
	public ActionExit() {
		super("Exit");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EXIT));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EXIT, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Fiddle.getInstance().shutdown();
	}

}
