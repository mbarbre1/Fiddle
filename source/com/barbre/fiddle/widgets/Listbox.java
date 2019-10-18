package com.barbre.fiddle.widgets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.barbre.fiddle.decorators.BorderDecorator;
import com.barbre.fiddle.elements.IListbox;
import com.barbre.fiddle.elements.IListboxColumn;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.widgets.utility.WidgetUtilities;

public class Listbox extends ImagePanel {

	/**
	 * Method Listbox.
	 * @param parent
	 * @param c
	 */
	public Listbox(Component parent, IScreenPiece c) {
		super(parent, c);
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		addColumns();
	}

	private void addColumns() {
		IListboxColumn[] cols = getListbox().getColumns();
		for (int i = 0; i < cols.length; i++) {
			ListboxColumn label = new ListboxColumn(this, cols[i]);
			add(label);
		}
	}

	class ListboxColumn extends JLabel {
		private BorderDecorator border;

		public ListboxColumn(Component parent, IListboxColumn column) {
			setOpaque(false);
			setText(column.getHeading());
			Dimension dim = new Dimension(column.getWidth(), getPreferredSize().height);
			setMinimumSize(dim);
			setMaximumSize(dim);
			setPreferredSize(dim);
			setFont(WidgetUtilities.getFont(getListbox().getFont()));
			if (getListbox().getTextColor() != null)
				setForeground(getListbox().getTextColor().getColor());
			border = new BorderDecorator((JComponent) parent,  getEqObject(), column.getHeaderObject());
		}

		/**
		 * @see JComponent#paintBorder(Graphics)
		 */
		protected void paintBorder(Graphics g) {
			border.paint(g);
		}
	}
	
	/**
	 * Method getListbox.
	 * @return IListbox
	 */
	private IListbox getListbox() {
		return (IListbox) getEqObject();
	}
}