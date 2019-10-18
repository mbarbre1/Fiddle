
package com.barbre.fiddle;

import javax.swing.JMenuBar;

import com.barbre.fiddle.menus.EditMenu;
import com.barbre.fiddle.menus.FileMenu;
import com.barbre.fiddle.menus.HelpMenu;
import com.barbre.fiddle.menus.InterfaceMenu;
import com.barbre.fiddle.menus.ViewMenu;

/**
 * This is the main menu bar for the application.
 */
public class MainMenu extends JMenuBar {
	
	/**
	 * Constructor for MainMenu.
	 */
	public MainMenu() {
		super();
		add(new FileMenu());
		add(new EditMenu());
		add(new ViewMenu());
		add(new InterfaceMenu());
		add(new HelpMenu());
	}

}
