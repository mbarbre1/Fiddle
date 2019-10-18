
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionResetHomeLocation extends AbstractAction {

	/**
	 * @see Object#Object()
	 */
	public ActionResetHomeLocation() {
		super("Reset EQ Home directory...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.RESET_HOME));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.RESET_HOME, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */	
	public void actionPerformed(ActionEvent e) {
		String eqHome = System.getProperty(FiddleConstants.HOME);
		PropertiesLoader.setEQLocation(true);
		String newHome = System.getProperty(FiddleConstants.HOME);		
		if (eqHome.equals(newHome) == false) {
			JOptionPane.showMessageDialog(null, "You must restart the application.");
			Fiddle.getInstance().shutdown();
		}
	}

}
