package com.barbre.fiddle.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.widgets.utility.IconFactory;
import com.barbre.fiddle.widgets.utility.ImageMediator;

public class ActionBackgroundImage extends AbstractAction {

	/**
	 * Constructor for ActionBackgroundImage.
	 */
	public ActionBackgroundImage() {
		super("Load background image...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.SET_BACKGROUND));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.SET_BACKGROUND, IconFactory.SIZE_16));
		putValue(SHORT_DESCRIPTION, "Change Background");
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser("c:\\program files\\everquest");
		chooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				String name = f.getName().toLowerCase();
				return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".bmp") || f.isDirectory();
			}

			public String getDescription() {
				return "image";
			}
		});
		int answer = chooser.showOpenDialog(Fiddle.getInstance().getFrame());
		if (answer == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			Image image = ImageMediator.getImage(f.getAbsolutePath());
			Fiddle.getInstance().getFrame().setBackgroundImage(image);

			try {
				PropertiesLoader.setProperty(FiddleConstants.BACKGROUND, f.getCanonicalPath());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}