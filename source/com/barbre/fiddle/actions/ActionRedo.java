
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionRedo extends AbstractAction {

	/**
	 * Constructor for ActionUndo.
	 */
	public ActionRedo() {
		super("Redo");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.REDO));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.REDO, IconFactory.SIZE_16));
	}


	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (Fiddle.getInstance().getUndoManager().canRedo()) {
			Fiddle.getInstance().getUndoManager().redo();
		}
	}


}
