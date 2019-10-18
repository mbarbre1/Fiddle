
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.dialogs.AboutBox;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionAbout extends AbstractAction {

	public ActionAbout() {
		super("About...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
	}
	
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		new AboutBox().showDialog();
	}

}
