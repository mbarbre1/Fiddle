package com.barbre.fiddle.widgets.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre44.util.Debug;

/**
 * SelectionManager.java
 */
public final class SelectionManager {
	public static final ModifierMouseListener MODIFIER_LISTENER = new ModifierMouseListener();
	private static boolean busy = false;
	private static boolean appendMode = false;
	private static List selectedObjects = new ArrayList();

	/**
	 * Method addSelection.
	 * @param w
	 * @param append
	 */
	public static void addSelection(BaseWidget w) {
		if (busy) {
			Debug.println(null, "busy");
			return;
		}
		busy = true;
		Debug.println(null, "Selecting " + w);
		boolean append = MODIFIER_LISTENER.isControlDown() | MODIFIER_LISTENER.isShiftDown();
		if (!append && !isAppendMode()) {
			clearSelections();
		}
		if (selectedObjects.contains(w) == false)
			selectedObjects.add(w);

		busy = false;
	}
	/**
	 * Method clearSelections.
	 */
	public static void clearSelections() {
		Debug.println(null, "Clearing selections");
		ListIterator i = selectedObjects.listIterator();
		while (i.hasNext()) {
			((BaseWidget) i.next()).setSelected(false);
			i.remove();
		}
	}
	/**
	 * Method getSelections.
	 * @return List
	 */
	public static List getSelections() {
		return selectedObjects;
	}

	/**
	 * ActionEvent bug workaround.
	 * Bug# 4026963
	 */
	private static class ModifierMouseListener implements MouseListener {
		private MouseEvent event = null;

		private ModifierMouseListener() {}
		public void mousePressed(MouseEvent e) {
			event = e;
		}
		public void mouseReleased(MouseEvent e) {
			event = e;
		}
		public void mouseEntered(MouseEvent e) {
			event = e;
		}
		public void mouseExited(MouseEvent e) {
			event = e;
		}
		public void mouseClicked(MouseEvent e) {
			event = e;
		}
		public boolean isShiftDown() {
			if (event != null)			
				return event.isShiftDown();
			return false;
		}
		public boolean isControlDown() {
			if (event != null)
				return event.isControlDown();
			return false;
		}
	}
	/**
	 * Returns the appendMode.
	 * @return boolean
	 */
	public static boolean isAppendMode() {
		return appendMode;
	}


	/**
	 * Sets the appendMode.
	 * @param appendMode The appendMode to set
	 */
	public static void setAppendMode(boolean appendMode) {
		SelectionManager.appendMode = appendMode;
	}


}
