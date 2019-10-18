package com.barbre.fiddle.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.barbre.fiddle.FiddleConstants;
import com.barbre.fiddle.widgets.utility.IconFactory;

/**
 * Description of class
 *
 * <p><hr><h3>Release History</h3><p><ul>
 *
 * <li>mike  Nov 22, 2002  Created class.
 *
 * </ul><p>
 * 
 * @version 02.12
 */
public class ActionReleaseNotes extends AbstractAction {

	/**
	 * Constructor for ActionReleaseNotes.
	 */
	public ActionReleaseNotes() {
		super("Release Notes...");
		putValue(FiddleConstants.LARGE_ICON, IconFactory.getIcon(IconFactory.EMPTY));
		putValue(SMALL_ICON, IconFactory.getIcon(IconFactory.EMPTY, IconFactory.SIZE_16));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		try {
			URL url = new File("v" + FiddleConstants.VERSION + ".html").toURL();
			JEditorPane pane = new JEditorPane(url);
			JScrollPane scroller = new JScrollPane(pane);
			Dimension d = new Dimension(400, 300);			
			scroller.setMinimumSize(d);
			scroller.setPreferredSize(d);
			JOptionPane.showMessageDialog(null, scroller, "Release Notes (" + FiddleConstants.VERSION + ")", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ActionReleaseNotes().actionPerformed(new ActionEvent("", 0, ""));
		System.exit(0);
	}
}
