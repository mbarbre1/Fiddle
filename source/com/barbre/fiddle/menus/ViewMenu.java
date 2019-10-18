package com.barbre.fiddle.menus;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import com.barbre.fiddle.actions.ActionBackgroundImage;
import com.barbre.fiddle.actions.ActionFlushGraphics;
import com.barbre.fiddle.actions.ActionGammaCorrection;
import com.barbre.fiddle.actions.ActionMetalLF;
import com.barbre.fiddle.actions.ActionMotifLF;
import com.barbre.fiddle.actions.ActionSystemLF;
import com.barbre.fiddle.actions.ActionZoom;
import com.barbre.fiddle.widgets.utility.IconFactory;
import com.barbre44.swing.ActionManager;

public class ViewMenu extends JMenu {

	/**
	 * Constructor for ViewMenu.
	 */
	public ViewMenu() {
		super();
		setText("View");
		setMnemonic('v');

		add(ActionManager.get(ActionZoom.class));
		addSeparator();
		add(ActionManager.get(ActionBackgroundImage.class));
		add(ActionManager.get(ActionGammaCorrection.class));
		add(ActionManager.get(ActionFlushGraphics.class));
		add(getLookAndFeelMenu());
	}

	/**
	 * Method getLookAndFeelMenu.
	 * @return JMenu
	 */
	private JMenu getLookAndFeelMenu() {
		JMenu lf = new JMenu("Look and Feel");
		lf.setIcon(IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		ButtonGroup group = new ButtonGroup();
		lf.add(createLFMenuItem(group, ActionMetalLF.class));
		lf.add(createLFMenuItem(group, ActionSystemLF.class));
		lf.add(createLFMenuItem(group, ActionMotifLF.class));
		return lf;
	}

	/**
	 * Method createLFMenuItem.
	 * @param group
	 * @param lfAction
	 * @return JRadioButtonMenuItem
	 */
	private JRadioButtonMenuItem createLFMenuItem(ButtonGroup group, Class lfAction) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(ActionManager.get(lfAction));
		group.add(item);
		return item;
	}
}