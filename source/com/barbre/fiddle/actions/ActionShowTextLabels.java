package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.MainToolbar;

public class ActionShowTextLabels extends AbstractAction {
	private static final String SHOW_LABELS = "Do not show text labels";
	private static final String NO_SHOW_LABELS = "Show text labels";

	/**
	 * @see Object#Object()
	 */
	public ActionShowTextLabels() {
		super(NO_SHOW_LABELS);
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		MainToolbar bar = (MainToolbar) Fiddle.getInstance().getFrame().getToolbar();
		if (getValue(NAME).equals(NO_SHOW_LABELS)) {
			putValue(NAME, SHOW_LABELS);
			bar.setShowText(false);
		} else {
			putValue(NAME, NO_SHOW_LABELS);
			bar.setShowText(true);			
		}

	}

}