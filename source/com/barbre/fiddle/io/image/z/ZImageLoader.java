package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

/*
 ZImageLoader 1.0   97/3/7
 Superclass for various image loaders.
 Copyright (c) 1997, Marcel Schoen and Andre Pinheiro

 Code by Marcel Schoen (Marcel.Schoen@village.ch)
 Documentation/Arrangements by Andre Pinheiro (l41325@alfa.ist.utl.pt)

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

public class ZImageLoader implements Runnable {
	// flag indicating whether or not image loading has finished
	public boolean imageLoaded = false;
	public Image newImage = null;

	// variables used by the loaders
	protected ZProgressStatus imageClient = null;
	protected int[] frameBuffer = null;
	protected ZFileInput imageFile = new ZFileInput();

	// whenever this variable is set to true, the loader should print debugging info
	protected boolean debug = false;

	protected int xRes = 0, yRes = 0, actLine = 0, lastPercent = 0, actPercent = 0;

	private static Component client = null;

	// internal stuff
	private MediaTracker tracker = null; // to control loading of JPEGs and GIFs
	private ZImageLoader newLoader = null;
	private DirectColorModel colorModel = null;
	private DataInputStream data = null;

	private Thread runner = null;
	private static int done = 0;
	private String theFileName;

	protected ZImageLoaderProducer myProd = null;

	// implements the header reading algorithm - should be overriden by all subclasses!
	protected void readHeader() throws IOException, ZUnsupportedFormatException, ZWrongFileFormatException {
		print("default readHeader() method");
	}

	// implements the decoding algorithm - should be overriden by all subclasses!
	protected void loadPicture() throws IOException, ZUnsupportedFormatException, ZWrongFileFormatException {
		print("default loadPicture() method");
	}

	// provides loader info -  should be overriden by all subclasses!
	public String getInfo() {
		return ("JavaZine's ZImageLoader");
	}

	// defines the image client (for callbacks)
	public final void setClient(ZProgressStatus instance) {
		imageClient = instance;
	}

	// sets image width and height and creates the frame buffer
	public final void initFrameBuffer(int w, int h) {
		xRes = w;
		yRes = h;
		frameBuffer = new int[w * h];
	}

	// this method should be called by an app
	public Image load(URL picFile, Component target) throws ZNoSuchLoaderException {
		String fileName = picFile.getFile().toUpperCase();

		print("URL file name: " + picFile.getFile());

		client = target;
		colorModel = new DirectColorModel(24, (255 << 16), (255 << 8), 255);
		theFileName = fileName;

		if (fileName.endsWith("GIF") | fileName.endsWith("JPG") | fileName.endsWith("JPEG")) {
			tracker = new MediaTracker(client);
			newImage = Toolkit.getDefaultToolkit().getImage(picFile);
			tracker.addImage(newImage, 0);

			try {
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				print(e.getMessage());
			}

			imageLoaded = true;
		} else {
			try {
				data = new DataInputStream(picFile.openStream());
				startLoadProcess(fileName, data);
			} catch (IOException e) {
				print(e.getMessage());
			}
		}

		return newImage;
	}

	// this method should be called by an app
	public Image load(String picFile, Component target) throws ZNoSuchLoaderException {
		String fileName = picFile.toUpperCase();

		client = target;
		colorModel = new DirectColorModel(24, (255 << 16), (255 << 8), 255);
		theFileName = fileName;

		if (fileName.endsWith("GIF") | fileName.endsWith("JPG") | fileName.endsWith("JPEG")) {
			tracker = new MediaTracker(client);
			newImage = Toolkit.getDefaultToolkit().getImage(picFile);
			tracker.addImage(newImage, 0);

			try {
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				print(e.getMessage());
			}

			imageLoaded = true;
		} else {
			try {
				data = new DataInputStream(new FileInputStream(picFile));
				startLoadProcess(fileName, data);
			} catch (FileNotFoundException e) {
				print(e.getMessage());
			}
		}

		return newImage;
	}

	// ClassLoader stuff
	private boolean startLoadProcess(String fileName, DataInputStream data) throws ZNoSuchLoaderException {
		Class picLoader = null;
		int len = fileName.length(), pointPos = fileName.lastIndexOf(".");
		String end = fileName.substring(pointPos + 1, len);
		client.addNotify();
		newImage = client.createImage(1, 1);

		try {
			Class myself = getClass();

			// use default ClassLoader
			try {
				picLoader = Class.forName("com.barbre.fiddle.io.image.z.ZImageLoader" + end);
				picLoader.getName();
			} catch (ClassNotFoundException e) {
				picLoader = null;
				print(e.getMessage());
			}

			if (picLoader == null) {
				print("ZImageLoader subclass 'ZImageLoader" + end + ".class' not found.");
				throw new ZNoSuchLoaderException("No loader for image type " + end + " found");
			}

			try {
				print("Invoking ZImageLoader class: " + picLoader.toString());

				newLoader = (ZImageLoader) picLoader.newInstance(); // invoke new ZImageLoader
				newLoader.debug(debug);
				newLoader.setClient(imageClient);
				newLoader.imageFile.use(data); // set input stream

				print("Starting loader " + newLoader.toString());

				// read the image's header and prepare the frame's buffer
				try {
					newLoader.readHeader();
				} catch (IOException e) {
					print(e.getMessage());
				} catch (ZUnsupportedFormatException e) {
					print(e.getMessage());
				} catch (ZWrongFileFormatException e) {
					print(e.getMessage());
				}

				// prepare the image producer and the image reference
				if (client != null) {
					newLoader.myProd = new ZImageLoaderProducer();
					newLoader.myProd.setDimensions(newLoader.xRes, newLoader.yRes, colorModel);
					newImage = newLoader.myProd.createImage(client);
				} else
					return false;

				start();
			} catch (IOException e) {
				print(e.getMessage());
			} catch (InstantiationException e) {
				print(e.getMessage());
			} catch (IllegalAccessException e) {
				print(e.getMessage());
			}
		} catch (Exception e) {
			print(e.getMessage());
		}

		return false;
	}

	// print the specified message if the debug flag has been set
	protected final void print(String text) {
		if (debug)
			System.out.println(text);
	}

	public final void debug(boolean flag) {
		debug = flag;
	}

	// sleep for some milliseconds
	protected final void sleep(int secs) {
		try {
			Thread.sleep(secs);
		} catch (InterruptedException e) {
		}
	}

	public void run() {
		try {
			print("Loading the image...");
			newLoader.loadPicture();
			print("Updating the frame's buffer...");
			newLoader.myProd.update(newLoader.frameBuffer);
			Graphics g = client.getGraphics();
			client.paint(g);
			if (imageClient != null)
				imageClient.imageLoaded(100, newImage, "Image completed ");
		} catch (IOException e) {
			print(e.getMessage());
		} catch (ZWrongFileFormatException e) {
			print(e.getMessage());
		} catch (ZUnsupportedFormatException e) {
			print(e.getMessage());
		}

		runner.stop(); // stop the thread now
		runner = null; // erase it
		System.gc(); // collect some garbagge
	}

	public void start() {
		//		runner = new Thread(this);
		//		runner.start();
		run();
	}

	/* look for ZProgressStatus' clients
	   and call their imageLoaded method to inform them about the loading progress */
	protected final void newStatus(int percent, String text) {
		actPercent = percent;

		if (actPercent != lastPercent) {
			imageClient.imageLoaded(actPercent, newImage, text);
			lastPercent = actPercent;
		}
	}

	/* This method is a bit special and can be called when the image
	   has been loaded into memory and is being decoded.
	   It should be called at the end of every decompressed scan line.
	   The line counter 'actLine' is initialized with zero and the
	   method itself calculates the percentage of the whole image that
	   has been decoded so far. */
	protected final void newStatus(String text) {
		actLine++;

		if (imageClient != null) {
			actPercent = (int) ((actLine * 100) / yRes); // calculate status
			if (actPercent != lastPercent) {
				imageClient.imageLoaded(actPercent, newImage, text);
				lastPercent = actPercent;
			}
		}
	}

}