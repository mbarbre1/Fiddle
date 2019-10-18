package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

import com.barbre.fiddle.widgets.Screen;
import com.barbre.fiddle.widgets.utility.SelectionManager;
import com.barbre44.util.Debug;

/**
 * Description of class
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>Feb 7, 2003  Created class.
 *
 * </ul><p>
 */
public final class MultipleSelectionDragger extends MouseAdapter implements MouseMotionListener, StateEditable {
	private Component theComponent;
	private Component primaryObject;
	private Point primaryObjectAtStartOfDrag;
	private Map selections;
	private StateEdit currentEdit;

	public MultipleSelectionDragger(Component c) {
		super();
		theComponent = c;
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
	}

	public void destroy() {
		theComponent.removeMouseListener(this);
		theComponent.removeMouseMotionListener(this);
	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		Point delta = primaryObject.getLocation();
		delta.x -= primaryObjectAtStartOfDrag.x;
		delta.y -= primaryObjectAtStartOfDrag.y;
		primaryObjectAtStartOfDrag = primaryObject.getLocation();

		Iterator i = SelectionManager.getSelections().iterator();
		while (i.hasNext()) {
			Component element = (Component) i.next();
			if ((element != primaryObject) && ((element instanceof Screen) == false)) {
				startUndoEdit();
				Point p = element.getLocation();
				p.x += delta.x;
				p.y += delta.y;
				element.setLocation(p);
			}
		}
	}

	/**
	 * Method startUndoEdit.
	 * @param element
	 */
	private void startUndoEdit() {
		if (selections != null)
			return;

		// this sequence of undo setup is used so that StateEdit will not 
		// incorrectly throw out the edit.  It will try to do this if the 
		// selections variable isn't reinitialized after currentEdit is
		// instantiated. This is because the maps.equals method thinks they
		// are the same even though the point has changed.  In this case,
		// StateEdit considers it redundant and throws the whole thing away.
		// This code is a "trick" to ensure that the object references are 
		// unique inside StateEdit.
		selections = new HashMap();
		currentEdit = new StateEdit(this);
		Fiddle.getInstance().getUndoManager().addEdit(currentEdit);
		selections = new HashMap();

		Iterator i = SelectionManager.getSelections().iterator();
		while (i.hasNext()) {
			Component element = (Component) i.next();
			if ((element != primaryObject) && ((element instanceof Screen) == false)) {
				Point p = new Point(element.getLocation().x, element.getLocation().y);
				selections.put(element, p);
			}
		}

	}

	/**
	 * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		primaryObject = (Component) e.getSource();
		primaryObjectAtStartOfDrag = primaryObject.getLocation();
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		if (currentEdit != null) {
			Debug.println(this, "mouse released.  Edit ending.");
			currentEdit.end();
			currentEdit = null;
			selections = null;
		}
	}

	/**
	 * Operation to perform when an undo is selected by the user.
	 * @see StateEditable#restoreState(Hashtable)
	 */
	public void restoreState(Hashtable state) {
		Map map = (Map) state.get("multibounds");
		Debug.println(this, "restoring from " + map + ".  State = " + state);
		if (map != null) {
			Iterator i = map.keySet().iterator();
			while (i.hasNext()) {
				Component element = (Component) i.next();
				Point p = (Point) map.get(element);
				element.setLocation(p.x, p.y);
			}
		}
	}

	/**
	 * Operation performed when an action is taken by the user, for undo support.
	 * @see StateEditable#storeState(Hashtable)
	 */
	public void storeState(Hashtable state) {
		state.put("multibounds", selections);
		Debug.println(this, "storing " + selections + " items.  State = " + state);
	}

}
