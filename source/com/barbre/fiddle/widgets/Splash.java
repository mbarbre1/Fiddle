package com.barbre.fiddle.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.Utility;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public final class Splash extends JWindow {
	private Font font = new Font("arial", Font.BOLD, 18);
	private String version = "version " + FiddleConstants.VERSION;
	private Color color = new Color(255, 255, 255);

	/**
	 * Constructor for Splash.
	 */
	public Splash() {
		super();
		Icon icon = new ImageIcon(ImageMediator.getImage(FiddleConstants.SPLASH_IMAGE));
		JLabel label = new JLabel(icon);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(label, BorderLayout.CENTER);
		((JComponent) getContentPane()).setBorder(BorderFactory.createLineBorder(Color.black));
		pack();
		setLocation(Utility.center(this));
	}

	/**
	 * @see Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(font);
		g.setColor(color);
		g.drawString(version, 20, getSize().height - 30);
	}

	public static void main(String[] arguments) {
		new Splash().setVisible(true);
	}
}