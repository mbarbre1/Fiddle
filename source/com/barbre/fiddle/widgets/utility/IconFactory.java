package com.barbre.fiddle.widgets.utility;

import java.awt.Image;

import javax.swing.ImageIcon;


public final class IconFactory {
	private static ImageIcon[] images = new ImageIcon[22];
	private static ImageIcon[] smallImages = new ImageIcon[22];
	private static boolean loaded = false;

	public static final int SIZE_32 = 32;
	public static final int SIZE_16 = 16;
	
	public static final int BROWSE_OBJECT = 0;
	public static final int CLOSE_ALL = 1;
	public static final int CLOSE_SELECTED = 2;
	public static final int COLOR_ICON = 3;
	public static final int COLOR_PRESSED_ICON = 4;
	public static final int COPY = 5;
	public static final int DELETE_FILE = 6;
	public static final int EXIT = 7;
	public static final int NEW_UI = 8;
	public static final int OPEN_UI = 9;
	public static final int REDO = 10;
	public static final int RESET_HOME = 11;
	public static final int SAVE = 12;
	public static final int SET_BACKGROUND = 13;
	public  static final int UNDO = 14;
	public static final int UP = 15;
	public  static final int ZOOM = 16;
	public  static final int EMPTY = 17;
	public  static final int STATUS = 18;	
	public  static final int PROPERTY = 19;		
	public  static final int ELEMENT = 20;			
	public  static final int OPEN = 21;				


	/**
	 * @see Object#Object()
	 */
	private IconFactory() {
	}

	/**
	 * Method loadImages.
	 */
	private static void loadImages() {
		if (loaded)
			return;

		images[BROWSE_OBJECT] = new ImageIcon(ImageMediator.getImage("icons/BrowseObject.gif"));
		images[CLOSE_ALL] = new ImageIcon(ImageMediator.getImage("icons/CloseAll.gif"));
		images[CLOSE_SELECTED] = new ImageIcon(ImageMediator.getImage("icons/CloseSelected.gif"));
		images[COLOR_ICON] = new ImageIcon(ImageMediator.getImage("icons/ColorIcon.gif"));
		images[COLOR_PRESSED_ICON] = new ImageIcon(ImageMediator.getImage("icons/ColorPressedIcon.gif"));
		images[COPY] = new ImageIcon(ImageMediator.getImage("icons/Copy.gif"));
		images[DELETE_FILE] = new ImageIcon(ImageMediator.getImage("icons/DeleteFile.gif"));
		images[EXIT] = new ImageIcon(ImageMediator.getImage("icons/Exit.gif"));
		images[NEW_UI] = new ImageIcon(ImageMediator.getImage("icons/NewUI.gif"));
		images[OPEN_UI] = new ImageIcon(ImageMediator.getImage("icons/OpenUI.gif"));
		images[REDO] = new ImageIcon(ImageMediator.getImage("icons/Redo.gif"));
		images[RESET_HOME] = new ImageIcon(ImageMediator.getImage("icons/ResetHome.gif"));
		images[SAVE] = new ImageIcon(ImageMediator.getImage("icons/Save.gif"));
		images[SET_BACKGROUND] = new ImageIcon(ImageMediator.getImage("icons/SetBackground.gif"));
		images[UNDO] = new ImageIcon(ImageMediator.getImage("icons/Undo.gif"));
		images[UP] = new ImageIcon(ImageMediator.getImage("icons/Up.gif"));
		images[ZOOM] = new ImageIcon(ImageMediator.getImage("icons/Zoom.gif"));
		images[EMPTY] = new ImageIcon(ImageMediator.getImage("icons/Empty.gif"));
		images[STATUS] = new ImageIcon(ImageMediator.getImage("icons/Status.gif"));				
		images[PROPERTY] = new ImageIcon(ImageMediator.getImage("icons/properties.gif"));						
		images[ELEMENT] = new ImageIcon(ImageMediator.getImage("icons/elements.gif"));								
		images[OPEN] = new ImageIcon(ImageMediator.getImage("icons/open.gif"));										
		
		for (int i = 0; i < images.length; i++) {
			smallImages[i] = new ImageIcon(images[i].getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		
		loaded = true;
	}

	public static ImageIcon getIcon(int type) {
		return getIcon(type, SIZE_32);
	}
	
	public static ImageIcon getIcon(int type, int size) {
		loadImages();
		switch (size) {
			case SIZE_32 :
				return images[type];

			case SIZE_16 :
				return smallImages[type];

			default :
				return null;
		}
	}

}