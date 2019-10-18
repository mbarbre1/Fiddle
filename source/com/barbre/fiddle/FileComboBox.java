package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Insets;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.barbre.fiddle.elements.UIFile;

/**
 * This is a custom JList for displaying the names of the EQ xml files.
 */
public class FileComboBox extends JComboBox implements ListCellRenderer, Comparator {
	private static final Insets INSETS = new Insets(5, 5, 5, 5);
	private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private JLabel label = null;
	private boolean adjusting = false;

	/**
	 * @see Object#Object()
	 */
	public FileComboBox() {
		super(new DefaultComboBoxModel());
		setRenderer(this);
	}

	/**
	 * @see ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		String text = "<File list>";
		if (value != null)
			text = ((UIFile) value).getName();
		label.setToolTipText(text);

		try {
			label.setText(text.substring(5, text.indexOf('.')));
		} catch (StringIndexOutOfBoundsException e) {
			label.setText(text);
		}
		return label;
	}

	/**
	 * Clears whatever is in the list currently and adds the files to the list.
	 * @param files
	 */
	public void setFiles(UIFile[] files) {
		setAdjusting(true);
		String dir = files[0].getSet().getDirectory() + File.separator;
		Arrays.sort(files, this);
		DefaultComboBoxModel model = (DefaultComboBoxModel) getModel();
		model.removeAllElements();
		for (int i = 0; i < files.length; i++) {
			if (new File(dir + files[i].getName()).exists())
				model.addElement(files[i]);
		}
		setAdjusting(false);
	}

	/**
	 * Returns the adjusting.
	 * @return boolean
	 */
	public boolean isAdjusting() {
		return adjusting;
	}

	/**
	 * Sets the adjusting.
	 * @param adjusting The adjusting to set
	 */
	public void setAdjusting(boolean adjusting) {
		this.adjusting = adjusting;
	}

	/**
	 * @see java.awt.Container#getInsets()
	 */
	public Insets getInsets() {
		return INSETS;
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		UIFile u1 = (UIFile) o1;
		UIFile u2 = (UIFile) o2;
		return u1.getName().compareTo(u2.getName());
	}

}