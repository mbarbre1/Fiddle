package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.io.loaders.UILoader;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionSaveFile extends AbstractAction {

	public ActionSaveFile() {
		super("Save");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.SAVE));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.SAVE, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		UIFile file = (UIFile) FileNavigationManager.getInstance().getFileComboBox().getSelectedItem();

		if (file != null) {
			UILoader.getInstance().saveUI(file);
			Fiddle.getInstance().getDirtyFiles().remove(file);
		}
	}

}