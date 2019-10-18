
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.MainToolbar;

public class ActionIconSize extends AbstractAction {
	private static final String LARGE = "Display small icons";
	private static final String SMALL = "Display large icons";

	public ActionIconSize() {
		super(LARGE);
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		MainToolbar bar = (MainToolbar) Fiddle.getInstance().getFrame().getToolbar();
		if (getValue(NAME).equals(LARGE)) {			
			bar.setLargeIcons(false);
			putValue(NAME, SMALL);
		} else {
			bar.setLargeIcons(true);
			putValue(NAME, LARGE);
		}
	}

}
