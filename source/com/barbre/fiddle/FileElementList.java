package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.INode;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.elements.UIFile;
import com.barbre44.util.Debug;

public class FileElementList extends JList implements ListCellRenderer, Comparator {
	private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private UIFile theFile = null;

	/**
	 * @see Object#Object()
	 */
	public FileElementList() {
		super(new DefaultListModel());
		setCellRenderer(this);
	}

	/**
	 * @see ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JLabel label = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		label.setText(((IClass) value).getitem());
		int style = Font.PLAIN;
		if (value instanceof IScreen) {
			style = Font.BOLD;
		}
		label.setFont(label.getFont().deriveFont(style));
		return label;
	}

	/**
	 * Method setFile.
	 * @param file
	 */
	public void setFile(UIFile file) {
		Debug.println(this, "setFile");
		theFile = file;
		DefaultListModel model = (DefaultListModel) getModel();
		model.clear();

		if (file == null)
			return;

		INode[] nodes = theFile.getChildren();
		Arrays.sort(nodes, this);
		for (int i = 0; i < nodes.length; i++) {
			Debug.println(this, "setFile node list.  nodes[" + i + "]=" + nodes[i]);
			if (nodes[i] instanceof IClass) {
				model.addElement((IClass) nodes[i]);
			}
		}
	}

	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		IClass n1 = (IClass) o1;
		IClass n2 = (IClass) o2;		
		return n1.getitem().compareTo(n2.getitem());
	}

}
