package com.barbre.fiddle;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class DesktopManager extends DefaultDesktopManager {
	private Desktop desktop;

	/**
	 * Constructor for DesktopManager.
	 */
	public DesktopManager(Desktop desktop) {
		super();
		this.desktop = desktop;
	}

	/**
	 * @see javax.swing.DesktopManager#endDraggingFrame(JComponent)
	 */
	public void endDraggingFrame(JComponent f) {
		super.endDraggingFrame(f);
		resizeDesktop();
	}

	/**
	 * @see javax.swing.DesktopManager#endResizingFrame(JComponent)
	 */
	public void endResizingFrame(JComponent f) {
		super.endResizingFrame(f);
		resizeDesktop();
	}

	/**
	 * Method getScrollPane.
	 * @return JScrollPane
	 */
	private JScrollPane getScrollPane() {
		if (desktop.getParent() instanceof JViewport) {
			JViewport viewport = (JViewport) desktop.getParent();
			if (viewport.getParent() instanceof JScrollPane)
				return (JScrollPane) viewport.getParent();
		}
		return null;
	}

	public void resizeDesktop() {
		int width = 0;
		int height = 0;
		JScrollPane scroller = getScrollPane();

		if (scroller != null) {
			JInternalFrame[] frames = desktop.getAllFrames();
			for (int i = 0; i < frames.length; i++) {
				width = Math.max(width, frames[i].getX() + frames[i].getWidth());
				height = Math.max(height, frames[i].getY() + frames[i].getHeight());
			}
			desktop.resizeDesktop(width, height);
		}
		scroller.revalidate();
	}
}
