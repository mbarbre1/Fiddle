
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButtonMenuItem;

import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.elements.UIFileSet;

import com.barbre44.swing.ActionManager;

public class ActionSelectUI implements ActionListener {

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		JRadioButtonMenuItem item = (JRadioButtonMenuItem) evt.getSource();
		UIFileSet set = (UIFileSet) item.getClientProperty("ui");
		FileNavigationManager.getInstance().setFileSet(set);		
		ActionManager.get(ActionCloseAllScreens.class).actionPerformed(evt);
	}

}
