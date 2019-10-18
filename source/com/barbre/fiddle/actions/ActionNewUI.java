package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre.fiddle.io.loaders.UILoader;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionNewUI extends AbstractAction {

	public ActionNewUI() {
		super("Create new User Interface...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.NEW_UI));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.NEW_UI, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		String name = JOptionPane.showInputDialog("User Interface Name");
		if (name == null)
			return;

		if (!UILoader.createUIFolder(name)) {
			JOptionPane.showMessageDialog(null, "User Interface not created.", "ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String dir = System.getProperty(FiddleConstants.HOME);
		try {
			UIFileSet set = UILoader.getInstance().loadUI(UILoader.DEFAULT_FILE_SET, dir, name);
			Fiddle.getInstance().getFileSetManager().add(set);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "An error occurred loading the user inteface files.", "LOAD ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}