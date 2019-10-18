package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionCloseAllScreens extends AbstractAction {

	/**
	 * Constructor for ActionCloseScreen.
	 */
	public ActionCloseAllScreens() {
		super("Close All Screens");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.CLOSE_ALL));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.CLOSE_ALL, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JInternalFrame[] frames = Fiddle.getInstance().getFrame().getDesktop().getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			Fiddle.getInstance().getFrame().getDesktop().remove(frames[i]);
		}
		Fiddle.getInstance().getFrame().getDesktop().repaint();
	}

}