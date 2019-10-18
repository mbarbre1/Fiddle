
package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionDeleteFile extends AbstractAction {

	/**
	 * Constructor for ActionDeleteFile.
	 */
	public ActionDeleteFile() {
		super("Delete File...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.DELETE_FILE));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.DELETE_FILE, IconFactory.SIZE_16));
	}


	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		UIFile selected = (UIFile) FileNavigationManager.getInstance().getFileComboBox().getSelectedItem();
		if (selected == null)
			return;
		
		if (selected.getSet().getName().equals("default")) {
			JOptionPane.showMessageDialog(null, "Can not delete default UI files", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		String path = selected.getSet().getDirectory() + File.separator + selected.getName();
		new File(path).delete();
		selected.getSet().getFiles().remove(selected);
	}

}
