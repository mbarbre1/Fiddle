package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.barbre.fiddle.actions.ActionCloseAllScreens;
import com.barbre.fiddle.actions.ActionCloseScreen;
import com.barbre.fiddle.actions.ActionCopyFile;
import com.barbre.fiddle.actions.ActionDeleteFile;
import com.barbre.fiddle.actions.ActionExit;
import com.barbre.fiddle.actions.ActionIconSize;
import com.barbre.fiddle.actions.ActionNewUI;
import com.barbre.fiddle.actions.ActionOpenScreen;
import com.barbre.fiddle.actions.ActionRedo;
import com.barbre.fiddle.actions.ActionResetHomeLocation;
import com.barbre.fiddle.actions.ActionSaveFile;
import com.barbre.fiddle.actions.ActionShowTextLabels;
import com.barbre.fiddle.actions.ActionUndo;
import com.barbre.fiddle.actions.ActionZoom;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre44.lang.StringUtil;
import com.barbre44.swing.ActionManager;

public class MainToolbar extends JToolBar {
	private static final Dimension SEPARATOR_DIMENSION = new Dimension(20, 1);
	private boolean largeIcons = true;

	/**
	 * @see Object#Object()
	 */
	public MainToolbar() {
		super();
		add(ActionManager.get(ActionOpenScreen.class)).setToolTipText("Open a screen");
		add(ActionManager.get(ActionCloseScreen.class)).setToolTipText("Close a screen");
		add(ActionManager.get(ActionCloseAllScreens.class)).setToolTipText("Close all screens");
		add(ActionManager.get(ActionZoom.class)).setToolTipText("Zoom");		
		addSeparator(SEPARATOR_DIMENSION);

		add(ActionManager.get(ActionSaveFile.class)).setToolTipText("Save the selected XML file");
		add(ActionManager.get(ActionNewUI.class)).setToolTipText("Create a new Interface");
		add(ActionManager.get(ActionCopyFile.class)).setToolTipText("Copy the selected XML file");
		add(ActionManager.get(ActionDeleteFile.class)).setToolTipText("Delete the selected XML file");
		addSeparator(SEPARATOR_DIMENSION);

		add(ActionManager.get(ActionUndo.class)).setToolTipText("Undo");
		add(ActionManager.get(ActionRedo.class)).setToolTipText("Redo");
		addSeparator(SEPARATOR_DIMENSION);

		add(ActionManager.get(ActionResetHomeLocation.class)).setToolTipText("Reset home directory");
		addSeparator(SEPARATOR_DIMENSION);

		add(ActionManager.get(ActionExit.class)).setToolTipText("Exit");

		createPopup();
	}

	/**
	 * @see JToolBar#add(Action)
	 */
	public JButton add(Action a) {
		JButton b = super.add(a);
		b.setBorderPainted(false);
		b.setMargin(new Insets(0, 0, 0, 0));
		return b;
	}

	/**
	 * Method setLargeIcons.
	 * @param b
	 */
	public void setLargeIcons(boolean b) {
		PropertiesLoader.setProperty(FiddleConstants.LARGE_ICON, StringUtil.boolean2String(b));
		largeIcons = b;
		String type = (b) ? FiddleConstants.LARGE_ICON : Action.SMALL_ICON;
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof JButton) {
				JButton button = (JButton) c[i];
				button.setIcon((Icon) button.getAction().getValue(type));
			}
		}
	}

	/**
	 * Method setShowText.
	 * @param b
	 */
	public void setShowText(boolean b) {
		PropertiesLoader.setProperty(FiddleConstants.ICON_TEXT, StringUtil.boolean2String(b));
		Component[] c = getComponents();
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof JButton) {
				JButton button = (JButton) c[i];
				if (!b) {
					button.setText(null);
				} else {
					button.setText((String) button.getAction().getValue(Action.SHORT_DESCRIPTION));
				}
			}
		}
	}

	/**
	 * Method isLargeIcons.
	 * @return boolean
	 */
	public boolean isLargeIcons() {
		return largeIcons;
	}

	/**
	 * Method createPopup.
	 * @return JPopupMenu
	 */
	private JPopupMenu createPopup() {
		final JPopupMenu menu = new JPopupMenu();
		menu.add(ActionManager.get(ActionIconSize.class));
		menu.add(ActionManager.get(ActionShowTextLabels.class));
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					Component comp = (Component) e.getSource();
					SwingUtilities.convertPointToScreen(e.getPoint(), comp);
					menu.show(comp, e.getPoint().x, e.getPoint().y);
				}
			}
		});

		return menu;
	}

}
