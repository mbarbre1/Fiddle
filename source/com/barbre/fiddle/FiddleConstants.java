package com.barbre.fiddle;

/**
 * Common constant values used throughout the application
 */
public interface FiddleConstants {
	/**
	 * Version
	 */
	public static final String VERSION = "0.7.3";

	/**
	 * Key used with the properties file that saves the location of the EQ directory.
	 */
	public static final String HOME = "EQhome";

	/**
	 * Default home directory.  This is probably where most users setup EQ
	 */
	public static final String DEFAULT_HOME = "c:\\program files";

	/**
	 * File name for the image icons
	 */
	public static final String ICONS = "icons.gif";
	
	/**
	 * The properties file name.
	 */
	public static final String PROPERTIES_FILENAME = "Fiddle.properties";

	/**
	 * The splash screen name
	 */
	public static final String SPLASH_IMAGE = "splash.gif";

	/**
	 * Property key for the background image on the desktop
	 */
	public static final String BACKGROUND = "background";

	/**
	 * System property key for getting the zoom factor
	 */
	public static final String ZOOM = "ZOOM";

	/**
	 * Property change key for added filesets
	 */
	public static final String PROPERTY_FILESET_ADDED = "PROPERTY_FILESET_ADDED";

	/**
	 * Property change key for removed filesets
	 */
	public static final String PROPERTY_FILESET_REMOVED = "PROPERTY_FILESET_REMOVED";
	
	/**
	 * Custom constant for large icons in actions
	 */
	public static final String LARGE_ICON = "LARGE_ICON";
	
	/**
	 * Icon text on toolbar
	 */
	public static final String ICON_TEXT = "ICON_TEXT";
	
	/**
	 * Screen size
	 */
	public static final String SCREEN_SIZE = "SCREEN_SIZE";
	
	/**
	 * Desktop size
	 */
	public static final String DESKTOP_SIZE = "DESKTOP_SIZE";

	/**
	 * Look and feel
	 */
	public static final String LOOK_AND_FEEL = "LAF";
	
	/** 
	 * Gamma correction
	 */
	public static final String GAMMA_CORRECTION = "GAMMA_CORRECTION";		
	
	/**
	 * Message property name
	 */
	public static final String USER_MESSAGE = "User_Message";

	/**
	 * Vertical split pane location
	 */
	public static final String SPLITPANE_LOC_1 = "SPLIT_PANE_1";
	
	/**
	 * Horizontal splitpane loc
	 */
	public static final String SPLITPANE_LOC_2 = "SPLIT_PANE_2";	

	/**
	 * Name of the basic control file
	 */
	public static final String BASIC_FILENAME = "BASIC_CONTROLS";	
}