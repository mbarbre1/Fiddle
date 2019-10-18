package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.utility.IconFactory;


public class ActionGarbageCollect extends AbstractAction {

	/**
	 * Constructor for ActionGarbageCollect.
	 */
	public ActionGarbageCollect() {
		super("Garbage Collection");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
	}


	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.gc();
	}

}

