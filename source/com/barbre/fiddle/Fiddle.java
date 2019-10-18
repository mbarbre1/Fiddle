package com.barbre.fiddle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import com.barbre.fiddle.dialogs.DirtyDialog;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.io.loaders.ImageLoader;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.io.loaders.UILoader;
import com.barbre.fiddle.widgets.ShutdownListener;
import com.barbre.fiddle.widgets.Splash;
import com.barbre.fiddle.widgets.utility.ImageMediator;

import com.barbre44.lang.StringUtil;
import com.barbre44.util.Debug;

/**
 * This is the main Application entrant point.  Additionally, this singleton object provides
 * additional functions:<ul>
 * <li>Tracking dirty files, i.e. those files that have changed.
 * <li>Houses the FileSetManager object
 * <li>Houses the UndoManager object
 * </ul>
 */
public final class Fiddle {
	private static Fiddle instance = null;
	private MainFrame frame = null;
	private List dirtyFiles = new ArrayList();
	private List shutdownListeners = new ArrayList();
	private FileSetManager fsm = new FileSetManager();
	private UndoManager undoManager = new UndoManager();

	/**
	 * Insure this object is a singleton
	 * @see Object#Object()
	 */
	private Fiddle() {
		super();
	}

	/**
	 * Get the singleton instance
	 * @return Fiddle
	 */
	public static Fiddle getInstance() {
		if (instance == null) {
			instance = new Fiddle();
			instance.init();
		}
		return instance;
	}

	/**
	 * Method init.
	 */
	private void init() {
		PropertiesLoader.setEQLocation();
		setGammaCorrection();
		Splash splash = showSplash();
		setLAF();
		setupFrame();
		setupToolbar();
		setupDesktopImage();
		splash.setVisible(false);
		frame.setVisible(true);
	}

	/**
	 * Method setGammaCorrection.
	 */
	private void setGammaCorrection() {
		String gc = PropertiesLoader.getProperty(FiddleConstants.GAMMA_CORRECTION);
		if ((gc == null) || (gc.trim().length() == 0))
			gc = "1.0";
		ImageLoader.setGamma(Double.parseDouble(gc));
	}

	/**
	 * Method setLAF.
	 */
	private void setLAF() {
		try {
			String laf = PropertiesLoader.getProperty(FiddleConstants.LOOK_AND_FEEL);
			if (laf != null) {
				UIManager.setLookAndFeel(laf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method setupDesktopImage.
	 */
	private void setupDesktopImage() {
		String bgName = PropertiesLoader.getProperty(FiddleConstants.BACKGROUND);
		if (bgName != null) {
			frame.setBackgroundImage(ImageMediator.getImage(bgName));
		} else {
			frame.getDesktop().setBackground(Color.black);
		}
	}

	/**
	 * Method setupFrame.
	 */
	private void setupFrame() {
		frame = new MainFrame();
		frame.setJMenuBar(new MainMenu());
		frame.setLocation(Utility.center(frame));

		String size = PropertiesLoader.getProperty(FiddleConstants.SCREEN_SIZE);
		if (size != null) {
			StringTokenizer t = new StringTokenizer(size, " ");
			int x = Integer.parseInt(t.nextToken());
			int y = Integer.parseInt(t.nextToken());
			int width = Integer.parseInt(t.nextToken());
			int height = Integer.parseInt(t.nextToken());
			frame.setBounds(x, y, width, height);
		}
	}

	/**
	 * Method setupToolbar.
	 */
	private void setupToolbar() {
		MainToolbar bar = new MainToolbar();
		frame.setToolbar(bar);

		String isLarge = PropertiesLoader.getProperty(FiddleConstants.LARGE_ICON, "true");
		bar.setLargeIcons(StringUtil.String2Boolean(isLarge));

		String showText = PropertiesLoader.getProperty(FiddleConstants.ICON_TEXT, "false");
		bar.setShowText(StringUtil.String2Boolean(showText));
	}

	private Splash showSplash() {
		Splash splash = new Splash();
		splash.setVisible(true);
		return splash;
	}

	/**
	 * Method main.  This is where the applicatin starts
	 * @param arguments
	 */
	public static void main(String[] arguments) {
		
		String version = System.getProperty("java.version");
		if (version.startsWith("1.4"))
			JOptionPane.showMessageDialog(null, "SIDL Fiddle may encounter memory problems running JVM 1.4.x");
		
		if (arguments.length > 0) {
			for (int i = 0; i < arguments.length; i++) {
				if (arguments[i].equalsIgnoreCase("-debug")) {
					Debug.setIsDebug(true);
					Debug.println(null, "SIDL Fiddle Debug mode");
				}
				if (arguments[i].equalsIgnoreCase("-filter")) {
					Debug.println(null, "Filtering on " + arguments[i + 1]);
					Debug.setFilter(arguments[i + 1]);
				}

			}
		}
		Fiddle.getInstance();
	}

	/**
	 * Get the Main-frame 
	 * @return Returns a MainFrame
	 */
	public MainFrame getFrame() {
		return frame;
	}

	/**
	 * Sets the frame.
	 * @param frame The frame to set
	 */
	public void setFrame(MainFrame frame) {
		this.frame = frame;
	}

	/**
	 * Shutdown and do any cleanup needed.
	 */
	public void shutdown() {
		if (dirtyFiles.isEmpty() == false) {
			List savers = DirtyDialog.showDialog(dirtyFiles);
			if (savers == null)
				return;
			Iterator i = savers.iterator();
			while (i.hasNext()) {
				UIFile element = (UIFile) i.next();
				UILoader.getInstance().saveUI(element);
			}
		}

		Iterator i = shutdownListeners.iterator();
		while (i.hasNext()) {
			ShutdownListener element = (ShutdownListener) i.next();
			element.shutdownAction();
		}

		PropertiesLoader.saveProperties();
		System.exit(0);
	}

	/**
	 * Ad a file to the dirty list
	 * @param file
	 */
	public void addDirty(UIFile file) {
		if (dirtyFiles.contains(file) == false)
			dirtyFiles.add(file);
	}

	/**
	 * Get the list of dirty files
	 * @return List
	 */
	public List getDirtyFiles() {
		return dirtyFiles;
	}

	/**
	 * Empty the dirty file list
	 */
	public void clearDirtyFiles() {
		dirtyFiles.clear();
	}

	/**
	 * Get the FileSetManager
	 * @return FileSetManager
	 */
	public FileSetManager getFileSetManager() {
		return fsm;
	}

	/**
	 * Get the undo manager.
	 * @return UndoManager
	 */
	public UndoManager getUndoManager() {
		return undoManager;
	}
	
	public void addShutdownListener(ShutdownListener l) {
		if (shutdownListeners.contains(l) == false)
			shutdownListeners.add(l);
	}
	
	public void removeShutdownListener(ShutdownListener l) {
		shutdownListeners.remove(l);
	}
}