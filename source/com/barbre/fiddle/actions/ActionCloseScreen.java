
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.MessageManager;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.widgets.InternalFrame;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionCloseScreen extends AbstractAction {

	/**
	 * Constructor for ActionCloseScreen.
	 */
	public ActionCloseScreen() {
		super("Close Screen");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.CLOSE_SELECTED));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.CLOSE_SELECTED, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = FileNavigationManager.getInstance().getFileElementList().getSelectedValue();
		
		if (o == null) {
			MessageManager.addMessage(this, "No screen has been selected.  Nothing to close.");
			return;
		}
		
		if ((o instanceof IScreen) == false) {
			MessageManager.addMessage(this, "Only screen objects can be closed.");
			return;
		}
		
		IScreen si = (IScreen) o;		

		
		JInternalFrame[] frames = Fiddle.getInstance().getFrame().getDesktop().getAllFrames();
		for (int i=0; i<frames.length; i++) {
			if (((InternalFrame)frames[i]).getScreen() == si) {
				Fiddle.getInstance().getFrame().getDesktop().remove(frames[i]);
				Fiddle.getInstance().getFrame().getDesktop().repaint();
				break;
			}
		}
	}

}
