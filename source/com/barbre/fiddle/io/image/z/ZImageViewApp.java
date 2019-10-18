package com.barbre.fiddle.io.image.z;

import java.awt.*;
import java.net.*;
import java.io.*;

import javax.swing.JLabel;

import com.barbre.fiddle.io.image.*; // JavaZine's zfileio package

/*
 ZImageViewApp 1.0   97/2/27
 Copyright (c) 1997, Marcel Schoen and Andre Pinheiro

 Code by Marcel Schoen (Marcel.Schoen@village.ch)
 Documentation/Arrangements by Andre Pinheiro (adlp@camoes.rnl.ist.utl.pt)

 All rights reserved.

 Published by JavaZine - Your Java webzine
 Links to JavaZine's websites
   - http://camoes.rnl.ist.utl.pt/~adlp/JavaZine/Links/JavaZine.html

 Permission to use, copy, modify, and distribute this software
 and its documentation for NON-COMMERCIAL or COMMERCIAL purposes
 and without fee is hereby granted provided that the copyright
 and "published by" messages above appear in all copies.
 We will not be held responsible for any unwanted effects due to
 the usage of this software or any derivative.
 No warrantees for usability for any specific application are
 given or implied.
*/

public class ZImageViewApp extends Frame implements ZProgressStatus {
	private Font font = new Font("Helvetica", Font.PLAIN, 18);
	private Image image = null;
	private Graphics direct = null;
	private int total = 0, lastPercent = 0, x = 10, y = 50;

	ZImageLoader picLoader = new ZImageLoader();

	public ZImageViewApp() {
		setTitle("ZImageViewApp");

		MenuBar mbar = new MenuBar();
		Menu m = new Menu("File");
		m.add(new MenuItem("Open"));
		m.add(new MenuItem("Exit"));
		mbar.add(m);
		setMenuBar(mbar);

		resize(300, 200);
		show();
		direct = getGraphics();
	}

	public static void main(String args[]) {
		new ZImageViewApp();
	}

	// event handling
	public boolean keyDown(Event evt, int key) {
		if (key == evt.END)
			System.exit(0); // quit

		return true;
	}

	public boolean handleEvent(Event evt) {
		if (evt.id == Event.WINDOW_DESTROY)
			System.exit(0);

		return super.handleEvent(evt);
	}

	public boolean action(Event evt, Object arg) {
		if (arg.equals("Open")) {
			//      FileDialog d = new FileDialog(this, "Open image file", FileDialog.LOAD);
			//    d.setFile("*.*");
			//  d.show();
			String f = "e:\\program files\\everquest\\uifiles\\default\\wnd_bg_light_rock.tga"; //d.getFile();

			if (f != null) {
				print("Image: " + f);
				picLoader.setClient(this); // prepare for status callbacks
				picLoader.debug(true);
				lastPercent = 100;

				try {
					image = picLoader.load(f, (Component) this);
				} catch (ZNoSuchLoaderException e) {
					print(e.getMessage());
				}
System.out.println("\n\nhere\n\n");
				if (image != null)
					resize(image.getWidth(null) + x + 10, image.getHeight(null) + y + 10);

				repaint();
			} else
				return false;
		} else if (arg.equals("Exit"))
			System.exit(0);

		return true;
	}

	// draw image
	public void paint(Graphics g) {
		if (image != null)
			g.drawImage(image, x, y, this);
	}

	// implement ZProgressStatus interface for callbacks
	public void imageLoaded(int percent, Image pic, String text) {
		if (lastPercent > percent) {
			direct.setColor(Color.white);
			direct.fillRect(0, 0, size().width, size().height);
			direct.setColor(Color.black);
			direct.fillRect(10, 10, size().width - 20, 30);
			direct.setColor(Color.red);
			direct.setFont(font);
			total = size().width - 24;
		}

		lastPercent = percent;
		direct.setColor(Color.red);
		direct.fillRect(12, 12, (total * percent) / 100, 26);
		direct.setColor(Color.red);
		direct.drawString(text, 10, 70);
		if (percent == 100)
			repaint();
		// print(text + percent + "%");
	}

	// writes a message to the console window
	public void print(String text) {
		System.out.println(text);
	}

}