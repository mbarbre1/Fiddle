package com.barbre.fiddle.actions;

import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.UpdateCapable;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionZoom extends AbstractAction implements StateEditable {

	/**
	 * Constructor for ActionZoom.
	 */
	public ActionZoom() {
		super("Zoom...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.ZOOM));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.ZOOM, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		int zoom = Integer.parseInt(System.getProperty(FiddleConstants.ZOOM, "1"));
		JSlider s = new JSlider(JSlider.HORIZONTAL, 1, 10, zoom);
		s.setSnapToTicks(true);
		s.setPaintLabels(true);
		s.setLabelTable(s.createStandardLabels(1));
		JOptionPane.showMessageDialog(null, s, "Select zoom multiplier", JOptionPane.QUESTION_MESSAGE);
		StateEdit edit = new StateEdit(this);
		Fiddle.getInstance().getUndoManager().addEdit(edit);
		System.setProperty(FiddleConstants.ZOOM, "" + s.getValue());
		edit.end();
		resizeOpenItems();
	}

	/**
	 * Method resizeOpenItems.
	 */
	private void resizeOpenItems() {
		JInternalFrame[] frames = Fiddle.getInstance().getFrame().getDesktop().getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] instanceof UpdateCapable) {
				((UpdateCapable)frames[i]).update();
			}
		}
	}

	/**
	 * @see StateEditable#restoreState(Hashtable)
	 */
	public void restoreState(Hashtable state) {
		Object o = state.get(FiddleConstants.ZOOM);
		if (o != null)
			System.setProperty(FiddleConstants.ZOOM, "" + o);
		resizeOpenItems();
	}

	/**
	 * @see StateEditable#storeState(Hashtable)
	 */
	public void storeState(Hashtable state) {
		state.put(FiddleConstants.ZOOM, System.getProperty(FiddleConstants.ZOOM, "1"));
	}

}