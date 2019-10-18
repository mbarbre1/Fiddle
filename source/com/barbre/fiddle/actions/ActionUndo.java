
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionUndo extends AbstractAction {

	/**
	 * Constructor for ActionUndo.
	 */
	public ActionUndo() {
		super("Undo");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.UNDO));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.UNDO, IconFactory.SIZE_16));

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(InputEvent.CTRL_MASK, KeyEvent.VK_Z));		
	}


	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (Fiddle.getInstance().getUndoManager().canUndo()) {
			Fiddle.getInstance().getUndoManager().undo();
		}
	}

}
