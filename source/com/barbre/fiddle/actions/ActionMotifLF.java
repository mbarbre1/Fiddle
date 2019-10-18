
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

public class ActionMotifLF extends AbstractAction {

	public ActionMotifLF() {
		super("Motif");
	}
		
	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(Fiddle.getInstance().getFrame());
			PropertiesLoader.setProperty(FiddleConstants.LOOK_AND_FEEL, MotifLookAndFeel.class.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
