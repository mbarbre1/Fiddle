package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.widgets.BaseWidget;
import com.barbre.fiddle.widgets.InternalFrame;
import com.barbre.fiddle.widgets.utility.SelectionManager;
import com.barbre44.swing.Rubberband;
import com.barbre44.util.Debug;

public class ScreenWidgetMediator implements MouseMotionListener, MouseListener, InternalFrameListener, StateEditable {
	private Rubberband rb;
	private Point cursorOffset = new Point();
	private IScreen theScreen = null;
	private InternalFrame theFrame = null;
	private boolean isDragging = false;
	private StateEdit currentEdit;

	/**
	 * Method ScreenMediator.
	 * @param screen
	 * @param frame
	 */
	public ScreenWidgetMediator(IScreen screen, InternalFrame frame) {
		super();
		theScreen = screen;
		theFrame = frame;
		rb = new Rubberband((JComponent) theFrame.getContentPane());		
		attachListeners();
	}

	/**
	 * Method attachListeners.
	 */
	private void attachListeners() {
		new ResizeAndMoveMediator(theScreen, theFrame);
		theFrame.addInternalFrameListener(this);
		theFrame.getContentPane().addMouseMotionListener(this);
		theFrame.getContentPane().addMouseListener(this);

		theFrame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				currentEdit = new StateEdit(ScreenWidgetMediator.this);
				Fiddle.getInstance().getUndoManager().addEdit(currentEdit);
			}

			public void mouseReleased(MouseEvent e) {
				currentEdit.end();
				currentEdit = null;
			}

		});
	}

	/**
	 * Method destroy.
	 */
	public void destroy() {
		//theFrame.removeComponentListener(this);
		theFrame.removeInternalFrameListener(this);
		theFrame.getContentPane().removeMouseMotionListener(this);
		theFrame.getContentPane().removeMouseListener(this);
	}

	/**
	 * @see MouseMotionListener#mouseDragged(MouseEvent)
	 */
	public void mouseDragged(MouseEvent evt) {
		if (isDragging) {
			Point translatedPoint = SwingUtilities.convertPoint(theFrame, evt.getPoint(), theFrame.getParent());
			translatedPoint.x -= cursorOffset.x;
			translatedPoint.y -= cursorOffset.y;
			theFrame.setLocation(translatedPoint);
			((DesktopManager) theFrame.getDesktopPane().getDesktopManager()).resizeDesktop();
			Fiddle.getInstance().addDirty(theScreen.getFile());
		}
	}

	/**
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */
	public void mouseMoved(MouseEvent evt) {
	}

	/**
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent evt) {
	}

	/**
	 * @see MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent evt) {
	}

	/**
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent evt) {
	}

	/**
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			Debug.println(this, "right click");
			rb.start(evt.getPoint());
			return;
		}
		
		currentEdit = new StateEdit(this);
		Fiddle.getInstance().getUndoManager().addEdit(currentEdit);

		if (evt.getSource() == theFrame.getContentPane()) {
			isDragging = true;
			cursorOffset.setLocation(evt.getPoint());			
			theFrame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
	}

	/**
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			rb.stop();
			Component[] components = rb.getContainedComponents();
			SelectionManager.clearSelections();
			SelectionManager.setAppendMode(true);
			for (int i = 0; i <components.length; i++) {
				if (components[i] instanceof BaseWidget) {
					Debug.println(this, components[i] + " selected.");
					((BaseWidget) components[i]).setSelected(true);
				}
			}
			SelectionManager.setAppendMode(false);			
			return;
		}			
		
		if ((evt.getSource() == theFrame.getContentPane()) && isDragging) {
			isDragging = false;
			currentEdit.end();
			currentEdit = null;
			theFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * @see InternalFrameListener#internalFrameActivated(InternalFrameEvent)
	 */
	public void internalFrameActivated(InternalFrameEvent evt) {
		FileNavigationManager.getInstance().getFileComboBox().setSelectedItem(theScreen.getFile());
		FileNavigationManager.getInstance().getFileElementList().setSelectedValue(theScreen, true);
	}

	/**
	 * @see InternalFrameListener#internalFrameClosed(InternalFrameEvent)
	 */
	public void internalFrameClosed(InternalFrameEvent evt) {
	}

	/**
	 * @see InternalFrameListener#internalFrameClosing(InternalFrameEvent)
	 */
	public void internalFrameClosing(InternalFrameEvent evt) {
		destroy();
	}

	/**
	 * @see InternalFrameListener#internalFrameDeactivated(InternalFrameEvent)
	 */
	public void internalFrameDeactivated(InternalFrameEvent evt) {
	}

	/**
	 * @see InternalFrameListener#internalFrameDeiconified(InternalFrameEvent)
	 */
	public void internalFrameDeiconified(InternalFrameEvent evt) {
	}

	/**
	 * @see InternalFrameListener#internalFrameIconified(InternalFrameEvent)
	 */
	public void internalFrameIconified(InternalFrameEvent evt) {
	}

	/**
	 * @see InternalFrameListener#internalFrameOpened(InternalFrameEvent)
	 */
	public void internalFrameOpened(InternalFrameEvent evt) {
	}

	/**
	 * @see StateEditable#restoreState(Hashtable)
	 */
	public void restoreState(Hashtable state) {
		Object o = state.get("bounds");
		if (o != null) {
			theFrame.setBounds((Rectangle) o);
		}
	}

	/**
	 * @see StateEditable#storeState(Hashtable)
	 */
	public void storeState(Hashtable state) {
		state.put("bounds", theFrame.getBounds());
	}

}