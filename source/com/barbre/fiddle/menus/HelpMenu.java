package com.barbre.fiddle.menus;

import javax.swing.JMenu;

import com.barbre.fiddle.actions.ActionAbout;
import com.barbre.fiddle.actions.ActionGarbageCollect;
import com.barbre.fiddle.actions.ActionReleaseNotes;
import com.barbre44.swing.ActionManager;

public class HelpMenu extends JMenu {

	/**
	 * Constructor for EditMenu.
	 */
	public HelpMenu() {
		super();
		setText("Help");
		setMnemonic('h');

		add(ActionManager.get(ActionGarbageCollect.class));
		addSeparator();
		add(ActionManager.get(ActionReleaseNotes.class));
		add(ActionManager.get(ActionAbout.class));
	}

}