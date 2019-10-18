package com.barbre.fiddle.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.barbre.fiddle.decorators.BorderDecorator;
import com.barbre.fiddle.elements.IPage;
import com.barbre.fiddle.elements.IScreenPiece;
import com.barbre.fiddle.elements.ITabBox;
import com.barbre.fiddle.widgets.utility.*;

public class TabBox extends ImagePanel implements ActionListener {
	private BorderDecorator borderDecorator;
	private Map loadedPages = new HashMap();

	/**
	 * Method TabBox.
	 * @param parent
	 * @param c
	 */
	public TabBox(Component parent, IScreenPiece c) {
		super(parent, c);
		setLayout(new BorderLayout(0, 0));

		if (getBox().getDrawTemplate() != null) {
			borderDecorator = new BorderDecorator(this, getEqObject(), getBox().getDrawTemplateObject());
		}
		addPages();
	}

	/**
	 * Method addPages.
	 */
	private void addPages() {
		JPanel tabPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		tabPane.setOpaque(false);
		add(tabPane, BorderLayout.NORTH);

		IPage[] pages = getBox().getPagesObject();
		for (int i = 0; i < pages.length; i++) {
			Page page = new Page(this, pages[i]);
			Tab tab = new Tab(pages[i]);
			tab.addActionListener(this);
			tab.page = page;
			tabPane.add(tab);
			loadedPages.put(tab, page);
		}
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Tab tab = (Tab) e.getSource();
		if (getComponentCount() == 2)
			remove(1);
		add((Component) loadedPages.get(tab), BorderLayout.CENTER);
		doLayout();
	}

	/**
	 * Method getBox.
	 * @return ITabBox
	 */
	private ITabBox getBox() {
		return (ITabBox) getEqObject();
	}

	/**
	 * 
	 * A tab
	 * 
	 */
	private class Tab extends JButton {
		private BorderDecorator borderDecorator;
		public Page page;

		/**
		 * Method Tab.
		 * @param p
		 */
		public Tab(IPage p) {
			super();
			setOpaque(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setText(p.getTabText());
			if (p.getTabTextColor() != null)
				setForeground(p.getTabTextColor().getColor());
			if (p.getTabIcon() != null)
				setIcon(new SizedImageIcon(ImageMediator.getImage(p.getTabIconObject())));
			setFont(WidgetUtilities.getFont(getBox().getFont()));

			if (getBox().getTabBorderTemplate() != null)
				borderDecorator = new BorderDecorator(this, getEqObject(), getBox().getTabBorderTemplateObject());
		}

		/**
		 * @see JComponent#paintBorder(Graphics)
		 */
		protected void paintBorder(Graphics g) {
			if (getBox().getTabBorderTemplate() != null) {
				borderDecorator.paint(g);
			}
		}

	}
}