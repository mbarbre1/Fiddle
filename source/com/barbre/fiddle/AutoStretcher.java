package com.barbre.fiddle;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.barbre.fiddle.elements.IScreenPiece;

/**
 * SIDL provides the ability for objects to stretch based on the edges of the window.
 * Typically, this is used to allow objects to resize when a window resizes.<p>
 * This class manages the resizing of auto-stretch components.
 */
public class AutoStretcher extends ComponentAdapter {
	private IScreenPiece control;
	private Component myComponent;
	private int zoom;

	/**
	 * Creates an instance connecting the provided component to its parent.  
	 * @param control A screen piece that might be autostretched.
	 * @param c The component to be stretched
	 * @param p Parent of the component
	 */
	public AutoStretcher(IScreenPiece control, Component c, Component p) {
		super();
		this.control = control;
		myComponent = c;
		p.addComponentListener(this);
	}

	/**
	 * Stretches the component if autoStretch is true
	 * @param parent Parent of the component to be stretched.
	 */
	public void autoStretch(Component parent) {
		if (control.getAutoStretch()) {
			zoom = Integer.parseInt(System.getProperty(FiddleConstants.ZOOM, "1"));
			Dimension parentSize = parent.getSize();
			Point parentLoc = new Point(0, 0);
			Rectangle newbounds = new Rectangle(0, 0, parentSize.width, parentSize.height);
			topAnchorToTop(parentSize, parentLoc, newbounds);
			leftAnchorToLeft(parentSize, parentLoc, newbounds);
			bottomAnchorToTop(parentSize, parentLoc, newbounds);
			rightAnchorToLeft(parentSize, parentLoc, newbounds);
			myComponent.setBounds(newbounds);
		}
	}

	/**
	 * Extrapolate zoom for value.
	 * @param value
	 * @return int
	 */
	private int getZoomValue(int value) {
		return Utility.getZoom(value);
	}

	/**
	 * If true, keep the right side of this window a fixed offset away from its parent's 
	 * left. Else, keep it a fixed offset away from its parent's right.
	 */
	private void rightAnchorToLeft(Dimension parentSize, Point parentLoc, Rectangle newbounds) {
		if (control.getRightAnchorToLeft()) {
			newbounds.width = parentLoc.x + getZoomValue(control.getRightAnchorOffset()) - newbounds.x;
		} else {
			newbounds.width = parentSize.width - getZoomValue(control.getRightAnchorOffset()) - newbounds.x;
		}
	}

	/**
	 * If true, keep the bottom side of this window a fixed offset away from its parent's 
	 * top. Else, keep it a fixed offset away from its parent's bottom.
	 */
	private void bottomAnchorToTop(Dimension parentSize, Point parentLoc, Rectangle newbounds) {
		if (control.getBottomAnchorToTop()) {
			newbounds.height = parentLoc.y + getZoomValue(control.getBottomAnchorOffset()) - newbounds.y;
		} else {
			newbounds.height = parentSize.height - getZoomValue(control.getBottomAnchorOffset()) - newbounds.y;
		}
	}

	/**
	 * If true, keep the left side of this window a fixed offset away from its parent's 
	 * left. Else, keep it a fixed offset away from its parent's right.
	 */
	private void leftAnchorToLeft(Dimension parentSize, Point parentLoc, Rectangle newbounds) {
		if (control.getLeftAnchorToLeft()) {
			newbounds.x = parentLoc.x + getZoomValue(control.getLeftAnchorOffset());
		} else {
			newbounds.x = parentSize.width - getZoomValue(control.getLeftAnchorOffset());
		}
	}

	/**
	 * If true, keep the top side of this window a fixed offset away from its parent's 
	 * top. Else, keep it a fixed offset away from its parent's bottom.
	 */
	private void topAnchorToTop(Dimension parentSize, Point parentLoc, Rectangle newbounds) {
		if (control.getTopAnchorToTop()) {
			newbounds.y += parentLoc.y + getZoomValue(control.getTopAnchorOffset());
		} else {
			newbounds.y = parentSize.height - getZoomValue(control.getTopAnchorOffset());
		}
	}

	/**
	 * Autostretch when the parent component is resized.
	 * @see ComponentListener#componentResized(ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		autoStretch(e.getComponent());
	}

}