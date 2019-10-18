package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;

import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.BaseWidget;

/**
 * This object mediates between a screen piece from an XML file and the actual component
 * used to represent the piece.  This is where resizing and moving are manages.
 */
public class ElementWidgetMediator implements MouseListener, MouseMotionListener, StateEditable, ListSelectionListener {
	private Point cursorOffset = new Point();
	private static final int RESIZE_MARGIN = 2;
	private IScreenPiece thePiece = null;
	private Component theComponent = null;
	private boolean isDragging = false;
	private boolean isResizing = false;
	private StateEdit currentEdit;
	private int resizeCornerSize = 2;
	private ResizeAndMoveMediator resizeMediator;
	private MultipleSelectionDragger multipleSelectionDragger;
	

	/**
	 * Method PieceMediator.
	 * @param piece
	 * @param button
	 */
	public ElementWidgetMediator(IScreenPiece piece, Component button) {
		thePiece = piece;
		theComponent = button;
		attachListeners();
	}

	/**
	 * Add needed listeners
	 */
	private void attachListeners() {
		resizeMediator = new ResizeAndMoveMediator(thePiece, theComponent);
		theComponent.addMouseListener(this);
		theComponent.addMouseMotionListener(this);
		multipleSelectionDragger = new MultipleSelectionDragger(theComponent);
		FileNavigationManager.getInstance().getFileElementList().addListSelectionListener(this);
	}

	/**
	 * removed listeners for garbage collection
	 */
	public void destroy() {
		theComponent.removeMouseListener(this);
		theComponent.removeMouseMotionListener(this);
		FileNavigationManager.getInstance().getFileElementList().removeListSelectionListener(this);
		resizeMediator.destroy();
		multipleSelectionDragger.destroy();
	}

	/**
	 * If the user is dragging or resizing the object this method will perform the required
	 * actions to update the component and it's xml piece.
	 * @see MouseMotionListener#mouseDragged(MouseEvent)
	 */
	public void mouseDragged(MouseEvent evt) {
		if (isDragging) {
			Point translatedPoint = SwingUtilities.convertPoint(theComponent, evt.getPoint(), theComponent.getParent());
			translatedPoint.x -= cursorOffset.x;
			translatedPoint.y -= cursorOffset.y;
			theComponent.setLocation(translatedPoint);
			Fiddle.getInstance().addDirty(thePiece.getFile());
		}

		if (isResizing) {
			Point translatedPoint = SwingUtilities.convertPoint(theComponent, evt.getPoint(), theComponent.getParent());
			int type = theComponent.getCursor().getType();
			Dimension size = theComponent.getSize();
			Point location = theComponent.getLocation();
			switch (type) {
				case Cursor.NW_RESIZE_CURSOR :
					resizeWest(translatedPoint, size, location);
					resizeNorth(translatedPoint, size, location);
					location = translatedPoint;
					break;
				case Cursor.N_RESIZE_CURSOR :
					resizeNorth(translatedPoint, size, location);
					translatedPoint.x = location.x;
					location = translatedPoint;
					break;
				case Cursor.NE_RESIZE_CURSOR :
					resizeEast(evt, size);
					resizeNorth(translatedPoint, size, location);
					translatedPoint.x = location.x;
					location = translatedPoint;
					break;
				case Cursor.W_RESIZE_CURSOR :
					resizeWest(translatedPoint, size, location);
					translatedPoint.y = location.y;
					location = translatedPoint;
					break;
				case Cursor.E_RESIZE_CURSOR :
					resizeEast(evt, size);
					break;
				case Cursor.SW_RESIZE_CURSOR :
					resizeWest(translatedPoint, size, location);
					resizeSouth(evt, size);
					translatedPoint.y = location.y;
					location = translatedPoint;
					break;
				case Cursor.S_RESIZE_CURSOR :
					resizeSouth(evt, size);
					break;
				case Cursor.SE_RESIZE_CURSOR :
					resizeEast(evt, size);
					resizeSouth(evt, size);
					break;
				default :
					break;
			}
			theComponent.setSize(size);
			theComponent.setLocation(location);
			Fiddle.getInstance().addDirty(thePiece.getFile());
		}
	}

	/**
	 * Update the dimension
	 * @param evt
	 * @param size
	 */
	private void resizeSouth(MouseEvent evt, Dimension size) {
		size.height += (evt.getPoint().y - size.height);
	}

	/**
	 * Update the dimension
	 * @param evt
	 * @param size
	 */
	private void resizeEast(MouseEvent evt, Dimension size) {
		size.width += (evt.getPoint().x - size.width);
	}

	/**
	 * Update the dimension
	 * @param translatedPoint
	 * @param size
	 * @param location
	 */
	private void resizeNorth(Point translatedPoint, Dimension size, Point location) {
		size.height += (location.y - translatedPoint.y);
	}

	/**
	 * Update the dimension
	 * @param translatedPoint
	 * @param size
	 * @param location
	 */
	private void resizeWest(Point translatedPoint, Dimension size, Point location) {
		size.width += (location.x - translatedPoint.x);
	}

	/**
	 * Move the component and update cursors, etc.
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {
		if (isResizing)
			return;
		
		JComponent comp = (JComponent) theComponent;
		if (e.getX() <= RESIZE_MARGIN) {
			if (e.getY() < RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			else if (e.getY() > comp.getHeight() - RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			else
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
		} else if (e.getX() >= comp.getWidth() - RESIZE_MARGIN) {
			if (e.getY() < RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			else if (e.getY() > comp.getHeight() - RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			else
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		} else if (e.getY() <= RESIZE_MARGIN) {
			if (e.getX() < RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			else if (e.getX() > comp.getWidth() - RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			else
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
		} else if (e.getY() >= comp.getHeight() - RESIZE_MARGIN) {
			if (e.getX() < RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			else if (e.getX() > comp.getWidth() - RESIZE_MARGIN)
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			else
				comp.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
		} else {
			theComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Update the lists based on a selection of a component on the desktop
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent evt) {
		FileNavigationManager.getInstance().getFileComboBox().setSelectedItem(thePiece.getFile());
		FileNavigationManager.getInstance().getFileElementList().setSelectedValue(thePiece, true);
	
	}

	/**
	 * Not currently used
	 * @see MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent evt) {
	}

	/**
	 * Cursor reset when dragging/resizing is finished.
	 * @see MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent evt) {
		if ((isResizing || isDragging) == false)
			theComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * When the mouse is pressed this method will determine if it is in the resize bounds,
	 * and if so the cursor will change appropriately.
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent evt) {
		if (SwingUtilities.isRightMouseButton(evt)) {
			return;
		}

		if (evt.getSource() == theComponent) {
			boolean top = evt.getPoint().y <= RESIZE_MARGIN;
			boolean left = evt.getPoint().x <= RESIZE_MARGIN;
			boolean right = evt.getPoint().x >= theComponent.getSize().width - RESIZE_MARGIN;
			boolean bottom = evt.getPoint().y >= theComponent.getSize().height - RESIZE_MARGIN;
			currentEdit = new StateEdit(this);
			Fiddle.getInstance().getUndoManager().addEdit(currentEdit);
			if (top || left || right || bottom) {
				isResizing = true;
			} else {
				cursorOffset = evt.getPoint();
				isDragging = true;
				theComponent.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
		}
	}

	/**
	 * Turn off draggin/resizing and reset cursor.
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent evt) {
		if ((evt.getSource() == theComponent) && (isDragging || isResizing)) {
			isDragging = false;
			isResizing = false;
			currentEdit.end();
			currentEdit = null;
			theComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Operation to perform when an undo is selected by the user.
	 * @see StateEditable#restoreState(Hashtable)
	 */
	public void restoreState(Hashtable state) {
		Object o = state.get("bounds");
		if (o != null) {
			theComponent.setBounds((Rectangle) o);
		}
	}

	/**
	 * Operation performed when an action is taken by the user, for undo support.
	 * @see StateEditable#storeState(Hashtable)
	 */
	public void storeState(Hashtable state) {
		state.put("bounds", theComponent.getBounds());
	}

	/**
	 * @see javax.swing.event.ListSelectionListener#valueChanged(ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		updateSelection();
	}

	private void updateSelection() {
		if(FileNavigationManager.getInstance().getFileElementList().getSelectedValue() == thePiece)
			((BaseWidget)theComponent).setSelected(true);
	}


}