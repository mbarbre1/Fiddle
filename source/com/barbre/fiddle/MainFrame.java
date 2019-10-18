package com.barbre.fiddle;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.widgets.ShutdownListener;

/**
 * This is the JFrame that surrounds the users view of the application.
 */
public class MainFrame extends JFrame implements ShutdownListener {
	private JLabel bgImage = new JLabel();
	private JDesktopPane desktop = null;
	private JToolBar bar = null;
	private JSplitPane splitter = null;
	private JScrollPane desktopScroller = null;	

	/**
	 * Construct a MainFrame that is 800x600.
	 */
	public MainFrame() {
		super();
		setTitle("SIDL Fiddle");
		setSize(800, 600);
		setContentPane(createContentPane());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Fiddle.getInstance().shutdown();
			}
		});
		Fiddle.getInstance().addShutdownListener(this);
	}

	/**
	 * set the background image for the desktop
	 */
	public void setBackgroundImage(Image image) {
		if (bgImage != null) {
			getDesktop().remove(bgImage);
		}
		if (image != null) {
			ImageIcon icon = new ImageIcon(image);
			bgImage.setIcon(icon);
			bgImage.setSize(icon.getIconWidth(), icon.getIconHeight());
			bgImage.validate();
			((Desktop) getDesktop()).resizeDesktop(icon.getIconWidth(), icon.getIconHeight());
			getDesktop().add(bgImage, JLayeredPane.FRAME_CONTENT_LAYER);
		}
	}

	/**
	 * get the desktop for this frame
	 * @return JDesktopPane
	 */
	public JDesktopPane getDesktop() {
		if (desktop == null) {
			desktop = new Desktop();
		}
		return desktop;
	}

	/**
	 * Create a content pane and override JFrame's default one.
	 * @return JPanel
	 */
	private JPanel createContentPane() {
		JPanel p = new JPanel(new BorderLayout());
		splitter = new JSplitPane();
		splitter.setLeftComponent(createWestPane());
		desktopScroller = new JScrollPane();
		desktopScroller.setViewportView(getDesktop());
		splitter.setRightComponent(desktopScroller);
		p.add(splitter, BorderLayout.CENTER);
		StatusBar sb = new StatusBar();
		MessageManager.getInstance().addPropertyChangeListener(sb);
		p.add(sb, BorderLayout.SOUTH);
		String s1 = PropertiesLoader.getProperty(FiddleConstants.SPLITPANE_LOC_1);
		Utility.setSplitPaneDividerLocation(splitter, s1);
		return p;
	}

	/**
	 * Create the list manager on the west side
	 * @return JPanel
	 */
	private JPanel createWestPane() {
		return FileNavigationManager.getInstance().getPanel();
	}
	
	
	/**
	 * Method setToolbar.
	 * @param bar
	 */
	public void setToolbar(JToolBar bar) {
		this.bar = bar;
		getContentPane().add(bar, BorderLayout.NORTH);
	}
	
	/**
	 * Method getToolbar.
	 * @return JToolBar
	 */
	public JToolBar getToolbar() {
		return bar;
	}
	/**
	 * @see com.barbre.fiddle.widgets.ShutdownListener#shutdownAction()
	 */
	public void shutdownAction() {
		Rectangle r = getBounds();
		PropertiesLoader.setProperty(FiddleConstants.SCREEN_SIZE, r.x + " " + r.y + " " + r.width + " " + r.height);
		PropertiesLoader.setProperty(FiddleConstants.SPLITPANE_LOC_1, "" + splitter.getDividerLocation());

	}

}