package com.barbre.fiddle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.barbre.fiddle.browser.PropertyBrowser;
import com.barbre.fiddle.browser.sun.beanbuilder.property.ObjectHolder;
import com.barbre.fiddle.browser.sun.beanbuilder.property.PropertyPane;
import com.barbre.fiddle.elements.IClass;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre.fiddle.event.WidgetSelectionEvent;
import com.barbre.fiddle.event.WidgetSelectionListener;
import com.barbre.fiddle.io.loaders.PropertiesLoader;
import com.barbre.fiddle.widgets.InternalFrame;
import com.barbre.fiddle.widgets.ShutdownListener;
import com.barbre.fiddle.widgets.utility.IconFactory;
import com.barbre44.swing.BarPanel;
import com.barbre44.util.Debug;

/**
 * This manager maintains the three lists on the left side of the application.
 */
public class FileNavigationManager implements ListSelectionListener, ItemListener, ShutdownListener, WidgetSelectionListener {
	private static FileNavigationManager instance = null;
	private JPanel panel = null;
	private JSplitPane splitPane = null;
	private FileComboBox fileList = null;
	private FileElementList screenList = null;
	private BarPanel propertyPane = null;


	/**
	 * Private constructor 
	 */
	private FileNavigationManager() {
		super();
		Fiddle.getInstance().addShutdownListener(this);
	}

	/**
	 * Get the singleton instance of this manager
	 * @return ListManager
	 */
	public static FileNavigationManager getInstance() {
		if (instance == null) {
			instance = new FileNavigationManager();
		}
		return instance;
	}

	/**
	 * Returns the panel that contains all three lists
	 * @return JPanel
	 */
	public JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel(new BorderLayout());
			panel.add(getFileComboBoxPanel(), BorderLayout.NORTH);
			panel.add(getSplitPanePanel());
			panel.setBorder(BorderFactory.createEtchedBorder());
		}
		return panel;
	}

	/**
	 * Method getSplitPanePanel.
	 * @return JSplitPane
	 */

	private JSplitPane getSplitPanePanel() {
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JScrollPane elementList = createScrollPane(getFileElementList(), "Elements");
		BarPanel bar = new BarPanel(elementList, "Elements", IconFactory.getIcon(IconFactory.ELEMENT));
		
		splitPane.setLeftComponent(bar);
		splitPane.setRightComponent(getPropertyEditor());
		
	
		
		
		
		String s2 = PropertiesLoader.getProperty(FiddleConstants.SPLITPANE_LOC_2);
		Utility.setSplitPaneDividerLocation(splitPane, s2);
		return splitPane;
	}

	/**
	 * Method getFileComboBoxPanel.
	 * @return JPanel
	 */

	private JPanel getFileComboBoxPanel() {
		JPanel north = new JPanel();
		JLabel label = new JLabel("  File  ");
		
		label.setForeground(Color.black);		
		BoxLayout box = new BoxLayout(north, BoxLayout.X_AXIS);
		north.setLayout(box);
		north.add(label);
		north.add(getFileComboBox());
		return north;
	}

	/**
	 * Gets the fileList.
	 * @return Returns a FileList
	 */
	public FileComboBox getFileComboBox() {
		if (fileList == null) {
			fileList = new FileComboBox();
			fileList.addItemListener(this);
		}
		return fileList;
	}

	/**
	 * Gets the screenList.
	 * @return Returns a ScreenList
	 */
	public FileElementList getFileElementList() {
		if (screenList == null) {
			screenList = new FileElementList();
			screenList.addListSelectionListener(this);
		}
		return screenList;
	}

	/**
	 * Gets the pieceList.
	 * @return Returns a PieceList
	 */
	public BarPanel getPropertyEditor() {
		if (propertyPane == null) {
			PropertyPane propertyEditor = new PropertyBrowser();
			propertyPane = new BarPanel(propertyEditor, "Properties", IconFactory.getIcon(IconFactory.PROPERTY));
			ObjectHolder.getInstance().addPropertyChangeListener(propertyEditor);
			propertyPane.setContentPane(propertyEditor);
			
			JButton open = createButton(IconFactory.getIcon(IconFactory.OPEN), "Open");
			JButton up = createButton(IconFactory.getIcon(IconFactory.UP), "Up");

			propertyEditor.setUp(up);
			propertyEditor.setDown(open);
			propertyPane.getTools().add(open);
			propertyPane.getTools().add(up);		
					
		}
		return propertyPane;
	}


	private JButton createButton(Icon icon, String tooltip) {
		JButton b = new JButton(icon);
		b.setToolTipText(tooltip);
		b.setOpaque(false);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
		b.setMargin(new Insets(0,0,0,0));			
		b.setPreferredSize(new Dimension(16,16));
		return b;
	}
	
	
	/**
	 * This method replaces files to the file list.
	 * @param files
	 */
	public void setFiles(UIFile[] files) {
		getFileComboBox().setFiles(files);
		getFileComboBox().setSelectedIndex(-1);
	}

	/**
	 * Method setFiles.
	 * @param files
	 */
	public void setFileSet(UIFileSet set) {
		setFiles((UIFile[]) set.getFiles().toArray(new UIFile[0]));
	}

	/**
	 * Method createScrollPane.
	 * @param list
	 * @param title
	 * @return JScrollPane
	 */
	private JScrollPane createScrollPane(JComponent comp, String title) {
//		TitledBorder border = createCustomBorder(title);
		JScrollPane scroller = new JScrollPane(comp);
//		scroller.setBorder(border);
		return scroller;
	}

	private TitledBorder createCustomBorder(String title) {
		Border b = BorderFactory.createEtchedBorder();
		TitledBorder border = BorderFactory.createTitledBorder(b, title);
		border.setTitleColor(Color.black);
		return border;
	}

	/**
	 * When a selection is made on a list it may propogate a change to a lower list.
	 * (e.g. a file selection changes the screen list to show all the screens for that file).
	 * this listener manages these changes.
	 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent evt) {
		if (evt.getValueIsAdjusting())
			return;

		Debug.println(this, "valueChanged");

		if (evt.getSource() == getFileElementList()) {
			IClass si = (IClass) getFileElementList().getSelectedValue();
			if (si != null) {
				ObjectHolder.getInstance().setRoot(si);
				if (si != null) {
					getFileComboBox().setSelectedItem(si.getFile());
					activateScreen(si);
				}
			}
		}
	}

	/**
	 * Method activateScreen.
	 * @param si
	 */
	protected void activateScreen(IClass si) {
		JInternalFrame[] frames = Fiddle.getInstance().getFrame().getDesktop().getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i] instanceof InternalFrame)
				if (((InternalFrame) frames[i]).getScreen() == si) {
					Fiddle.getInstance().getFrame().getDesktop().getDesktopManager().activateFrame(frames[i]);
					break;
				}
		}
	}

	/**
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent evt) {
		if (evt.getSource() == getFileComboBox()) {
			if (getFileComboBox().isAdjusting() || (evt.getStateChange() != ItemEvent.SELECTED))
				return;
			Debug.println(this, "item=" + evt.getItem());
			getFileElementList().setFile((UIFile) getFileComboBox().getSelectedItem());
		}
	}

	/**
	 * @see com.barbre.fiddle.widgets.ShutdownListener#shutdownAction()
	 */
	public void shutdownAction() {
		PropertiesLoader.setProperty(FiddleConstants.SPLITPANE_LOC_2, "" + splitPane.getDividerLocation());		
	}

	/**
	 * @see com.barbre.fiddle.event.WidgetSelectionListener#widgetSelection(WidgetSelectionEvent)
	 */
	public void widgetSelection(WidgetSelectionEvent evt) {
// -- need more design
//		IScreenPiece thePiece = (IScreenPiece) evt.getSource();
//		getFileComboBox().setSelectedItem(thePiece.getFile());
//		getFileElementList().setSelectedValue(thePiece, true);
	}


}