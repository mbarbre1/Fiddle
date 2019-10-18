
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.MessageManager;
import com.barbre.fiddle.ScreenWidgetMediator;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.widgets.InternalFrame;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionOpenScreen extends AbstractAction {

	/**
	 * Constructor for ActionOpenScreen.
	 */
	public ActionOpenScreen() {
		super("Open Screen");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.OPEN_UI));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.OPEN_UI, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = FileNavigationManager.getInstance().getFileElementList().getSelectedValue();
		
		if (o == null) {
			MessageManager.addMessage(this, "No screen has been selected.  Nothing to open.");
			return;
		}
		
		if ((o instanceof IScreen) == false) {
			MessageManager.addMessage(this, "Only screen objects can be opened.");
			return;
		}
		
		IScreen si = (IScreen) o;		
		InternalFrame f = new InternalFrame(si);
		Fiddle.getInstance().getFrame().getDesktop().add(f);
		new ScreenWidgetMediator(si, f);
	}

	

}
