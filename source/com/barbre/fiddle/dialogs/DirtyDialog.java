package com.barbre.fiddle.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import com.barbre.fiddle.elements.UIFile;

public class DirtyDialog extends JPanel implements ListCellRenderer, MouseListener {
	private static DirtyDialog dialog = null;
	private JList dirty = null;
	private DefaultListModel model = null;

	/**
	 * @see Object#Object()
	 */
	private DirtyDialog() {
		super();
		init();
	}

	/**
	 * Method showDialog.
	 * @param dirtyFiles
	 * @return List
	 */
	public static List showDialog(List dirtyFiles) {
		if (dialog == null) {
			dialog = new DirtyDialog();
		}

		dialog.reset(dirtyFiles);
		if (JOptionPane.showConfirmDialog(null, dialog, "Save changes to checked items", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
			return null;
		return dialog.getItemsToSave();
	}

	/**
	 * Method init.
	 */
	private void init() {
		model = new DefaultListModel();
		dirty = new JList(model);
		dirty.setCellRenderer(this);
		dirty.addMouseListener(this);

		setLayout(new BorderLayout());
		add(new JScrollPane(dirty), BorderLayout.CENTER);
	}

	/**
	 * Method reset.
	 * @param dirtyFiles
	 */
	private void reset(List dirtyFiles) {
		model.clear();
		Iterator i = dirtyFiles.iterator();
		while (i.hasNext()) {
			UIFile element = (UIFile) i.next();
			JCheckBox box = new JCheckBox(element.getName());
			box.putClientProperty("file", element);
			box.setOpaque(false);
			model.addElement(box);
		}
	}

	/**
	 * Method getItemsToSave.
	 * @return List
	 */
	private List getItemsToSave() {
		List list = new ArrayList();
		Enumeration e = model.elements();
		while (e.hasMoreElements()) {
			JCheckBox element = (JCheckBox) e.nextElement();
			if (element.isSelected())
				list.add(element.getClientProperty("file"));
		}
		return list;
	}

	/**
	 * @see ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		return (Component) value;
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		JCheckBox box = (JCheckBox) dirty.getSelectedValue();
		box.setSelected(!box.isSelected());
		dirty.repaint();
	}

	/**
	 * @see MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

}