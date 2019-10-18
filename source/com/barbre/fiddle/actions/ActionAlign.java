package com.barbre.fiddle.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.utility.SelectionManager;

public abstract class ActionAlign extends AbstractAction implements StateEditable {

	public ActionAlign(String name) {
		super(name);
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
		Point endPoint = getDelta(root);
		Iterator i = selections.iterator();
		while (i.hasNext()) {
			BaseWidget element = (BaseWidget) i.next();
			alignObject(element, endPoint);
		}

		edit.end();
	}

	public abstract Point getDelta(BaseWidget root);

	public abstract void alignObject(BaseWidget target, Point endPoint);

	/**
	 * @see StateEditable#restoreState(Hashtable)
	 */
	public void restoreState(Hashtable state) {
		Iterator i = state.keySet().iterator();
		while (i.hasNext()) {
			BaseWidget element = (BaseWidget) i.next();
			element.setLocation((Point) state.get(element));
		}
	}

	/**
	 * @see StateEditable#storeState(Hashtable)
	 */
	public void storeState(Hashtable state) {
		Iterator i = SelectionManager.getSelections().iterator();
		while (i.hasNext()) {
			BaseWidget element = (BaseWidget) i.next();
			state.put(element, element.getLocation());
		}
	}

}