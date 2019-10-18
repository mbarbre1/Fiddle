package com.barbre.fiddle.widgets;

import java.awt.Component;

import com.barbre.fiddle.ElementWidgetMediator;
import com.barbre.fiddle.elements.IPage;
import com.barbre.fiddle.elements.IScreenPiece;

public class Page extends Screen {

	/**
	 * Constructor for Page.
	 * @param parent
	 * @param c
	 */
	public Page(Component parent, IScreenPiece c) {
		super(parent, c);
	}

	/**
	 * Method addPieces.
	 * @param si
	 */
	protected void addPieces() {
		removeAll();
		IScreenPiece[] nodes = ((IPage) getEqObject()).getPiecesObject();
		for (int i = nodes.length - 1; i >= 0; i--) {
			Component c = WidgetFactory.createWidget(this, nodes[i]);
			if (c != null) {
				add(c);
				new ElementWidgetMediator(nodes[i], c);
			}
		}
	}
	/**
	 * @see com.barbre.fiddle.widgets.UpdateCapable#update()
	 */
	public void update() {
		super.update();
		addPieces();
	}

}
