/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package com.barbre.fiddle.browser.sun.beanbuilder.property;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Customizer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.Stack;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.barbre.fiddle.browser.sun.beanbuilder.util.StatusBar;

/**
 * The UI for listing, sorting, and setting component
 * properties.
 *
 * @version 1.25 02/27/02
 * @author  John J. Walker
 * @author  Mark Davidson
 */
public class PropertyPane extends JPanel implements ActionListener, TableModelListener, PropertyChangeListener {

	private Object bean; // Current Bean.
	private Stack beanStack; // Stack of beans for walking bean hierarchy.

	private JTable table;
	private PropertyColumnModel columnModel;
	private PropertyTableModel tableModel;

	// UI for the property control panel.
	private JLabel nameLabel;

	private JButton up;
	private JButton down;
	private JButton add;
	private JButton cust;

	private static final int ROW_HEIGHT = 20;

	// View options.
	private static final String[] VIEW_CHOICES = { "All", "Standard", "Expert", "Read Only", "Bound", "Constrained", "Hidden", "Preferred" };

	private Insets buttonInsets = new Insets(0, 0, 0, 0);

	/**
	 * Constructor
	 */
	public PropertyPane() {
		super(new BorderLayout());

		tableModel = new PropertyTableModel();
		tableModel.addTableModelListener(this);

		columnModel = new PropertyColumnModel();
		table = new JTable(tableModel, columnModel);
		table.setRowHeight(ROW_HEIGHT);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				if (evt.getClickCount() == 2 && table.getSelectedColumn() == 0)
					// Double clicking on the first column will call the down
					// action on the current object.
					handleDownAction();
			}
		});

		add(new JScrollPane(table), BorderLayout.CENTER);
		add(createNamePanel(), BorderLayout.SOUTH);
	}

	/** 
	 * Handler for UI interactions.
	 */
	public void actionPerformed(ActionEvent evt) {
		Object obj = evt.getSource();

		if (obj instanceof JButton) {
			if (obj == up) {
				handleUpAction();
			} else if (obj == down) {
				handleDownAction();
			} else if (obj == add) {
				handleAddAction();
			} else if (obj == cust) {
				handleCustomizerAction();
			}
		}

	}

	/** 
	 * Configures the button according to the Action.
	 * These properties are added by default in CommonMenuBar 
	 * and CommonToolBar. See the comparable methods in those classes
	 * for details.
	 */
	private void configureButton(JButton button, Action action) {
		button.setAction(action);
		// identification key for ActionManager
		button.setName((String) action.getValue(Action.NAME));
		button.setText("");
		button.setAlignmentY(0.5f);
		button.setMargin(buttonInsets);
		button.addActionListener(this);
		button.addMouseListener(StatusBar.getInstance());
	}

	/** 
	 * The name panel shows the current selected item
	 */
	private JPanel createNamePanel() {
		JLabel label = new JLabel("Properties for: ");
		nameLabel = new JLabel("< Current Object >");

		JPanel panel = new JPanel();
		FlowLayout layout = (FlowLayout) panel.getLayout();
		layout.setAlignment(FlowLayout.LEFT);

		panel.add(label);
		panel.add(nameLabel);

		return panel;
	}

	/** 
	 * Handler for the down action
	 */
	public void handleDownAction() {
		int index = table.getSelectedRow();
		if (index != -1) {
			Object obj = tableModel.getValueAt(index, PropertyTableModel.COL_VALUE);

			if (beanStack == null) {
				beanStack = new Stack();
			}
			beanStack.push(bean);
			setBean(obj);
		}
		setButtonState();
	}

	/** 
	 * Handler for the up action
	 */
	public void handleUpAction() {
		if (beanStack != null && !beanStack.empty()) {
			setBean(beanStack.pop());
		}
		setButtonState();
	}

	/** 
	 * Handle the add gesture. Informs prop change listener to add the selected
	 * current property sheet component.
	 */
	public void handleAddAction() {
		int index = table.getSelectedRow();
		if (index != -1) {
			Object obj = tableModel.getValueAt(index, PropertyTableModel.COL_VALUE);

			if (obj != null && !(obj instanceof Component)) {
				String message = obj.getClass().getName();
				message += " sent to design panel";
				StatusBar.getInstance().setMessage(message);
				ObjectHolder.getInstance().add(obj);
			}
		}
	}

	/** 
	 * Handle the customizer action. Will display a customizer in a dialog
	 */
	public void handleCustomizerAction() {
		Component comp = tableModel.getCustomizer();

		if (comp != null) {
			CustomizerDialog dlg = new CustomizerDialog(comp);
			dlg.setVisible(true);
		}
	}

	/** 
	 * A customizer dialog which takes a Component which implements the 
	 * customizer interface.
	 */
	private class CustomizerDialog extends JDialog implements ActionListener {

		public CustomizerDialog(Component comp) {
			super(new JFrame(), "Customizer Dialog");

			Customizer customizer = (Customizer) comp;
			customizer.setObject(bean);

			JPanel okpanel = new JPanel();
			//            okpanel.add(CommonUI.createButton(CommonUI.BUTTONTEXT_OK, this,
			//                                               CommonUI.MNEMONIC_OK));
			Container pane = getContentPane();
			pane.add(comp, BorderLayout.CENTER);
			pane.add(okpanel, BorderLayout.SOUTH);
			pack();

			//            CommonUI.centerComponent(this, PropertyPane.this);
		}

		public void actionPerformed(ActionEvent evt) {
			this.dispose();
		}

	}

	/** 
	 * Sets the state of the up and down buttons based on the contents of the stack.
	 */
	private void setButtonState() {

	}

	/** 
	 * Sets the PropertyPane to show the properties of the named bean.
	 */
	protected void setBean(Object bean) {
		this.bean = bean;

		if (bean != null) {
			nameLabel.setText(bean.getClass().getName());

			tableModel.setObject(bean);
		}
		
	
	}

	//
	// Table Model Listener methods
	//
	public void tableChanged(TableModelEvent evt) {
		// Adjust the preferred height of the row to the the same as
		// the property editor.
		// This seems to be necessary or the # rows doesn't change
		table.setRowHeight(ROW_HEIGHT);

		PropertyEditor editor;
		Component comp;
		Dimension prefSize;

		for (int i = 0; i < table.getRowCount(); i++) {
			editor = tableModel.getPropertyEditor(i);
			if (editor != null) {
				comp = editor.getCustomEditor();
				if (comp != null) {
					prefSize = comp.getPreferredSize();
					if (prefSize.height != table.getRowHeight(i)) {
						table.setRowHeight(i, Math.max(table.getRowHeight(i), prefSize.height));
					}
				}
			}
		}
	}

	//
	// PropertyChangeListener method
	// 

	public void propertyChange(PropertyChangeEvent evt) {
		Object source = evt.getSource();
		String prop = evt.getPropertyName();
		Object newValue = evt.getNewValue();

		if (source == ObjectHolder.getInstance()) {
			if (prop.equals("selectedItem")) {
				// Sets the selected item for the property sheet.
				beanStack = null; // Reset the bean stack.

				if (newValue != null) {
					setBean(newValue);
					setButtonState();
				}
			}
		}
	}
	/**
	 * Returns the down.
	 * @return JButton
	 */
	public JButton getDown() {
		return down;
	}


	/**
	 * Returns the up.
	 * @return JButton
	 */
	public JButton getUp() {
		return up;
	}


	/**
	 * Sets the down.
	 * @param down The down to set
	 */
	public void setDown(JButton down) {
		if (this.down != null)
			this.down.removeActionListener(this);
		this.down = down;
		if (this.down != null)
			this.down.addActionListener(this);
	}


	/**
	 * Sets the up.
	 * @param up The up to set
	 */
	public void setUp(JButton up) {
		if (this.up != null)
			this.up.removeActionListener(this);
		this.up = up;
		if (this.up != null)
			this.up.addActionListener(this);
	}


}
