package com.barbre.fiddle;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.barbre.fiddle.widgets.utility.IconFactory;

/**
 * StatusBar.java
 */
public class StatusBar extends JPanel implements PropertyChangeListener, Runnable {
	private static final String BLANKS =
		"                                                                                                                                                                                                                                                                                                                                  ";
	private static final long MINUTE = 60000;
	private static final long PAUSE = 10000;
	private JLabel text;
	private LinkedList queue = new LinkedList();
	private Thread thread = null;
	private long sleep = PAUSE;

	/**
	 * @see java.lang.Object#Object()
	 */
	public StatusBar() {
		super();
		init();
		thread = new Thread(this);
		thread.start();
		queue.add("SIDL Fiddle version " + FiddleConstants.VERSION + ". This program is still in beta test.  Please backup your UI files before saving.");
	}

	/**
	 * Method init.
	 */
	private void init() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(BorderFactory.createEtchedBorder());
		text = new JLabel(IconFactory.getIcon(IconFactory.STATUS));
		text.setForeground(Color.black);
		add(text);
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		queue.add("" + evt.getNewValue());
		thread.interrupt();
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (1 == 1) {
			try {
				while (queue.size() > 0) {
					text.setText(format("" + queue.removeFirst()));
					doLayout();
					Rectangle r = text.getBounds();
					text.paintImmediately(0, 0, r.width, r.height);
				}
				Thread.sleep(sleep);
				fx();
			} catch (InterruptedException e) {
				sleep = PAUSE;
			}
		}
	}

	/**
	 * Special effects
	 */
	private void fx() {
		int len = text.getText().length();
		if (len < 1) {
			sleep = MINUTE;
		} else {
			sleep = 50;
			text.setText(text.getText().substring(1, len));

		}
	}

	private String format(String text) {
		return (text + BLANKS).substring(0, 200);
	}
}
