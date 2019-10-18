package com.barbre.fiddle;

import java.awt.Dimension;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDesktopPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.barbre.fiddle.widgets.InternalFrame;
import com.barbre.fiddle.widgets.utility.SelectionManager;

/**
 * This custom JDesktopPane ensures that components that activate are sent to the front.
 */
public class Desktop extends JDesktopPane implements InternalFrameListener, ContainerListener {

	/**
	 * Constructor for Desktop.
	 */
	public Desktop() {
		super();
		setDesktopManager(new DesktopManager(this));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				SelectionManager.clearSelections();
			}

		});
	}

	/**
	 * Method resizeDesktop.
	 * @param width
	 * @param height
	 */
	public void resizeDesktop(int width, int height) {
		Dimension d = getSize();
		d.width = Math.max(d.width, width);
		d.height = Math.max(d.height, height);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
	}

	/**
	 * A frame that activates is sent to front
	 */
	public void internalFrameActivated(InternalFrameEvent e) {
		e.getInternalFrame().toFront();
	}

	/**
	 * Not currently used.
	 * @see InternalFrameListener#internalFrameClosed(InternalFrameEvent)
	 */
	public void internalFrameClosed(InternalFrameEvent e) {
	}

	/**
	 * Not currently used.
	 * @see InternalFrameListener#internalFrameClosing(InternalFrameEvent)
	 */
	public void internalFrameClosing(InternalFrameEvent e) {
	}

	/**
	 * Not currently used.
	 * @see InternalFrameListener#internalFrameDeactivated(InternalFrameEvent)
	 */
	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	/**
	 * Not currently used.
	 * @see InternalFrameListener#internalFrameDeiconified(InternalFrameEvent)
	 */
	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	/**
	 * Not currently used.
	 * @see InternalFrameListener#internalFrameIconified(InternalFrameEvent)
	 */
	public void internalFrameIconified(InternalFrameEvent e) {
	}

	/**
	 * Not currently used.
	 * @see InternalFrameListener#internalFrameOpened(InternalFrameEvent)
	 */
	public void internalFrameOpened(InternalFrameEvent e) {
	}

	/**
	 * Add the component to this desktop listener
	 * @see ContainerListener#componentAdded(ContainerEvent)
	 */
	public void componentAdded(ContainerEvent e) {
		if (e.getComponent() instanceof InternalFrame) {
			((InternalFrame) e.getComponent()).addInternalFrameListener(this);
		}
	}

	/**
	 * Add the component to this desktop listener
	 * @see ContainerListener#componentRemoved(ContainerEvent)
	 */
	public void componentRemoved(ContainerEvent e) {
		if (e.getComponent() instanceof InternalFrame) {
			((InternalFrame) e.getComponent()).removeInternalFrameListener(this);
		}
	}

}