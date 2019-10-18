package com.barbre.fiddle.menus;

import javax.swing.JMenu;

import com.barbre.fiddle.actions.ActionAdd;
import com.barbre.fiddle.actions.ActionAlignBottom;
import com.barbre.fiddle.actions.ActionAlignHorizontalCenter;
import com.barbre.fiddle.actions.ActionAlignLeft;
import com.barbre.fiddle.actions.ActionAlignRight;
import com.barbre.fiddle.actions.ActionAlignStack;
import com.barbre.fiddle.actions.ActionAlignTop;
import com.barbre.fiddle.actions.ActionAlignVerticalCenter;
import com.barbre.fiddle.actions.ActionRedo;
import com.barbre.fiddle.actions.ActionResize;
import com.barbre.fiddle.actions.ActionUndo;
import com.barbre.fiddle.widgets.utility.IconFactory;
import com.barbre44.swing.ActionManager;

public class EditMenu extends JMenu {

	/**
	 * Constructor for EditMenu.
	 */
	public EditMenu() {
		super();
		setText("Edit");
		setMnemonic('e');

		add(ActionManager.get(ActionUndo.class));
		add(ActionManager.get(ActionRedo.class));
		addSeparator();
		add(ActionManager.get(ActionAdd.class));
		addSeparator();		
		add(getAlignMenu());
		add(ActionManager.get(ActionResize.class));

	}

	private JMenu getAlignMenu() {
		JMenu lf = new JMenu("Alignment");
		lf.setIcon(IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		lf.add(ActionManager.get(ActionAlignLeft.class));
		lf.add(ActionManager.get(ActionAlignHorizontalCenter.class));
		lf.add(ActionManager.get(ActionAlignRight.class));
		lf.add(ActionManager.get(ActionAlignTop.class));
		lf.add(ActionManager.get(ActionAlignVerticalCenter.class));
		lf.add(ActionManager.get(ActionAlignBottom.class));
		lf.add(ActionManager.get(ActionAlignStack.class));
		return lf;
	}
}