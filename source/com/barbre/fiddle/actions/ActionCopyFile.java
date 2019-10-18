package com.barbre.fiddle.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JOptionPane;

import com.barbre.fiddle.Fiddle;
import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.FileNavigationManager;
import com.barbre.fiddle.elements.UIFile;
import com.barbre.fiddle.elements.UIFileSet;
import com.barbre.fiddle.io.loaders.UILoader;
import com.barbre.fiddle.widgets.utility.IconFactory;

public class ActionCopyFile extends AbstractAction {

	/**
	 * Constructor for ActionCopyFile.
	 */
	public ActionCopyFile() {
		super("Copy File...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.COPY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.COPY, IconFactory.SIZE_16));
	}

	/**
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		UIFile selected = (UIFile) FileNavigationManager.getInstance().getFileComboBox().getSelectedItem();
		if (selected == null)
			return;

		List names = Fiddle.getInstance().getFileSetManager().getNames();
		names.remove("default");
		names.remove(selected.getSet().getName());
		JList choices = new JList(names.toArray());
		choices.setPreferredSize(new Dimension(400, 200));
		choices.setSelectedIndex(0);
		choices.setBorder(BorderFactory.createEtchedBorder());
		String title = "Copy " + selected.getName() + " from " + selected.getSet().getName() + " to...";
		int answer = JOptionPane.showConfirmDialog(null, choices, title, JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			copyFile(selected, (String) choices.getSelectedValue());
		}
	}

	/**
	 * Method copyFile.
	 * @param file
	 * @param name
	 */
	private void copyFile(UIFile file, String name) {
		UIFileSet target = Fiddle.getInstance().getFileSetManager().get(name);

		if (overwrite(file, target)) {
			String from = file.getSet().getDirectory() + File.separator + file.getName();
			String to = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles" + File.separator + target.getName() + File.separator + file.getName();
			//JOptionPane.showMessageDialog(null, new Object[]{from,to});
			try {
				doCopy(new File(from), new File(to));
				
				/// reload for now
				String fromSet = System.getProperty(FiddleConstants.HOME) + File.separator + "uifiles" + File.separator + target.getName();
				UIFileSet set = UILoader.getInstance().loadUI(UILoader.DEFAULT_FILE_SET, fromSet, name);
				Fiddle.getInstance().getFileSetManager().add(set);
			} catch(Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Method overwrite.
	 * @param file
	 * @param target
	 * @return boolean
	 */
	private boolean overwrite(UIFile file, UIFileSet target) {
		Iterator i = target.getFiles().iterator();
		while (i.hasNext()) {
			UIFile element = (UIFile) i.next();
			if (element.getName().equalsIgnoreCase(file.getName())) {
				int answer = JOptionPane.showConfirmDialog(null, file.getName() + " already exists in " + target.getName() + " Overwrite this file?");
				if (answer == JOptionPane.NO_OPTION)
					return false;
			}
		}
		return true;
	}

	/**
	 * Method CopyFile.
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	public void doCopy(File in, File out) throws Exception {
		FileInputStream fis = new FileInputStream(in);
		
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;
		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}
		fis.close();
		fos.flush();
		fos.close();
	}

}