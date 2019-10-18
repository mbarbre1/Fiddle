package com.barbre.fiddle;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import com.barbre.fiddle.elements.IPoint;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.ISize;

public class ResizeAndMoveMediator extends ComponentAdapter {
	private IScreenPiece thePiece = null;
	private Component theComponent = null;

	/**
	 * Method MovementManager.
	 * @param piece
	 * @param component
	 */
	public ResizeAndMoveMediator(IScreenPiece piece, Component component) {
		super();
		thePiece = piece;
		theComponent = component;
		theComponent.addComponentListener(this);
	}

	public void destroy() {
		theComponent.removeComponentListener(this);
	}		

	/**
	 * @see ComponentListener#componentMoved(ComponentEvent)
	 */
	public void componentMoved(ComponentEvent e) {
		if (e.getSource() == theComponent) {
			IPoint p = thePiece.getLocation();
			if (p != null) {
				int zoom = Utility.getZoom();
				p.setX(theComponent.getLocation().x / zoom);
				p.setY(theComponent.getLocation().y / zoom);
				Fiddle.getInstance().addDirty(thePiece.getFile());
			}
		}
	}

	/**
	 * @see ComponentListener#componentResized(ComponentEvent)
	 */
	public void componentResized(ComponentEvent e) {
		if (e.getSource() == theComponent) {
			ISize s = thePiece.getSize();
			if (s != null) {
				int zoom = Utility.getZoom();
				s.setCX(theComponent.getSize().width / zoom);
				s.setCY(theComponent.getSize().height / zoom);
				Fiddle.getInstance().addDirty(thePiece.getFile());
			}
		}
	}
}