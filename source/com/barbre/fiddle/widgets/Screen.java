package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.barbre.fiddle.ElementWidgetMediator;
import com.barbre.fiddle.elements.IScreen;
import com.barbre.fiddle.elements.IScreenPiece;

public class Screen extends ImagePanel implements UpdateCapable {
	private List mediators = new ArrayList();

	/**
	 * @see com.barbre.fiddle.widgets.BaseWidget#BaseWidget(Component, IScreenPiece)
	 */
	public Screen(Component parent, IScreenPiece c) {
		super(parent, c);
		setLayout(null);
		addPieces();
	}

	/**
	 * Method addPieces.
	 * @param si
	 */
	protected void addPieces() {
		IScreenPiece[] nodes = ((IScreen) getEqObject()).getPiecesObject();
		if (nodes == null)
			return;
		for (int i = nodes.length - 1; i >= 0; i--) {
			Component c = WidgetFactory.createWidget(this, nodes[i]);
			if (c != null) {
				add(c);
				mediators.add(new ElementWidgetMediator(nodes[i], c));
			}
		}
	}

	/**
	 * @see UpdateCapable#update()
	 */
	public void update() {
		Iterator i = mediators.iterator();
		while (i.hasNext()) {
			ElementWidgetMediator element = (ElementWidgetMediator) i.next();
			element.destroy();
		}

		removeAll();
		addPieces();
		//		super.update();
		//		Component[] c = getComponents();
		//		for (int i = 0; i < c.length; i++) {
		//			if (c[i] instanceof UpdateCapable) {
		//				((UpdateCapable) c[i]).update();
		//			}
		//		}
	}

}