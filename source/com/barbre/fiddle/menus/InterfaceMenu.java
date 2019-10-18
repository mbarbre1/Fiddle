package com.barbre.fiddle.menus;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.actions.ActionSelectUI;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre.fiddle.io.loaders.UILoader;

public class InterfaceMenu extends JMenu implements PropertyChangeListener {
	private ButtonGroup group = new ButtonGroup();
	private ActionSelectUI action = new ActionSelectUI();

	/**
	 * Constructor for ViewMenu.
	 */
	public InterfaceMenu() {
		super();
		setText("Interface");
		setMnemonic('i');
		Fiddle.getInstance().getFileSetManager().addPropertyChangeListener(this);

		try {
			UIFileSet[] set = UILoader.getInstance().loadAllUI();
			for (int i = 0; i < set.length; i++) {
				Fiddle.getInstance().getFileSetManager().add(set[i]);
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "An error occurred loading the user inteface files.", "LOAD ERROR", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}


	/**
	 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == FiddleConstants.PROPERTY_FILESET_ADDED) {
			UIFileSet set = (UIFileSet) evt.getNewValue();
			String name = set.getName();
			name = name.substring(name.lastIndexOf(File.separatorChar)+1);
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(name);
			item.putClientProperty("ui", set);
			item.addActionListener(action);
			if (set.getName().equalsIgnoreCase("default"))
				item.doClick();
			group.add(item);
			add(item);
		}
		
		if (evt.getPropertyName() == FiddleConstants.PROPERTY_FILESET_REMOVED) {
			UIFileSet set = (UIFileSet) evt.getNewValue();
			Component[] c = getMenuComponents();
			for (int i = 0; i <c.length; i++) {
				JRadioButtonMenuItem item = (JRadioButtonMenuItem) c[i];
				if (item.getText().equals(set.getName())) {
					remove(item);
					return;
				}
			}
		}
	}

}