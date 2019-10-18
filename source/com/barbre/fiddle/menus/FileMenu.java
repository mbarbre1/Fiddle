package com.barbre.fiddle.menus;

import javax.swing.JMenu;

import com.barbre.fiddle.actions.ActionCloseAllScreens;
import com.barbre.fiddle.actions.ActionCloseScreen;
import com.barbre.fiddle.actions.ActionExit;
import com.barbre.fiddle.actions.ActionNewUI;
import com.barbre.fiddle.actions.ActionOpenScreen;
import com.barbre.fiddle.actions.ActionResetHomeLocation;
import com.barbre.fiddle.actions.ActionSaveFile;
import com.barbre.fiddle.widgets.utility.IconFactory;
import com.barbre44.swing.ActionManager;

public class FileMenu extends JMenu {

	/**
	 * Constructor for FileMenu.
	 */
	public FileMenu() {
		super();
		setText("File");
		setMnemonic('f');

		add(getNewMenu());
		add(ActionManager.get(ActionOpenScreen.class));
		addSeparator();
		add(ActionManager.get(ActionCloseScreen.class));
		add(ActionManager.get(ActionCloseAllScreens.class));
		addSeparator();
		add(ActionManager.get(ActionResetHomeLocation.class));
		addSeparator();		
		add(ActionManager.get(ActionSaveFile.class));
		addSeparator();				
		add(ActionManager.get(ActionExit.class));
	}

	/**
	 * Method getNewMenu.
	 * @return JMenu
	 */
	private JMenu getNewMenu() {
		JMenu menu = new JMenu("New");
		menu.setIcon(IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		menu.add(ActionManager.get(ActionNewUI.class));
		return menu;
	}
}