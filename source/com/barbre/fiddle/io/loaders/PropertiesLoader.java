package com.barbre.fiddle.io.loaders;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.dialogs.DirectoryChooser;

public final class PropertiesLoader implements FileFilter {
	private static Properties props = null;

	/**
	 * Method loadProperties.
	 */
	public static void loadProperties() {
		try {
			props = new Properties();
			FileInputStream fis = new FileInputStream(FiddleConstants.PROPERTIES_FILENAME);
			props.load(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("Properties not found.  Creating new file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method saveProperties.
	 */
	public static void saveProperties() {
		try {
			FileOutputStream fos = new FileOutputStream(FiddleConstants.PROPERTIES_FILENAME);
			props.store(fos, "");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the EQ Location from properties, or ask user for the location.
	 */
	public static void setEQLocation() {
		setEQLocation(false);
	}

	/**
	 * Get the EQ Location from properties, or ask user for the location.
	 */
	public static void setEQLocation(boolean reset) {
		loadProperties();
		if (reset || (props.getProperty(FiddleConstants.HOME) == null)) {
			requestHomeLocation();
		}
		System.setProperty(FiddleConstants.HOME, props.getProperty(FiddleConstants.HOME));
		saveProperties();
	}
	

	/**
	 * Method requestHomeLocation.
	 */
	private static void requestHomeLocation() {
		File f = DirectoryChooser.showDialog();
		
		if (f != null) {
			String[] files = f.list();
			for (int i = 0; i <files.length; i++) {
				if (files[i].equalsIgnoreCase("uifiles")) {
					props.setProperty(FiddleConstants.HOME, f.getPath());		
					return;
				}
			}
		}
			
		int answer = JOptionPane.showConfirmDialog(null, "This directory is not the Everquest home Directory.  Try again?");
		if (answer == JOptionPane.YES_OPTION) {
			requestHomeLocation();
		} else {
			Fiddle.getInstance().shutdown();
		}
	}

	/**
	 * Method getProperty.
	 * @param key
	 * @return String
	 */
	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	/**
	 * Method setProperty.
	 * @param key
	 * @param value
	 */
	public static void setProperty(String key, String value) {
		props.setProperty(key, value);
	}

	/**
	 * Method setProperty.
	 * @param key
	 * @param value
	 */
	public static String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

 

	/**
	 * @see java.io.FileFilter#accept(File)
	 */
	public boolean accept(File pathname) {
		return pathname.isDirectory();
	}


}