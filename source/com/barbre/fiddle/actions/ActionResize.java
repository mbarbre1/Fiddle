package com.barbre.fiddle.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.IconFactory;
import com.barbre.fiddle.widgets.utility.SelectionManager;

public class ActionResize extends AbstractAction implements StateEditable {

	public ActionResize() {
		super("Equalize size");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "Equalize size");
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		List selections = SelectionManager.getSelections();
		if (selections.size() <= 1)
			return;

		StateEdit edit = new StateEdit(this);
		Fiddle.getInstance().getUndoManager().addEdit(edit);

		BaseWidget root = (BaseWidget) selections.get(0);
		Dimension size = getDelta(root);
		Iterator i = selections.iterator();
		while (i.hasNext()) {
			BaseWidget element = (BaseWidget) i.next();
			resize(element, size);
		}

		edit.end();
	}

	/**
	 * Method getDelta.
	 * @param root
	 * @return Dimension
	 */
	public Dimension getDelta(BaseWidget root) {
		return root.getSize();
	}

	/**
	 * Method resize.
	 * @param target
	 * @param d
	 */
	public void resize(BaseWidget target, Dimension d) {
		target.setSize(d);
	}

	/**
	 * @see StateEditable#restoreState(Hashtable)
	 */
	public void restoreState(Hashtable state) {
		Iterator i = state.keySet().iterator();
		while (i.hasNext()) {
			BaseWidget element = (BaseWidget) i.next();
			element.setSize((Dimension) state.get(element));
		}
	}

	/**
	 * @see StateEditable#storeState(Hashtable)
	 */
	public void storeState(Hashtable state) {
		Iterator i = SelectionManager.getSelections().iterator();
		while (i.hasNext()) {
			BaseWidget element = (BaseWidget) i.next();
			state.put(element, element.getSize());
		}
	}

}