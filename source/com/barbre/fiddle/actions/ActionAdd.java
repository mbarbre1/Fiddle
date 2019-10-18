package com.barbre.fiddle.actions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.io.utility.BasicControlManager;
import com.barbre.fiddle.widgets.utility.IconFactory;

/**
 * ActionAdd.java
 */
public class ActionAdd extends AbstractAction implements ListCellRenderer {
	private static JComboBox SCREEN_PIECES = null; 
	private static final Dimension LABEL_SIZE = new Dimension(125, 21);
	private static final Insets INSETS = new Insets(5, 5, 5, 5);
	private static final String UNNAMED = "ScreenPiece_";
	private static final DefaultListCellRenderer DEFAULT_RENDERER = new DefaultListCellRenderer();
	private static int COUNTER = 1;

	/**
	 * @see java.lang.Object#Object()
	 */
	public ActionAdd() {
		super("Add screen piece...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (SCREEN_PIECES == null) {
			SCREEN_PIECES = new JComboBox(BasicControlManager.getInstance().getTypes().toArray());
		}
		
		INode selected = (INode) FileNavigationManager.getInstance().getFileComboBox().getSelectedItem();
		if (selected == null) {
			return;
		}

		JTextField name = new JTextField(25);
		JComboBox toScreen = getScreenCombo();
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(addRow("Element type", SCREEN_PIECES));
		p.add(addRow("Add to screen", toScreen));
		p.add(addRow("Name", name));

		int answer = JOptionPane.showConfirmDialog(null, p, "Add element to " + selected.getFile().getName(), JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			if (name.getText().trim().length() == 0)
				name.setText(UNNAMED + (COUNTER++));

			
			Object o = BasicControlManager.getInstance().createType((String) SCREEN_PIECES.getSelectedItem(), selected);
			((IScreenPiece) o).setitem(name.getText());
			if (o != null) {
				selected.getFile().getSet().getTypeManager().add((IClass) o);
			}	
			FileNavigationManager.getInstance().getFileElementList().setFile(selected.getFile());
			selected.addChild((INode)o);

			if (toScreen.getSelectedItem() instanceof IScreen) {
				IScreen item = (IScreen) toScreen.getSelectedItem();
				int len = item.getPieces().length;
				String[] newPieces = new String[len + 1];
				for (int i = 0; i < len; i++) {
					newPieces[i] = item.getPieces(i);
				}
				newPieces[len] = name.getText();
				item.setPieces(newPieces);
			}
			Fiddle.getInstance().addDirty(selected.getFile());
		}
	}

	/**
	 * Method addRow.
	 * @param label
	 * @param comp
	 * @return JPanel
	 */
	private JPanel addRow(String label, JComponent comp) {
		JPanel p = new JPanel() {
			public Insets getInsets() {
				return INSETS;
			}
		};
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

		JLabel l = new JLabel(label);
		l.setForeground(Color.black);
		l.setOpaque(false);
		l.setMinimumSize(LABEL_SIZE);
		l.setMaximumSize(LABEL_SIZE);
		l.setPreferredSize(LABEL_SIZE);

		p.add(l);
		p.add(comp);

		return p;
	}

	/**
	 * Method getScreenCombo.
	 * @return JComboBox
	 */
	private JComboBox getScreenCombo() {
		JComboBox screens = new JComboBox();
		screens.setRenderer(this);
		screens.addItem("Do not add to screen");

		ListModel model = FileNavigationManager.getInstance().getFileElementList().getModel();
		int size = model.getSize();
		for (int i = 0; i < size; i++) {
			INode n = (INode) model.getElementAt(i);
			if (n instanceof IScreen)
				screens.addItem(((IScreen) n));
		}
		return screens;
	}

	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = (JLabel) DEFAULT_RENDERER.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value instanceof IScreen)
			label.setText(((IScreen) value).getitem());
		return label;
	}

}
