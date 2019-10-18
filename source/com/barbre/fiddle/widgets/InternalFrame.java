package com.barbre.fiddle.widgets;

import java.awt.Graphics;

import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.barbre.fiddle.decorators.BackgroundDecorator;
import com.barbre.fiddle.decorators.BorderDecorator;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

public class InternalFrame extends JInternalFrame implements UpdateCapable {
	private IScreen screen = null;
	private BorderDecorator borderDecorator;
	private BackgroundDecorator backgroundDecorator;

	/**
	 * Method InternalFrame.
	 * @param si
	 */
	public InternalFrame(IScreen si) {
		super();
		screen = si;

		setContentPane(new Screen(this, screen));
		borderDecorator = new BorderDecorator(this,  screen, screen.getDrawTemplateObject());
		update();
		setLocation(WidgetUtilities.getViewLocation(si.getLocation()));
//		setLocation(0,0);
		setIconifiable(true);
		setResizable(true);
		setOpaque(false);
		removeTitleBar();
		setVisible(true);
	}

	/**
	 * @see UpdateCapable#update()
	 */
	public void update() {
		if (screen != null)
			setSize(WidgetUtilities.getViewSize(screen.getSize()));		
		((UpdateCapable) getContentPane()).update();
	}

	/**
	 * Method removeTitleBar.
	 */
	private void removeTitleBar() {
		((BasicInternalFrameUI) getUI()).setNorthPane(null);
	}

	/**
	 * Method getScreen.
	 * @return IScreen
	 */
	public IScreen getScreen() {
		return screen;
	}


	/**
	 * @see JComponent#paintBorder(Graphics)
	 */
	protected void paintBorder(Graphics g) {
		if (screen.getStyle_Border()) {
			borderDecorator.paint(g);
		}
	}

	
	/**
	 * @see java.awt.Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
	}


}